package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetServiceImgListResponse;

/**
 * Created by Administrator on 2015/9/23.
 */
public class GetServiceImgListRequest extends BaseRequest<GetServiceImgListResponse> {

    @Override
    public String getApiMethodName() {
        return "/imgService/getImgList.htm";
    }

    @Override
    public Class<GetServiceImgListResponse> getResponseClass() {
        return GetServiceImgListResponse.class;
    }

    public void setParams(long cityId){
        addParams("cityId", cityId);
    }
}
