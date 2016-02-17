package com.chengniu.client.service;

import com.chengniu.client.pojo.BaoxianInsureInfo;
import com.chengniu.client.pojo.BaoxianUnderwriting;

import java.util.Map;

public interface FanhuaService {

    /**
     * 查询车型
     *
     * @param keyword
     * @param pageNum
     * @return
     */
    String fetchPagniateVehicles(String keyword, String pageNum);

    /**
     * 报价阶段修改配送地址
     *
     * @param underwriting
     * @param bis
     */
    boolean updatePeisongInQuoteStage(BaoxianUnderwriting underwriting, BaoxianInsureInfo bis);

    /**
	 * 提交配送地址
	 * 
	 * @param underwritingId
	 * @return
	 * @throws Exception
	 */
	Boolean updatePeisong(String underwritingId) throws Exception;

	/**
	 * 支付
	 *
	 * @param payId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> disposePay(String payId, String type, String bankNo,
			String bankName, String bankCode) throws Exception;

    /**
     * 查询支付状态
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean disposeManualNotifyPay(String id) throws Exception;

    /**
     * 处理泛华支付回调消息
     * @param id
     * @param response
     */
    boolean handlePaymentResult(String id, String response);

    /**
	 * 发送影像到泛华
	 *
	 * @param underwritingId
	 * @return
	 */
	Boolean disposeUnderwritingUploadMedia(String underwritingId);

    /**
     * 处理泛华回调
     * @param underwritingId
     * @param body
     * @return
     */
    boolean handleNotification(String underwritingId, String body) throws Exception;

}