package com.chengniu.client.service;

import java.util.List;

import com.chengniu.client.pojo.BaoxianCompany;

public interface BaoxianCompanyService {
	List<BaoxianCompany> list(String cityCode, Integer userType);

	int openCount(String cityCode, Integer userType, Integer kind);

	BaoxianCompany query(String code, String cityCode);

    boolean insert(BaoxianCompany company);

    boolean updateCompanyEnabled(BaoxianCompany company);

    List<BaoxianCompany> queryCompaniesInCityByChannel(String cityCode, String channel);
}