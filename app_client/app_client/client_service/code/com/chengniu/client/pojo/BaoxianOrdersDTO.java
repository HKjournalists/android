package com.chengniu.client.pojo;

import java.io.Serializable;

public class BaoxianOrdersDTO implements Serializable {

    private String intentId;
    private String underwritingId;
    private String orderId;
    private String ordersNum;
    private boolean valid;
    private String msg;


    public String getIntentId() {
        return intentId;
    }

    public void setIntentId(String intentId) {
        this.intentId = intentId;
    }

    public String getUnderwritingId() {
        return underwritingId;
    }

    public void setUnderwritingId(String underwritingId) {
        this.underwritingId = underwritingId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(String ordersNum) {
        this.ordersNum = ordersNum;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}