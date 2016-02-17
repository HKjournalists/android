package com.chengniu.client.dao;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.PlatUser;

@Repository("platUserDAO")
public class PlatUserDAO extends SuperDAO<PlatUser, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.PlatUserMapper";
	}

	/**
	 * 报价查询
	 * 
	 * @param key
	 * @return
	 */
	public PlatUser queryByAppKey(String key) {
		return this.getSqlSession().selectOne(this.tip("queryByAppKey"), key);
	}
}