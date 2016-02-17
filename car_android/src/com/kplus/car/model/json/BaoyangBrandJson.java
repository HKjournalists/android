package com.kplus.car.model.json;

import com.kplus.car.model.BaoyangBrand;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

public class BaoyangBrandJson extends BaseModelObj {

	@ApiListField("list")
	private List<BaoyangBrand> list;
	@ApiField("total")
	private int total;

	public List<BaoyangBrand> getList() {
		return list;
	}

	public void setList(List<BaoyangBrand> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
