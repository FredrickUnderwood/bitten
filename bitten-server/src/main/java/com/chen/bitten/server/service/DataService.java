package com.chen.bitten.server.service;

import com.chen.bitten.common.vo.UserTimeLineVO;

public interface DataService {

    UserTimeLineVO getMessageTrace(String messageId);

    UserTimeLineVO getUserTrace(String receiver);

}
