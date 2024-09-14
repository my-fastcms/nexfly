package com.nexfly.system.manager;

import com.nexfly.ai.common.model.CreateModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;

public interface ModelManager {

    ChatClient getChatClient(Long orgId, String modelName);

    ChatModel getChatModel(Long orgId, String modelName);

    EmbeddingModel getEmbeddingModel(Long orgId, String modelName);

    ImageModel getImageModel(Long orgId, String modelName);

    Object createModel(CreateModel createModel) throws Exception;

}
