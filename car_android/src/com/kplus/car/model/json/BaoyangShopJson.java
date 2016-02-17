package com.kplus.car.model.json;

import com.kplus.car.model.BaoyangShop;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiListField;

import java.util.List;

public class BaoyangShopJson extends BaseModelObj {

	@ApiListField("list")
	private List<BaoyangShop> list;

	public List<BaoyangShop> getList() {
		return list;
	}

	public void setList(List<BaoyangShop> list) {
		this.list = list;
	}
}
