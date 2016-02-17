package com.kplus.car.model;

import java.util.List;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class CouponList extends BaseModelObj{
	@ApiField("name")
	private String name;
	@ApiListField("list")
	private List<Coupon> list;
	@ApiField("total")
	private Integer total;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Coupon> getList() {
		return list;
	}
	public void setList(List<Coupon> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
