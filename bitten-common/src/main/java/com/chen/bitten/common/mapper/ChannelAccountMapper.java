package com.chen.bitten.common.mapper;

import com.chen.bitten.common.domain.persistence.ChannelAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface ChannelAccountMapper {

    @Select("SELECT * FROM channel_account WHERE id = #{accountId}")
    ChannelAccount findById(Integer accountId);

}
