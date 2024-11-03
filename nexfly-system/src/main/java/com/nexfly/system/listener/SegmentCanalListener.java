package com.nexfly.system.listener;

import cn.throwx.canal.gule.model.CanalBinLogResult;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.nexfly.rocketmq.canal.SegmentCanalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
@Component
public class SegmentCanalListener extends BaseCanalBinlogEventProcessor<SegmentCanalResponse> {

    public static final String SEGMENT_INDEX = "segment_index";

    private static final Logger log = LoggerFactory.getLogger(SegmentCanalListener.class);

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    protected void processInsertInternal(CanalBinLogResult<SegmentCanalResponse> segmentResult) {
        SegmentCanalResponse beforeData = segmentResult.getBeforeData();
        if (Objects.isNull(beforeData.getContent())) {
            return;
        }
        try {
            syncSegmentToElasticsearch(segmentResult.getAfterData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param result
     */
    @Override
    protected void processUpdateInternal(CanalBinLogResult<SegmentCanalResponse> result) {
        SegmentCanalResponse beforeData = result.getBeforeData();
        if (Objects.isNull(beforeData.getContent())) {
            return;
        }
        SegmentCanalResponse afterData = result.getAfterData();
        log.info(afterData.toString());
        try {
            processUpdateInternal(afterData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 同步数据到Elasticsearch的插入操作
    private void syncSegmentToElasticsearch(SegmentCanalResponse segment) throws IOException {
        IndexRequest<SegmentCanalResponse> request = IndexRequest.of(i -> i
                .index(SEGMENT_INDEX)  // 索引名称
                .id(String.valueOf(segment.getContentId()))  // 使用contentId作为Elasticsearch文档ID
                .document(segment)  // 文档内容，直接传入对象，Elasticsearch Java API Client会序列化为JSON
        );
        IndexResponse response = elasticsearchClient.index(request);
        log.info("Data inserted into Elasticsearch with ID: {}", response.id());
    }

    // 更新Elasticsearch中的数据
    private void processUpdateInternal(SegmentCanalResponse segment) throws IOException {
        UpdateRequest<SegmentCanalResponse, SegmentCanalResponse> request = UpdateRequest.of(u -> u
                .index(SEGMENT_INDEX)  // 索引名称
                .id(String.valueOf(segment.getContentId()))  // 文档ID
                .doc(segment)  // 更新的文档内容
                .docAsUpsert(true)  // 如果文档不存在，则插入
        );
        UpdateResponse<SegmentCanalResponse> response = elasticsearchClient.update(request, SegmentCanalResponse.class);
        log.info("Data updated in Elasticsearch with ID: {}", response.id());
    }

}
