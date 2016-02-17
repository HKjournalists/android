package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetActvityListResponse;
import com.kplus.car.util.StringUtils;

public class GetActivityListRequest extends BaseRequest<GetActvityListResponse> {

	@Override
	public String getApiMethodName() {
		return "/activity/getActivityList.htm";
	}

	@Override
	public Class<GetActvityListResponse> getResponseClass() {
		return GetActvityListResponse.class;
	}
	
	public void setParams(int pageIndex, int pageSize, String updateTime){
		if(pageIndex != 0)
			addParams("pageIndex", pageIndex);
		if(pageSize != 0)
			addParams("pageSize", pageSize);
		if(!StringUtils.isEmpty(updateTime))
			addParams("updateTime", updateTime);
	}

}
