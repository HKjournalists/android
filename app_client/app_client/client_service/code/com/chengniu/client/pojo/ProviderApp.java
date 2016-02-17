package com.chengniu.client.pojo;

import java.io.Serializable;
import java.util.Date;

public class ProviderApp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8793647665908082089L;

	private String id;

	private String key;

	private Integer type;

	private String name;

	private Integer status;

	private Date createTime;

	private Date updateTime;

	private Integer maxVersion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key == null ? null : key.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getMaxVersion() {
		return maxVersion;
	}

	public void setMaxVersion(Integer maxVersion) {
		this.maxVersion = maxVersion;
	}
}