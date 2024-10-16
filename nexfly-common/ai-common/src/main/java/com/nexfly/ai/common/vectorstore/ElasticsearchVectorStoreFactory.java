package com.nexfly.ai.common.vectorstore;

import org.elasticsearch.client.RestClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.ElasticsearchVectorStoreOptions;
import org.springframework.ai.vectorstore.SimilarityFunction;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Author wangjun
 * @Date 2024/9/27
 **/
@Service
@Primary
public class ElasticsearchVectorStoreFactory implements VectorStoreFactory {

    @Autowired
    private RestClient restClient;

    public static final String SEGMENT_INDEX = "segment_index";

    @Override
    public VectorStore getVectorStore(String className, EmbeddingModel embeddingModel) throws Exception {
        ElasticsearchVectorStoreOptions options = new ElasticsearchVectorStoreOptions();
        options.setIndexName(className);
        options.setSimilarity(SimilarityFunction.dot_product);
        return new ElasticsearchVectorStore(options, restClient, embeddingModel, true);
    }

    @Override
    public VectorStore getVectorStore(EmbeddingModel embeddingModel) throws Exception {
        return getVectorStore(SEGMENT_INDEX, embeddingModel);
    }

}
