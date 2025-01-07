package com.nexfly.common.auth.utils;

import com.nexfly.common.auth.model.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public final class AuthUtils {

    public static UserInfo getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : (UserInfo) authentication.getPrincipal();
    }

    public static Long getUserId() {
        return Objects.requireNonNull(getUser()).getUserId();
    }

}
