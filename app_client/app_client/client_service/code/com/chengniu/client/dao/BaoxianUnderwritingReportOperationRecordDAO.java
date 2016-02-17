package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianUnderwritingReportOperationRecord;
import org.springframework.stereotype.Repository;

@Repository
public class BaoxianUnderwritingReportOperationRecordDAO
        extends SuperDAO<BaoxianUnderwritingReportOperationRecord, Long> {

    @Override
    protected String namespace() {
        return "mybatis.xml.BaoxianUnderwritingReportOperationRecordMapper";
    }
}