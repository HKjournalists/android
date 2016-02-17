package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianCityMapping;

@Repository("baoxianCityMappingDAO")
public class BaoxianCityMappingDAO extends SuperDAO<BaoxianCityMapping, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianCityMappingMapper";
	}

	public BaoxianCityMapping queryCity(String code) {
		return this.getSqlSession().selectOne(this.tip("queryCity"), code);
	}

}