package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWOrderResponse;

public class FWOrderRequest extends BaseRequest<FWOrderResponse> {

	@Override
	public String getApiMethodName() {
		return "/fw/orders.htm";
	}

	@Override
	public Class<FWOrderResponse> getResponseClass() {
		return FWOrderResponse.class;
	}
	
	public void setParams(long uid, int pageIndex, int pageSize, int status){
		addParams("uid", uid).addParams("pageIndex", pageIndex).addParams("pageSize", pageSize);
		if(status != 0)
			addParams("status", status);
	}

}
