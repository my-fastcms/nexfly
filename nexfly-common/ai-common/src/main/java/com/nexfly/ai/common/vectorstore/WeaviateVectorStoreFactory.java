package com.nexfly.ai.common.vectorstore;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateAuthClient;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.v1.schema.model.DataType;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WeaviateVectorStoreFactory implements VectorStoreFactory {

    @Value("${vector-store.weaviate.host:}")
    private String host;

    @Value("${vector-store.weaviate.key:}")
    private String apiKey;

    @Override
    public VectorStore getVectorStore(String className, EmbeddingModel embeddingModel) throws Exception {
        WeaviateClient weaviateClient = WeaviateAuthClient.apiKey(new Config("http", host), apiKey);
        // weaviateClient.schema().classDeleter().withClassName("SpringAiWeaviate").run();
        // 定义属性
        List<Property> properties = new ArrayList<>();
        properties.add(Property.builder().name("document_id").dataType(Arrays.stream(new String[] {DataType.UUID}).toList()).build());
        properties.add(Property.builder().name("dateset_id").dataType(Arrays.stream(new String[] {DataType.UUID}).toList()).build());
        properties.add(Property.builder().name("content").dataType(Arrays.stream(new String[] {DataType.TEXT}).toList()).build());
        properties.add(Property.builder().name("metadata").dataType(Arrays.stream(new String[] {DataType.TEXT}).toList()).build());
        // 定义一个新的 Class
        WeaviateClass newClass = WeaviateClass.builder().properties(properties).className(className).build();
        // 创建 schema
        if (!weaviateClient.schema().exists().withClassName(className).run().getResult()) {
            weaviateClient.schema().classCreator().withClass(newClass).run();
        }
        return WeaviateVectorStore.builder(weaviateClient, embeddingModel).objectClass(className).consistencyLevel(WeaviateVectorStore.ConsistentLevel.ONE).build();
    }

    @Override
    public VectorStore getVectorStore(EmbeddingModel embeddingModel) throws Exception {
        return getVectorStore("SpringAiWeaviate", embeddingModel);
    }

}
