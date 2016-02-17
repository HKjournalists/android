package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;

import java.util.Date;

/**
 * 提交报价
 */
public class QuoteSubmitEvent {

    private BaoxianUnderwritingRequest request;
    private String quoteId;
    private Operator operator;

    private Date createTime;

    public BaoxianUnderwritingRequest getRequest() {
        return request;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static QuoteSubmitEvent create(BaoxianUnderwritingRequest request, String quoteId, Operator op) {
        QuoteSubmitEvent e = new QuoteSubmitEvent();

        e.request = request;
        e.quoteId = quoteId;
        e.operator = op;
        e.createTime = new Date();

        return e;
    }
}
