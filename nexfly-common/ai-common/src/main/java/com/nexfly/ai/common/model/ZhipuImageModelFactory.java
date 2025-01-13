package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiImageApi;
import org.springframework.stereotype.Service;

@Service("zhipu-image")
public class ZhipuImageModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new ZhiPuAiImageModel(new ZhiPuAiImageApi(getApiKey()), ZhiPuAiImageOptions.builder().model(createModel.getModelName()).build(), RetryUtils.DEFAULT_RETRY_TEMPLATE);
    }

}
