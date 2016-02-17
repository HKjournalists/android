package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetServiceImgListJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/9/23.
 */
public class GetServiceImgListResponse extends Response {
    @ApiField("data")
    private GetServiceImgListJson data;

    public GetServiceImgListJson getData() {
        return data;
    }

    public void setData(GetServiceImgListJson data) {
        this.data = data;
    }
}
