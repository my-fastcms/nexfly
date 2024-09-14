package com.nexfly.ai.common.provider;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class TtsSystemProviderModel extends AbstractSystemProviderModel implements SystemProviderModel {

    TtsProperties modelProperties;

    public TtsProperties getModelProperties() {
        return modelProperties;
    }

    public void setModelProperties(TtsProperties modelProperties) {
        this.modelProperties = modelProperties;
    }

}
