package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class Provider extends BaseModel implements Serializable {
    private Long providerId;

    private Long orgId;

    private String label;

    private String providerName;

    private String config;

    private static final long serialVersionUID = 1L;

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", providerId=").append(providerId);
        sb.append(", orgId=").append(orgId);
        sb.append(", providerName=").append(providerName);
        sb.append(", config=").append(config);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}