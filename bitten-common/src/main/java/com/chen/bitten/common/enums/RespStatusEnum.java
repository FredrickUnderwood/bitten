package com.chen.bitten.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespStatusEnum {
    /**
     * 错误
     */
    ERROR_500("500", "服务器未知错误"),
    ERROR_400("400", "错误请求"),

    /**
     * OK：操作成功
     */
    SUCCESS("0", "操作成功"),
    FAIL("-1", "操作失败"),

    /**
     * 客户端
     */
    CLIENT_BAD_PARAMETERS("CL0001", "客户端参数错误"),
    TOO_MANY_RECEIVERS("CL0002", "Receiver数量过多"),
    MESSAGE_TEMPLATE_NOT_FOUND("CL0003", "消息模板不存在或已删除"),

    /**
     * 服务端
     */
    SERVER_ERROR("SE0001", "服务端异常"),


    /**
     * 流程
     */
    CONTEXT_NULL("PR0001", "流程上下文为空"),
    PROCESS_CODE_NULL("PR0002", "流程业务code为空"),
    PROCESS_TEMPLATE_NULL("PR0003", "流程模板为空"),
    BUSINESS_PROCESS_LIST_NULL("PR0004", "businessProcess列表为空")

    ;

    /**
     * 响应状态
     */
    private final String code;
    /**
     * 响应编码
     */
    private final String msg;

}
