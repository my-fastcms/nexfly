package com.nexfly.ai.common.provider;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/23
 **/
public class SystemProvider {

    String provider;
    String label;
    String description;
    String iconSmall;
    String iconLarge;
    String background;
    Help help;
    List<String> supportedModelTypes;
    List<String> configurateMethods;
    List<ModelCredential> modelCredentials;

    /**
     * 模型提供商对应的模型列表
     */
    List<SystemProviderModel> providerModelList;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconSmall() {
        return iconSmall;
    }

    public void setIconSmall(String iconSmall) {
        this.iconSmall = iconSmall;
    }

    public String getIconLarge() {
        return iconLarge;
    }

    public void setIconLarge(String iconLarge) {
        this.iconLarge = iconLarge;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Help getHelp() {
        return help;
    }

    public void setHelp(Help help) {
        this.help = help;
    }

    public List<String> getSupportedModelTypes() {
        return supportedModelTypes;
    }

    public void setSupportedModelTypes(List<String> supportedModelTypes) {
        this.supportedModelTypes = supportedModelTypes;
    }

    public List<String> getConfigurateMethods() {
        return configurateMethods;
    }

    public void setConfigurateMethods(List<String> configurateMethods) {
        this.configurateMethods = configurateMethods;
    }

    public List<ModelCredential> getModelCredentials() {
        return modelCredentials;
    }

    public void setModelCredentials(List<ModelCredential> modelCredentials) {
        this.modelCredentials = modelCredentials;
    }

    public List<SystemProviderModel> getProviderModelList() {
        return providerModelList;
    }

    public void setProviderModelList(List<SystemProviderModel> providerModelList) {
        this.providerModelList = providerModelList;
    }

    public static class Help {
        String title;
        String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ModelCredential {
        String variable;
        String label;
        String type;
        Boolean required;
        String placeholder;

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }
    }

}
