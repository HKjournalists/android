package com.kplus.car.model;

import com.kplus.car.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RemindCustom implements Serializable {
    private int id = 0;
	private String vehicleNum = "";
    private String date = "";
    private String remindDate1 = "";
    private String remindDate2 = "";
    private int repeatType = Constants.REPEAT_TYPE_NONE;
    private String orignalDate = "";
    private String name = "";
    private String location = "";
    private String remark = "";

    public RemindCustom(){

    }

	public RemindCustom(String vehicleNum){
		this.vehicleNum = vehicleNum;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemindDate1() {
        return remindDate1;
    }

    public void setRemindDate1(String remindDate1) {
        this.remindDate1 = remindDate1;
    }

    public String getRemindDate2() {
        return remindDate2;
    }

    public void setRemindDate2(String remindDate2) {
        this.remindDate2 = remindDate2;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public String getOrignalDate() {
        return orignalDate;
    }

    public void setOrignalDate(String orignalDate) {
        this.orignalDate = orignalDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
