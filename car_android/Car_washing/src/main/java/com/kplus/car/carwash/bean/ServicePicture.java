package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/17.
 */
public class ServicePicture extends BaseInfo {
    private long id;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
