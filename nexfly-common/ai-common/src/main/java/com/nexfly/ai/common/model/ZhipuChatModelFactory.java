package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.stereotype.Service;

/**
 * 智谱
 */
@Service("zhipu-chat")
public class ZhipuChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        ZhiPuAiChatOptions.Builder builder = ZhiPuAiChatOptions.builder().model(createModel.getModelName());
        if (jsonObject != null) {
            if (jsonObject.get(TEMPERATURE) != null) {
                builder.temperature(jsonObject.getDouble(TEMPERATURE));
            }

            if (jsonObject.get(TOPP) != null) {
                builder.topP(jsonObject.getDouble(TOPP));
            }

            if (jsonObject.get(MAXTOKENS) != null) {
                builder.maxTokens(jsonObject.getInteger(MAXTOKENS));
            }

        }
        return new ZhiPuAiChatModel(new ZhiPuAiApi(getApiKey()), builder.build());
    }

}
