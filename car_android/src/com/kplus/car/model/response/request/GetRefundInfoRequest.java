package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetRefundInfoResponse;
import com.kplus.car.model.response.GetUserInfoResponse;
import com.kplus.car.util.MD5;

public class GetRefundInfoRequest extends BaseRequest<GetRefundInfoResponse>{

	@Override
	public String getApiMethodName() {
		return "/refund/getRefundAccount.htm";
	}

	@Override
	public Class<GetRefundInfoResponse> getResponseClass() {
		return GetRefundInfoResponse.class;
	}
	
	public void setParams(long uid){
		addParams("uid", uid);
	}

}
