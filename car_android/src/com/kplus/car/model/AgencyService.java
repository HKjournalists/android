package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class AgencyService extends BaseModelObj {

	@ApiField("id")
	private Long id;
	@ApiField("name")
	private String name;
	@ApiField("logo")
	private String logo;
	@ApiField("regoin")
	private String regoin;
	@ApiField("link")
	private String link;
	@ApiField("level")
	private Integer level;

	private String cityName;

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

	public String getRegoin() {
		return regoin;
	}

	public void setRegoin(String regoin) {
		this.regoin = regoin;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
