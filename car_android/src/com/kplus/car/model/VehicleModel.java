package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class VehicleModel extends BaseModelObj {

	@ApiField("id")
	private Long id;
	@ApiField("name")
	private String name;
	@ApiField("image")
	private String image;
	@ApiField("classfy")
	private String classfy;

	private long brandId;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getClassfy() {
		return classfy;
	}

	public void setClassfy(String classfy) {
		this.classfy = classfy;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

}
