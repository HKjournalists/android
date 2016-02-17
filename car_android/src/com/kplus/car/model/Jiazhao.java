package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/5/22.
 */
public class Jiazhao extends BaseModelObj {
    @ApiField("id")
    private String id;
    @ApiField("jszh")
    private String jszh;
    @ApiField("xm")
    private String xm;
    @ApiField("startTime")
    private String startTime;
    @ApiField("isHidden")
    private String isHidden;
    @ApiField("space")
    private Integer space;
    @ApiField("dabh")
    private String dabh;
    @ApiField("endTime")
    private String date;
    @ApiField("remindDate")
    private String remindDate;
    @ApiField("ljjf")
    private Integer ljjf = 0;
    @ApiField("zjcx")
    private String zjcx;
    private Integer dbid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJszh() {
        return jszh;
    }

    public void setJszh(String jszh) {
        this.jszh = jszh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public String getDabh() {
        return dabh;
    }

    public void setDabh(String dabh) {
        this.dabh = dabh;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }

    public Integer getLjjf() {
        return ljjf;
    }

    public void setLjjf(Integer ljjf) {
        this.ljjf = ljjf;
    }

    public String getZjcx() {
        return zjcx;
    }

    public void setZjcx(String zjcx) {
        this.zjcx = zjcx;
    }

    public Integer getDbid() {
        return dbid;
    }

    public void setDbid(Integer dbid) {
        this.dbid = dbid;
    }
}
