package com.chengniu.client.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.UserPushClient;
import com.google.common.collect.Maps;

@Repository("userPushClientDAO")
public class UserPushClientDAO extends SuperDAO<UserPushClient, Long> {

	@Override
	protected String namespace() {
		return "mybatis.xml.UserPushClientMapper";
	}

	public List<UserPushClient> selectPushClientByUserIds(
			List<Long> userIdList, int type, int maxCount) {
		if (userIdList == null || userIdList.isEmpty()) {
			return null;
		}

		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		if (userIdList.size() == 1) {
			params.put("userId", userIdList.get(0));
		} else {
			params.put("userIdList", userIdList);
		}
		params.put("type", type);
		params.put("maxCount", maxCount);
		return getSqlSession().selectList(tip("selectPushClientByUserIds"),
				params);
	}
}