package com.kplus.car.carwash.bean;

import java.sql.Timestamp;

/**
 * Created by Fu on 2015/5/17.
 */
public class City extends BaseInfo {
    private long id;
    private String code;
    private String name;
    private long provinceId;
    private String provinceName;
    private int status;
    private boolean hot;
    private Timestamp updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("id=").append(id);
        buffer.append("，name=").append(name);
        buffer.append("，provinceId=").append(provinceId);
        buffer.append("，provinceName=").append(provinceName);
        buffer.append("，status=").append(status);
        return buffer.toString();
    }
}
