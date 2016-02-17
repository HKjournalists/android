package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianPeisong {
	private String id;

	private Date createTime;

	private Date updateTime;

	private String mobile;

	private String provinceCode;

	private String cityCode;

	private String townCode;

	private String name;

	private String address;

	private String userId;

	private Integer userType;

	private String provinceName;
	private String idCardAddress;

	public String getIdCardAddress() {
		return idCardAddress;
	}

	public void setIdCardAddress(String idCardAddress) {
		this.idCardAddress = idCardAddress;
	}

	private String cityName;

	private String townName;

	private Boolean deleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode == null ? null : provinceCode.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getTownCode() {
		return townCode;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode == null ? null : townCode.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName == null ? null : provinceName.trim();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName == null ? null : cityName.trim();
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName == null ? null : townName.trim();
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}