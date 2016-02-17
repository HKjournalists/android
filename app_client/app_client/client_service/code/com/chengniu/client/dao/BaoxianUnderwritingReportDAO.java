package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianUnderwritingReport;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;

@Repository("baoxianUnderwritingReportDAO")
public class BaoxianUnderwritingReportDAO extends
		SuperDAO<BaoxianUnderwritingReport, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianUnderwritingReportMapper";
	}

	/**
	 * 报价查询
	 * 
	 * @param price
	 * @return
	 */
	public BaoxianUnderwritingReport queryByUnderwritingId(String id) {
		return this.getSqlSession().selectOne(
				this.tip("queryByUnderwritingId"), id);
	}

	public int updateSubmitOrders(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updateSubmitOrders"), t);
	}

	public BaoxianUnderwritingReport queryByOrderNum(String orderNum,
			Integer userType) {
		BaoxianUnderwritingReport report = new BaoxianUnderwritingReport();
		report.setOrdersNum(orderNum);
		report.setUserType(userType);
		return this.getSqlSession().selectOne(this.tip("queryByOrderNum"),
				report);
	}

	public int queryCount(BaoxianUnderwritingReport t) {
		return this.getSqlSession().selectOne(this.tip("queryCount"), t);
	}

	public int updateExpressStatus(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updateExpressStatus"), t);
	}

	public int updatePropNum(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updatePropNum"), t);
	}

	public int updatePolicyNo(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updatePolicyNo"), t);
	}

	public int updatePolicyStatus(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updatePolicyStatus"), t);
	}

	public int updateStatus(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updateStatus"), t);
	}

	public List<StatisticsMap> queryStatisticsMap(SearchVO vo) {
		return this.getSqlSession().selectList(tip("statistics"), vo);
	}

	public int updatePayStatus(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updatePayStatus"), t);
	}

	public int updateInsuerInfoStatus(BaoxianUnderwritingReport t) {
		return this.getSqlSession().update(this.tip("updateInsuerInfoStatus"),
				t);
	}
}