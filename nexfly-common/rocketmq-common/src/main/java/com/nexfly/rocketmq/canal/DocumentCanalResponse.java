package com.nexfly.rocketmq.canal;

import cn.throwx.canal.gule.annotation.CanalModel;
import cn.throwx.canal.gule.common.FieldNamingPolicy;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
@CanalModel(database = "nexfly", table = "document", fieldNamingPolicy = FieldNamingPolicy.LOWER_UNDERSCORE)
public class DocumentCanalResponse implements Serializable {

    private Long documentId;

    private Long orgId;

    private Long datasetId;

    private Long fileId;

    private String name;

    /**
     * 创建时间
     */
    protected Date createAt;

    /**
     * 更新时间
     */
    protected Date updateAt;

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
        return "DocumentCanalResponse{" +
                "documentId=" + documentId +
                ", orgId=" + orgId +
                ", datasetId=" + datasetId +
                ", fileId=" + fileId +
                ", name='" + name + '\'' +
                '}';
    }
}
