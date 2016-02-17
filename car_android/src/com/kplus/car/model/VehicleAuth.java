package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class VehicleAuth extends BaseModelObj{
	@ApiField("vehicleNum")
	private String vehicleNum;//车牌号
	@ApiField("status")
	private Integer status;//认证状态
	@ApiField("belong")
	private Boolean belong;//是否归属当前用户，“是”则显示以下详细信息
	@ApiField("drivingLicenceUrl")
	private String drivingLicenceUrl;//行驶证图片地址
	@ApiField("authDatetime")
	private String authDatetime;//认证审核通过时间	
	@ApiField("owner")
	private String owner;//车主姓名
	@ApiField("brandModel")
	private String brandModel;//车型品牌
	@ApiField("motorNum")
	private String motorNum;//发动机号
	@ApiField("frameNum")
	private String frameNum;//车架号
	@ApiField("issueDate")
	private String issueDate;//保险到期日期
	@ApiField("residueDegree")
	private Integer residueDegree;//救援剩余次数
	@ApiField("descr")
	private String descr;//备注(认证审核不通过原因)
	@ApiField("adjustDate")
	private String adjustDate;//车辆保险矫正日期
	@ApiField("id")
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVehicleNum() {
		return vehicleNum;
	}
	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean getBelong() {
		return belong;
	}
	public void setBelong(Boolean belong) {
		this.belong = belong;
	}
	public String getDrivingLicenceUrl() {
		return drivingLicenceUrl;
	}
	public void setDrivingLicenceUrl(String drivingLicenceUrl) {
		this.drivingLicenceUrl = drivingLicenceUrl;
	}
	public String getAuthDatetime() {
		return authDatetime;
	}
	public void setAuthDatetime(String authDatetime) {
		this.authDatetime = authDatetime;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getBrandModel() {
		return brandModel;
	}
	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}
	public String getMotorNum() {
		return motorNum;
	}
	public void setMotorNum(String motorNum) {
		this.motorNum = motorNum;
	}
	public String getFrameNum() {
		return frameNum;
	}
	public void setFrameNum(String frameNum) {
		this.frameNum = frameNum;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public Integer getResidueDegree() {
		return residueDegree;
	}
	public void setResidueDegree(Integer residueDegree) {
		this.residueDegree = residueDegree;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getAdjustDate() {
		return adjustDate;
	}
	public void setAdjustDate(String adjustDate) {
		this.adjustDate = adjustDate;
	}
	
}
