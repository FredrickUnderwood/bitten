package com.chen.bitten.common.process;

import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class ProcessController {

    /**
     * 流程code -> 流程模板
     */
    private Map<String, ProcessTemplate> templates = null;

    public ProcessContext process(ProcessContext context) {
        try {
            preCheck(context);
        } catch (ProcessException e) {
            return e.getProcessContext();
        }

        List<BusinessProcess> processList = templates.get(context.getCode()).getProcessList();
        for (BusinessProcess businessProcess: processList) {
            businessProcess.process(context);
            if (context.getNeedBreak()) {
                break;
            }
        }
        return context;
    }

    public void preCheck(ProcessContext context) throws ProcessException {

        if (Objects.isNull(context)) {
            context = new ProcessContext();
            context.setResponse(BasicResultVO.fail(RespStatusEnum.CONTEXT_NULL));
            throw new ProcessException(context);
        }

        if (Objects.isNull(context.getCode())) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.PROCESS_CODE_NULL));
            throw new ProcessException(context);
        }

        ProcessTemplate template = templates.get(context.getCode());
        if (Objects.isNull(template)) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.PROCESS_TEMPLATE_NULL));
            throw new ProcessException(context);
        }

        List<BusinessProcess> businessProcessList = template.getProcessList();
        if (Objects.isNull(businessProcessList) || businessProcessList.isEmpty()) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.BUSINESS_PROCESS_LIST_NULL));
            throw new ProcessException(context);
        }
    }

}
