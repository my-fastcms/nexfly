package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.tongyi.TongYiImageModel;
import com.nexfly.ai.tongyi.TongYiImageOptions;
import com.nexfly.ai.tongyi.api.TongYiAiImageApi;
import org.springframework.stereotype.Service;

/**
 * 通义千问
 */
@Service("tongyi-qianwen-image")
public class TongYiImagesModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new TongYiImageModel(new TongYiAiImageApi(getApiKey()), TongYiImageOptions.builder().withN(1000).withHeight(1222).build());
    }

}
