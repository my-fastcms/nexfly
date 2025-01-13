package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.stereotype.Service;

@Service("zhipu-embedding")
public class ZhipuEmbeddingModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new ZhiPuAiEmbeddingModel(new ZhiPuAiApi(getApiKey()), MetadataMode.EMBED,
                ZhiPuAiEmbeddingOptions.builder().model(createModel.getModelName()).build());
    }

}
