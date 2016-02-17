package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by admin on 2015/6/3.
 */
public class ProviderInfo extends BaseModelObj {
    @ApiField("name")
    private String name;
    @ApiField("image")
    private String image;
    private String openUserId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }
}
