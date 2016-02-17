package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianBaseInfo {
	public enum USER_TYPE {
		PERSON(0), PROVIDER(1);
		public int value;

		USER_TYPE(int type) {
			this.value = type;
		}
	}

	public enum DRIVING_CHECKED {
		NONE(0), FAIL(-1), SUCCESS(1);
		public int value;

		DRIVING_CHECKED(int type) {
			this.value = type;
		}
	}

	public enum ID_CARD_CHECKED {
		NONE(0), FAIL(-1), SUCCESS(1);
		public int value;

		ID_CARD_CHECKED(int type) {
			this.value = type;
		}
	}
 private String id;

    private String vehicleModelPrice;

    private String vehicleModelCode;

    private String vehicleModelName;

    private Long drivingId;

    private String drivingUrl;

    private Integer drivingChecked;

    private String idCardNum;

    private Date birthday;

    private Integer sex;

    private String idCardName;

    private String cityCode;

    private String idCardUrl;

    private Integer idCardChecked;

    private Date createTime;

    private Date updateTime;

    private String cityName;

    private String mobile;

    private String vehicleNum;

    private Boolean guohu;

    private String userId;

    private Integer userType;

    private String toubaorenCardNum;

    private String toubaorenCardName;

    private String beibaorenCardNum;

    private String beibaorenCardName;

    private String frameNum;

    private String motorNum;

    private Date registerDate;

    private Boolean toubaoren;

    private Boolean beibaoren;

    private Boolean deleted;

    private String operatorId;

    private String operatorName;

    private String guohuDate;

    private Integer idCardType;

    private Integer toubaorenCardType;

    private Integer beibaorenCardType;

    private String owner;

    private String ownerType;

    private String useProperty;

    private String issueDate;

    private String brandModel;

    private String carType;

    private String displacement;

    private String fullLoad;
    private Float avgMileage;
    private Integer ratedPassengerCapacity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getVehicleModelPrice() {
        return vehicleModelPrice;
    }

    public void setVehicleModelPrice(String vehicleModelPrice) {
        this.vehicleModelPrice = vehicleModelPrice == null ? null : vehicleModelPrice.trim();
    }

    public String getVehicleModelCode() {
        return vehicleModelCode;
    }

    public void setVehicleModelCode(String vehicleModelCode) {
        this.vehicleModelCode = vehicleModelCode == null ? null : vehicleModelCode.trim();
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName == null ? null : vehicleModelName.trim();
    }

    public Long getDrivingId() {
        return drivingId;
    }

    public void setDrivingId(Long drivingId) {
        this.drivingId = drivingId;
    }

    public String getDrivingUrl() {
        return drivingUrl;
    }

    public void setDrivingUrl(String drivingUrl) {
        this.drivingUrl = drivingUrl == null ? null : drivingUrl.trim();
    }

    public Integer getDrivingChecked() {
        return drivingChecked;
    }

    public void setDrivingChecked(Integer drivingChecked) {
        this.drivingChecked = drivingChecked;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum == null ? null : idCardNum.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName == null ? null : idCardName.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getIdCardUrl() {
        return idCardUrl;
    }

    public void setIdCardUrl(String idCardUrl) {
        this.idCardUrl = idCardUrl == null ? null : idCardUrl.trim();
    }

    public Integer getIdCardChecked() {
        return idCardChecked;
    }

    public void setIdCardChecked(Integer idCardChecked) {
        this.idCardChecked = idCardChecked;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum == null ? null : vehicleNum.trim();
    }

    public Boolean getGuohu() {
        return guohu;
    }

    public void setGuohu(Boolean guohu) {
        this.guohu = guohu;
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

    public String getToubaorenCardNum() {
        return toubaorenCardNum;
    }

    public void setToubaorenCardNum(String toubaorenCardNum) {
        this.toubaorenCardNum = toubaorenCardNum == null ? null : toubaorenCardNum.trim();
    }

    public String getToubaorenCardName() {
        return toubaorenCardName;
    }

    public void setToubaorenCardName(String toubaorenCardName) {
        this.toubaorenCardName = toubaorenCardName == null ? null : toubaorenCardName.trim();
    }

    public String getBeibaorenCardNum() {
        return beibaorenCardNum;
    }

    public void setBeibaorenCardNum(String beibaorenCardNum) {
        this.beibaorenCardNum = beibaorenCardNum == null ? null : beibaorenCardNum.trim();
    }

    public String getBeibaorenCardName() {
        return beibaorenCardName;
    }

    public void setBeibaorenCardName(String beibaorenCardName) {
        this.beibaorenCardName = beibaorenCardName == null ? null : beibaorenCardName.trim();
    }

    public String getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum == null ? null : frameNum.trim();
    }

    public String getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum == null ? null : motorNum.trim();
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Boolean getToubaoren() {
        return toubaoren;
    }

    public void setToubaoren(Boolean toubaoren) {
        this.toubaoren = toubaoren;
    }

    public Boolean getBeibaoren() {
        return beibaoren;
    }

    public void setBeibaoren(Boolean beibaoren) {
        this.beibaoren = beibaoren;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getGuohuDate() {
        return guohuDate;
    }

    public void setGuohuDate(String guohuDate) {
        this.guohuDate = guohuDate == null ? null : guohuDate.trim();
    }

    public Integer getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Integer idCardType) {
        this.idCardType = idCardType;
    }

    public Integer getToubaorenCardType() {
        return toubaorenCardType;
    }

    public void setToubaorenCardType(Integer toubaorenCardType) {
        this.toubaorenCardType = toubaorenCardType;
    }

    public Integer getBeibaorenCardType() {
        return beibaorenCardType;
    }

    public void setBeibaorenCardType(Integer beibaorenCardType) {
        this.beibaorenCardType = beibaorenCardType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType == null ? null : ownerType.trim();
    }

    public String getUseProperty() {
        return useProperty;
    }

    public void setUseProperty(String useProperty) {
        this.useProperty = useProperty == null ? null : useProperty.trim();
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate == null ? null : issueDate.trim();
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel == null ? null : brandModel.trim();
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType == null ? null : carType.trim();
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement == null ? null : displacement.trim();
    }

    public String getFullLoad() {
        return fullLoad;
    }

    public void setFullLoad(String fullLoad) {
        this.fullLoad = fullLoad == null ? null : fullLoad.trim();
    }

    public Integer getRatedPassengerCapacity() {
        return ratedPassengerCapacity;
    }

    public void setRatedPassengerCapacity(Integer ratedPassengerCapacity) {
        this.ratedPassengerCapacity = ratedPassengerCapacity;
    }

	public Float getAvgMileage() {
		return avgMileage;
	}

	public void setAvgMileage(Float avgMileage) {
		this.avgMileage = avgMileage;
	}
}