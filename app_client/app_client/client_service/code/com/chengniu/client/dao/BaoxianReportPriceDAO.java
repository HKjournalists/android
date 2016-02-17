package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianReportPrice;

@Repository("baoxianReportPriceDAO")
public class BaoxianReportPriceDAO extends SuperDAO<BaoxianReportPrice, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianReportPriceMapper";
	}

	/**
	 * 报价查询
	 * 
	 * @param price
	 * @return
	 */
	public BaoxianReportPrice queryMinPrice(BaoxianReportPrice price) {
		return this.getSqlSession().selectOne(this.tip("queryPrice"), price);
	}

	/**
	 * 报价查询
	 * 
	 * @param price
	 * @return
	 */
	public List<BaoxianReportPrice> queryPrice(BaoxianReportPrice price) {
		return this.getSqlSession().selectList(this.tip("queryPrice"), price);
	}
}