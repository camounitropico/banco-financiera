package com.banco_financiera.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumUtils {

    public static List<String> getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static boolean isStringInEnum(String value, Class<? extends Enum<?>> e) {
        List<String> enumNames = getEnumNames(e);
        return enumNames.contains(value.toUpperCase());
    }
}
