package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/19.
 */
public class CancelOrderResp extends BaseInfo {
    private int status;
    private String statusName;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
