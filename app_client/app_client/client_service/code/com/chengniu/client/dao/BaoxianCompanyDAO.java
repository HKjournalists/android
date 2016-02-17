package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.pojo.BaoxianCompanyVO;

@Repository("baoxianCompanyDAO")
public class BaoxianCompanyDAO extends SuperDAO<BaoxianCompany, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianCompanyMapper";
	}

	/**
	 * 报价查询
	 *
	 * @param code
	 * @param cityCode
	 * @return
	 */
	public BaoxianCompany query(String code, String cityCode) {
		BaoxianCompany company = new BaoxianCompany();
		company.setCityCode(cityCode);
		company.setCode(code);
		return this.getSqlSession().selectOne(this.tip("queryCode"), company);
	}

	/**
	 * 报价查询
	 *
	 * @param cityCode
	 * @param userType
	 * @return
	 */
	public List<BaoxianCompany> list(String cityCode, Integer userType) {
		BaoxianCompanyVO company = new BaoxianCompanyVO();
		company.setCityCode(cityCode);
		company.setUserType(userType);
		return this.getSqlSession().selectList(this.tip("list"), company);
	}

	/**
	 * 报价开通的数量
	 *
	 * @param company
	 * @return
	 */
	public int openCount(BaoxianCompanyVO company) {
		return this.getSqlSession().selectOne(this.tip("openCount"), company);
	}

	/**
	 * 获取所有的公司
	 *
	 * @return
	 */
	public List<BaoxianCompany> findAll() {
		return this.getSqlSession().selectList(this.tip("findAll"));
	}

    public int updateEnabled(BaoxianCompany company) {
        return getSqlSession().update(tip("updateEnabled"), company);
    }

    public List<BaoxianCompany> queryCompaniesInCityByChannel(String cityCode, String channel) {
        BaoxianCompany c = new BaoxianCompany();
        c.setCityCode(cityCode);
        c.setChannel(channel);
        return getSqlSession().selectList(tip("queryCompaniesInCityByChannel"), c);
    }
}