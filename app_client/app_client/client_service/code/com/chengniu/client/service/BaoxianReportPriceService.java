package com.chengniu.client.service;

import java.util.List;

import com.chengniu.client.pojo.BaoxianReportPrice;

public interface BaoxianReportPriceService {
	List<BaoxianReportPrice> queryPrice(String cityCode, String price);
}