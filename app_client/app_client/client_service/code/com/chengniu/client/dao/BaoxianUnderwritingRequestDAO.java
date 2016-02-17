package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianUnderwritingRequest;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;

@Repository("baoxianUnderwritingRequestDAO")
public class BaoxianUnderwritingRequestDAO extends
		SuperDAO<BaoxianUnderwritingRequest, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianUnderwritingRequestMapper";
	}

	public int insertSelective(BaoxianUnderwritingRequest t) {
		if (t == null)
			return 0;
		return this.getSqlSession().insert(this.tip("insertSelective"), t);
	}

	public int updateByPrimaryKeySelective(BaoxianUnderwritingRequest t) {
		if (t == null)
			return 0;
		return this.getSqlSession().update(
				this.tip("updateByPrimaryKeySelective"), t);
	}

	public BaoxianUnderwritingRequest queryByIntentId(String intentId) {
		return this.getSqlSession().selectOne(this.tip("queryByIntentId"),
				intentId);
	}

	public int updateStatus(BaoxianUnderwritingRequest request) {
		return this.getSqlSession().update(this.tip("updateStatus"), request);
	}

	public List<StatisticsMap>  queryStatisticsMap(SearchVO vo) {
		return this.getSqlSession().selectList(tip("statistics"), vo);
	}
}