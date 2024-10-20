package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.tongyi.TongYiChatModel;
import com.nexfly.ai.tongyi.TongYiChatOptions;
import com.nexfly.ai.tongyi.api.TongYiAiApi;
import org.springframework.stereotype.Service;

/**
 * 通义千问
 */
@Service("tongyi-qianwen-chat")
public class TongYiChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        TongYiChatOptions.Builder builder = TongYiChatOptions.builder().withModel(getModelName());

        if (jsonObject != null) {
            if (jsonObject.get(TEMPERATURE) != null) {
                builder.withTemperature(jsonObject.getDouble(TEMPERATURE));
            }

            if (jsonObject.get(TOPP) != null) {
                builder.withTopP(jsonObject.getDouble(TOPP));
            }

            if (jsonObject.get(MAXTOKENS) != null) {
                builder.withMaxTokens(jsonObject.getInteger(MAXTOKENS));
            }

            if (jsonObject.get(FREQUENCYPENALTY) != null) {
                builder.withFrequencyPenalty(jsonObject.getDouble(FREQUENCYPENALTY));
            }

            if (jsonObject.get(PRESENCEPENALTY) != null) {
                builder.withPresencePenalty(jsonObject.getDouble(PRESENCEPENALTY));
            }

        }

        return new TongYiChatModel(new TongYiAiApi(getApiKey()), builder.build());
    }

}
