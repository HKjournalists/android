package com.kplus.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kplus.car.parser.ApiField;

public class CityVehicle extends BaseModelObj implements Parcelable {

	@ApiField("id")
	private Long id;
	@ApiField("province")
	private String province;
	@ApiField("name")
	private String name;
	@ApiField("prefix")
	private String prefix;
	@ApiField("motorNumLen")
	private Integer motorNumLen;
	@ApiField("frameNumLen")
	private Integer frameNumLen;
	@ApiField("PY")
	private String PY;
	@ApiField("valid")
	private Boolean valid;
	@ApiField("hot")
	private Boolean hot;
	@ApiField("owner")
	private Boolean owner;
	@ApiField("accountLen")
	private Integer accountLen;
	@ApiField("passwordLen")
	private Integer passwordLen;
	@ApiField("motorvehiclenumLen")
	private Integer motorvehiclenumLen;
	@ApiField("ownerIdNoLen")
	private Integer ownerIdNoLen;
	@ApiField("drivingLicenseName")
	private Boolean drivingLicenseName;
	@ApiField("drivingLicenseNoLen")
	private Integer drivingLicenseNoLen;
	@ApiField("fieldComment")
	private String fieldComment;
	private String foreignId;

	public CityVehicle() {
	}

	public CityVehicle(Long id, String name, Integer motorNumLen,
			Integer frameNumLen) {
		super();
		this.id = id;
		this.name = name;
		this.motorNumLen = motorNumLen;
		this.frameNumLen = frameNumLen;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getMotorNumLen() {
		return motorNumLen;
	}

	public void setMotorNumLen(Integer motorNumLen) {
		this.motorNumLen = motorNumLen;
	}

	public Integer getFrameNumLen() {
		return frameNumLen;
	}

	public void setFrameNumLen(Integer frameNumLen) {
		this.frameNumLen = frameNumLen;
	}

	public String getPY() {
		return PY;
	}

	public void setPY(String pY) {
		PY = pY;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	public Boolean getHot() {
		return hot;
	}

	public void setHot(Boolean hot) {
		this.hot = hot;
	}

	public Boolean getOwner() {
		return owner;
	}

	public void setOwner(Boolean owner) {
		this.owner = owner;
	}

	public Integer getAccountLen() {
		return accountLen;
	}

	public void setAccountLen(Integer accountLen) {
		this.accountLen = accountLen;
	}

	public Integer getPasswordLen() {
		return passwordLen;
	}

	public void setPasswordLen(Integer passwordLen) {
		this.passwordLen = passwordLen;
	}

	public Integer getMotorvehiclenumLen() {
		return motorvehiclenumLen;
	}

	public void setMotorvehiclenumLen(Integer motorvehiclenumLen) {
		this.motorvehiclenumLen = motorvehiclenumLen;
	}

	public Integer getOwnerIdNoLen() {
		return ownerIdNoLen;
	}

	public void setOwnerIdNoLen(Integer ownerIdNoLen) {
		this.ownerIdNoLen = ownerIdNoLen;
	}

	public Boolean isDrivingLicenseName() {
		return drivingLicenseName;
	}

	public void setDrivingLicenseName(Boolean drivingLicenseName) {
		this.drivingLicenseName = drivingLicenseName;
	}

	public Integer getDrivingLicenseNoLen() {
		return drivingLicenseNoLen;
	}

	public void setDrivingLicenseNoLen(Integer drivingLicenseNoLen) {
		this.drivingLicenseNoLen = drivingLicenseNoLen;
	}

	public String getFieldComment() {
		return fieldComment;
	}

	public void setFieldComment(String fieldComment) {
		this.fieldComment = fieldComment;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.motorNumLen);
		dest.writeInt(this.frameNumLen);
	}

	// 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
	public static final Parcelable.Creator<CityVehicle> CREATOR = new Parcelable.Creator<CityVehicle>() {
		@Override
		public CityVehicle createFromParcel(Parcel source) {
			// 从Parcel中读取数据，返回person对象
			return new CityVehicle(source.readLong(), source.readString(),
					source.readInt(), source.readInt());
		}

		@Override
		public CityVehicle[] newArray(int size) {
			return new CityVehicle[size];
		}
	};
}
