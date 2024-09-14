package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.stereotype.Service;

/**
 * text 2 audio or audio 2 text
 */
@Service("openai-att")
public class OpenAiAudioTranscriptionModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        var openAiAudioApi = new OpenAiAudioApi(getApiKey());
        return new OpenAiAudioTranscriptionModel(openAiAudioApi);
    }

}
