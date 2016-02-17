package com.kplus.car.carwash.bean;


/**
 * Created by Fu on 2015/5/17.
 */
public class Position extends BaseInfo {
    private double latitude;
    private double longitude;

    private Address address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
