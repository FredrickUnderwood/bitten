package com.chen.bitten.common.mapper;

import com.chen.bitten.common.domain.persistence.ChannelAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ChannelAccountMapper {

    @Select("SELECT * FROM channel_account WHERE id = #{accountId}")
    ChannelAccount findById(Integer accountId);

    Long insert(ChannelAccount channelAccount);

    void update(ChannelAccount channelAccount);

    List<ChannelAccount> queryByChannelType(@Param("channelType") Integer channelType, @Param("creator") String creator);

    List<ChannelAccount> list(@Param("creator") String creator);

    void deleteByIds(@Param("ids") List<Long> ids);
}
