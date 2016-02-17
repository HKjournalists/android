package com.chengniu.client.pojo;

public class BaoxianCityMapping {

	private Integer id;

	private String cityCodeFanhua;

	private String cityNameFanhua;

	private String cityCodeYangguang;

	private String cityNameYangguang;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCityCodeFanhua() {
		return cityCodeFanhua;
	}

	public void setCityCodeFanhua(String cityCodeFanhua) {
		this.cityCodeFanhua = cityCodeFanhua == null ? null : cityCodeFanhua
				.trim();
	}

	public String getCityNameFanhua() {
		return cityNameFanhua;
	}

	public void setCityNameFanhua(String cityNameFanhua) {
		this.cityNameFanhua = cityNameFanhua == null ? null : cityNameFanhua
				.trim();
	}

	public String getCityCodeYangguang() {
		return cityCodeYangguang;
	}

	public void setCityCodeYangguang(String cityCodeYangguang) {
		this.cityCodeYangguang = cityCodeYangguang == null ? null
				: cityCodeYangguang.trim();
	}

	public String getCityNameYangguang() {
		return cityNameYangguang;
	}

	public void setCityNameYangguang(String cityNameYangguang) {
		this.cityNameYangguang = cityNameYangguang == null ? null
				: cityNameYangguang.trim();
	}
}