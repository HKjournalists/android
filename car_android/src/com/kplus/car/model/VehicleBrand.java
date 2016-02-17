package com.kplus.car.model;

import java.util.List;

import com.kplus.car.parser.ApiField;

public class VehicleBrand extends BaseModelObj {

	@ApiField("id")
	private Long id;
	@ApiField("name")
	private String name;
	@ApiField("logo")
	private String logo;
	@ApiField("code")
	private String code;
	@ApiField("sub")
	private List<VehicleModel> models;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<VehicleModel> getModels() {
		return models;
	}

	public void setModels(List<VehicleModel> models) {
		this.models = models;
	}

}
