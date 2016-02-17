package com.kplus.car.model.response.request;

import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.response.JiazhaoSaveResponse;

public class JiazhaoSaveRequest extends BaseRequest<JiazhaoSaveResponse> {

	@Override
	public String getApiMethodName() {
		return "/jiazhao/save.htm";
	}

	@Override
	public Class<JiazhaoSaveResponse> getResponseClass() {
		return JiazhaoSaveResponse.class;
	}

	public void setParams(Jiazhao data, String clientType, long uid) {
		addParams("jszh", data.getJszh());
		if (data.getId() != null)
			addParams("id", data.getId());
		addParams("dabh", data.getDabh());
		addParams("isHidden", data.getIsHidden());
		addParams("xm", data.getXm());
		addParams("space", data.getSpace());
		addParams("startTime", data.getStartTime());
		addParams("endTime", data.getDate());
		addParams("remindDate", data.getRemindDate());
		addParams("ljjf", data.getLjjf());
		if(uid != 0)
			addParams("uid", uid);
		addParams("clientType", clientType);
	}
}
