package com.nexfly.api.system.bean;

import java.io.Serializable;

/**
 * @author wangjun
 **/
public class Oauth2UserInfo implements Serializable {

    String clientId;

    String openid;

    String username;

    String nickname;

    String picture;

    public Oauth2UserInfo(String clientId, String openid, String username, String nickname, String picture) {
        this.clientId = clientId;
        this.openid = openid;
        this.username = username;
        this.nickname = nickname;
        this.picture = picture;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
