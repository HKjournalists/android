package com.kplus.car.model.response.request;

import com.kplus.car.model.response.AddUserInfoReponse;
import com.kplus.car.util.StringUtils;

public class AddUserInfoRequest extends BaseRequest<AddUserInfoReponse>{

	@Override
	public String getApiMethodName() {
		return "/user/addUserInfo.htm";
	}

	@Override
	public Class<AddUserInfoReponse> getResponseClass() {
		return AddUserInfoReponse.class;
	}
	
	public void setParams(long uid, String iconUrl,String name, int sex, String address, String info){
		addParams("uid", uid);
		if(!StringUtils.isEmpty(iconUrl))
			addParams("iconUrl", iconUrl);
		if(!StringUtils.isEmpty(name))
			addParams("name", name);
		addParams("sex", sex);
		if(!StringUtils.isEmpty(address))
			addParams("address", address);
		if(!StringUtils.isEmpty("info"))
			addParams("info", info);
	}

}
