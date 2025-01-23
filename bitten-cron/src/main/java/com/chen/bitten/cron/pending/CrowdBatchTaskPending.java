package com.chen.bitten.cron.pending;

import com.chen.bitten.api.service.SendService;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.constant.ThreadPoolConstant;
import com.chen.bitten.common.domain.CrowdTaskInfo;
import com.chen.bitten.common.domain.MessageParam;
import com.chen.bitten.common.domain.PendingParam;
import com.chen.bitten.common.dto.BatchSendRequest;
import com.chen.bitten.cron.config.AsyncThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 延迟批量处理人群信息
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdTaskInfo> {

    private static final String SEND_CODE = "send";

    @Autowired
    private SendService sendService;

    public CrowdBatchTaskPending() {
        PendingParam<CrowdTaskInfo> pendingParam = new PendingParam<>();
        pendingParam.setQueue(new LinkedBlockingDeque<>(ThreadPoolConstant.BLOCKING_QUEUE_SIZE))
                .setBatchThreshold(ThreadPoolConstant.BATCH_THRESHOLD)
                .setTimeThreshold(ThreadPoolConstant.TIME_THRESHOLD)
                .setExecutorService(AsyncThreadPoolConfig.getConsumePendingTasksExecutor());
        this.pendingParam = pendingParam;
    }
    @Override
    public void doHandle(List<CrowdTaskInfo> tasks) {
        // 如果参数相同，组装成同一个MessageParam发送
        Map<Map<String, String>, String> paramMap = new HashMap<>();
        for (CrowdTaskInfo crowdTaskInfo: tasks) {
            String receiver = crowdTaskInfo.getReceiver();
            Map<String, String> params = crowdTaskInfo.getParams();
            if (!paramMap.containsKey(params)) {
                paramMap.put(params, receiver);
            } else {
                String newReceiver = paramMap.get(params) + CommonConstant.COMMA + receiver;
                paramMap.put(params, newReceiver);
            }
        }
        List<MessageParam> messageParamList = new ArrayList<>();
        for (Map.Entry<Map<String, String>, String> param: paramMap.entrySet()) {
            MessageParam messageParam = MessageParam.builder()
                    .variables(param.getKey())
                    .receivers(param.getValue()).build();
            messageParamList.add(messageParam);
        }
        BatchSendRequest batchSendRequest = BatchSendRequest.builder().code(SEND_CODE)
                .messageTemplateId(tasks.stream().findFirst().get().getMessageTemplateId())
                .messageParamList(messageParamList).build();
        sendService.batchSend(batchSendRequest);
    }
}
