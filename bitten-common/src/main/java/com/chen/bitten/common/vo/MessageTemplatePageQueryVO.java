package com.chen.bitten.common.vo;

import com.chen.bitten.common.domain.persistence.MessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageTemplatePageQueryVO {

    private List<MessageTemplate> rows;

    private Long total;

}
