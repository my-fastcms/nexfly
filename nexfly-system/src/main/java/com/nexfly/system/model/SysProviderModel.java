package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class SysProviderModel extends BaseModel implements Serializable {
    private Long sysProviderModelId;

    private String modelName;

    private String modelType;

    private String providerName;

    private Integer maxTokens;

    private String tags;

    private String status;

    private static final long serialVersionUID = 1L;

    public Long getSysProviderModelId() {
        return sysProviderModelId;
    }

    public void setSysProviderModelId(Long sysProviderModelId) {
        this.sysProviderModelId = sysProviderModelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sysProviderModelId=").append(sysProviderModelId);
        sb.append(", modelName=").append(modelName);
        sb.append(", modelType=").append(modelType);
        sb.append(", providerName=").append(providerName);
        sb.append(", maxTokens=").append(maxTokens);
        sb.append(", tags=").append(tags);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}