package com.chengniu.client.service;

import com.chengniu.client.pojo.BaoxianUnderwritingReport;
import com.google.gson.JsonObject;

public interface BaoxianUnderwritingReportService {

    /**
     * 提交报价
     * @param baoxianUnderwritingId
     * @param quoteId
     * @param result
     * @return
     */
    BaoxianUnderwritingReport disposeFanhuaReport(String baoxianUnderwritingId, String quoteId,
                                                  JsonObject result);

	/**
	 * 保存核保信息
	 *
	 * @param report
	 * @return
	 */
	boolean save(BaoxianUnderwritingReport report);

	/**
	 * 查询投保中的数据
	 *
	 * @param userId
	 * @param userType
	 * @return
	 */
	int queryCount(String userId, Integer userType);

	/**
	 * 查询报价
	 *
	 * @param id
	 * @return
	 */
	BaoxianUnderwritingReport query(String id);

	/**
	 * 按UnderwritingId查询报价
	 *
	 * @param id
	 * @return
	 */
	BaoxianUnderwritingReport queryByUnderwritingId(String id);

	/**
	 * 按基础订单号查询
	 * @param orderNum
	 * @param userType
	 * @return
	 */
	BaoxianUnderwritingReport queryByOrderNum(String orderNum, Integer userType);

}