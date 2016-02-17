package com.chengniu.client.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chengniu.client.dao.BaoxianCompanyDAO;
import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.pojo.BaoxianCompanyVO;
import com.chengniu.client.service.BaoxianCompanyService;

@Service("baoxianCompanyService")
public class BaoxianCompanyServiceImpl implements BaoxianCompanyService {

	@Resource(name = "baoxianCompanyDAO")
	private BaoxianCompanyDAO baoxianCompanyDAO;



	@Override
	public int openCount(String cityCode, Integer userType, Integer kind) {
		BaoxianCompanyVO company = new BaoxianCompanyVO();
		if (kind != null && kind == 0) {
			company.setProvince(cityCode);
		} else
			company.setCityCode(cityCode);
		company.setUserType(userType);
		return baoxianCompanyDAO.openCount(company);
	}

	@Override
	public List<BaoxianCompany> list(String cityCode, Integer userType) {
		return baoxianCompanyDAO.list(cityCode, userType);
	}

	@Override
	public BaoxianCompany query(String code, String cityCode) {
		return this.baoxianCompanyDAO.query(code, cityCode);
	}

    @Override
    public boolean insert(BaoxianCompany company) {
        return this.baoxianCompanyDAO.insert(company) > 0;
    }

    @Override
    public boolean updateCompanyEnabled(BaoxianCompany company) {
        return baoxianCompanyDAO.updateEnabled(company) > 0;
    }

    @Override
    public List<BaoxianCompany> queryCompaniesInCityByChannel(String cityCode, String channel) {
        return baoxianCompanyDAO.queryCompaniesInCityByChannel(cityCode, channel);
    }
}