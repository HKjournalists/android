package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * 应用版本
 * 
 * @author suyilei
 * 
 */
public class AppVersion extends BaseModelObj {

	@ApiField("version")
	private Float version;
	@ApiField("hasNew")
	private Boolean hasNew;
	@ApiField("downloadUrl")
	private String downloadUrl;
	@ApiField("desc")
	private String desc;

	public Float getVersion() {
		return version;
	}

	public void setVersion(Float version) {
		this.version = version;
	}

	public Boolean getHasNew() {
		return hasNew;
	}

	public void setHasNew(Boolean hasNew) {
		this.hasNew = hasNew;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
