package com.chen.bitten.common.vo;

import com.chen.bitten.common.enums.RespStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BasicResultVO<T> implements Serializable {

    /**
     * 响应状态
     * 比如：500
     */
    private String status;

    /**
     * 响应编码
     * 比如：服务器内部异常
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    // TODO 构造方法等未实现

    public BasicResultVO(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public BasicResultVO(RespStatusEnum status) {
        this(status, null);
    }

    public BasicResultVO(RespStatusEnum status, T data) {
        this(status, status.getMsg(), data);
    }

    public BasicResultVO(RespStatusEnum status, String msg, T data) {
        this(status.getCode(), msg, data);
    }

    public static BasicResultVO<Void> success() {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS);
    }

    public static <T> BasicResultVO<T> success(T data) {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, data);
    }

    public static <T> BasicResultVO<T> fail() {
        return new BasicResultVO<>(
                RespStatusEnum.FAIL,
                RespStatusEnum.FAIL.getMsg(),
                null
        );
    }

    public static <T> BasicResultVO<T> fail(String msg) {
        return fail(RespStatusEnum.FAIL, msg);
    }

    public static <T> BasicResultVO<T> fail(RespStatusEnum status) {
        return fail(status, status.getMsg());
    }

    public static <T> BasicResultVO<T> fail(RespStatusEnum status, String msg) {
        return new BasicResultVO<>(status, msg, null);
    }
}
