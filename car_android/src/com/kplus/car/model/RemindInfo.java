package com.kplus.car.model;

/**
 * Created by Administrator on 2015/3/14.
 */
public class RemindInfo {
    private int id = 0;
    private String vehicleNum = "";
    private String title = "";
    private String desc = "";
    private int type = 0;
    private boolean isHidden = false;
    private int position = 0;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
