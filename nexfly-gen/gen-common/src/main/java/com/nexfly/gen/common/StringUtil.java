package com.nexfly.gen.common;

public final class StringUtil {

    public static String firstLetterToLower(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

}
