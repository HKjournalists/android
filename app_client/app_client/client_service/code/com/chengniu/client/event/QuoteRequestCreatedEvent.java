package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;

import java.util.Date;

/**
 * 报价请求已创建
 */
public class QuoteRequestCreatedEvent {

    private BaoxianUnderwritingRequest request;
    private Operator operator;

    private Date createTime;

    public BaoxianUnderwritingRequest getRequest() {
        return request;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static QuoteRequestCreatedEvent create(BaoxianUnderwritingRequest request, Operator op) {
        QuoteRequestCreatedEvent e = new QuoteRequestCreatedEvent();

        e.request = request;
        e.operator = op;
        e.createTime = new Date();

        return e;
    }
}
