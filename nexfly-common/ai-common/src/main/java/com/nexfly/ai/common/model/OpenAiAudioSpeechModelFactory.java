package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.stereotype.Service;

@Service("openai-tts")
public class OpenAiAudioSpeechModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new OpenAiAudioSpeechModel(new OpenAiAudioApi(getApiKey()), OpenAiAudioSpeechOptions.builder().model(getModelName()).build());
    }

}
