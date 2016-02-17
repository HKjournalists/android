package com.chengniu.client.service;

import com.chengniu.client.pojo.BaoxianVehicleModel;
import com.chengniu.client.pojo.SearchVO;
import com.kplus.orders.rpc.dto.PageVO;

public interface BaoxianVehicleModelService {
	PageVO<BaoxianVehicleModel> page(SearchVO vo);
}