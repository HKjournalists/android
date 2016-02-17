package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;

import java.util.Date;

/**
 * 订单状态变化
 */
public class OrderPayStatusChangedEvent {

    private BaoxianUnderwritingReport report;
    private Operator operator;

    private Date createTime;

    public BaoxianUnderwritingReport getReport() {
        return report;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static OrderPayStatusChangedEvent create(BaoxianUnderwritingReport report, Operator op) {
        OrderPayStatusChangedEvent e = new OrderPayStatusChangedEvent();

        e.report = report;
        e.operator = op;
        e.createTime = new Date();

        return e;
    }
}
