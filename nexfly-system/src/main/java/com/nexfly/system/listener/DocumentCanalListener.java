package com.nexfly.system.listener;

import cn.throwx.canal.gule.model.CanalBinLogResult;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.nexfly.rocketmq.canal.DocumentCanalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

import static com.nexfly.common.core.constants.NexflyConstants.DOCUMENT_INDEX;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
@Component
public class DocumentCanalListener extends BaseCanalBinlogEventProcessor<DocumentCanalResponse> {

    private static final Logger log = LoggerFactory.getLogger(DocumentCanalListener.class);

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    protected void processInsertInternal(CanalBinLogResult<DocumentCanalResponse> documentResult) {
        try {
            syncDocumentToElasticsearch(documentResult.getAfterData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param result
     */
    @Override
    protected void processUpdateInternal(CanalBinLogResult<DocumentCanalResponse> result) {
        DocumentCanalResponse beforeData = result.getBeforeData();
        if (Objects.isNull(beforeData.getName())) {
            return;
        }
        DocumentCanalResponse afterData = result.getAfterData();
        log.info(afterData.toString());
        try {
            processUpdateInternal(afterData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void syncDocumentToElasticsearch(DocumentCanalResponse document) throws IOException {
        IndexRequest<DocumentCanalResponse> request = IndexRequest.of(i -> i
                .index(DOCUMENT_INDEX.concat(String.valueOf(document.getOrgId())))  // 索引名称
                .id(String.valueOf(document.getDocumentId()))  // 使用segmentId作为Elasticsearch文档ID
                .document(document)  // 文档内容，直接传入对象，Elasticsearch Java API Client会序列化为JSON
        );
        IndexResponse response = elasticsearchClient.index(request);
        log.info("Data inserted into Elasticsearch with ID: {}", response.id());
    }

    // 更新Elasticsearch中的数据
    private void processUpdateInternal(DocumentCanalResponse document) throws IOException {
        UpdateRequest<DocumentCanalResponse, DocumentCanalResponse> request = UpdateRequest.of(u -> u
                .index(DOCUMENT_INDEX.concat(String.valueOf(document.getOrgId())))  // 索引名称
                .id(String.valueOf(document.getDocumentId()))  // 文档ID
                .doc(document)  // 更新的文档内容
                .docAsUpsert(true)  // 如果文档不存在，则插入
        );
        UpdateResponse<DocumentCanalResponse> response = elasticsearchClient.update(request, DocumentCanalResponse.class);
        log.info("Data updated in Elasticsearch with ID: {}", response.id());
    }

}
