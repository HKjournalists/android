package com.chengniu.client.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chengniu.client.dao.BaoxianVehicleModelDAO;
import com.chengniu.client.pojo.BaoxianVehicleModel;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.service.BaoxianVehicleModelService;
import com.kplus.orders.rpc.dto.PageVO;

@Service("baoxianVehicleModelService")
public class BaoxianVehicleModelServiceImpl implements
		BaoxianVehicleModelService {
	@Resource(name = "baoxianVehicleModelDAO")
	private BaoxianVehicleModelDAO baoxianVehicleModelDAO;

	@Override
	public PageVO<BaoxianVehicleModel> page(SearchVO search) {
		return this.baoxianVehicleModelDAO.queryByPage(search);
	}
}