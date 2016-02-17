package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetInitVehicleJson extends BaseModelObj {

	@ApiListField("city")
	private CityVehicle city;
	@ApiField("vehicleNum")
	private String vehicleNum;

	public CityVehicle getCity() {
		return city;
	}

	public void setCity(CityVehicle city) {
		this.city = city;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

}
