package com.chengniu.client.service;

import com.chengniu.client.pojo.PlatUser;
import com.chengniu.client.pojo.ProviderApp;

public interface AppKeyService {
	PlatUser queryByAppKey(String key);

	ProviderApp queryProviderAppById(String key);
}