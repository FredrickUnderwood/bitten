package com.chen.bitten.common.utils;

import com.chen.bitten.common.enums.BittenEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumUtils {

    public static <T extends BittenEnum> T getEnumByCode(Integer code, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst().orElse(null);
    }

    public static <T extends BittenEnum> List<Integer> getCodeList(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(BittenEnum::getCode)
                .collect(Collectors.toList());
    }
}
