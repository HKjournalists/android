package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/20.
 */
public class OrderPayment extends BaseInfo {
    private int payType;
    private String payName;
    private String payDesc;
    private int payIcon;
    private boolean isCheck;
    private boolean isEnabled;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public int getPayIcon() {
        return payIcon;
    }

    public void setPayIcon(int payIcon) {
        this.payIcon = payIcon;
    }
}
