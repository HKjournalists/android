package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianReportPrice {
	private String id;

	private String cityCode;

	private String cityName;

	private String vehicleModelPrice;

	private String type;

	private String baoxianCompanyName;

	private String baoxianCompanyCode;

	private String szx;

	private String csx;

	private String ckx;

	private String dqx;

	private String blx;

	private String hfx;

	private String bjmp;

	private String zrx;

	private String totalPrice;

	private Date updateTime;

	private String taskCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName == null ? null : cityName.trim();
	}

	public String getVehicleModelPrice() {
		return vehicleModelPrice;
	}

	public void setVehicleModelPrice(String vehicleModelPrice) {
		this.vehicleModelPrice = vehicleModelPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getBaoxianCompanyName() {
		return baoxianCompanyName;
	}

	public void setBaoxianCompanyName(String baoxianCompanyName) {
		this.baoxianCompanyName = baoxianCompanyName == null ? null
				: baoxianCompanyName.trim();
	}

	public String getBaoxianCompanyCode() {
		return baoxianCompanyCode;
	}

	public void setBaoxianCompanyCode(String baoxianCompanyCode) {
		this.baoxianCompanyCode = baoxianCompanyCode == null ? null
				: baoxianCompanyCode.trim();
	}

	public String getSzx() {
		return szx;
	}

	public void setSzx(String szx) {
		this.szx = szx == null ? null : szx.trim();
	}

	public String getCsx() {
		return csx;
	}

	public void setCsx(String csx) {
		this.csx = csx == null ? null : csx.trim();
	}

	public String getCkx() {
		return ckx;
	}

	public void setCkx(String ckx) {
		this.ckx = ckx == null ? null : ckx.trim();
	}

	public String getDqx() {
		return dqx;
	}

	public void setDqx(String dqx) {
		this.dqx = dqx == null ? null : dqx.trim();
	}

	public String getBlx() {
		return blx;
	}

	public void setBlx(String blx) {
		this.blx = blx == null ? null : blx.trim();
	}

	public String getHfx() {
		return hfx;
	}

	public void setHfx(String hfx) {
		this.hfx = hfx == null ? null : hfx.trim();
	}

	public String getBjmp() {
		return bjmp;
	}

	public void setBjmp(String bjmp) {
		this.bjmp = bjmp == null ? null : bjmp.trim();
	}

	public String getZrx() {
		return zrx;
	}

	public void setZrx(String zrx) {
		this.zrx = zrx == null ? null : zrx.trim();
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice == null ? null : totalPrice.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode == null ? null : taskCode.trim();
	}
}