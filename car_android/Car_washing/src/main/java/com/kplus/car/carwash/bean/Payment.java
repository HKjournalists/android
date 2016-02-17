package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/19.
 */
public class Payment extends BaseInfo {
    private Long id;

    private Long orderId;

    private Integer payType;

    private Float money;

    private Float cash;

    private String payNum;

    private String payUser;

    private Integer status;

    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getCash() {
        return cash;
    }

    public void setCash(Float cash) {
        this.cash = cash;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
