package com.chen.bitten.common.utils;

import com.chen.bitten.common.enums.BittenEnum;

import java.util.Arrays;

public class EnumUtils {

    public static <T extends BittenEnum> T getEnumByCode(Integer code, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst().orElse(null);
    }
}
