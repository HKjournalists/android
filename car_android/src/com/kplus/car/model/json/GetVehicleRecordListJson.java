package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.VehicleRecord;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetVehicleRecordListJson extends BaseModelObj
{
	@ApiField("total")
	private int total;
	@ApiListField("list")
	private List<VehicleRecord> list;
	
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public List<VehicleRecord> getList()
	{
		return list;
	}
	public void setList(List<VehicleRecord> list)
	{
		this.list = list;
	}
}
