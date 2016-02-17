package com.chengniu.client.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付初始化结果
 */
public class BaoxianPayDTO implements Serializable {

    private boolean result;

    private BigDecimal amount;

    private String quoteId;
    private String payinfoid;
    private String insOrg;
    private String bizId;
    private String areaCode;
    private String bankArea;

    private String payUrl;
    private String notifyUrl;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getPayinfoid() {
        return payinfoid;
    }

    public void setPayinfoid(String payinfoid) {
        this.payinfoid = payinfoid;
    }

    public String getInsOrg() {
        return insOrg;
    }

    public void setInsOrg(String insOrg) {
        this.insOrg = insOrg;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBankArea() {
        return bankArea;
    }

    public void setBankArea(String bankArea) {
        this.bankArea = bankArea;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
