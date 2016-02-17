package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * 代办订单
 * 
 * @author suyilei
 * 
 */
public class Order extends BaseModelObj {

	@ApiField("id")
	private Long id; // 订单ID
	@ApiField("orderNum")
	private String orderNum; // 订单号
	@ApiField("vehicleNum")
	private String vehicleNum; // 车牌号，关联车辆
	@ApiField("orderTime")
	private String orderTime; // 下单时间
	@ApiField("status")
	private Integer status; // 订单状态
	@ApiField("price")
	private Float price; // 最终成交价格
	@ApiField("content")
	private String content; // 订单内容

	private long userId; // 客户设备ID，关联车辆
	private long pId; // 客户手机ID，关联手机
	private String contactName; // 联系人
	private String contactPhone; // 联系电话
	private String recordIds; // 违章记录ID，多个用”,”分割
	private String overdueRecordIds; // 标记有滞纳金的违章记录ID，多个用”,”分割
	private int userBalance; // 是否使用余额，0=不使用，1=使用

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getpId() {
		return pId;
	}

	public void setpId(long pId) {
		this.pId = pId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(String recordIds) {
		this.recordIds = recordIds;
	}

	public String getOverdueRecordIds() {
		return overdueRecordIds;
	}

	public void setOverdueRecordIds(String overdueRecordIds) {
		this.overdueRecordIds = overdueRecordIds;
	}

	public int getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(int userBalance) {
		this.userBalance = userBalance;
	}

}
