package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class AccountOpenid extends BaseModel implements Serializable {
    private Long openidId;

    private String openid;

    private String clientId;

    private String username;

    private static final long serialVersionUID = 1L;

    public Long getOpenidId() {
        return openidId;
    }

    public void setOpenidId(Long openidId) {
        this.openidId = openidId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", openidId=").append(openidId);
        sb.append(", openid=").append(openid);
        sb.append(", clientId=").append(clientId);
        sb.append(", username=").append(username);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}