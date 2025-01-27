package com.chen.bitten.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTimeLineVO {

    List<UserTimeLineVO.ItemsVO> items;

    @Data
    @Builder
    public static class ItemsVO {
        private String businessId;
        /**
         * title 模板名称
         */
        private String title;
        /**
         * detail 发送细节
         */
        private String detail;
        /**
         * 发送类型
         */
        private String sendType;
        /**
         * 模板创建者
         */
        private String creator;
    }
}
