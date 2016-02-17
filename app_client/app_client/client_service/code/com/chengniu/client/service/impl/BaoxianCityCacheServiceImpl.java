package com.chengniu.client.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.NotFoundException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.chengniu.client.dao.BaoxianCityDAO;
import com.chengniu.client.dao.BaoxianCompanyDAO;
import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.pojo.BaoxianCityFanhua;
import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.pojo.BaoxianCompanyVO;
import com.chengniu.client.pojo.OpenedCityBO;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianCityCacheService;
import com.chengniu.client.service.BaoxianCompanyService;
import com.whalin.MemCached.MemCachedClient;

/**
 * 保险城市相关接口,从缓存中读取 2015-11-03
 *
 * @author zhongjie
 */
@Service("baoxianCityCacheService")
public class BaoxianCityCacheServiceImpl implements BaoxianCityCacheService {
	private static final Logger logger = LogManager
			.getLogger(BaoxianCityCacheServiceImpl.class);
	private static List<OpenedCityBO> localCities;
	@Resource
	private MemCachedClient memcachedClient;
	@Resource
	private BaoxianCityDAO baoxianCityDAO;
	@Resource
	private BaoxianCompanyDAO baoxianCompanyDAO;
	@Resource
	private BaoxianBaseInfoService baoxianBaseInfoService;
	@Resource
	private BaoxianCompanyService baoxianCompanyService;

	@PostConstruct
	public void initCache() {
		localCities = buildOpenedCities();
		sync2Memcached();
	}

	private void sync2Memcached() {
		Object obj = memcachedClient.get(BAOXIAN_OPENED_CITIES);
		if (obj == null) {
			memcachedClient.add(BAOXIAN_OPENED_CITIES, localCities);
		} else {
			memcachedClient.set(BAOXIAN_OPENED_CITIES, localCities);
		}
	}

	private List<OpenedCityBO> buildOpenedCities() {
		List<OpenedCityBO> cityList = new ArrayList<OpenedCityBO>();

		// 构建B端和C端,已开通城市的map
		List<BaoxianCompany> companies = baoxianCompanyDAO.findAll();
		Map<String, Boolean> bMap = new HashMap<String, Boolean>();
		Map<String, Boolean> cMap = new HashMap<String, Boolean>();
		if (!CollectionUtils.isEmpty(companies)) {
			for (BaoxianCompany company : companies) {
				if (StringUtils.isBlank(company.getCityCode())) {
					continue;
				}
				// 初始化开关信息
				Boolean bp = bMap.get(company.getProvince());
				if (bp == null) {
					bMap.put(company.getProvince(), Boolean.FALSE);
				}
				Boolean cp = cMap.get(company.getProvince());
				if (cp == null) {
					cMap.put(company.getProvince(), Boolean.FALSE);
				}
				Boolean bc = bMap.get(company.getCityCode());
				if (bc == null) {
					bMap.put(company.getCityCode(), Boolean.FALSE);
				}
				Boolean cc = cMap.get(company.getCityCode());
				if (cc == null) {
					cMap.put(company.getCityCode(), Boolean.FALSE);
				}

                if ((company.getChannelStatus() != null && company.getChannelStatus())) {
                    if (company.getOpenInfo() != null
                            && (company.getOpenInfo() == BaoxianCompanyVO.OPEN_TYPE.OPENED_ALL.value)) {
                        bMap.put(company.getCityCode(), Boolean.TRUE);
                        cMap.put(company.getCityCode(), Boolean.TRUE);
                        if (StringUtils.isNotBlank(company.getProvince())) {
                            bMap.put(company.getProvince(), Boolean.TRUE);
                            cMap.put(company.getProvince(), Boolean.TRUE);
                        }
                    } else if (company.getOpenInfo() != null
                            && (company.getOpenInfo() == BaoxianCompanyVO.OPEN_TYPE.OPENED_B.value)) {
                        bMap.put(company.getCityCode(), Boolean.TRUE);
                        if (StringUtils.isNotBlank(company.getProvince())) {
                            bMap.put(company.getProvince(), Boolean.TRUE);
                        }
                    } else if (company.getOpenInfo() != null
                            && (company.getOpenInfo() == BaoxianCompanyVO.OPEN_TYPE.OPENED_C.value)) {
                        cMap.put(company.getCityCode(), Boolean.TRUE);
                        if (StringUtils.isNotBlank(company.getProvince())) {
                            cMap.put(company.getProvince(), Boolean.TRUE);
                        }
                    }
                }
			}
		}

		// 遍历城市,构建已开通城市的列表
		List<BaoxianCityFanhua> cities = baoxianCityDAO.findAllOrderByKindAsc();
		if (!CollectionUtils.isEmpty(cities)) {
			for (BaoxianCityFanhua city : cities) {
				OpenedCityBO openedCityBO = new OpenedCityBO();
				openedCityBO.city = city;
				Boolean isOpenedB = bMap.get(city.getCityCode());
				openedCityBO.isOpenedB = isOpenedB != null ? isOpenedB
						: Boolean.FALSE;
				Boolean isOpenedC = cMap.get(city.getCityCode());
				openedCityBO.isOpenedC = isOpenedC != null ? isOpenedC
						: Boolean.FALSE;
				cityList.add(openedCityBO);
			}
		}

		return cityList;
	}

