package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.Weather;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/21.
 * <br/><br/>
 */
public class WeatherJson extends BaseModelObj {

    @ApiListField("list")
    private List<Weather> list;

    public List<Weather> getList() {
        return list;
    }

    public void setList(List<Weather> list) {
        this.list = list;
    }
}
