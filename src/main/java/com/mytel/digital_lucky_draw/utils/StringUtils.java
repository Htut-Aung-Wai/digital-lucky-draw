package com.mytel.digital_lucky_draw.utils;

public class StringUtils {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
