package com.nexfly.ai.common.model;

public class CreateModel {

    private Long orgId;
    private String providerName;
    private String modelName;
    private String modelType;
    private String modelConfig;
    private String config;

    public CreateModel(Long orgId, String providerName, String modelName, String modelType, String modelConfig, String config) {
        this.orgId = orgId;
        this.providerName = providerName;
        this.modelName = modelName;
        this.modelType = modelType;
        this.modelConfig = modelConfig;
        this.config = config;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(String modelConfig) {
        this.modelConfig = modelConfig;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
