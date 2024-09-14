package com.nexfly.ai.tongyi.aot;

import com.nexfly.ai.tongyi.api.TongYiAiApi;
import com.nexfly.ai.tongyi.api.TongYiAiAudioApi;
import com.nexfly.ai.tongyi.api.TongYiAiImageApi;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static org.springframework.ai.aot.AiRuntimeHints.findJsonAnnotatedClassesInPackage;

public class TongYiAiRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@NonNull RuntimeHints hints, @Nullable ClassLoader classLoader) {
        var mcs = MemberCategory.values();
        for (var tr : findJsonAnnotatedClassesInPackage(TongYiAiApi.class))
            hints.reflection().registerType(tr, mcs);
        for (var tr : findJsonAnnotatedClassesInPackage(TongYiAiAudioApi.class))
            hints.reflection().registerType(tr, mcs);
        for (var tr : findJsonAnnotatedClassesInPackage(TongYiAiImageApi.class))
            hints.reflection().registerType(tr, mcs);
    }

}
