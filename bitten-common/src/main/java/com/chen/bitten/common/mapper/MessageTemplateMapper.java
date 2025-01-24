package com.chen.bitten.common.mapper;

import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.MessageTemplateParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageTemplateMapper {

    @Select("SELECT * FROM message_template WHERE id = #{id}")
    MessageTemplate findById(Long id);

    Long insert(MessageTemplate messageTemplate);

    void update(MessageTemplate messageTemplate);

    Page<MessageTemplate> queryPage(MessageTemplateParam messageTemplateParam);
}
