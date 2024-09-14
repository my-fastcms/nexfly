package com.nexfly.ai.common.provider;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class EmbedSystemProviderModel extends AbstractSystemProviderModel implements SystemProviderModel {

    EmbedProperties modelProperties;

    public EmbedProperties getModelProperties() {
        return modelProperties;
    }

    public void setModelProperties(EmbedProperties modelProperties) {
        this.modelProperties = modelProperties;
    }

}
