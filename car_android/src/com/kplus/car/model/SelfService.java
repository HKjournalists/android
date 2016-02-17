package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class SelfService extends BaseModelObj {

	@ApiField("id")
	private Long id;
	@ApiField("name")
	private String name;
	@ApiField("address")
	private String address;
	@ApiField("phone")
	private String phone;
	@ApiField("lat")
	private Double lat;
	@ApiField("lng")
	private Double lng;

	private String cityName;
	@ApiField("regionName")
	private String regionName;
	private float distanceValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public float getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(float distanceValue) {
		this.distanceValue = distanceValue;
	}

}
