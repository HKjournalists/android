package com.kplus.car.model.response.request;

import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.response.RestrictSaveResponse;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RestrictSaveRequest extends BaseRequest<RestrictSaveResponse> {

    @Override
    public String getApiMethodName() {
        return "/restrict/save.htm";
    }

    @Override
    public Class<RestrictSaveResponse> getResponseClass() {
        return RestrictSaveResponse.class;
    }

    public void setParams(RemindRestrict data, long uid) {
        if (data.getId() != null)
            addParams("id", data.getId());
        addParams("isHidden", data.getIsHidden());
        addParams("vehicleNum", data.getVehicleNum());
        addParams("phone", data.getPhone());
        addParams("messageRemind", data.getMessageRemind());
        addParams("remindDateType", data.getRemindDateType());
        addParams("remindDate", data.getRemindDate());
        if(uid != 0)
            addParams("uid", uid);
    }
}
