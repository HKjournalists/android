package com.kplus.car.carwash.bean;

/**
 * Description
 * <br/><br/>Created by Fu on 2015/6/23.
 * <br/><br/>
 */
public class FetchOrderResp extends BaseInfo {
    private static final long serialVersionUID = 1L;

    private ServiceOrder order;

    public ServiceOrder getOrder() {
        return order;
    }

    public void setOrder(ServiceOrder order) {
        this.order = order;
    }
}
