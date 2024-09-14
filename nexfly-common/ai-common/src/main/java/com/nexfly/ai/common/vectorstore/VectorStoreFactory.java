package com.nexfly.ai.common.vectorstore;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;

/**
 * 获取向量数据库实例工厂
 */
public interface VectorStoreFactory {

    VectorStore getVectorStore(String className, EmbeddingModel embeddingModel) throws Exception;

    VectorStore getVectorStore(EmbeddingModel embeddingModel) throws Exception;

}
