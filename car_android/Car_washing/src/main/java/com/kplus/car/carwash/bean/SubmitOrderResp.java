package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/18.
 */
public class SubmitOrderResp extends BaseInfo {
    private long orderId;
    private long formOrderId;

    // true: 发现重复订单， false:非重复
    private boolean duplicated;
    private int status;
    private double price;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getFormOrderId() {
        return formOrderId;
    }

    public void setFormOrderId(long formOrderId) {
        this.formOrderId = formOrderId;
    }

    public boolean isDuplicated() {
        return duplicated;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
