package com.nexfly.common.model;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseModel implements Serializable {

    /**
     * 创建时间
     */
    protected Date createAt;

    /**
     * 更新时间
     */
    protected Date updateAt;

    /**
     * 创建者
     */
    protected Long createBy;

    /**
     * 更新者
     */
    protected Long updateBy;

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

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

}
