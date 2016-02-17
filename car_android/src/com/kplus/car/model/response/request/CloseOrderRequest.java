package com.kplus.car.model.response.request;

import com.kplus.car.model.response.CloseOrderResponse;

public class CloseOrderRequest extends BaseRequest<CloseOrderResponse>{

	@Override
	public String getApiMethodName() {
		return "/formOrder/closeOrder.htm";
	}

	@Override
	public Class<CloseOrderResponse> getResponseClass() {
		return CloseOrderResponse.class;
	}
	
	public void setParams(long orderId, long pId){
		addParams("orderId", orderId).addParams("pId", pId);
	}

}
