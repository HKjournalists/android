package com.kplus.car.model.response.request;

import com.kplus.car.model.response.AddRefundInfoReponse;
import com.kplus.car.util.StringUtils;

public class AddRefundInfoRequest extends BaseRequest<AddRefundInfoReponse>{

	@Override
	public String getApiMethodName() {
		return "/refund/add.htm";
	}

	@Override
	public Class<AddRefundInfoReponse> getResponseClass() {
		return AddRefundInfoReponse.class;
	}
	
	public void setParams(long uid, int type,String account,String name, String bank){
		addParams("uid", uid);
		addParams("type",type);
		if(!StringUtils.isEmpty(account))
			addParams("account", account);
		if(!StringUtils.isEmpty(name))
			addParams("name", name);
		if(!StringUtils.isEmpty(bank))
			addParams("bank", bank);
	}

}
