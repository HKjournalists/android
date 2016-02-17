package com.chengniu.client.service.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chengniu.client.dao.BaoxianIntentDAO;
import com.chengniu.client.dao.BaoxianUnderwritingDAO;
import com.chengniu.client.dao.BaoxianUnderwritingReportDAO;
import com.chengniu.client.dao.BaoxianUnderwritingRequestDAO;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;
import com.chengniu.client.service.StatisticsService;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
	@Resource
	private BaoxianIntentDAO baoxianIntentDAO;
	@Resource
	private BaoxianUnderwritingDAO baoxianUnderwritingDAO;
	@Resource
	private BaoxianUnderwritingReportDAO baoxianUnderwritingReportDAO;
	@Resource
	private BaoxianUnderwritingRequestDAO baoxianUnderwritingRequestDAO;

	@Override
	public Map<String, StatisticsMap> queryStatisticsMap(SearchVO vo) {
		List<StatisticsMap> intentList = this.baoxianIntentDAO
				.queryStatisticsMap(vo);
		List<StatisticsMap> underwritingList = this.baoxianUnderwritingDAO
				.queryStatisticsMap(vo);
		List<StatisticsMap> underwritingRequestList = this.baoxianUnderwritingRequestDAO
				.queryStatisticsMap(vo);
		vo.setStatus("1");
		List<StatisticsMap> ordersList = this.baoxianUnderwritingReportDAO
				.queryStatisticsMap(vo);
		List<StatisticsMap> underwritingSuccessList = this.baoxianUnderwritingDAO
				.queryStatisticsMap(vo);
		List<StatisticsMap> underwritingRequestSucessList = this.baoxianUnderwritingRequestDAO
				.queryStatisticsMap(vo);
		if (intentList == null || intentList.isEmpty())
			return null;
		Map<String, StatisticsMap> map = new TreeMap<String, StatisticsMap>();
		if (intentList != null) {
			for (StatisticsMap stat : intentList) {
				if (stat != null) {
					StatisticsMap sm = new StatisticsMap();
					sm.setDate(stat.getDate());
					sm.setIntentTotal(stat.getTotal());
					map.put(stat.getDate(), sm);
				}
			}
		}
		if (underwritingList != null) {
			for (StatisticsMap stat : underwritingList) {
				if (stat != null) {
					StatisticsMap sm = map.get(stat.getDate());
					if (sm == null)
						sm = new StatisticsMap();
					sm.setUnderwritingTotal(stat.getTotal());
					map.put(stat.getDate(), sm);
				}
			}
		}
		if (ordersList != null) {
			for (StatisticsMap stat : ordersList) {
				if (stat != null) {
					StatisticsMap sm = map.get(stat.getDate());
					if (sm == null)
						sm = new StatisticsMap();
					sm.setOrdersTotal(stat.getTotal());
					map.put(stat.getDate(), sm);
				}
			}
		}
		if (underwritingRequestList != null) {
			for (StatisticsMap stat : underwritingRequestList) {
				if (stat != null) {
					StatisticsMap sm = map.get(stat.getDate());
					if (sm == null)
						sm = new StatisticsMap();
					sm.setReportTotal(stat.getTotal());
					map.put(stat.getDate(), sm);
				}
			}
		}
		if (underwritingRequestSucessList != null) {
			for (StatisticsMap stat : underwritingRequestSucessList) {
				if (stat != null) {
					StatisticsMap sm = map.get(stat.getDate());
					if (sm == null)
						sm = new StatisticsMap();
					sm.setReportSuccessTotal(stat.getTotal());
					map.put(stat.getDate(), sm);
				}
			}
		}
		if (underwritingSuccessList != null) {
			for (StatisticsMap stat : underwritingSuccessList) {
				if (stat != null) {
					StatisticsMap sm = map.get(stat.getDate());
					if (sm == null)
						sm = new StatisticsMap();
					sm.setUnderwritingSuccessTotal(stat.getTotal());
					map.put(stat.getDate(), sm);
				}
			}
		}
		return map;
	}
}