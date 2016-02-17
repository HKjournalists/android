package com.chengniu.client.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianIntent;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;
import com.google.common.collect.Maps;
import com.kplus.orders.rpc.dto.PageVO;

@Repository("baoxianIntentDAO")
public class BaoxianIntentDAO extends SuperDAO<BaoxianIntent, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianIntentDAO";
	}

	public int insertSelective(BaoxianIntent record) {
		return this.getSqlSession().insert(tip("insertSelective"), record);
	}

	public int updateByPrimaryKeySelective(BaoxianIntent record) {
		return this.getSqlSession().update(tip("updateByPrimaryKeySelective"),
				record);
	}

	public PageVO<BaoxianIntent> page(SearchVO search) {
		return super.queryByPage(search);
	}

	public List<BaoxianIntent> queryByVehicleAndStatus(Integer userType,
			String userId, String vehicleNum, String status) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userType", userType);
		params.put("userId", userId);
		params.put("vehicleNum", vehicleNum);
		params.put("status", status);

		return getSqlSession().selectList(tip("queryByVehicleAndStatus"),
				params);
	}

	public int updateStatus(BaoxianIntent intent) {
		return this.getSqlSession().update(tip("updateStatus"), intent);
	}

	public List<StatisticsMap> queryStatisticsMap(SearchVO vo) {
		return this.getSqlSession().selectList(tip("statistics"), vo);
	}

	public Integer queryCount(String status, String userId, Integer userType) {
		SearchVO vo = new SearchVO();
		vo.setUserType(userType);
		vo.setUserId(userId);
		List<String> stautsList = new ArrayList<String>();
		if ("0".equals(status)) {
			stautsList.add("1");
			stautsList.add("2");
			stautsList.add("4");
			stautsList.add("6");
		} else if ("1".equals(status)) {
			stautsList.add("10");
			stautsList.add("12");
			stautsList.add("14");
		} else if ("2".equals(status)) {
			stautsList.add("20");
			stautsList.add("26");
			stautsList.add("22");
			stautsList.add("24");
			stautsList.add("21");
		} else if (status != null)
			stautsList.add(status);
		if (stautsList.isEmpty())
			stautsList = null;
		vo.setListStatus(stautsList);
		return this.getSqlSession().selectOne(tip("pageCount"), vo);
	}
}