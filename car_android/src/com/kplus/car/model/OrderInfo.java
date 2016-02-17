package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * 订单补充信息
 * 
 * @author Administrator
 * 
 */
public class OrderInfo extends BaseModelObj {

	@ApiField("cardImageUrl")
	private String cardImageUrl; // 身份证
	@ApiField("drivingLicenseImageUrl")
	private String drivingLicenseImageUrl; // 行驶证
	@ApiField("driverLicenseImageUrl")
	private String driverLicenseImageUrl; // 驾驶证
	@ApiField("cardImageUrl2")
	private String cardImageUrl2; // 身份证背面
	@ApiField("drivingLicenseImageUrl2")
	private String drivingLicenseImageUrl2; // 行驶证背面
	@ApiField("driverLicenseImageUrl2")
	private String driverLicenseImageUrl2; // 驾驶证背面
	@ApiField("ownerName")
	private String ownerName; // 车主姓名

	public String getCardImageUrl() {
		return cardImageUrl;
	}

	public void setCardImageUrl(String cardImageUrl) {
		this.cardImageUrl = cardImageUrl;
	}

	public String getDrivingLicenseImageUrl() {
		return drivingLicenseImageUrl;
	}

	public void setDrivingLicenseImageUrl(String drivingLicenseImageUrl) {
		this.drivingLicenseImageUrl = drivingLicenseImageUrl;
	}

	public String getDriverLicenseImageUrl() {
		return driverLicenseImageUrl;
	}

	public void setDriverLicenseImageUrl(String driverLicenseImageUrl) {
		this.driverLicenseImageUrl = driverLicenseImageUrl;
	}

	public String getCardImageUrl2() {
		return cardImageUrl2;
	}

	public void setCardImageUrl2(String cardImageUrl2) {
		this.cardImageUrl2 = cardImageUrl2;
	}

	public String getDrivingLicenseImageUrl2() {
		return drivingLicenseImageUrl2;
	}

	public void setDrivingLicenseImageUrl2(String drivingLicenseImageUrl2) {
		this.drivingLicenseImageUrl2 = drivingLicenseImageUrl2;
	}

	public String getDriverLicenseImageUrl2() {
		return driverLicenseImageUrl2;
	}

	public void setDriverLicenseImageUrl2(String driverLicenseImageUrl2) {
		this.driverLicenseImageUrl2 = driverLicenseImageUrl2;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

}
