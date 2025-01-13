package com.nexfly.ai.common.vectorstore;

import org.elasticsearch.client.RestClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStoreOptions;
import org.springframework.ai.vectorstore.elasticsearch.SimilarityFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import static com.nexfly.common.core.constants.NexflyConstants.SEGMENT_INDEX;

/**
 * @Author wangjun
 * @Date 2024/9/27
 **/
@Service
@Primary
public class ElasticsearchVectorStoreFactory implements VectorStoreFactory {

    @Autowired
    private RestClient restClient;

    @Override
    public VectorStore getVectorStore(String className, EmbeddingModel embeddingModel) throws Exception {
        ElasticsearchVectorStoreOptions options = new ElasticsearchVectorStoreOptions();
        options.setIndexName(className);
        options.setSimilarity(SimilarityFunction.dot_product);
        ElasticsearchVectorStore elasticsearchVectorStore = ElasticsearchVectorStore.builder(restClient, embeddingModel).options(options).initializeSchema(true).build();
        elasticsearchVectorStore.afterPropertiesSet();
        return elasticsearchVectorStore;
    }

    @Override
    public VectorStore getVectorStore(EmbeddingModel embeddingModel) throws Exception {
        return getVectorStore(SEGMENT_INDEX, embeddingModel);
    }

}
