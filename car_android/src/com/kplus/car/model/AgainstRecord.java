package com.kplus.car.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kplus.car.parser.ApiField;

public class AgainstRecord extends BaseModelObj /*implements Parcelable*/ {

	@ApiField("id")
	private Long id;
	@ApiField("vehicleNum")
	private String vehicleNum;
	@ApiField("cityId")
	private Long cityId;
	@ApiField("cityName")
	private String cityName;
	@ApiField("address")
	private String address;
	@ApiField("behavior")
	private String behavior;
	@ApiField("time")
	private String time;
	@ApiField("score")
	private Integer score;
	@ApiField("money")
	private Integer money;
	@ApiField("status")
	private Integer status;
	@ApiField("lat")
	private Double lat;
	@ApiField("lng")
	private Double lng;

	@ApiField("orderStatus")
	private Integer orderStatus;
	@ApiField("canSubmit")
	private Integer canSubmit;
	@ApiField("pId")
	private Long pId;
	@ApiField("orderId")
	private Long orderId;
	@ApiField("selfProcess")
	private Integer selfProcess;//是否是橙牛处理，0：否，1：是
	@ApiField("dataSourceTitle")
	private String dataSourceTitle;//违章数据来源
	@ApiField("paymentStatus")
	private Integer paymentStatus;//0 未缴费 1 已缴费
	@ApiField("resultType")
	private Integer resultType;//0 文字 1图片
	@ApiField("orderCode")
	private String orderCode;
	@ApiField("ordertime")
	private String ordertime;
	@ApiField("recordType")
	private Integer recordType;//0:一般，1:城管，2:高速，3:铁路
	@ApiField("imageName")
	private ImageNames imageName;
	@ApiField("selfScore")
	private Integer selfScore = -1;
	@ApiField("selfMoney")
	private Integer selfMoney = -1;


	private int overdueFlag;
	
