package com.chen.bitten.api.service;

import com.chen.bitten.common.dto.BatchSendRequest;
import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;

/**
 * 统一发送接口
 */
public interface SendService {

    SendResponse send(SendRequest sendRequest);

    SendResponse batchSend(BatchSendRequest batchSendRequest);
}
