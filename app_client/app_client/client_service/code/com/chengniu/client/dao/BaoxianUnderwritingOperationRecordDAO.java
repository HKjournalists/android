package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianUnderwritingOperationRecord;
import org.springframework.stereotype.Repository;

@Repository("baoxianUnderwritingOperationRecordDAO")
public class BaoxianUnderwritingOperationRecordDAO extends SuperDAO<BaoxianUnderwritingOperationRecord, Long> {

    @Override
    protected String namespace() {
        return "mybatis.xml.BaoxianUnderwritingOperationRecordMapper";
    }
}