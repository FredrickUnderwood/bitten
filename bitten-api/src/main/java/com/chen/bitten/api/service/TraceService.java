package com.chen.bitten.api.service;

import com.chen.bitten.common.dto.TraceResponse;

public interface TraceService {

    TraceResponse traceByMessageId(String messageId);

}
