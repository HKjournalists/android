package com.kplus.car.model.json;

import com.kplus.car.model.Advert;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiListField;

import java.util.List;

public class AdvertJson extends BaseModelObj {
    @ApiListField("vehicleBody")
    private List<Advert> vehicleBody;
    @ApiListField("serviceHead")
    private List<Advert> serviceHead;
    @ApiListField("serviceHeadNew")
    private List<Advert> serviceHeadNew;
    @ApiListField("providerHead")
    private List<Advert> providerHead;
    @ApiListField("userBody")
    private List<Advert> userBody;
    @ApiListField("home")
    private List<Advert> home;
    @ApiListField("tab")
    private List<Advert> tab;
    @ApiListField("vehicleDetailHead")
    private List<Advert> vehicleDetailHead;
    @ApiListField("oilHead")
    private List<Advert> oilHead;
    @ApiListField("userLogin")
    private List<Advert> userLogin;
    @ApiListField("newUser")
    private List<Advert> newUser;

    public List<Advert> getVehicleBody() {
        return vehicleBody;
    }

    public void setVehicleBody(List<Advert> vehicleBody) {
        this.vehicleBody = vehicleBody;
    }

    public List<Advert> getServiceHead() {
        return serviceHead;
    }

    public void setServiceHead(List<Advert> serviceHead) {
        this.serviceHead = serviceHead;
    }

    public List<Advert> getServiceHeadNew() {
        return serviceHeadNew;
    }

    public void setServiceHeadNew(List<Advert> serviceHeadNew) {
        this.serviceHeadNew = serviceHeadNew;
    }

    public List<Advert> getProviderHead() {
        return providerHead;
    }

    public void setProviderHead(List<Advert> providerHead) {
        this.providerHead = providerHead;
    }

    public List<Advert> getUserBody() {
        return userBody;
    }

    public void setUserBody(List<Advert> userBody) {
        this.userBody = userBody;
    }

    public List<Advert> getHome() {
        return home;
    }

    public void setHome(List<Advert> home) {
        this.home = home;
    }

    public List<Advert> getTab() {
        return tab;
    }

    public void setTab(List<Advert> tab) {
        this.tab = tab;
    }

    public List<Advert> getVehicleDetailHead() {
        return vehicleDetailHead;
    }

    public void setVehicleDetailHead(List<Advert> vehicleDetailHead) {
        this.vehicleDetailHead = vehicleDetailHead;
    }

    public List<Advert> getOilHead() {
        return oilHead;
    }

    public void setOilHead(List<Advert> oilHead) {
        this.oilHead = oilHead;
    }

    public List<Advert> getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(List<Advert> userLogin) {
        this.userLogin = userLogin;
    }

    public List<Advert> getNewUser() {
        return newUser;
    }

    public void setNewUser(List<Advert> newUser) {
        this.newUser = newUser;
    }
}
