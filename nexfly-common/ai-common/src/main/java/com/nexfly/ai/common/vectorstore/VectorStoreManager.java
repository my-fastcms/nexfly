package com.nexfly.ai.common.vectorstore;

import com.nexfly.common.core.utils.ApplicationUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/9/27
 **/
@Service
public class VectorStoreManager implements InitializingBean {

    @Value("${vector-store.type:}")
    private String vectorStoreType;

    final String WEAVIATE_FACTORY = "weaviateVectorStoreFactory";

    final String ELASTICSEARCH_FACTORY = "elasticsearchVectorStoreFactory";

    Map<String, VectorStoreFactory> vectorStoreFactoryMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, VectorStoreFactory> beansOfType = ApplicationUtils.getBeansOfType(VectorStoreFactory.class);
        vectorStoreFactoryMap.putAll(beansOfType);
    }

    public VectorStoreFactory getVectorStoreFactory() {
        if (vectorStoreType.equals("weaviate")) {
            return vectorStoreFactoryMap.get(WEAVIATE_FACTORY);
        }
        if (vectorStoreType.equals("elasticsearch")) {
            return vectorStoreFactoryMap.get(ELASTICSEARCH_FACTORY);
        }
        return vectorStoreFactoryMap.get(WEAVIATE_FACTORY);
    }

}
