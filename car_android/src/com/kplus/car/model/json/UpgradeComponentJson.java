package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.UpgradeComponent;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class UpgradeComponentJson extends BaseModelObj{
	@ApiListField("list")
	private List<UpgradeComponent> list;
	@ApiField("total")
	private Integer total;
	
	public List<UpgradeComponent> getList() {
		return list;
	}
	public void setList(List<UpgradeComponent> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
