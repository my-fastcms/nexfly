package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.tongyi.TongYiChatModel;
import com.nexfly.ai.tongyi.TongYiChatOptions;
import com.nexfly.ai.tongyi.api.TongYiAiApi;
import org.springframework.stereotype.Service;

/**
 * 通义千问
 */
@Service("tongyi-chat")
public class TongYiChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        TongYiChatOptions.Builder builder = TongYiChatOptions.builder().withModel(getModelName()).withTemperature(0.7);

        if (jsonObject != null) {
            if (jsonObject.get("temperature") != null) {
                builder.withTemperature(jsonObject.getDouble("temperature"));
            }
        }

        return new TongYiChatModel(new TongYiAiApi(getApiKey()), builder.build());
    }

}
