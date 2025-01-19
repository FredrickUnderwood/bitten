package com.chen.bitten.common.dto;

import com.chen.bitten.common.domain.TraceAnchorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraceResponse {

    private String code;

    private String msg;

    private List<TraceAnchorInfo> data;
}
