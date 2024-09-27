package com.nexfly.ai.common.model;

public class CreateModel {

    private String apiKey;
    private String apiUrl;
    private String providerName;
    private String modelName;
    private String modelType;
    private String config;

    public CreateModel(String apiKey, String apiUrl, String providerName, String modelName, String modelType, String config) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.providerName = providerName;
        this.modelName = modelName;
        this.modelType = modelType;
        this.config = config;
    }

    public CreateModel(String appKey, String apiUrl, String providerName, String modelName, String modelType) {
        this(appKey, apiUrl, providerName, modelName, modelType, null);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
