package com.nexfly.system.manager;

import com.nexfly.ai.common.model.CreateModel;
import com.nexfly.system.service.ProviderService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;

public interface ModelManager {

    ChatModel getChatModel(Long appId);

    EmbeddingModel getEmbeddingModel(Long datasetId);

    ImageModel getImageModel(Long appId);

    Object createModel(CreateModel createModel) throws Exception;

    Boolean checkApiKey(ProviderService.ApiKeyRequest apiKey);

}
