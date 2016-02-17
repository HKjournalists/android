package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianBaseInfoMedia extends BaoxianBaseInfoMediaKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8813400141519593428L;

	private String name;

	private String url;

	private Integer status;

	private Integer weight;

	private Date createTime;

	private Date updateTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}