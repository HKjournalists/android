package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetValidCouponListResponse;

public class GetValidCouponListResquest extends BaseRequest<GetValidCouponListResponse>{

	@Override
	public String getApiMethodName() {
		return "/coupon/getValidList.htm";
	}

	@Override
	public Class<GetValidCouponListResponse> getResponseClass() {
		return GetValidCouponListResponse.class;
	}
	
	public void setParams(long orderTypeId, long uid, float price){
		addParams("orderTypeId", orderTypeId).addParams("uid", uid).addParams("price", price);
	}

}
