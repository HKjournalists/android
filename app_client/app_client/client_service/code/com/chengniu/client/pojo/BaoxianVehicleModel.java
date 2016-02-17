package com.chengniu.client.pojo;

public class BaoxianVehicleModel {
	private Integer id;

	private String vehicleCode;

	private String brandName;

	private String familyName;

	private String standardName;

	private String vehicleType;

	private String vehicleSource;

	private String aliasName;

	private String gearBoxName;

	private String configureName;

	private String groupName;

	private Integer seat;

	private String displacementTag;

	private String displacement;

	private String price;

	private String taxPrice;

	private Integer marktimeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode == null ? null : vehicleCode.trim();
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName == null ? null : brandName.trim();
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName == null ? null : familyName.trim();
	}

	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName == null ? null : standardName.trim();
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType == null ? null : vehicleType.trim();
	}

	public String getVehicleSource() {
		return vehicleSource;
	}

	public void setVehicleSource(String vehicleSource) {
		this.vehicleSource = vehicleSource == null ? null : vehicleSource
				.trim();
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName == null ? null : aliasName.trim();
	}

	public String getGearBoxName() {
		return gearBoxName;
	}

	public void setGearBoxName(String gearBoxName) {
		this.gearBoxName = gearBoxName == null ? null : gearBoxName.trim();
	}

	public String getConfigureName() {
		return configureName;
	}

	public void setConfigureName(String configureName) {
		this.configureName = configureName == null ? null : configureName
				.trim();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName == null ? null : groupName.trim();
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public String getDisplacementTag() {
		return displacementTag;
	}

	public void setDisplacementTag(String displacementTag) {
		this.displacementTag = displacementTag == null ? null : displacementTag
				.trim();
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement == null ? null : displacement.trim();
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price == null ? null : price.trim();
	}

	public String getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice == null ? null : taxPrice.trim();
	}

	public Integer getMarktimeId() {
		return marktimeId;
	}

	public void setMarktimeId(Integer marktimeId) {
		this.marktimeId = marktimeId;
	}
}