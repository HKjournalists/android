package com.kplus.car.carwash.bean;

import java.util.Date;

/**
 * Created by Fu on 2015/5/17.
 */
public class ServingTime extends BaseInfo {
    private Date day;
    private int beginInMins;
    private int endInMins;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getBeginInMins() {
        return beginInMins;
    }

    public void setBeginInMins(int beginInMins) {
        this.beginInMins = beginInMins;
    }

    public int getEndInMins() {
        return endInMins;
    }

    public void setEndInMins(int endInMins) {
        this.endInMins = endInMins;
    }
}
