package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

import java.io.Serializable;

public class Advert extends BaseModelObj implements Serializable {
    @ApiField("id")
    private Long id;
    @ApiField("imgUrl")
    private String imgUrl;
    @ApiField("motionType")
    private String motionType;
    @ApiField("motionValue")
    private String motionValue;
    @ApiField("seq")
    private Integer seq;
    @ApiField("canClose")
    private Boolean canClose;
    @ApiField("identity")
    private String identity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMotionType() {
        return motionType;
    }

    public void setMotionType(String motionType) {
        this.motionType = motionType;
    }

    public String getMotionValue() {
        return motionValue;
    }

    public void setMotionValue(String motionValue) {
        this.motionValue = motionValue;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Boolean getCanClose() {
        return canClose;
    }

    public void setCanClose(Boolean canClose) {
        this.canClose = canClose;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
