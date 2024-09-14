package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.tongyi.TongYiEmbeddingModel;
import com.nexfly.ai.tongyi.TongYiEmbeddingOptions;
import com.nexfly.ai.tongyi.api.TongYiAiApi;
import com.nexfly.ai.tongyi.api.TongYiAiApiConstants;
import org.springframework.ai.document.MetadataMode;
import org.springframework.stereotype.Service;

@Service("tongyi-embedding")
public class TongYiTextEmbeddingModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new TongYiEmbeddingModel(new TongYiAiApi(TongYiAiApiConstants.DEFAULT_API_BASE_URL, getApiKey()), MetadataMode.EMBED, TongYiEmbeddingOptions.builder().withModel(getModelName()).build());
    }

}
