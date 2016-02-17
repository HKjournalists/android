package com.kplus.car.model;

import android.support.annotation.IntegerRes;

import com.kplus.car.parser.ApiField;

public class ActivityInfo extends BaseModelObj {
	@ApiField("isTop")
	private Boolean isTop;
	@ApiField("valid")
	private Boolean valid;
	@ApiField("link")
	private String link;
	@ApiField("img")
	private String img;
	@ApiField("content")
	private String content;
	@ApiField("title")
	private String title;
	@ApiField("summary")
	private String summary;
	@ApiField("startTime")
	private String startTime;
	@ApiField("endTime")
	private String endTime;
	@ApiField("visitCount")
	private Integer visitCount;
	
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}
}
