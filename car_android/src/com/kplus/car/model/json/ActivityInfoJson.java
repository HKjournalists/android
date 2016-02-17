package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.ActivityInfo;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class ActivityInfoJson extends BaseModelObj {
	@ApiField("total")
	private Integer total;
	@ApiListField("list")
	private List<ActivityInfo> list;
	@ApiField("updateTime")
	private String updateTime;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<ActivityInfo> getList() {
		return list;
	}
	public void setList(List<ActivityInfo> list) {
		this.list = list;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
