package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianVehicleModel;

@Repository("baoxianVehicleModelDAO")
public class BaoxianVehicleModelDAO extends
		SuperDAO<BaoxianVehicleModel, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianVehicleModelMapper";
	}

	/**
	 * 报价查询
	 * 
	 * @param key
	 * @return
	 */
	public BaoxianVehicleModel queryByCode(String vehicleCode) {
		return this.getSqlSession().selectOne(this.tip("queryByCode"),
				vehicleCode);
	}
}