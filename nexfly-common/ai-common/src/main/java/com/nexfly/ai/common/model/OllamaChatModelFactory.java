package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

/**
 * ollama
 */
@Service("ollama-llm")
public class OllamaChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new OllamaChatModel(new OllamaApi(getApiUrl()), OllamaOptions.builder().withModel(createModel.getModelName()).build());
    }

}
