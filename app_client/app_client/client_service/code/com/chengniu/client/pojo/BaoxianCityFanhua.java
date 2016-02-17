package com.chengniu.client.pojo;

import java.io.Serializable;

public class BaoxianCityFanhua implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6257718478967763070L;

	private Integer id;

	private String cityCode;

	private String cityName;

	private Integer rank;

	private String spelling;

	private String spellingacronym;

	private String cityplate;

	private String proplate;

	private Integer kind;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName == null ? null : cityName.trim();
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getSpelling() {
		return spelling;
	}

	public void setSpelling(String spelling) {
		this.spelling = spelling == null ? null : spelling.trim();
	}

	public String getSpellingacronym() {
		return spellingacronym;
	}

	public void setSpellingacronym(String spellingacronym) {
		this.spellingacronym = spellingacronym == null ? null : spellingacronym
				.trim();
	}

	public String getCityplate() {
		return cityplate;
	}

	public void setCityplate(String cityplate) {
		this.cityplate = cityplate == null ? null : cityplate.trim();
	}

	public String getProplate() {
		return proplate;
	}

	public void setProplate(String proplate) {
		this.proplate = proplate == null ? null : proplate.trim();
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}
}