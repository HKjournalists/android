package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianInformalReport;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;

import java.util.Date;

/**
 * 报价完成事件
 */
public class QuoteFinishedEvent {

    private BaoxianUnderwritingRequest request;
    private int count;
    private Operator operator;

    private Date createTime;

    public BaoxianUnderwritingRequest getRequest() {
        return request;
    }

    public int getCount() {
        return count;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static QuoteFinishedEvent create(BaoxianUnderwritingRequest request, int count, Operator op) {
        QuoteFinishedEvent e = new QuoteFinishedEvent();

        e.request = request;
        e.count = count;
        e.operator = op;
        e.createTime = new Date();

        return e;
    }
}
