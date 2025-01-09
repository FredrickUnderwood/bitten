package com.chen.bitten.common.process;

public interface BusinessProcess <T extends ProcessModel>{

    /**
     * 真正执行业务的地方
     * @param context
     */
    void process(ProcessContext<T> context);
}
