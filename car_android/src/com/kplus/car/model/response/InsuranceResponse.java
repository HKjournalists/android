package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.Insurance;
import com.kplus.car.model.json.InsuranceJson;
import com.kplus.car.model.json.RemindQueryJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class InsuranceResponse extends Response {

    @ApiField("data")
    private InsuranceJson data;

    public InsuranceJson getData() {
        return data;
    }

    public void setData(InsuranceJson data) {
        this.data = data;
    }
}
