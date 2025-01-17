package com.chen.bitten.handler.handler.pending;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.process.ProcessController;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.handler.config.HandlerProcessConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task执行器
 */
@Component
@Data
@Accessors(chain = true)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable{

    private TaskInfo taskInfo;

    @Autowired
    @Qualifier("handlerProcessController")
    private ProcessController processController;


    @Override
    public void run() {
        ProcessContext context = ProcessContext.builder()
                .code(HandlerProcessConfig.HANDLER_PROCESS_CODE)
                .processModel(taskInfo)
                .needBreak(false)
                .response(BasicResultVO.success())
                .build();
        processController.process(context);
    }
}
