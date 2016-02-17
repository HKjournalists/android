package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.CarServicesJson;
import com.kplus.car.parser.ApiField;

/**
 * Description：
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarServicesResponse extends Response {

    @ApiField("data")
    private CarServicesJson data;

    public CarServicesJson getData() {
        if (data == null)
            data = new CarServicesJson();
        return data;
    }

    public void setData(CarServicesJson data) {
        this.data = data;
    }
}
