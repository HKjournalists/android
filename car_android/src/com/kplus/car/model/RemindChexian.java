package com.kplus.car.model;

import com.kplus.car.Constants;
import com.kplus.car.util.DateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RemindChexian implements Serializable {
    private int id = 0;
	private String vehicleNum = "";
	private int type = 1;//1为交强险，2为商业险
    private int money = 0;
    private String baodanhao = "";
    private String photo = "";
    private String date = "";
    private String remindDate1 = "";
    private String remindDate2 = "";
    private int repeatType = Constants.REPEAT_TYPE_YEAR;
    private String orignalDate = "";
    private int same = 0;//1表示商业险与交强险一致
    private int fromType = 0;

    public RemindChexian(){

    }

	public RemindChexian(String vehicleNum, int type, Calendar calendar){
		this.vehicleNum = vehicleNum;
        this.type = type;
        orignalDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        while (calendar.before(Calendar.getInstance()))
            calendar.add(Calendar.YEAR, 1);
        date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
        if (gap > 7){
            calendar.add(Calendar.DATE, -7);
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            remindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
            calendar.add(Calendar.DATE, 6);
            remindDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
        }
        else {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            remindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
        }
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getBaodanhao() {
        return baodanhao;
    }

    public void setBaodanhao(String baodanhao) {
        this.baodanhao = baodanhao;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public int getSame() {
        return same;
    }

    public void setSame(int same) {
        this.same = same;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }
}
