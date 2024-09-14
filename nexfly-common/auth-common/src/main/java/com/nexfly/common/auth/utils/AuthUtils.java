package com.nexfly.common.auth.utils;

import com.nexfly.common.auth.model.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtils {

    public static UserInfo getUser() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

}
