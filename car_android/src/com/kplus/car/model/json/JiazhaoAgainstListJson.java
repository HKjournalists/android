package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.JiazhaoAgainst;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

public class JiazhaoAgainstListJson extends BaseModelObj {

	@ApiListField("list")
	private List<JiazhaoAgainst> list;

	@ApiField("total")
	private Integer total;

	public List<JiazhaoAgainst> getList() {
		return list;
	}

	public void setList(List<JiazhaoAgainst> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
