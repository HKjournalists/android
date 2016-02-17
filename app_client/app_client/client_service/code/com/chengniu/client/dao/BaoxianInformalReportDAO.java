package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianInformalReport;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("baoxianInformalReportDAO")
public class BaoxianInformalReportDAO extends
		SuperDAO<BaoxianInformalReport, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianInformalReportMapper";
	}

	public BaoxianInformalReport querySid(String sid) {
		return this.getSqlSession().selectOne(this.tip("querySid"), sid);
	}

	public Integer queryCountByRequestId(String requestId) {
		return this.getSqlSession().selectOne(
				this.tip("queryCountByRequestId"), requestId);
	}

	public int insertSelective(BaoxianInformalReport record) {
		return getSqlSession().insert(tip("insertSelective"), record);
	}

	public int updateByPrimaryKeySelective(BaoxianInformalReport record) {
		return getSqlSession().update(tip("updateByPrimaryKeySelective"),
				record);
	}

	public List<BaoxianInformalReport> queryByRequestId(String requestId) {
		return getSqlSession().selectList(tip("queryByRequestId"), requestId);
	}

	public int updateDelete(String requestId) {
		return this.getSqlSession().update(this.tip("updateDelete"), requestId);
	}

	public BaoxianInformalReport queryByRequestIdAndCompanyCode(
			String requestId, String code) {
		BaoxianInformalReport report = new BaoxianInformalReport();
		report.setBaoxianUnderwritingRequestId(requestId);
		report.setBaoxianCompanyCode(code);
		return getSqlSession().selectOne(tip("queryByRequestIdAndCompanyCode"),
				report);
	}
}