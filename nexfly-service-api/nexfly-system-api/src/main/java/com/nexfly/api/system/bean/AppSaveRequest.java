package com.nexfly.api.system.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/23
 **/
public class AppSaveRequest implements Serializable {
    Long appId;
    String description;
    String name;
    String icon;
    List<Long> datasetIds;
    String modelId;
    ModelConfig modelConfig;
    String rerankModelId;
    String ttsModelId;
    PromptConfig promptConfig;
    Float similarityThreshold;
    Integer topN;
    Float vectorSimilarityWeight;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Long> getDatasetIds() {
        return datasetIds;
    }

    public void setDatasetIds(List<Long> datasetIds) {
        this.datasetIds = datasetIds;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public ModelConfig getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public String getRerankModelId() {
        return rerankModelId;
    }

    public void setRerankModelId(String rerankModelId) {
        this.rerankModelId = rerankModelId;
    }

    public PromptConfig getPromptConfig() {
        return promptConfig;
    }

    public void setPromptConfig(PromptConfig promptConfig) {
        this.promptConfig = promptConfig;
    }

    public String getTtsModelId() {
        return ttsModelId;
    }

    public void setTtsModelId(String ttsModelId) {
        this.ttsModelId = ttsModelId;
    }

    public Float getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(Float similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public Integer getTopN() {
        return topN;
    }

    public void setTopN(Integer topN) {
        this.topN = topN;
    }

    public Float getVectorSimilarityWeight() {
        return vectorSimilarityWeight;
    }

    public void setVectorSimilarityWeight(Float vectorSimilarityWeight) {
        this.vectorSimilarityWeight = vectorSimilarityWeight;
    }

    public static class ModelConfig {

        private Double temperature;

        private Double topP;

        private Double presencePenalty;

        private Double frequencyPenalty;

        private Integer maxTokens;

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Double getTopP() {
            return topP;
        }

        public void setTopP(Double topP) {
            this.topP = topP;
        }

        public Double getPresencePenalty() {
            return presencePenalty;
        }

        public void setPresencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
        }

        public Double getFrequencyPenalty() {
            return frequencyPenalty;
        }

        public void setFrequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
        }

        public Integer getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
        }

    }

    public static class PromptConfig {
        /**
         * 空回复
         */
        String emptyResponse;

        /**
         * 变量
         * json array格式
         * 示例值：[{key: "knowledge", optional: false}]
         */
        List<Parameter> parameters;

        /**
         * 开场白
         */
        String prologue;

        /**
         * 是否显示文字引用
         */
        Boolean quote;

        /**
         * 系统设定
         */
        String system;

        public String getEmptyResponse() {
            return emptyResponse;
        }

        public void setEmptyResponse(String emptyResponse) {
            this.emptyResponse = emptyResponse;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }

        public String getPrologue() {
            return prologue;
        }

        public void setPrologue(String prologue) {
            this.prologue = prologue;
        }

        public Boolean getQuote() {
            return quote;
        }

        public void setQuote(Boolean quote) {
            this.quote = quote;
        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

    }

    public static class Parameter {
        String key;

        String variable;

        Boolean optional;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public Boolean getOptional() {
            return optional;
        }

        public void setOptional(Boolean optional) {
            this.optional = optional;
        }
    }

}
