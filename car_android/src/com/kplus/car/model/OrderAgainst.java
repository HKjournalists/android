package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * 订单违章记录
 * 
 * @author Administrator
 * 
 */
public class OrderAgainst extends BaseModelObj {

	@ApiField("address")
	private String address; // 违章地址
	@ApiField("score")
	private Integer score; // 扣分
	@ApiField("money")
	private Integer money; // 罚款
	@ApiField("overdueFlag")
	private Boolean overdueFlag; // 是否现场单
	@ApiField("id")
	private Long id; 
	@ApiField("time")
	private String time;
	@ApiField("behavior")
	private String behavior;
	@ApiField("againstStatus")
	private Integer againstStatus;


	public Integer getAgainstStatus()
	{
		return againstStatus;
	}

	public void setAgainstStatus(Integer againstStatus)
	{
		this.againstStatus = againstStatus;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getBehavior()
	{
		return behavior;
	}

	public void setBehavior(String behavior)
	{
		this.behavior = behavior;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Boolean getOverdueFlag() {
		return overdueFlag == null ? false : overdueFlag;
	}

	public void setOverdueFlag(Boolean overdueFlag) {
		this.overdueFlag = overdueFlag;
	}

}
