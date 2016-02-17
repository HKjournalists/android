package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UpgradeComponent extends BaseModelObj {
	@ApiField("comId")
	private String comId;
	@ApiField("hasNew")
	private Boolean hasNew;
	@ApiField("lazy")
	private Boolean lazy = true;
	@ApiField("downloadUrl")
	private String downloadUrl;
	private String time;
	public String getComId() {
		return comId;
	}
	public void setComId(String comId) {
		this.comId = comId;
	}
	public Boolean getHasNew() {
		return hasNew;
	}
	public void setHasNew(Boolean hasNew) {
		this.hasNew = hasNew;
	}
	public Boolean getLazy() {
		return lazy;
	}
	public void setLazy(Boolean lazy) {
		this.lazy = lazy;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
