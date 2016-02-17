package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class UserInviteContentJson extends BaseModelObj{
	@ApiField("title")
	private String title;//分享框标题
	@ApiField("summary")
	private String summary;//分享框提示摘要
	@ApiField("content")
	private String content;//分享内容（部分活动包含链接url）
	@ApiField("inviteUrl")
	private String inviteUrl;
	@ApiField("imgUrl")
	private String imgUrl;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInviteUrl() {
		return inviteUrl;
	}
	public void setInviteUrl(String inviteUrl) {
		this.inviteUrl = inviteUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
