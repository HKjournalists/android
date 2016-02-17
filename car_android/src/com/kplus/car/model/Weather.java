package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/21.
 * <br/><br/>
 */
public class Weather extends BaseModelObj {

    @ApiField("phenomenon")
    private String phenomenon;

    @ApiField("temperatureDay")
    private String temperatureDay;

    @ApiField("wash")
    private Integer wash;

    @ApiField("temperatureNight")
    private String temperatureNight;

    @ApiField("type")
    private Integer type;

    @ApiField("washDescr")
    private String washDescr;

    @ApiField("dateStr")
    private String dateStr;

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getTemperatureDay() {
        return temperatureDay;
    }

    public void setTemperatureDay(String temperatureDay) {
        this.temperatureDay = temperatureDay;
    }

    public Integer getWash() {
        return wash;
    }

    public void setWash(Integer wash) {
        this.wash = wash;
    }

    public String getTemperatureNight() {
        return temperatureNight;
    }

    public void setTemperatureNight(String temperatureNight) {
        this.temperatureNight = temperatureNight;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getWashDescr() {
        return washDescr;
    }

    public void setWashDescr(String washDescr) {
        this.washDescr = washDescr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
