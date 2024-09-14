package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class AppModel extends BaseModel implements Serializable {
    private Long appModelId;

    private Long appId;

    private Long modelId;

    private String modelConfig;

    private static final long serialVersionUID = 1L;

    public Long getAppModelId() {
        return appModelId;
    }

    public void setAppModelId(Long appModelId) {
        this.appModelId = appModelId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(String modelConfig) {
        this.modelConfig = modelConfig;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", appModelId=").append(appModelId);
        sb.append(", appId=").append(appId);
        sb.append(", modelId=").append(modelId);
        sb.append(", modelConfig=").append(modelConfig);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}