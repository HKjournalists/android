package com.kplus.car.model.response.request;

import com.kplus.car.Response;
import com.kplus.car.util.StringUtils;

public class AgainstReportRequest extends BaseRequest<Response> {

	@Override
	public String getApiMethodName() {
		return "/against/report.htm";
	}

	@Override
	public Class<Response> getResponseClass() {
		return Response.class;
	}
	
	public void setParams(String errorKey, String errorMsg, String recordId){
		if(!StringUtils.isEmpty(errorKey))
			addParams("errorKey", errorKey);
		if(!StringUtils.isEmpty(errorMsg))
			addParams("errorMsg", errorMsg);
		if(!StringUtils.isEmpty(recordId))
			addParams("recordId", recordId);
	}

}
