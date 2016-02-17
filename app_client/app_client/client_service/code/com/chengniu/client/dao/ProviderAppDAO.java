package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.ProviderApp;

@Repository("providerAppDAO")
public class ProviderAppDAO extends SuperDAO<ProviderApp, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.ProviderAppMapper";
	}
}