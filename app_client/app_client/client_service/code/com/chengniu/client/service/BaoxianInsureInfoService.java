package com.chengniu.client.service;

import com.chengniu.client.pojo.*;
import com.kplus.orders.rpc.dto.PageVO;

public interface BaoxianInsureInfoService {

	/**
	 * 保存用户配送信息
	 *
	 * @param underwriting
	 * @param info
	 * @return
	 */
	boolean disposePeisong(BaoxianUnderwriting underwriting, BaoxianPeisong info);

    /**
     * 提交配送地址
     * @param underwriting
     */
    void disposePeisong(BaoxianUnderwriting underwriting);

	/**
	 * 保存用户信息
	 *
	 * @param info
	 * @return
	 */
	BaoxianInsureInfo save(BaoxianInsureInfo info);

	/**
	 * 分页查询
	 *
	 * @param vo
	 * @return
	 */
	PageVO<BaoxianInsureInfo> page(SearchVO vo);

	/**
	 * 查询单个
	 *
	 * @return
	 */
	BaoxianInsureInfo queryByUserId(String id, String userId,
									Integer userType);

	/**
	 * 查询单个
	 *
	 * @param id
	 * @return
	 */
	BaoxianInsureInfo query(String id);

	/**
	 * 分页查询配送
	 *
	 * @param vo
	 * @return
	 */
	PageVO<BaoxianPeisong> pagePeisong(SearchVO vo);

	/**
	 * 查询配送信息
	 *
	 * @param userId
	 * @param userType
	 * @return
	 */
	BaoxianPeisong queryPeisong(String id, String userId,
								Integer userType);

	/**
	 * 设置配送信息
	 *
	 *
	 * @param report
	 * @param t
	 * @return
	 */
	BaoxianUnderwritingExpress disposeExpress(
			BaoxianUnderwritingReport report, BaoxianUnderwritingExpress t);

	/**
	 * 按BaoxianUnderwritingReportId查询报价
	 *
	 * @param id
	 * @return
	 */
	BaoxianUnderwritingExpress queryByBaoxianUnderwritingReportId(
			String id);

	/**
	 * 删除配送信息
	 *
	 * @param peisong
	 * @return
	 */
	boolean deletePeisong(BaoxianPeisong peisong);

	/**
	 * 查询配送信息
	 *
	 * @param id
	 * @return
	 */
	BaoxianInsureInfo queryByUnderwritingId(String id);
}