package com.nexfly.ai.common.provider;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class AbstractSystemProviderModel implements SystemProviderModel {

    protected String provider;
    protected String model;
    protected String label;
    protected String modelType;
    protected Pricing pricing;

    @Override
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

}
