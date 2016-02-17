package com.chengniu.client.domain;

/**
 * 报价请求状态
 */
public enum QuoteRequestStatus {
	Invalid(-4, "缺少方案"), AuthroizeFailed(-3, "资料初审未通过"), SubmitFailed(-2,
			"提交失败"), RequestSubmit(0, "已提交初审"), Authroized(1, "已通过初审"), QuotingPending(
			2, "等待提交"), Quoting(3, "报价中"), QuoteFailed(-88, "报价失败"), QuoteFinished(
			99, "报价完成"), UnderwritingPending(100, "核保中");
	private int value;
	private String desc;

	QuoteRequestStatus(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public static boolean isMediaFixable(int status) {
		return RequestSubmit.value >= status || status != SubmitFailed.value;
	}

	public static boolean isRequestCommitable(int status) {
		return RequestSubmit.value >= status;
	}

	/**
	 * 是否可以提交报价
	 * 
	 * @param status
	 * @return
	 */
	public static boolean isQuoteCommitable(int status) {
		return RequestSubmit.value == status || Authroized.value == status
				|| QuotingPending.value == status
				|| QuoteFailed.value == status;
	}

	public static boolean isQuoteFailed(Integer status) {
		return status != null && status == QuoteFailed.getValue();
	}

	public static boolean isFinished(Integer status) {
		return status != null && status == QuoteFinished.getValue();
	}
}
