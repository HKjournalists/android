package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResidueDegreeResponse;


public class GetResidueDegreeRequest extends BaseRequest<GetResidueDegreeResponse>
{

	@Override
	public Class<GetResidueDegreeResponse> getResponseClass() {
		return GetResidueDegreeResponse.class;
	}

	@Override
	public String getApiMethodName()
	{
		return "/clientLicence/getResidueDegree.htm";
	}
	
	public void setParams(long userId) {
		addParams("userId", userId);
	}
}
