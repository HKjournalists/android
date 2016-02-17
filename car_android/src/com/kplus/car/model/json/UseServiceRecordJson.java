package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.UseServiceRecord;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class UseServiceRecordJson extends BaseModelObj
{
	@ApiListField("list")
	private List<UseServiceRecord> list;
	@ApiField("total")
	private Integer total;
	
	public List<UseServiceRecord> getList()
	{
		return list;
	}
	public void setList(List<UseServiceRecord> list)
	{
		this.list = list;
	}
	public Integer getTotal()
	{
		return total;
	}
	public void setTotal(Integer total)
	{
		this.total = total;
	}	
}
