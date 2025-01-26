package com.chen.bitten.server.service;

import com.chen.bitten.common.domain.persistence.ChannelAccount;

import java.util.List;

public interface ChannelAccountService {

    ChannelAccount saveOrUpdate(ChannelAccount channelAccount);

    List<ChannelAccount> queryByChannelType(Integer channelType, String creator);

    List<ChannelAccount> list(String creator);

    void deleteByIds(List<Long> ids);
}
