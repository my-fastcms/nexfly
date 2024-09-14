package com.nexfly.common.auth.token;

import java.io.Serializable;

public class AccessToken implements Serializable {

    private long tokenValidityInSeconds;

    private String token;

    public AccessToken(long tokenValidityInSeconds, String token) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
        this.token = token;
    }

    public long getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }

    public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
