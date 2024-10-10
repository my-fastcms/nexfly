package com.nexfly.ai.common.vectorstore;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.ElasticsearchVectorStoreOptions;
import org.springframework.ai.vectorstore.SimilarityFunction;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Author wangjun
 * @Date 2024/9/27
 **/
@Service
@Primary
public class ElasticsearchVectorStoreFactory implements VectorStoreFactory {

    @Value("${vector-store.elasticsearch.host:}")
    private String host;

    @Value("${vector-store.elasticsearch.username:}")
    private String username;

    @Value("${vector-store.elasticsearch.password:}")
    private String password;

    @Override
    public VectorStore getVectorStore(String className, EmbeddingModel embeddingModel) throws Exception {
        ElasticsearchVectorStoreOptions options = new ElasticsearchVectorStoreOptions();
        options.setIndexName(className);
        options.setSimilarity(SimilarityFunction.dot_product);
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        RestClient restClient = RestClient.builder(HttpHost.create(host)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)).build();
        return new ElasticsearchVectorStore(options, restClient, embeddingModel, true);
    }

    @Override
    public VectorStore getVectorStore(EmbeddingModel embeddingModel) throws Exception {
        return getVectorStore("index_dot_nexfly", embeddingModel);
    }

}
