package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/13.
 */
public class ServiceTime extends BaseInfo {
    private String mTimeSlot;
    private int mStatu;

    public String getTimeSlot() {
        return mTimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        mTimeSlot = timeSlot;
    }

    public int getStatu() {
        return mStatu;
    }

    public void setStatu(int statu) {
        mStatu = statu;
    }
}
