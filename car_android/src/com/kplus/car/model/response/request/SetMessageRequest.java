package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetAgainstRecordListResponse;

public class SetMessageRequest extends BaseRequest<GetAgainstRecordListResponse> {

	@Override
	public String getApiMethodName() {
		return "/against/setMessage.htm";
	}

	@Override
	public Class<GetAgainstRecordListResponse> getResponseClass() {
		return GetAgainstRecordListResponse.class;
	}
	
	public void setParams(String sessionId, String message){
		addParams("sessionId", sessionId).addParams("message", message);
	}

}
