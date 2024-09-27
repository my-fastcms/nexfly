package com.nexfly.api.system.bean;

/**
 * @Author wangjun
 * @Date 2024/9/23
 **/
public class AppModelInfo {

    private Long appModelId;
    private Long appId;
    private Long modelId;
    private String modelConfig;
    private String providerName;
    private String modelName;
    private String modelType;

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

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

}
