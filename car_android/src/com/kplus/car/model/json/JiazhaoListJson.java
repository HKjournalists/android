package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

public class JiazhaoListJson extends BaseModelObj {

	@ApiListField("list")
	private List<Jiazhao> list;

	@ApiField("total")
	private Integer total;

	public List<Jiazhao> getList() {
		return list;
	}

	public void setList(List<Jiazhao> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
