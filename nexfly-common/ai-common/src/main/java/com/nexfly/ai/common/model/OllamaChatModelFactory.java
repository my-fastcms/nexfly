package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

/**
 * ollama
 */
@Service("ollama-chat")
public class OllamaChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return OllamaChatModel.builder()
                .withOllamaApi(new OllamaApi(getApiUrl()))
                .withDefaultOptions(OllamaOptions.create().withModel(createModel.getModelName()).withTemperature(0.9))
                .build();
    }

}
