package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.NoticeContent;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class NoticeContentJson extends BaseModelObj {
	@ApiListField("list")
	private List<NoticeContent> list;
	@ApiField("total")
	private Integer total;

	public List<NoticeContent> getList() {
		return list;
	}
	public void setList(List<NoticeContent> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
