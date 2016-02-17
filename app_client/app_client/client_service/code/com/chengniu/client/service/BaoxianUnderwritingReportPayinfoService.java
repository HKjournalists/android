package com.chengniu.client.service;

import com.chengniu.client.pojo.BaoxianUnderwritingReportPayinfo;

public interface BaoxianUnderwritingReportPayinfoService {

	/**
	 * 保存支付信息
	 * 
	 * @param info
	 * @return
	 */
	boolean save(BaoxianUnderwritingReportPayinfo info);

	/**
	 * 更新支付信息
	 * @param payInfo
	 */
	void update(BaoxianUnderwritingReportPayinfo payInfo);

	/**
	 * 修改支付状态
	 *
	 * @param id
	 * @return
	 */
	boolean updateStatus(String id, Integer status);

	/**
	 * 修改第三方流水
	 *
	 * @param id
	 * @return
	 */
	boolean updateTradeNum(String id, String tradeNum);

	/**
	 * 修改请求信息
	 *
	 * @param id
	 * @param request
	 * @return
	 */
	boolean updateRequest(String id, String request);

    /**
     * 保存结果通知
     *
     * @param id
     * @param tradeNum
     * @param status
     * @param response
     * @return
     */
    boolean updateResponse(String id, String tradeNum, int status, String response);

	/**
	 * 按报价查询报价
	 *
	 * @param id
	 * @return
	 */
	BaoxianUnderwritingReportPayinfo queryByReportId(String id);

	BaoxianUnderwritingReportPayinfo queryByPayedReportId(String id);

    BaoxianUnderwritingReportPayinfo query(String id);
}