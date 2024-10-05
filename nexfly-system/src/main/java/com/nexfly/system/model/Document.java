package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;

import java.io.Serializable;

public class Document extends BaseModel implements Serializable {
    private Long documentId;

    private Long orgId;

    private Long datasetId;

    private Long fileId;

    private String name;

    private Integer dataSource;

    private Integer wordCount;

    private Integer tokenCount;

    private String dataUrl;

    private String processType;

    private Integer processStatus;

    private Integer status;

    private static final long serialVersionUID = 1L;

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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", documentId=").append(documentId);
        sb.append(", orgId=").append(orgId);
        sb.append(", datasetId=").append(datasetId);
        sb.append(", name=").append(name);
        sb.append(", dataSource=").append(dataSource);
        sb.append(", wordCount=").append(wordCount);
        sb.append(", tokenCount=").append(tokenCount);
        sb.append(", dataUrl=").append(dataUrl);
        sb.append(", processType=").append(processType);
        sb.append(", processStatus=").append(processStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}