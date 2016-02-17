package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.RemindQueryJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class RemindQueryResponse extends Response {

    @ApiField("data")
    private RemindQueryJson data;

    public RemindQueryJson getData() {
        if(data == null)
            data = new RemindQueryJson();
        return data;
    }

    public void setData(RemindQueryJson data) {
        this.data = data;
    }
}
