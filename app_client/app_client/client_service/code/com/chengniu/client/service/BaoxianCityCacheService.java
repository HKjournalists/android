package com.chengniu.client.service;

import java.util.List;

import javassist.NotFoundException;

import com.chengniu.client.pojo.BaoxianCityFanhua;

/**
 * 保险城市相关接口,从缓存中读取 2015-11-03
 *
 * @author zhongjie
 */
public interface BaoxianCityCacheService {
	/**
	 * 城市列表缓存所使用的KEY
	 */
	String BAOXIAN_OPENED_CITIES = "BaoxianOpenedCities";

	/**
	 * 初始化缓存
	 */
	void initCache();

	/**
	 * 查询已开通保险的城市列表
	 *
	 * @param city
	 * @param userType
	 * @return
	 */
	List<BaoxianCityFanhua> queryOpenedCities(BaoxianCityFanhua city,
			Integer userType);

	/**
	 * 修改保险供应商,并同步修改缓存中的内容
	 *
	 * @param provinceCode
	 *            供应商所属省份CODE
	 * @param cityCode
	 *            供应商所属城市CODE
	 * @throws NotFoundException
	 *             未找到省份或城市时抛出异常
	 */
	void modifyCompany(String provinceCode, String cityCode)
			throws NotFoundException;

}
