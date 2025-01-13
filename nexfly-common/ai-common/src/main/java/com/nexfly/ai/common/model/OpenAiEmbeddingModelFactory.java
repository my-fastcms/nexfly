package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.stereotype.Service;

@Service("openai-embedding")
public class OpenAiEmbeddingModelFactory extends OpenAiChatModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new OpenAiEmbeddingModel(getOpenAiApi(), MetadataMode.EMBED, OpenAiEmbeddingOptions.builder().model(createModel.getModelName()).build());
    }

}
