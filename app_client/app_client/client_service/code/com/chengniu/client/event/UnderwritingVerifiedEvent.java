package com.chengniu.client.event;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;

import java.util.Date;

/**
 * 核保成功
 */
public class UnderwritingVerifiedEvent {

    private BaoxianUnderwriting underwriting;
    private BaoxianUnderwritingReport report;

    private Date createTime;

    public BaoxianUnderwriting getUnderwriting() {
        return underwriting;
    }

    public BaoxianUnderwritingReport getReport() {
        return report;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static UnderwritingVerifiedEvent create(BaoxianUnderwriting underwriting, BaoxianUnderwritingReport report) {
        UnderwritingVerifiedEvent e = new UnderwritingVerifiedEvent();

        e.underwriting = underwriting;
        e.report = report;
        e.createTime = new Date();

        return e;
    }
}
