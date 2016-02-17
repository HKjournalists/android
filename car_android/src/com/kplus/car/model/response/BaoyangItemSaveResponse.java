package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.BaoyangItem;
import com.kplus.car.parser.ApiField;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemSaveResponse extends Response {
    @ApiField("data")
    private BaoyangItem data;

    public BaoyangItem getData() {
        return data;
    }

    public void setData(BaoyangItem data) {
        this.data = data;
    }
}
