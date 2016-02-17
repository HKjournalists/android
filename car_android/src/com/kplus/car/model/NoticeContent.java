package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class NoticeContent extends BaseModelObj {
	@ApiField("id")
	private Long id;
	@ApiField("content")
	private String content;
	@ApiField("img_url")
	private String imgUrl;
	@ApiField("create_time")
	private String noticeTime;
	@ApiField("motion_type")
	private String motionType;
	@ApiField("motion_value")
	private String motionValue;
	private int readflag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	public String getMotionType() {
		return motionType;
	}

	public void setMotionType(String motionType) {
		this.motionType = motionType;
	}

	public String getMotionValue() {
		return motionValue;
	}

	public void setMotionValue(String motionValue) {
		this.motionValue = motionValue;
	}

	public int getReadflag() {
		return readflag;
	}

	public void setReadflag(int readflag) {
		this.readflag = readflag;
	}
}
