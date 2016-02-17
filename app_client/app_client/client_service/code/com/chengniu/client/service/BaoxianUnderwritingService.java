package com.chengniu.client.service;

import java.util.Date;
import java.util.Map;

import com.chengniu.client.domain.AutomaticStatus;
import com.chengniu.client.pojo.*;
import com.google.gson.JsonObject;
import com.kplus.orders.rpc.dto.PageVO;

public interface BaoxianUnderwritingService {

    /**
     * 提交核保
     *
     * @param underwritingId
     * @return
     */
    boolean disposeUnderwriting(String underwritingId);

    /**
     * 取消核保请求
     * @param underwriting
     * @return
     */
    Map<String,Object> disposeCancel(BaoxianUnderwriting underwriting);

    /**
     * 处理泛化核保回写
     * @param underwritingId
     * @param result
     * @return
     */
    boolean handleFanhuaNotification(String underwritingId, JsonObject result);

    /**
	 * 按用户查新信息
	 * 
	 * @param id
	 * @return
	 */
	BaoxianUnderwriting query(String id);

	BaoxianUnderwriting queryByIntentId(String intentId);

	String querySession(String id, Integer step);

	/**
	 * 修改阳光核保状态
	 * 
	 * @param id
	 * @param message
	 * @return
	 */
	boolean updateYangguangFaid(String id, String message);

	/**
	 * 查询列表
	 * 
	 * @param vo
	 * @return
	 */
	PageVO<BaoxianUnderwritingDTO> page(SearchVO vo);

	/**
	 * 保存用户信息
	 * 
	 * @param info
	 * @return
	 */
	boolean save(BaoxianUnderwriting info);

	/**
	 * 重新提交核保
	 *
	 * @param underwriting
	 * @param reportId
	 * @return
	 */
	boolean resumeReport(BaoxianUnderwriting underwriting, String reportId);

	/**
	 * 关闭订单
	 * 
	 * @param id
	 * @return
	 */
	boolean updateClose(String id, String userId, Integer userType);

	/**
	 * 查询提交信息
	 * 
	 * @param id
	 * @return
	 */
	BaoxinUnderwritingChangeLogDTO queryRequestChangesInfo(String id);

	/**
	 * 补全资料继续核保
	 * 
	 * @param id
	 * @param userId
	 * @param userType
     * @param reason
	 * @return
	 */
	boolean resumeReport(String id, String userId, Integer userType, String reason);

	/**
	 * 设置影像状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	boolean updateMediaStatus(String id, Integer status);

	/**
	 * 修改配送信息状态
	 * 
	 * @param id
	 * @param insuerInfoId
	 * @return
	 */
	boolean updateInsuerInfo(String id, String insuerInfoId);

	/**
	 * 修改自动核保状态
	 * 
	 * @param id
	 * @param status
	 * @param message
	 * @return
	 */
	boolean updateAutomatic(String id, AutomaticStatus status, String message);

	/**
	 * 修改报价信息
	 * 
	 *
	 * @param request
	 * @param t
	 * @return
	 */
	boolean updateStatusReported(BaoxianUnderwritingRequest request,
			BaoxianUnderwriting t);

	/**
	 * 设置失败
	 * 
	 * @param id
	 * @param message
	 * @return
	 */
	boolean updateCantUnderwritingFail(String id, String message);

	/**
	 * 设置失败
	 * 
	 * @param id
	 * @param message
	 * @param reason
	 *            -1: 异常 0: 接口错误 1: 回调通知
	 * @return
	 */
	boolean updateStatusFail(String id, String message, int reason);

	/**
	 * 查询数量
	 * 
	 * @param status
	 * @param userId
	 * @param userType
	 * @return
	 */
	int queryCount(Integer status, String userId, Integer userType);

	/**
	 * 查询指定时间段内报价单数量
	 *
	 * @param status
	 *            核保状态
	 * @param userId
	 *            用户ID
	 * @param userType
	 *            用户类型
	 * @param beginCreateDate
	 *            起始创建时间
	 * @param endCreateDate
	 *            截止创建时间
	 * @return
	 */
	int queryReportCount(Integer status, String userId, Integer userType,
			Date beginCreateDate, Date endCreateDate);

	/**
	 * 查询指定时间段内提交的核保单数量
	 * 
	 * @param status
	 * @param userId
	 * @param userType
	 * @param fromDateRaw
	 * @param toDateRaw
	 * @return
	 */
	int queryRequestCount(Integer status, String userId, Integer userType,
			Date fromDateRaw, Date toDateRaw);

}