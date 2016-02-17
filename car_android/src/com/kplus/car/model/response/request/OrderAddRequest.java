package com.kplus.car.model.response.request;

import com.kplus.car.model.Order;
import com.kplus.car.model.response.OrderAddResponse;
import com.kplus.car.util.StringUtils;

public class OrderAddRequest extends BaseRequest<OrderAddResponse> {

	@Override
	public String getApiMethodName() {
		return "/formOrder/add.htm";
	}

	@Override
	public Class<OrderAddResponse> getResponseClass() {
		return OrderAddResponse.class;
	}

	public void setParams(Order order, long uid, String couponId) {
		addParams("pId", order.getpId())
				.addParams("vehicleNum", order.getVehicleNum())
				.addParams("userId", order.getUserId())
				.addParams("name", order.getContactName())
				.addParams("phone", order.getContactPhone())
				.addParams("recordIds", order.getRecordIds())
				.addParams("overdueRecordIds", order.getOverdueRecordIds())
				.addParams("estimatePrice", order.getPrice())
				.addParams("userBalance", order.getUserBalance());
		if(!StringUtils.isEmpty(couponId) && !couponId.trim().equals("{}"))
			addParams("couponId", couponId);
		if(uid != 0)
			addParams("uid", uid);
	}

}
