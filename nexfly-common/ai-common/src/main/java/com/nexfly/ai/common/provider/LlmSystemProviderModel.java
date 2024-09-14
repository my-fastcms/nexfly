package com.nexfly.ai.common.provider;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class LlmSystemProviderModel extends AbstractSystemProviderModel implements SystemProviderModel {

    Boolean deprecated;

    List<String> features;

    ModelProperties modelProperties;

    List<ModelParameters> modelParameters;

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public ModelProperties getModelProperties() {
        return modelProperties;
    }

    public void setModelProperties(ModelProperties modelProperties) {
        this.modelProperties = modelProperties;
    }

    public List<ModelParameters> getModelParameters() {
        return modelParameters;
    }

    public void setModelParameters(List<ModelParameters> modelParameters) {
        this.modelParameters = modelParameters;
    }

}
