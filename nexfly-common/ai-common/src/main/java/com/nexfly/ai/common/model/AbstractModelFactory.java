package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public abstract class AbstractModelFactory implements ModelFactory {

    protected final static String TEMPERATURE = "temperature";

    protected final static String TOPP = "topP";

    protected final static String MAXTOKENS = "maxTokens";

    protected final static String FREQUENCYPENALTY = "frequencyPenalty";

    protected final static String PRESENCEPENALTY = "presencePenalty";

    protected String apiKey;

    protected String apiUrl;

    protected String modelName;

    @Override
    public Object getInstance(CreateModel createModel) throws Exception {
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(createModel.getConfig());
        this.apiKey = createModel.getApiKey();
        this.apiUrl = createModel.getApiUrl();
        this.modelName = createModel.getModelName();
        return doCreate(createModel, jsonObject);
    }

    abstract Object doCreate(CreateModel createModel, JSONObject jsonObject);

    protected String getApiKey() {
        return apiKey;
    }

    protected String getApiUrl() {
        return apiUrl;
    }

    protected String getModelName() {
        return modelName;
    }

}
