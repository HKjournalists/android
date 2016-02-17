package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianUnderwriting;

import java.util.Date;

/**
 * 核保失败
 */
public class UnderwritingVerifyFailedEvent {

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

    public static UnderwritingVerifyFailedEvent create(BaoxianUnderwriting underwriting, Operator op) {
        UnderwritingVerifyFailedEvent e = new UnderwritingVerifyFailedEvent();

        e.underwriting = underwriting;
        e.operator = op;
        e.createTime = new Date();

        return e;
    }
}
