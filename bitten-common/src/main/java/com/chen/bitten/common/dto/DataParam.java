package com.chen.bitten.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataParam {

    private String messageId;

    private String receiver;

    private String businessId;

    private Long dateTime;
}
