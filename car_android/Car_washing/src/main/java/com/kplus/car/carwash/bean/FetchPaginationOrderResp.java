package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Created by Fu on 2015/5/19.
 */
public class FetchPaginationOrderResp extends BaseInfo {
    private long total;
    private List<ServiceOrder> orders;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<ServiceOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ServiceOrder> orders) {
        this.orders = orders;
    }
}
