package com.chengniu.client.domain;

/**
 * 意向状态
 */
public enum IntentStatus {
    Intent(0, "初步意向"),
    Quoting(2, "报价中"),
    QUOTING_LOST(1, "缺少方案"),
    QuoteFailed(4, "报价失败"),
    QuoteFinished(6, "报价完成"),
    UnderwritingPending(10, "核保中"),
    UnderwritingFailed(12, "核保失败"),
    UnderwritingSuccess(14, "核保完成"),
    PayPending(20, "待支付"),
    Paid(21, "承保中"),
    UnderwritingGranted(22, "已承保"),
    UnderwritingCanceled(24, "承保失败"),
    PolicyDelivered(26, "保单已配送");

    private int value;
    private String desc;

    IntentStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 是否可以补全基本信息
     *
     * @param status
     * @return
     */
    public static boolean isUserInfoFixable(int status) {
        return Intent.value == status;
    }

    /**
     * 是否可以提交多方报价
     * @param status
     * @return
     */
    public static boolean canRequestQuote(int status) {
        return Quoting.value > status;
    }

    /**
     * 意向阶段
     * @param status
     * @return
     */
    public static boolean isInformalIntentStage(int status) {
        return status == Intent.value;
    }

    /**
     * 报价阶段
     * @param status
     * @return
     */
    public static boolean isQuoteStage(int status) {
        return status >= QUOTING_LOST.value && status < UnderwritingPending.value;
    }

    /**
     * 核保阶段
     * @param status
     * @return
     */
    public static boolean isUnderwritingState(int status) {
        return status >= UnderwritingPending.value && status < PayPending.value;
    }

    /**
     * 出单阶段
     * @param status
     * @return
     */
    public static boolean isOrderStage(int status) {
        return status >= PayPending.value;
    }

}
