package com.chengniu.client.event;

import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;

import java.util.Date;

/**
 * 已承包
 */
public class UnderwritingSuccessEvent {

    private BaoxianUnderwritingReport report;

    private Date createTime;

    public BaoxianUnderwritingReport getReport() {
        return report;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static UnderwritingSuccessEvent create(BaoxianUnderwritingReport report) {
        UnderwritingSuccessEvent e = new UnderwritingSuccessEvent();

        e.report = report;
        e.createTime = new Date();

        return e;
    }
}
