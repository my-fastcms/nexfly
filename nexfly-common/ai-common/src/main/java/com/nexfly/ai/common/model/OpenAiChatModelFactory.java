package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

/**
 * open ai
 */
@Service("openai-chat")
public class OpenAiChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        OpenAiChatOptions.Builder builder = OpenAiChatOptions.builder().model(createModel.getModelName());

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

            if (jsonObject.get(FREQUENCYPENALTY) != null) {
                builder.frequencyPenalty(jsonObject.getDouble(FREQUENCYPENALTY));
            }

            if (jsonObject.get(PRESENCEPENALTY) != null) {
                builder.presencePenalty(jsonObject.getDouble(PRESENCEPENALTY));
            }

        }

        return new OpenAiChatModel(getOpenAiApi(), builder.build());
    }

    protected OpenAiApi getOpenAiApi() {
        OpenAiApi openAiApi = new OpenAiApi(getApiKey());
        if (StringUtils.isNotBlank(getApiUrl())) {
            openAiApi = new OpenAiApi(getApiUrl(), getApiKey());
        }
        return openAiApi;
    }

}
