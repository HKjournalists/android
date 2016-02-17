package com.chengniu.client.domain;

/**
 * 报价状态
 */
public enum QuoteStatus {
    Expired(-3, "已过期"),
    Canceled(-2, "已取消"),
    Failed(-1, "报价失败"),
    Waiting(0, "报价中"),
    SUCCESS(1, "报价成功");

    private int value;
    private String desc;

    QuoteStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isSuccess(Integer status) {
        return status != null && SUCCESS.getValue() == status;
    }
}
