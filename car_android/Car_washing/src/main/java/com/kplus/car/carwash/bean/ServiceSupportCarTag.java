package com.kplus.car.carwash.bean;

import java.sql.Timestamp;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/9/23.
 * <br/><br/>
 */
public class ServiceSupportCarTag extends BaseInfo {
    private long serviceId;
    private long tagId;
    private boolean deleted;

    private Timestamp updateTime;

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
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
