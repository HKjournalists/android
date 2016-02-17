package com.kplus.car.model.response.request;

import com.kplus.car.model.response.UpgradeComponentResponse;

public class UpgradeComponentRequest extends BaseRequest<UpgradeComponentResponse>{

	@Override
	public String getApiMethodName() {
		return "/app/upgradeComponent.htm";
	}

	@Override
	public Class<UpgradeComponentResponse> getResponseClass() {
		return UpgradeComponentResponse.class;
	}
	
	public void setParams(String comId, String lastModified, String clientType){
		addParams("comId", comId);
		addParams("lastModified", lastModified);
		addParams("clientType", clientType);
	}

}