	@Override
	public List<BaoxianCityFanhua> queryOpenedCities(BaoxianCityFanhua cond,
			Integer userType) {
		if (localCities == null) {
			// 缓存中没有数据,依旧从数据库中查出
			logger.debug("query from database...");
			return queryCitiesByDatabase(cond, userType);
		} else {
			logger.debug("query from cache...");
			return filterCitiesByCache(cond, userType);
		}
	}

	@Override
	public void modifyCompany(String provinceCode, String cityCode)
			throws NotFoundException {
		logger.debug("modify company,province code:{}; city code:{}",
				provinceCode, cityCode);
		// 找出对应的已缓存城市对象
		OpenedCityBO province = null;
		OpenedCityBO city = null;
		if (!CollectionUtils.isEmpty(localCities)) {
			for (OpenedCityBO ccity : localCities) {
				if (ccity.city.getCityCode().equals(provinceCode)) {
					province = ccity;
				}
				if (ccity.city.getCityCode().equals(cityCode)) {
					city = ccity;
				}
				if (province != null && city != null) {
					break;
				}
			}
		}
		if (province == null && city == null) {
			throw new NotFoundException("在缓存中未找到对应的省市对象!");
		}

		// 查询省市的开通情况
		BaoxianCompanyVO companyVO = new BaoxianCompanyVO();
		if (province != null) {
			companyVO.setProvince(provinceCode);
			companyVO.setCityCode(null);
			companyVO.setUserType(BaoxianBaseInfo.USER_TYPE.PROVIDER.value);
			province.isOpenedB = baoxianCompanyDAO.openCount(companyVO) > 0;
			companyVO.setUserType(BaoxianBaseInfo.USER_TYPE.PERSON.value);
			province.isOpenedC = baoxianCompanyDAO.openCount(companyVO) > 0;
			logger.debug("province status is: b:{}, c:{}", province.isOpenedB,
					province.isOpenedC);
		}
		if (city != null) {
			companyVO.setProvince(null);
			companyVO.setCityCode(cityCode);
			companyVO.setUserType(BaoxianBaseInfo.USER_TYPE.PROVIDER.value);
			city.isOpenedB = baoxianCompanyDAO.openCount(companyVO) > 0;
			companyVO.setUserType(BaoxianBaseInfo.USER_TYPE.PERSON.value);
			city.isOpenedC = baoxianCompanyDAO.openCount(companyVO) > 0;
			logger.debug("city status is: b:{}, c:{}", city.isOpenedB,
					city.isOpenedC);
		}
		sync2Memcached();
	}

	private List<BaoxianCityFanhua> queryCitiesByDatabase(
			BaoxianCityFanhua cond, Integer userType) {
		List<BaoxianCityFanhua> list = baoxianBaseInfoService.queryCity(cond);
		List<BaoxianCityFanhua> resultList;
		if (cond.getKind() != null
				&& (cond.getKind() == 0 || cond.getKind() == 1)) {
			resultList = new ArrayList<BaoxianCityFanhua>();
			for (BaoxianCityFanhua baoxiancityfanhua : list) {
				if (this.baoxianCompanyService.openCount(
						baoxiancityfanhua.getCityCode(), userType,
						cond.getKind()) > 0) {
					resultList.add(baoxiancityfanhua);
				}
			}
		} else {
			resultList = list;
		}
		return resultList;
	}

	/**
	 * 从缓存中取出城市并过滤,且只返回已开通的城市
	 *
	 * @param cond
	 * @param userType
	 * @return
	 */
	private List<BaoxianCityFanhua> filterCitiesByCache(BaoxianCityFanhua cond,
			Integer userType) {
		if (CollectionUtils.isEmpty(localCities)) {
			return new ArrayList<BaoxianCityFanhua>();
		}
		Integer kind = cond.getKind();
		String cityCode = cond.getCityCode();
		String cityName = cond.getCityName();
		if (kind == null) {
			kind = 0;
		}

		List<BaoxianCityFanhua> res = new ArrayList<BaoxianCityFanhua>();
		if (!CollectionUtils.isEmpty(localCities)) {
			for (OpenedCityBO cachedCity : localCities) {
				if (!kind.equals(cachedCity.city.getKind())) {
					continue;
				}
				if (StringUtils.isNotBlank(cityCode)
						&& cachedCity.city.getCityCode().indexOf(cityCode) != 0) {
					continue;
				}
				if (StringUtils.isNotBlank(cityName)
						&& !cityName.equals(cachedCity.city.getCityName())) {
					continue;
				}
				if (!userType.equals(BaoxianCompanyVO.OPEN_TYPE.OPENED_B.value)
						&& !userType
								.equals(BaoxianCompanyVO.OPEN_TYPE.OPENED_C.value)) {
					if (!cachedCity.isOpenedB || !cachedCity.isOpenedC) {
						continue;
					}
				} else {
					if (userType
							.equals(BaoxianCompanyVO.OPEN_TYPE.OPENED_B.value)
							&& !cachedCity.isOpenedB) {
						continue;
					} else if (userType
							.equals(BaoxianCompanyVO.OPEN_TYPE.OPENED_C.value)
							&& !cachedCity.isOpenedC) {
						continue;
					}
				}
				res.add(cachedCity.city);
			}
		}
		return res;
	}

}
