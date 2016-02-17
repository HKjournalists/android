package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;

import java.util.Date;

/**
 * 提交报价
 */
public class UnderwritingSubmitedEvent {

    private BaoxianUnderwriting underwriting;
    private Operator operator;

    private Date createTime;

    public BaoxianUnderwriting getUnderwriting() {
        return underwriting;
    }

    public Operator getOperator() {
        return operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static UnderwritingSubmitedEvent create(BaoxianUnderwriting underwriting, Operator op) {
        UnderwritingSubmitedEvent e = new UnderwritingSubmitedEvent();

        e.underwriting = underwriting;
        e.operator = op;
        e.createTime = new Date();

        return e;
    }
}
