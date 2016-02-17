package com.chengniu.client.pojo;

import java.util.Map;

/**
 * 保险项目变更记录
 */
public class BaoxinUnderwritingChangeLogDTO {

	Map<String, BaoxianUnderwritingChangeLog> changeLogMap;
	String remark;

	public Map<String, BaoxianUnderwritingChangeLog> getChangeLogMap() {
		return changeLogMap;
	}

	public void setChangeLogMap(
			Map<String, BaoxianUnderwritingChangeLog> changeLogMap) {
		this.changeLogMap = changeLogMap;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
