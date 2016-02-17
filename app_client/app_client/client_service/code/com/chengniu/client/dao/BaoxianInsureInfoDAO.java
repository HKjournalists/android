package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianInsureInfo;

@Repository("baoxianInsureInfoDAO")
public class BaoxianInsureInfoDAO extends SuperDAO<BaoxianInsureInfo, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianInsureInfoMapper";
	}

	/**
	 * 报价查询
	 * 
	 * @param uid
	 * @return
	 */
	public BaoxianInsureInfo queryByUnderwritingId(String id) {
		return this.getSqlSession().selectOne(
				this.tip("queryByUnderwritingId"), id);
	}

	/**
	 * 补齐信息
	 * 
	 * @param info
	 * @return
	 */
	public int fixedInfo(BaoxianInsureInfo info) {
		return this.getSqlSession().update(this.tip("fixedInfo"), info);
	}

	/**
	 * 报价查询
	 * 
	 * @param uid
	 * @return
	 */
	public List<BaoxianInsureInfo> queryByUid(Long uid) {
		return this.getSqlSession().selectList(this.tip("queryByUid"), uid);
	}

}