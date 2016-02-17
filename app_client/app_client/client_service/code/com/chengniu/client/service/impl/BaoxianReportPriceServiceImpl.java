package com.chengniu.client.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.chengniu.client.dao.BaoxianReportPriceDAO;
import com.chengniu.client.pojo.BaoxianReportPrice;
import com.chengniu.client.service.BaoxianReportPriceService;

@Service("baoxianReportPriceService")
public class BaoxianReportPriceServiceImpl implements BaoxianReportPriceService {
	@Resource(name = "baoxianReportPriceDAO")
	private BaoxianReportPriceDAO baoxianReportPriceDAO;

	@Override
	public List<BaoxianReportPrice> queryPrice(String cityCode,
			String vehicleModelPrice) {
		int vehicleModelPriceInt = Double.valueOf(vehicleModelPrice).intValue();
		BaoxianReportPrice price = new BaoxianReportPrice();
		price.setCityCode(cityCode);
		if (vehicleModelPriceInt > 200 || vehicleModelPriceInt <= 0)
			vehicleModelPriceInt = 200;
		else
			price.setVehicleModelPrice(String.valueOf(vehicleModelPriceInt));
		List<BaoxianReportPrice> list = this.baoxianReportPriceDAO
				.queryPrice(price);
		if (list == null || list.isEmpty()) {
			BaoxianReportPrice brp = this.baoxianReportPriceDAO
					.queryMinPrice(price);
			if (brp != null && StringUtils.hasText(brp.getVehicleModelPrice())) {
				price.setVehicleModelPrice(brp.getVehicleModelPrice());
				// 再次报价
				list = this.baoxianReportPriceDAO.queryPrice(price);
			}
		}
		return list;
	}
}