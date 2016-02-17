package com.chengniu.client.domain;

/**
 * 报价失败原因
 */
public enum QuoteFailType {
    IdCardWrongOrMissing(1, "身份证错误"),
    LicenseWrongOrMissing(2, "行驶证错误"),
    TaxClearanceCertWrongOrMissing(3, "车船税完税证明错误");

    private int value;
    private String msg;

    QuoteFailType(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }
}
