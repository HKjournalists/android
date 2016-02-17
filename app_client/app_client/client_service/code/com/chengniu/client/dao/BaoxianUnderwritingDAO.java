package com.chengniu.client.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingDTO;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;
import com.google.common.collect.Maps;
import com.kplus.orders.rpc.dto.PageVO;

@Repository("baoxianUnderwritingDAO")
public class BaoxianUnderwritingDAO extends
		SuperDAO<BaoxianUnderwriting, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianUnderwritingMapper";
	}

	public int updateSupportAutomatic(BaoxianUnderwriting t) {
		return this.getSqlSession().update(this.tip("updateSupportAutomatic"),
				t);
	}

	public int updateAutomatic(BaoxianUnderwriting t) {
		return this.getSqlSession().update(this.tip("updateAutomatic"), t);
	}

	public int queryCount(BaoxianUnderwriting t) {
		return this.getSqlSession().selectOne(this.tip("queryCount"), t);
	}

	public int queryReportCountInPeriod(Integer status, String userId,
			Integer userType, Date beginCreateDate, Date endCreateDate) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
		params.put("status", status);
		params.put("userId", userId);
		params.put("userType", userType);
		params.put("beginCreateDate", beginCreateDate);
		params.put("endCreateDate", endCreateDate);
		return this.getSqlSession().selectOne(
				this.tip("queryReportCountInPeriod"), params);
	}

	public int queryRequestCountInPeriod(Integer status, String userId,
			Integer userType, Date beginCreateDate, Date endCreateDate) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
		params.put("status", status);
		params.put("userId", userId);
		params.put("userType", userType);
		params.put("beginCreateDate", beginCreateDate);
		params.put("endCreateDate", endCreateDate);
		return this.getSqlSession().selectOne(
				this.tip("queryRequestCountInPeriod"), params);
	}

	public PageVO<BaoxianUnderwritingDTO> page(SearchVO search) {
		return super.queryByPage(search);
	}

	public int updateStatusReported(BaoxianUnderwriting t) {
		return this.getSqlSession().update(this.tip("updateStatusReported"), t);
	}

	public List<StatisticsMap> queryStatisticsMap(SearchVO vo) {
		return this.getSqlSession().selectList(tip("statistics"), vo);
	}

	public int updateStatus(BaoxianUnderwriting t) {
		return this.getSqlSession().update(this.tip("updateStatus"), t);
	}

	public int updateInsuerInfoStatus(BaoxianUnderwriting t) {
		return this.getSqlSession().update(this.tip("updateInsuerInfoStatus"),
				t);
	}

	public int close(BaoxianUnderwriting o) {
		return this.getSqlSession().update(this.tip("close"), o);
	}

	public int updateMediaStatus(BaoxianUnderwriting t) {
		return this.getSqlSession().update(this.tip("updateMediaStatus"), t);
	}

	public BaoxianUnderwriting queryByIntentId(String intentId) {
		return this.getSqlSession().selectOne(this.tip("queryByIntentId"),
				intentId);
	}
}