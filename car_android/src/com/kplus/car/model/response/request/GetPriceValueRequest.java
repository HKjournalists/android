package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetPriceValueResponse;

public class GetPriceValueRequest extends BaseRequest<GetPriceValueResponse> {

	@Override
	public String getApiMethodName() {
		return "/formOrder/genPrice.htm";
	}

	@Override
	public Class<GetPriceValueResponse> getResponseClass() {
		return GetPriceValueResponse.class;
	}

	public void setParams(String recordIds, long pId) {
		addParams("recordIds", recordIds);
		if(pId != 0)
			addParams("pId", pId);
	}
}
