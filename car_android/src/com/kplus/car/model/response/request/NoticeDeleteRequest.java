package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BooleanResultResponse;

public class NoticeDeleteRequest extends BaseRequest<BooleanResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/notice/delete.htm";
	}

	@Override
	public Class<BooleanResultResponse> getResponseClass() {
		return BooleanResultResponse.class;
	}
	
	public void setParams(String noticeIds){
		addParams("noticeIds", noticeIds);
	}

}
