package com.chen.bitten.common.domain.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelAccount {
    private Long id;
    private String name;
    private Integer sendChannel;
    private String accountConfig;
    private Integer isDeleted;
    private String creator;
    private String createdTime;
    private String updatedTime;
}
