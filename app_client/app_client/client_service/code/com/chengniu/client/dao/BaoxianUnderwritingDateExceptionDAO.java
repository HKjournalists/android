package com.chengniu.client.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianUnderwritingDateException;

@Repository("baoxianUnderwritingDateExceptionDAO")
public class BaoxianUnderwritingDateExceptionDAO extends
		SuperDAO<BaoxianUnderwritingDateException, String> {

	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianUnderwritingDateExceptionMapper";
	}

	public BaoxianUnderwritingDateException queryNextWorkDay(Date date) {
		return this.getSqlSession().selectOne(this.tip("selectNextWorkDay"),
				date);
	}

	public BaoxianUnderwritingDateException queryWorkDayByDate(Date date) {
		return this.getSqlSession().selectOne(this.tip("selectLastWorkDay"),
				date);
	}
}