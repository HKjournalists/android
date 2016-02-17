package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianUnderwritingReportPayinfo;

@Repository("baoxianUnderwritingReportPayinfoDAO")
public class BaoxianUnderwritingReportPayinfoDAO extends
		SuperDAO<BaoxianUnderwritingReportPayinfo, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianUnderwritingReportPayinfoMapper";
	}

	public BaoxianUnderwritingReportPayinfo queryByReportId(String id) {
		return this.getSqlSession().selectOne(this.tip("queryByReportId"), id);
	}

	public BaoxianUnderwritingReportPayinfo queryByPayedReportId(String id) {
		return this.getSqlSession().selectOne(this.tip("queryByPayedReportId"),
				id);
	}

	public int updateResponse(BaoxianUnderwritingReportPayinfo pay) {
		return this.getSqlSession().update(this.tip("updateResponse"), pay);
	}

	public int updateTradeNum(BaoxianUnderwritingReportPayinfo pay) {
		return this.getSqlSession().update(this.tip("updateTradeNum"), pay);
	}

	public int updateRequest(BaoxianUnderwritingReportPayinfo pay) {
		return this.getSqlSession().update(this.tip("updateRequest"), pay);
	}

	public int updateStatus(BaoxianUnderwritingReportPayinfo pay) {
		return this.getSqlSession().update(this.tip("updateStatus"), pay);
	}
}