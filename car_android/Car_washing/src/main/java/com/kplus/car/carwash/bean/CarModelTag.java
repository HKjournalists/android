package com.kplus.car.carwash.bean;

import java.sql.Timestamp;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/9/23.
 * <br/><br/>
 */
public class CarModelTag extends BaseInfo {
    private long tagId;
    private long modelId;
    private boolean deleted;
    private Timestamp updateTime;

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
