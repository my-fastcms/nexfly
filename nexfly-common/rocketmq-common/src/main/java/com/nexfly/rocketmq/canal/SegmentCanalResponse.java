package com.nexfly.rocketmq.canal;

import cn.throwx.canal.gule.annotation.CanalModel;
import cn.throwx.canal.gule.common.FieldNamingPolicy;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
@CanalModel(database = "nexfly", table = "document_segment", fieldNamingPolicy = FieldNamingPolicy.LOWER_UNDERSCORE)
public class SegmentCanalResponse implements Serializable {

    private Long segmentId;

    private Long documentId;

    private Long orgId;

    private Long datasetId;

    private String contentId;

    private String content;

    private String keywords;

    /**
     * 创建时间
     */
    protected Date createAt;

    /**
     * 更新时间
     */
    protected Date updateAt;

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Long datasetId) {
        this.datasetId = datasetId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "SegmentCanalResponse{" +
                "segmentId=" + segmentId +
                ", documentId=" + documentId +
                ", orgId=" + orgId +
                ", datasetId=" + datasetId +
                ", contentId='" + contentId + '\'' +
                ", content='" + content + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }

}
