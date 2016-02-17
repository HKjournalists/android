package com.chengniu.client.service;

import java.util.List;

import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.pojo.BaoxianIntent;
import com.chengniu.client.pojo.SearchVO;
import com.kplus.orders.rpc.dto.PageVO;

/**
 * 保险意向管理
 */
public interface BaoxianIntentService {

	/**
	 * 按车牌号获取进行中的有效意向
	 * 
	 * @param userType
	 * @param userId
	 * @param vehicleNum
	 * @return
	 */
	List<BaoxianIntent> fetchPendingValidIntent(int userType, String userId,
			String vehicleNum);

	/**
	 * 获取意向基本信息
	 *
	 * @param intentId
	 * @return
	 */
	BaoxianIntent query(String intentId);

	/**
	 * 获取用户的有效意向列表
	 *
	 * @param vo
	 * @return
	 */
	PageVO<BaoxianIntent> fetchPaginateIntents(SearchVO vo);

	/**
	 * 新增意向
	 * 
	 * @param info
	 */
	BaoxianIntent submitIntent(BaoxianBaseInfo info);

	/**
	 * 修改意向信息
	 *
	 * @param intent
	 * @param info
	 * @return
	 */
	boolean updateUserInfo(BaoxianIntent intent, BaoxianBaseInfo info);

	/**
	 * 标记意向无效
	 *
	 * @param intent
	 * @return
	 */
	boolean deleteIntent(BaoxianIntent intent);

	/**
	 * 更新意向状态
	 * 
	 * @param intent
	 * @return
	 */
	boolean updateIntentStatus(BaoxianIntent intent);

	/**
	 * 查询数量
	 * 
	 * @param string
	 * @param userId
	 * @param userType
	 * @return
	 */
	Integer queryCount(String string, String userId, Integer userType);
}
