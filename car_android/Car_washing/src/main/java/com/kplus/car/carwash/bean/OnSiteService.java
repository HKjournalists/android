package com.kplus.car.carwash.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Fu on 2015/5/17.
 */
public class OnSiteService extends BaseInfo {
    private boolean isChecked = false;

    private long snapshotId;

    private long id;
    private long pid;
    private String name;
    private String desc;
    private List<ServicePicture> pictures;
    private BigDecimal marketPrice;
    private BigDecimal price;
    private int status;
    private int flag;
    private int weight;
    private int avgSpentTime;  // 评价服务时长

    private Timestamp updateTime;

    private long cityId;

    private String templateId;
    private String url;

    private String logo;
    private String poster;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ServicePicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ServicePicture> pictures) {
        this.pictures = pictures;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAvgSpentTime() {
        return avgSpentTime;
    }

    public void setAvgSpentTime(int avgSpentTime) {
        this.avgSpentTime = avgSpentTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
