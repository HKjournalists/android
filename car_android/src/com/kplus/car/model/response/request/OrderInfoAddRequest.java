package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class OrderInfoAddRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/formOrder/addInfo.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(long orderId, String fileUrl, int certType,
			String ownerName) {
		addParams("orderId", orderId);
		if (fileUrl != null)
			addParams("fileUrl", fileUrl);
		if (certType != 0)
			addParams("certType", certType);
		if (ownerName != null)
			addParams("ownerName", ownerName);
	}

}
