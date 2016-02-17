package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.CouponList;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class CouponListJson extends BaseModelObj{
	@ApiListField("list")
	private List<CouponList> list;
	@ApiField("total")
	private Integer total;
	public List<CouponList> getList() {
		return list;
	}
	public void setList(List<CouponList> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
