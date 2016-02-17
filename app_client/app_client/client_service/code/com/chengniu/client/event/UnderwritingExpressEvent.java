package com.chengniu.client.event;

import com.chengniu.client.pojo.BaoxianUnderwritingExpress;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;

import java.util.Date;

/**
 * 配送成功
 */
public class UnderwritingExpressEvent {

    private BaoxianUnderwritingReport report;
    private BaoxianUnderwritingExpress express;

    private Date createTime;

    public BaoxianUnderwritingReport getReport() {
        return report;
    }

    public BaoxianUnderwritingExpress getExpress() {
        return express;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static UnderwritingExpressEvent create(BaoxianUnderwritingReport report, BaoxianUnderwritingExpress express) {
        UnderwritingExpressEvent e = new UnderwritingExpressEvent();

        e.report = report;
        e.express = express;
        e.createTime = new Date();

        return e;
    }
}
