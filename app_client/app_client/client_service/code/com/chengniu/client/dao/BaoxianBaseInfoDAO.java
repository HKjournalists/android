package com.chengniu.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chengniu.client.pojo.BaoxianBaseInfo;

@Repository("baoxianBaseInfoDAO")
public class BaoxianBaseInfoDAO extends SuperDAO<BaoxianBaseInfo, String> {
	@Override
	protected String namespace() {
		return "mybatis.xml.BaoxianBaseInfoMapper";
	}

	/**
	 * 报价查询
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	public List<BaoxianBaseInfo> queryByUserId(String userId, Integer userType) {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setUserId(userId);
		info.setUserType(userType);
		return this.getSqlSession().selectList(this.tip("queryByUid"), info);
	}

	public BaoxianBaseInfo queryByVehicleNum(Integer userType, String userId, String vehicleNum) {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setUserId(userId);
		info.setUserType(userType);
		info.setVehicleNum(vehicleNum);
		return this.getSqlSession().selectOne(this.tip("queryByVehicleNum"), info);
	}

	/**
	 * 报价查询
	 *
	 * @param uid
	 * @return
	 */
	public BaoxianBaseInfo queryLastInfo(String userId, Integer userType) {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setUserId(userId);
		info.setUserType(userType);
		return this.getSqlSession().selectOne(this.tip("queryLastInfo"), info);
	}

	public int deleteInfo(BaoxianBaseInfo info) {
		return this.getSqlSession().update(this.tip("deleteInfo"), info);
	}

	/**
	 * 补齐信息
	 *
	 * @param info
	 * @return
	 */
	public int fixedInfo(BaoxianBaseInfo info) {
		return this.getSqlSession().update(this.tip("fixedInfo"), info);
	}

	/**
	 * 修改证件图片
	 *
	 * @param info
	 * @return
	 */
	public int updateUserIDUrl(BaoxianBaseInfo info) {
		return this.getSqlSession().update(this.tip("updateUserIDUrl"), info);
	}

	/**
	 * 修改驾驶证图片
	 *
	 * @param info
	 * @return
	 */
	public int updateDrivingLicenseUrl(BaoxianBaseInfo info) {
		return this.getSqlSession().update(this.tip("updateDrivingLicenseUrl"),
				info);
	}
}