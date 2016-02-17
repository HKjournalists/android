package com.chengniu.client.action.baoxian;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chengniu.client.action.SuperController;
import com.chengniu.client.service.BaoxianCityCacheService;

/**
 * 保险供应商归属城市的相关接口 2015-11-11
 *
 * @author zhongjie
 */
@Controller
@RequestMapping("/insurance/city")
public class CityController extends SuperController {
	@Autowired
	private BaoxianCityCacheService baoxianCityCacheService;

	@ResponseBody
	@RequestMapping("/modify/{provinceCode}/{cityCode}")
	public String modify(@PathVariable String provinceCode,
			@PathVariable String cityCode) throws Exception {
		if (StringUtils.equals(provinceCode, "null")) {
			provinceCode = null;
		}
		if (StringUtils.equals(cityCode, "null")) {
			cityCode = null;
		}
		baoxianCityCacheService.modifyCompany(provinceCode, cityCode);
		return "success";
	}
}
