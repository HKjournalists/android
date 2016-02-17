package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class Insurance {
    private String company;
    private String phone;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
