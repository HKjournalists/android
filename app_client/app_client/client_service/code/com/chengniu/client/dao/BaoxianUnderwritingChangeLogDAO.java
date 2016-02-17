package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianUnderwritingChangeLog;
import org.springframework.stereotype.Repository;

@Repository("baoxianUnderwritingChangeLogDAO")
public class BaoxianUnderwritingChangeLogDAO extends SuperDAO<BaoxianUnderwritingChangeLog, Long> {

    @Override
    protected String namespace() {
        return "com.xml.BaoxianUnderwritingChangeLogMapper";
    }
}