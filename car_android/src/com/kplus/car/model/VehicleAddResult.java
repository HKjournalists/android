package com.kplus.car.model;

import java.util.List;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class VehicleAddResult extends BaseModelObj
{
	@ApiField("result")
	private Boolean result;
	@ApiField("id")
	private Long id;
	@ApiField("type")
	private Integer type;
	@ApiListField("rule")
	private List<CityVehicle> rule;
	
	public Boolean getResult()
	{
		return result;
	}
	public void setResult(Boolean result)
	{
		this.result = result;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<CityVehicle> getRule() {
		return rule;
	}
	public void setRule(List<CityVehicle> rule) {
		this.rule = rule;
	}
}
