package com.kplus.car.model;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/5/22.
 */
public class JiazhaoAgainst extends BaseModelObj {
    @ApiField("vehicleNum")
    private String vehicleNum;
    @ApiField("total")
    private Integer total;
    @ApiListField("list")
    private List<AgainstRecord> list;

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AgainstRecord> getList() {
        return list;
    }

    public void setList(List<AgainstRecord> list) {
        this.list = list;
    }
}
