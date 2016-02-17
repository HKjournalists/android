package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetOrderListResponse;

public class GetOrderListRequest extends BaseRequest<GetOrderListResponse> {

	@Override
	public String getApiMethodName() {
		return "/formOrder/getList.htm";
	}

	@Override
	public Class<GetOrderListResponse> getResponseClass() {
		return GetOrderListResponse.class;
	}

	public void setParams(long pId, int pageIndex,int status) {
		addParams("pId", pId).addParams("pageIndex", pageIndex).addParams("pageSize", 10);
		if(status != 0)
			addParams("status", status);
	}
}
