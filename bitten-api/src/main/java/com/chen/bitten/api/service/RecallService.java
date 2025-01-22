package com.chen.bitten.api.service;

import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;

public interface RecallService {

    /**
     * 传入messageTemplateId，recall该模板的所有信息
     * 传入recallMessageIdList，recall该列表中的message
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}
