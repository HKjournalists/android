package com.kplus.car.model;

import com.kplus.car.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RemindChedai implements Serializable {
    private int id = 0;
	private String vehicleNum = "";
    private String date = "";
    private String remindDate1 = "";
    private String remindDate2 = "";
    private int repeatType = Constants.REPEAT_TYPE_MONTH;
    private String orignalDate = "";
    private int money = 0;
    private int total = 0;
    private int fenshu = 0;
    private String remark = "";
    private int dayOfMonth = 0;

    public RemindChedai(){

    }

	public RemindChedai(String vehicleNum, Calendar calendar){
		this.vehicleNum = vehicleNum;
        calendar.add(Calendar.MONTH, 1);
        date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        remindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        remindDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFenshu() {
        return fenshu;
    }

    public void setFenshu(int fenshu) {
        this.fenshu = fenshu;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
