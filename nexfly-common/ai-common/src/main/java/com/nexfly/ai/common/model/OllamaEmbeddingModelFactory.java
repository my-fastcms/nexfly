package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

@Service("ollama-embedding")
public class OllamaEmbeddingModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return OllamaEmbeddingModel.builder()
                .withOllamaApi(new OllamaApi(getApiUrl()))
                .withDefaultOptions(OllamaOptions.create().withModel(createModel.getModelName()))
                .build();
    }

}
