package com.nexfly.common.core.utils;

import java.util.UUID;

/**
 * @Author wangjun
 * @Date 2024/9/26
 **/
public abstract class UuidUtil {

    public static String getSimpleUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
