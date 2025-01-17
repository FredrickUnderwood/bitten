package com.chen.bitten.common.dto.model;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JSONType(typeName = "EmailContentModel")
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
