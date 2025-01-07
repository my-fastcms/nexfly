package com.nexfly.common.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class UserInfo extends User {

    private final Long userId;

    public UserInfo(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public UserInfo(Long userId, String username, String password) {
        this(userId, username, password, Collections.synchronizedCollection(new ArrayList<>()));
    }

    public Long getUserId() {
        return userId;
    }

}
