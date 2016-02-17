package com.kplus.car.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/3/25 0025.
 */
public class BaoyangRecord implements Serializable {

    private String vehicleNum = "";
    private String date = "";
    private int licheng = 0;
    private int money = 0;
    private String company = "";
    private String phone = "";
    private String remark = "";
    private String lat;
    private String lon;
    private String address;

    private String baoyangItemId = "";
    private String baoyangItem = "";

    public BaoyangRecord(){

    }

    public BaoyangRecord(String vehicleNum){
        this.vehicleNum = vehicleNum;
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

    public int getLicheng() {
        return licheng;
    }

    public void setLicheng(int licheng) {
        this.licheng = licheng;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBaoyangItemId() {
        return baoyangItemId;
    }

    public void setBaoyangItemId(String baoyangItemId) {
        this.baoyangItemId = baoyangItemId;
    }

    public String getBaoyangItem() {
        return baoyangItem;
    }

    public void setBaoyangItem(String baoyangItem) {
        this.baoyangItem = baoyangItem;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
