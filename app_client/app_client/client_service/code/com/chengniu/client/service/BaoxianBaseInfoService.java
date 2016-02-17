package com.chengniu.client.service;

import java.util.Date;
import java.util.List;

import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.pojo.BaoxianBaseInfoMedia;
import com.chengniu.client.pojo.BaoxianCityFanhua;
import com.chengniu.client.pojo.BaoxianCityMapping;
import com.chengniu.client.pojo.SearchVO;
import com.kplus.orders.rpc.dto.PageVO;

public interface BaoxianBaseInfoService {
	/**
	 * 按用户查新信息
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	List<BaoxianBaseInfo> queryByUserId(String userId, Integer userType);

	/**
	 * 按用户查最新信息
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	BaoxianBaseInfo queryLastInfo(String userId, Integer userType);

	/**
	 * 按id和用户查新信息
	 * 
	 * @param id
	 * @param uid
	 * @return
	 */
	BaoxianBaseInfo query(String id, String userId, Integer userType);

	/**
	 * 用车牌好获取车辆信息
	 * @param userType
	 * @param userId
	 * @param vehicleNum
	 * @return
	 */
	BaoxianBaseInfo queryByVehicleNum(Integer userType, String userId, String vehicleNum);

	/**
	 * 保存用户信息
	 *
	 * @param uid
	 * @return
	 */
	boolean save(BaoxianBaseInfo info);

	/**
	 * 删除用户
	 *
	 * @param info
	 * @return
	 */
	boolean delete(BaoxianBaseInfo info);

	/**
	 * 分页查询用户，userId和UserType必须有
	 *
	 * @param vo
	 * @return
	 */
	PageVO<BaoxianBaseInfo> page(SearchVO vo);

	/**
	 * 查询城市列表
	 *
	 * @param info
	 * @return
	 */
	List<BaoxianCityFanhua> queryCity(BaoxianCityFanhua info);

	/**
	 * 获取阳光的cityCode
	 *
	 * @param code
	 * @return
	 */
	BaoxianCityMapping queryCity(String code);

	/**
	 * 获取最近的工作日
	 *
	 * @param date
	 * @return
	 */
	String queryLastWork(Date date);

	/**
	 * 查询城市
	 *
	 * @param cityCode
	 * @param kind
	 * @return
	 */
	BaoxianCityFanhua queryCityInfo(String cityCode, Integer kind);

	/**
	 * 查询城市
	 *
	 * @param cityName
	 * @return
	 */
	BaoxianCityFanhua queryByCityName(String cityName);

	/**
	 * 查询影像资料列表
	 *
	 * @return
	 * @param baseInfoId
	 */
	List<BaoxianBaseInfoMedia> queryMediaInfo(String baseInfoId);

	/**
	 * 更新影像资料
	 *
	 * @param media
	 */
	void updateMedia(BaoxianBaseInfoMedia media) throws Exception;
}