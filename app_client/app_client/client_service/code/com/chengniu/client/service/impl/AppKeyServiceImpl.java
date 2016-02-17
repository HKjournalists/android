package com.chengniu.client.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chengniu.client.dao.PlatUserDAO;
import com.chengniu.client.dao.ProviderAppDAO;
import com.chengniu.client.pojo.PlatUser;
import com.chengniu.client.pojo.ProviderApp;
import com.chengniu.client.service.AppKeyService;
import com.whalin.MemCached.MemCachedClient;

@Service("appKeyService")
public class AppKeyServiceImpl implements AppKeyService {
	@Resource(name = "memcachedClient")
	private MemCachedClient memcachedClient;
	@Resource(name = "platUserDAO")
	private PlatUserDAO platUserDAO;
	@Resource(name = "providerAppDAO")
	private ProviderAppDAO providerAppDAO;

	@Override
	public PlatUser queryByAppKey(String key) {
		String tmp = "PlatUser" + key;
		Object pu = memcachedClient.get(tmp);
		if (pu == null) {
			PlatUser platuser = this.platUserDAO.queryByAppKey(key);
			memcachedClient.set(tmp, platuser, new Date(250 * 250 * 1000));
			pu = platuser;
		}
		return (PlatUser) pu;
	}

	@Override
	public ProviderApp queryProviderAppById(String key) {
		String tmp = "ProviderApp" + key;
		Object pu = memcachedClient.get(tmp);
		if (pu == null) {
			ProviderApp providerapp = this.providerAppDAO.query(key);
			memcachedClient.set(tmp, providerapp, new Date(250 * 250 * 1000));
			pu = providerapp;
		}
		return (ProviderApp) pu;
	}
}