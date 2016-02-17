package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianBaseInfoMedia;
import com.chengniu.client.pojo.BaoxianBaseInfoMediaKey;

@Repository("baoxianBaseInfoMediaDAO")
public class BaoxianBaseInfoMediaDAO extends
		SuperDAO<BaoxianBaseInfoMedia, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianBaseInfoMediaMapper";
	}

	public int deleteByPrimaryKey(BaoxianBaseInfoMediaKey key) {
		return this.getSqlSession().delete(this.tip("deleteByPrimaryKey"), key);
	}

	public int insert(BaoxianBaseInfoMedia record) {
		return this.getSqlSession().insert(this.tip("insert"), record);
	}

	public int insertSelective(BaoxianBaseInfoMedia record) {
		return this.getSqlSession().insert(this.tip("insertSelective"), record);
	}

	public BaoxianBaseInfoMedia selectByPrimaryKey(BaoxianBaseInfoMediaKey key) {
		return this.getSqlSession().selectOne(this.tip("selectByPrimaryKey"),
				key);
	}

	public List<BaoxianBaseInfoMedia> selectListById(String id) {
		return this.getSqlSession().selectList(this.tip("selectListById"), id);
	}

	public int updateByPrimaryKeySelective(BaoxianBaseInfoMedia record) {
		return this.getSqlSession().update(
				this.tip("updateByPrimaryKeySelective"), record);
	}

	public int updateByPrimaryKey(BaoxianBaseInfoMedia record) {
		return this.getSqlSession().update(this.tip("updateByPrimaryKey"),
				record);
	}
}