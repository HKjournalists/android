package com.chengniu.client.action.baoxian;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.pojo.BaoxianCityFanhua;
import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianCityCacheService;
import com.chengniu.client.service.BaoxianCompanyService;
import com.chengniu.client.service.FanhuaService;
import com.kplus.orders.execption.DisposeException;

/**
 * 配置接口
 */
public class BaoxianConfigurationAction extends SuperAction {

	public String cityCode;
	public String cityName;
	public String level;
	public String open;

	private String keyword;

	@Autowired
	private BaoxianCityCacheService baoxianCityCacheService;

	@Autowired
	public BaoxianBaseInfoService baoxianBaseInfoService;

	@Autowired
	public BaoxianCompanyService baoxianCompanyService;

	@Autowired
	private FanhuaService fanhuaService;

	public String queryCity() throws Exception {
		BaoxianCityFanhua city = new BaoxianCityFanhua();
		city.setCityCode(cityCode);
		try {
			city.setKind(Integer.parseInt(level));
		} catch (Exception e) {
			city.setKind(0);
		}
		if (city.getKind() != null && city.getKind() != 0) {
			if (cityCode == null)
				throw new DisposeException("请重新登录");
			city.setCityCode(city.getCityCode()
					.substring(0, city.getKind() * 2));
		} else if (cityCode != null && cityCode.length() > 4)
			city.setCityCode(city.getCityCode().substring(0, 2) + "0000");
		city.setCityName(cityName);
		List<BaoxianCityFanhua> resultList;
		if ("true".equals(open)) {
			resultList = baoxianCityCacheService.queryOpenedCities(city, this
					.getCurrentOperator().getUserType());
		} else {
			resultList = baoxianBaseInfoService.queryCity(city);
		}
		return super.ajax(resultList);
	}

	public String queryCityByName() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BaoxianCityFanhua city = this.baoxianBaseInfoService
				.queryByCityName(cityName);
		if (city != null) {
			map.put("cityName", city.getCityName());
			map.put("cityCode", city.getCityCode());
			if (city != null) {
				city.setCityCode(city.getCityCode().substring(0, 2) + "0000");
				try {
					city.setKind(Integer.parseInt(level));
				} catch (Exception e) {
				}
				BaoxianCityFanhua pro = this.baoxianBaseInfoService
						.queryCityInfo(city.getCityCode(), 0);
				if (pro != null) {
					map.put("provinceName", pro.getCityName());
					map.put("provinceCode", pro.getCityCode());
				}
			}
		}
		return super.ajax(map);
	}

	public String getLocalCompanyList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.hasText(cityCode))
			cityCode = "330100";
		List<BaoxianCompany> list = baoxianCompanyService.list(cityCode, this
				.getCurrentOperator().getUserType());
		map.put("list", list);
		map.put("total", list != null ? list.size() : 0);
		return super.ajax(map);
	}

	public String fetchPagniateSupportVehicles() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write("{\"code\":\"0\",\"msg\":\"\",\"data\":"
					+ this.fanhuaService.fetchPagniateVehicles(keyword, this.getPageNum())
					+ "}");
			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
		return NONE;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
