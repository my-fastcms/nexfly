package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class AppConversation extends BaseModel implements Serializable {
    private Long conversationId;

    private String name;

    private Long appId;

    private static final long serialVersionUID = 1L;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", conversationId=").append(conversationId);
        sb.append(", name=").append(name);
        sb.append(", appId=").append(appId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}