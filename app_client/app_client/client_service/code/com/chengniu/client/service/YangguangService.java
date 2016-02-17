package com.chengniu.client.service;

import java.util.Map;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianPayDTO;
import com.chengniu.client.pojo.BaoxianPeisong;
import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;

public interface YangguangService {

	/**
	 * 获取报价需要的信息
	 *
	 * @param v
	 * @param name
	 * @param cityCode
	 * @param op
	 * @return
	 */
	Map<String, String> disposePreReport(String vehicleNum,
										 String idCardName, String cityCode, String mobile, Operator op);

	/**
	 * 提交信息
	 *
	 * @param param
	 * @param op
	 * @return
	 * @throws Exception 
	 */
	Map<String, String> disposeCommitBaseInfo(String param,
			String session, Operator op) throws Exception;

	/**
	 * 报价数据到阳光
	 * 
	 * @param info
	 * @param session
	 * @return
	 * @throws Exception
	 */
	Map<String, String> disposeReport(BaoxianUnderwriting info,
			String session) throws Exception;

	/**
	 * 提交核保
	 * 
	 * @param resultReport
	 * @return
	 * @throws Exception
	 */
	BaoxianUnderwritingReport disposeSaveReport(
			BaoxianUnderwriting info, String session,
			Map<String, Map<String, String>> resultReport) throws Exception;

	/**
	 * 提交核保
	 *
	 * @param session
	 * @return
	 * @throws Exception
	 */
	Map<String, String> disposeUnderwriting(String session, Operator op)
			throws Exception;

	/**
	 * 配送
	 * 
	 * @param id
	 * @param info
	 * @param session
	 * @param op
	 * @return
	 */
	Map<String, String> disposePeisong(String id, BaoxianPeisong info,
			String session, Operator op);

	/**
	 * 支付
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> disposePay(String session, Operator op)
			throws Exception;

	/**
	 * 查询最后一次报价
	 * 
	 * @param session
	 * @return
	 */
	Map<String, String> disposeLatestReport(String session);
}