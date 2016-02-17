package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetCommonDictionaryResponse;

public class GetCommonDictionaryRequest extends BaseRequest<GetCommonDictionaryResponse> {

	@Override
	public String getApiMethodName() {
		return "/common/getDictionary.htm";
	}

	@Override
	public Class<GetCommonDictionaryResponse> getResponseClass() {
		return GetCommonDictionaryResponse.class;
	}
	
	public void setParams(String type){
		addParams("type", type);
	}

}
