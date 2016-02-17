package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianPeisong;

@Repository("baoxianPeisongDAO")
public class BaoxianPeisongDAO extends SuperDAO<BaoxianPeisong, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianPeisongMapper";
	}
}