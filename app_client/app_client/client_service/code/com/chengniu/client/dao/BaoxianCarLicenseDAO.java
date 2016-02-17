package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianCarLicense;
@Repository("baoxianCarLicenseDAO")
public class BaoxianCarLicenseDAO extends SuperDAO<BaoxianCarLicense, String> {
	@Override
	protected String namespace() {
		return "com.chengniu.client.dao.BaoxianCarLicenseMapper";
	}
}