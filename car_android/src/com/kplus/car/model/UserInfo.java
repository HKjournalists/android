package com.kplus.car.model;

import java.util.List;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class UserInfo extends BaseModelObj{
	@ApiField("uid")//用户ID
	private Long uid;
	@ApiField("iconUrl")//头像url
	private String iconUrl;
	@ApiField("name")//昵称
	private String name;
	@ApiField("sex")//性别
	private Integer sex = -1;
	@ApiField("address")//所在地
	private String address;
	@ApiField("info")//个人签名信息
	private String info;
	@ApiField("cashBalance")//现金余额
	private Float cashBalance;
	@ApiField("coupon")//代金券(原余额)
	private Integer coupon;
	@ApiField("orderCount")//订单数
	private Integer orderCount;
	@ApiListField("accounts")
	private List<Account> accounts;//关联帐号列表
	@ApiField("ordersCount")
	private OrderAccount ordersCount;
	
	public Long getUid()
	{
		return uid;
	}
	public void setUid(Long uid)
	{
		this.uid = uid;
	}
	public String getIconUrl()
	{
		return iconUrl;
	}
	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getSex()
	{
		return sex;
	}
	public void setSex(Integer sex)
	{
		this.sex = sex;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getInfo()
	{
		return info;
	}
	public void setInfo(String info)
	{
		this.info = info;
	}
	public Float getCashBalance() {
		return cashBalance;
	}
	public void setCashBalance(Float cashBalance) {
		this.cashBalance = cashBalance;
	}
	public Integer getCoupon() {
		return coupon;
	}
	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public OrderAccount getOrdersCount() {
		return ordersCount;
	}
	public void setOrdersCount(OrderAccount ordersCount) {
		this.ordersCount = ordersCount;
	}
}
