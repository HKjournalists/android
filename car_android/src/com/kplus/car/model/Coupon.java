package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class Coupon extends BaseModelObj{
	@ApiField("id")
	private Long id;//代金券ID
	@ApiField("name")
	private String name;//代金券名称
	@ApiField("info")
	private String info;//代金券描述
	@ApiField("amount")
	private Integer amount;//代金券金额
	@ApiField("condition")
	private Integer condition;//代金券使用条件，满金额
	@ApiField("beginTime")
	private String beginTime;//有效开始时间
	@ApiField("endTime")
	private String endTime;//有效结束时间
    @ApiField("status")
    private Integer status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getCondition() {
		return condition;
	}
	public void setCondition(Integer condition) {
		this.condition = condition;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
