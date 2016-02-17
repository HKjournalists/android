package com.kplus.car.model;

import java.io.Serializable;

public class UserVehicle implements Serializable {

	private String vehicleNum;
	private String cityId;
	private String cityName;
	private String motorNum;
	private String frameNum;
	private long vehicleModelId;
	private String modelName;
	private String picUrl;
	private String descr;
    private String nianjianDate;
    private String jiazhaofenDate;
    private String company;
    private String phone;

	// 车辆相关统计数据
	private int score;
	private int money;
	private int newNum;
	private int oldNum;

	private long vehicleId;
	private boolean isHiden = false;
	private int canDeal;//是否支持代办(0，1)
	private String url;//服务地址
	private String maxFrameNum;
	private String maxMotorNum;
	private String updateTime;
	private boolean hasRuleError;
	private boolean hasParamError;
	private String cityUnValid;
	private long requestTime;//违章请求时间
	private long returnTime;//违章结果返回时间
	private String account;
	private String password;
	private Integer vehicleType;
	private String issueDate;//发证日期
	private String regDate;//注册日期
	private String vehicleOwner;//车主
	private String vehicleRegCerNo;//机动车登记证书编号
	private boolean hasSearchFail;
	private int newRecordNumber;
	private String restrictNum;//限行号码
	private String restrictRegion;//限行区域
	private String restrictNums;//限行规则
	private String ownerId;//车主身份证号
	private String driverName;//驾驶人姓名
	private String driverId;//驾驶人驾驶证号
	
	public UserVehicle(){
		vehicleNum = "";
		cityId = "";
		cityName = "";
		motorNum = "";
		frameNum = "";
		modelName = "";
		picUrl = "";
		descr = "";
        nianjianDate = "";
        jiazhaofenDate = "";
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public long getVehicleModelId() {
		return vehicleModelId;
	}

	public void setVehicleModelId(long vehicleModelId) {
		this.vehicleModelId = vehicleModelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

    public String getNianjianDate() {
        return nianjianDate;
    }

    public void setNianjianDate(String nianjianDate) {
        this.nianjianDate = nianjianDate;
    }

    public String getJiazhaofenDate() {
        return jiazhaofenDate;
    }

    public void setJiazhaofenDate(String jiazhaofenDate) {
        this.jiazhaofenDate = jiazhaofenDate;
    }

    public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getNewNum() {
		return newNum;
	}

	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}

	public int getOldNum() {
		return oldNum;
	}

	public void setOldNum(int oldNum) {
		this.oldNum = oldNum;
	}

	public long getVehicleId()
	{
		return vehicleId;
	}

	public void setVehicleId(long vehicleId)
	{
		this.vehicleId = vehicleId;
	}

	public boolean isHiden()
	{
		return isHiden;
	}

	public void setHiden(boolean isHiden)
	{
		this.isHiden = isHiden;
	}

	public int getCanDeal()
	{
		return canDeal;
	}

	public void setCanDeal(int canDeal)
	{
		this.canDeal = canDeal;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getMaxFrameNum() {
		return maxFrameNum;
	}

	public void setMaxFrameNum(String maxFrameNum) {
		this.maxFrameNum = maxFrameNum;
	}

	public String getMaxMotorNum() {
		return maxMotorNum;
	}

	public void setMaxMotorNum(String maxMotorNum) {
		this.maxMotorNum = maxMotorNum;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isHasRuleError() {
		return hasRuleError;
	}

	public void setHasRuleError(boolean hasRuleError) {
		this.hasRuleError = hasRuleError;
	}

	public boolean isHasParamError() {
		return hasParamError;
	}

	public void setHasParamError(boolean hasParamError) {
		this.hasParamError = hasParamError;
	}

	public String getCityUnValid() {
		return cityUnValid;
	}

	public void setCityUnValid(String cityUnValid) {
		this.cityUnValid = cityUnValid;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getVehicleOwner() {
		return vehicleOwner;
	}

	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}

	public String getVehicleRegCerNo() {
		return vehicleRegCerNo;
	}

	public void setVehicleRegCerNo(String vehicleRegCerNo) {
		this.vehicleRegCerNo = vehicleRegCerNo;
	}

	public boolean isHasSearchFail() {
		return hasSearchFail;
	}

	public void setHasSearchFail(boolean hasSearchFail) {
		this.hasSearchFail = hasSearchFail;
	}

	public int getNewRecordNumber() {
		return newRecordNumber;
	}

	public void setNewRecordNumber(int newRecordNumber) {
		this.newRecordNumber = newRecordNumber;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

	public String getRestrictNum() {
		return restrictNum;
	}

	public void setRestrictNum(String restrictNum) {
		this.restrictNum = restrictNum;
	}

	public String getRestrictRegion() {
		return restrictRegion;
	}

	public void setRestrictRegion(String restrictRegion) {
		this.restrictRegion = restrictRegion;
	}

	public String getRestrictNums() {
		return restrictNums;
	}

	public void setRestrictNums(String restrictNums) {
		this.restrictNums = restrictNums;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
}
