package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.ProviderInfoJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by admin on 2015/6/3.
 */
public class GetProviderByOpenUserIdResponse extends Response {
    @ApiField("data")
    private ProviderInfoJson data;

    public ProviderInfoJson getData() {
        if(data == null)
            data = new ProviderInfoJson();
        return data;
    }

    public void setData(ProviderInfoJson data) {
        this.data = data;
    }
}
