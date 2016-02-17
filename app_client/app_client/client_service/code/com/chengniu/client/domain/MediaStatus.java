package com.chengniu.client.domain;

/**
 * 影像资料状态
 */
public enum MediaStatus {
	SHORTAGE, VERIFY_PENDING, VERIFIED, UPLOADING, UPLOADED;

	/**
	 * 是否可补全
	 * 
	 * @param status
	 * @return
	 */
	public static boolean isUserFixable(int status) {
		return status != VERIFIED.ordinal();
	}

	public static boolean isWaitingForFixed(Integer mediaStatus) {
		return mediaStatus != null && SHORTAGE.ordinal() == mediaStatus;
	}

	public static boolean isReady(Integer status) {
		return status != null && status == VERIFIED.ordinal();
	}

	public static boolean hasUploaded(Integer mediaStatus) {
		return mediaStatus != null && mediaStatus == UPLOADED.ordinal();
	}
}
