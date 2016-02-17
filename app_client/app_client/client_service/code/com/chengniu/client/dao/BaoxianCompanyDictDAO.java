package com.chengniu.client.dao;

import com.chengniu.client.pojo.BaoxianCompanyDict;
import org.springframework.stereotype.Repository;

@Repository("baoxianCompanyDictDAO")
public class BaoxianCompanyDictDAO extends SuperDAO<BaoxianCompanyDict, Integer> {

    @Override
    protected String namespace() {
        return "mybatis.xml.BaoxianCompanyDictMappingMapper";
    }

    public BaoxianCompanyDict selectCompanyShortName(String name) {
        return getSqlSession().selectOne(tip("selectCompanyShortName"), name + "%");
    }

}