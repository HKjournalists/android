package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetAgainstRecordListJson extends BaseModelObj{

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ApiListField("list")
	private List<AgainstRecord> list;
	@ApiField("total")
	private Integer total;
	@ApiField("canDeal")
	private Integer canDeal;//是否支持代办(0，1)
	@ApiField("dealServiceUrl")
	private String dealServiceUrl;
	@ApiField("updateTime")
	private String updateTime;
	@ApiField("type")
	private String type;
	@ApiField("resultType")
	private Integer resultType;
	@ApiField("sessionId")
	private String sessionId;

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}

	public List<AgainstRecord> getList() {
		if (list == null)
			list = new ArrayList<AgainstRecord>();
		return list;
	}

	public void setList(List<AgainstRecord> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCanDeal()
	{
		return canDeal;
	}

	public void setCanDeal(Integer canDeal)
	{
		this.canDeal = canDeal;
	}

	public String getDealServiceUrl()
	{
		return dealServiceUrl;
	}

	public void setDealServiceUrl(String dealServiceUrl)
	{
		this.dealServiceUrl = dealServiceUrl;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@ApiListField("rule")
	private List<CityVehicle> rule;

	public List<CityVehicle> getRule() {
		return rule;
	}

	public void setRule(List<CityVehicle> rule) {
		this.rule = rule;
	}
	
	@ApiField("city")
	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
