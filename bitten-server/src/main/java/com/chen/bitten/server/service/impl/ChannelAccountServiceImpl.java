package com.chen.bitten.server.service.impl;

import cn.hutool.core.date.DateUtil;
import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.domain.persistence.ChannelAccount;
import com.chen.bitten.common.mapper.ChannelAccountMapper;
import com.chen.bitten.server.service.ChannelAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ChannelAccountServiceImpl implements ChannelAccountService {

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    @Override
    public ChannelAccount saveOrUpdate(ChannelAccount channelAccount) {
        channelAccount.setUpdatedTime(Math.toIntExact(DateUtil.currentSeconds()));
        if (Objects.isNull(channelAccount.getId())) {
            channelAccount.setCreator(channelAccount.getCreator().isBlank() ? BittenConstant.DEFAULT_CREATOR : channelAccount.getCreator());
            channelAccount.setCreatedTime(Math.toIntExact(DateUtil.currentSeconds()));
            channelAccount.setIsDeleted(0);
            Long id = channelAccountMapper.insert(channelAccount);
            channelAccount.setId(id);
        } else {
            channelAccountMapper.update(channelAccount);
        }
        return channelAccount;
    }

    @Override
    public List<ChannelAccount> queryByChannelType(Integer channelType, String creator) {
        return channelAccountMapper.queryByChannelType(channelType, creator);
    }

    @Override
    public List<ChannelAccount> list(String creator) {
        return channelAccountMapper.list(creator);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        channelAccountMapper.deleteByIds(ids);
    }
}
