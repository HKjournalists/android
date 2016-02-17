package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianUnderwritingRequestOperationRecord;
import org.springframework.stereotype.Repository;

@Repository("baoxianUnderwritingRequestOperationRecordDAO")
public class BaoxianUnderwritingRequestOperationRecordDAO extends
        SuperDAO<BaoxianUnderwritingRequestOperationRecord, Long> {

    protected String namespace() {
        return "mybatis.xml.BaoxianUnderwritingRequestOperationRecordDAO";
    }

}