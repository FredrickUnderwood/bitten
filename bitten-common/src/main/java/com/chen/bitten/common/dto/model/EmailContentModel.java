package com.chen.bitten.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailContentModel extends ContentModel {
    /**
     * 邮件标题
     */
    private String title;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件链接
     */
    private String url;
}
