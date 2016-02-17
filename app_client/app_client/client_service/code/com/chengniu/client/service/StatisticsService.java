package com.chengniu.client.service;

import java.util.List;
import java.util.Map;

import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;

public interface StatisticsService {
	public Map<String, StatisticsMap> queryStatisticsMap(SearchVO vo);
}