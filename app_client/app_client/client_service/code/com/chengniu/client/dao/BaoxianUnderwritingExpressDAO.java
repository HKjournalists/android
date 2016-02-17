package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianUnderwritingExpress;

@Repository("baoxianUnderwritingExpressDAO")
public class BaoxianUnderwritingExpressDAO extends
		SuperDAO<BaoxianUnderwritingExpress, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianUnderwritingExpressMapper";
	}

	public BaoxianUnderwritingExpress queryByBaoxianUnderwritingReportId(
			String id) {
		return this.getSqlSession().selectOne(
				this.tip("queryByBaoxianUnderwritingReportId"), id);
	}
}