package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.JiazhaoAgainstListJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class JiazhaoAgainstListResponse extends Response {

    @ApiField("data")
    private JiazhaoAgainstListJson data;

    public JiazhaoAgainstListJson getData() {
        return data;
    }

    public void setData(JiazhaoAgainstListJson data) {
        this.data = data;
    }
}
