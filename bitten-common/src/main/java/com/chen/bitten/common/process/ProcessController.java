package com.chen.bitten.common.process;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProcessController {

    /**
     * 流程code -> 流程模板
     */
    private Map<String, ProcessTemplate> templates = null;

    public ProcessContext process(ProcessContext context) {
        // TODO 前置检查

        List<BusinessProcess> processList = templates.get(context.getCode()).getProcessList();
        for (BusinessProcess businessProcess: processList) {
            businessProcess.process(context);
            if (context.getNeedBreak()) {
                break;
            }
        }
        return context;
    }

}
