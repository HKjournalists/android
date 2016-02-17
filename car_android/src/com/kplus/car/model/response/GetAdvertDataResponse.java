package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.parser.ApiField;

public class GetAdvertDataResponse extends Response {
    @ApiField("data")
    private AdvertJson data;

    public AdvertJson getData() {
        if(data == null)
            data = new AdvertJson();
        return data;
    }

    public void setData(AdvertJson data) {
        this.data = data;
    }
}