	private Integer canDispose;// 0 cannot 1 can
	private int nType;

//	public AgainstRecord() {
//	}
//
//	public AgainstRecord(Long id, String address, Integer score, Integer money,
//			int overdueFlag) {
//		super();
//		this.id = id;
//		this.address = address;
//		this.score = score;
//		this.money = money;
//		this.overdueFlag = overdueFlag;
//	}
//	
//	public AgainstRecord(Long id,String vehicleNum,Long cityId,String cityName,String address,String behavior,String time,
//			Integer score,Integer money,Integer status,Double lat,Double lng,Integer orderStatus,Integer canSubmit,Long pId,
//			Long orderId,int overdueFlag, Integer canDispose, Integer selfProcess, int nType){
//		super();
//		this.id = id;
//		this.vehicleNum = vehicleNum;
//		this.cityId = cityId;
//		this.cityName = cityName;
//		this.address = address;
//		this.behavior = behavior;
//		this.time = time;
//		this.score = score;
//		this.money = money;
//		this.status = status;
//		this.lat = lat;
//		this.lng = lng;
//		this.orderStatus = orderStatus;
//		this.canSubmit = canSubmit;
//		this.pId = pId;
//		this.orderId = orderId;
//		this.overdueFlag = overdueFlag;
//		this.canDispose = canDispose;
//		this.selfProcess = selfProcess;
//		this.nType = nType;
//	}

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

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBehavior() {
		return behavior;
	}

	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getScore() {
		return score == null ? 0 : score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getMoney() {
		return money == null ? 0 : money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getOrderStatus() {
		return orderStatus == null ? 0 : orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getCanSubmit() {
		return canSubmit == null ? 1 : canSubmit;
	}

	public void setCanSubmit(Integer canSubmit) {
		this.canSubmit = canSubmit;
	}

	public Long getPId() {
		return pId == null ? 0 : pId;
	}

	public void setPId(Long pId) {
		this.pId = pId;
	}

	public Long getOrderId() {
		return orderId == null ? 0 : orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public int getOverdueFlag() {
		return overdueFlag;
	}

	public void setOverdueFlag(int overdueFlag) {
		this.overdueFlag = overdueFlag;
	}

	public Integer getCanDispose() {
		return canDispose;
	}

	public void setCanDispose(Integer canDispose) {
		this.canDispose = canDispose;
	}

	public Integer getSelfProcess() {
		return selfProcess;
	}

	public int getnType() {
		return nType;
	}

	public void setnType(int nType) {
		this.nType = nType;
	}

	public void setSelfProcess(Integer selfProcess) {
		this.selfProcess = selfProcess;
	}

	public String getDataSourceTitle() {
		return dataSourceTitle;
	}

	public void setDataSourceTitle(String dataSourceTitle) {
		this.dataSourceTitle = dataSourceTitle;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public Integer getRecordType() {
		return recordType;
	}

	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}


	public Integer getSelfScore() {
		return selfScore;
	}

	public void setSelfScore(Integer selfScore) {
		this.selfScore = selfScore;
	}

	public Integer getSelfMoney() {
		return selfMoney;
	}

	public void setSelfMoney(Integer selfMoney) {
		this.selfMoney = selfMoney;
	}
	
	public ImageNames getImageName() {
		return imageName;
	}

	public void setImageName(ImageNames imageName) {
		this.imageName = imageName;
	}

	public String getRecordTypeLabel(){
		String result = null;
		if(recordType != null){
			switch(recordType.intValue()){
			case 0:
				result = "道路";
				break;
			case 1:
				result = "城管";
				break;
			case 2:
				result = "高速";
				break;
			case 3:
				result = "铁路";
				break;
				default:
					break;
			}
		}
		return result;
	}

//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeLong(this.id == null ? 0 : this.id);
//		dest.writeString(this.vehicleNum == null ? "" : this.vehicleNum);
//		dest.writeLong(this.cityId == null ? 0 : this.cityId);
//		dest.writeString(this.cityName == null ? "" : this.cityName);
//		dest.writeString(this.address == null ? "" : this.address);
//		dest.writeString(this.behavior == null ? "" : this.behavior);
//		dest.writeString(this.time == null ? "" : this.time);
//		dest.writeInt(this.score == null ? 0 : this.score);
//		dest.writeInt(this.money == null ? 0 : this.money);
//		dest.writeInt(this.status == null ? 0 : this.status);
//		dest.writeDouble(this.lat == null ? 0 : this.status);
//		dest.writeDouble(this.lng == null ? 0 : this.status);
//		dest.writeInt(this.orderStatus == null ? 0 : this.orderStatus);
//		dest.writeInt(this.canSubmit == null ? 0 : this.canSubmit);
//		dest.writeLong(this.pId == null ? 0 : this.pId);
//		dest.writeLong(this.orderId == null ? 0 : this.orderId);
//		dest.writeInt(this.overdueFlag);
//		dest.writeInt(this.canDispose == null ? 0 : this.canDispose);
//		dest.writeInt(this.selfProcess == null ? 0 : this.selfProcess);
//		dest.writeInt(this.nType);
//	}
//
//	// 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
//	public static final Parcelable.Creator<AgainstRecord> CREATOR = new Parcelable.Creator<AgainstRecord>() {
//		@Override
//		public AgainstRecord createFromParcel(Parcel source) {
//			// 从Parcel中读取数据，返回person对象
//			return new AgainstRecord(source.readLong(),
//					source.readString(),
//					source.readLong(),
//					source.readString(),
//					source.readString(),
//					source.readString(),
//					source.readString(),
//					source.readInt(),
//					source.readInt(),
//					source.readInt(),
//					source.readDouble(),
//					source.readDouble(),
//					source.readInt(),
//					source.readInt(),
//					source.readLong(),
//					source.readLong(),
//					source.readInt(),
//					source.readInt(),
//					source.readInt(),
//					source.readInt());
//					
//		}
//
//		@Override
//		public AgainstRecord[] newArray(int size) {
//			return new AgainstRecord[size];
//		}
//	};
}
