package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.JiazhaoListJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class JiazhaoListResponse extends Response {

    @ApiField("data")
    private JiazhaoListJson data;

    public JiazhaoListJson getData() {
        if(data == null)
            data = new JiazhaoListJson();
        return data;
    }

    public void setData(JiazhaoListJson data) {
        this.data = data;
    }
}
