package com.chengniu.client.domain;

/**
 * 自动核保状态
 */
public enum AutomaticStatus {
	PENDING(0), REQUEST_FAILED(-2), // 提交失败
	REPORT_FAILED(-1), // 核保失败
	REPORT_SUCCEED(1), // 核保成功
	SUBMITED(2); // 提交成功

	private int rawValue;

	AutomaticStatus(int raw) {
		rawValue = raw;
	}

	public int rawValue() {
		return rawValue;
	}

	/**
	 * 简化的状态
	 * 
	 * @param status
	 * @return
	 */
	public static int simplifiedStatus(Integer status) {
		status = (status == null) ? PENDING.rawValue : status;
		if (status == PENDING.rawValue || status == SUBMITED.rawValue) {
			return PENDING.rawValue;
		} else if (status == REPORT_FAILED.rawValue
				|| status == REQUEST_FAILED.rawValue) {
			return REPORT_FAILED.rawValue;
		}

		return status;
	}

	/**
	 * 已成功提交
	 * 
	 * @param status
	 * @return
	 */
	public static boolean hasSubmited(Integer status) {
		return status != null
				&& (status == REPORT_SUCCEED.rawValue || status == SUBMITED.rawValue);
	}

	/**
	 * 从未提交成功
	 * 
	 * @param status
	 * @return
	 */
	public static boolean hasNeverSubmited(Integer status) {
		return status == null || status == REQUEST_FAILED.rawValue;
	}
}
