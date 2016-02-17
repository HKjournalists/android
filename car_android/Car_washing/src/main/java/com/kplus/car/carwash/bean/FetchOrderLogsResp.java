package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Created by Fu on 2015/5/19.
 */
public class FetchOrderLogsResp extends BaseInfo {
    private List<OrderLog> logs;

    public List<OrderLog> getLogs() {
        return logs;
    }

    public void setLogs(List<OrderLog> logs) {
        this.logs = logs;
    }
}
