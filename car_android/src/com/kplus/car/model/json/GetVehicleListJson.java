package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.Vehicle;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetVehicleListJson extends BaseModelObj
{
	@ApiListField("list")
	private List<Vehicle> list;
	@ApiField("total")
	private Integer total;
	
	public List<Vehicle> getList()
	{
		return list;
	}
	public void setList(List<Vehicle> list)
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
