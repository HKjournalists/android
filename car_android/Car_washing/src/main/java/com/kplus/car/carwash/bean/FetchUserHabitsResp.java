package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Created by Fu on 2015/5/17.
 */
public class FetchUserHabitsResp extends BaseInfo {
    private Contact contact;
    private Car car;
    private Position position;
    private List<OnSiteService> services;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<OnSiteService> getServices() {
        return services;
    }

    public void setServices(List<OnSiteService> services) {
        this.services = services;
    }
}
