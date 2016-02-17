package com.chengniu.client.service;

import com.chengniu.client.pojo.*;

import java.util.List;
import java.util.Map;

/**
 * 报价合作接口
 */
public interface BaoxianApi {

	/**
	 * 保存报价请求
	 * 
	 * @param request
	 * @param baseInfo
	 * @param companys
	 * @return
	 */
	Map<String, Object> disposeQuoteRequest(BaoxianUnderwritingRequest request,
			BaoxianBaseInfo baseInfo, List<String> companys, String remark);

	/**
	 * 提交报价请求
	 * 
	 * @param requestId
	 * @param quoteId
	 * @return
	 */
	Map<String, Object> submitQuoteRequest(String requestId, String quoteId);

	/**
	 * 提交影像资料
	 * 
	 * @param requestId
	 * @param quoteId
	 * @param list
	 * @return
	 */
	boolean disposeMedia(String requestId, String quoteId,
			List<BaoxianBaseInfoMedia> list);

	/**
	 * 查询报价状态
	 * 
	 * @param t
	 * @return
	 */
	String disposeQuoteQuery(BaoxianInformalReport t);

    /**
     * 取消报价请求
     * @param id
     * @param sid
     * @return
     */
	Map<String, Object>  disposeCancel(String id,String sid);

	/**
	 * 提交核保请求
	 * 
	 * @param underwriting
	 * @param report
	 * @return
	 */
    Map<String, Object> disposeUnderwriting(BaoxianUnderwriting underwriting,
			BaoxianInformalReport report);

    /**
     * 报价阶段修改配送信息
     *
     * @param underwriting
     * @param peisong
     * @return
     */
    boolean updatePeisongInQuote(BaoxianUnderwriting underwriting, BaoxianInsureInfo peisong);

    /**
     * 提交配送地址
     *
     * @param underwritingId
     * @param mainQuoteId
     *@param baoxianUnderwritingId
     * @param peisong   @return
     */
    Boolean updatePeisongInUnderwrite(String underwritingId, String mainQuoteId, String baoxianUnderwritingId, BaoxianInsureInfo peisong);


    /**
     * 发起支付
     *
     * @param report
     * @param type
     * @param bankArea
     * @param bank
     * @param pay
     * @return
     */
    Map<String,Object> initiatePay(BaoxianUnderwritingReport report, String type, String bankArea,
                                   Map<String, String> bank, BaoxianUnderwritingReportPayinfo pay);

    /**
     * 查询城市下保险机构
     * @param cityCode
     * @return
     */
    List<BaoxianCompanyFanhua> queryCompaniesInCity(String cityCode);

    /**
     * 查询支付的银行信息
     *
     * @param bankNo
     * @param bankName
     * @param bankCode
     * @return
     */
    Map<String,String> queryBankInfo(String bankNo, String bankName, String bankCode);

    /**
     * 同步支付信息
     *
     * @param payinfo
     * @return
     */
    String syncPayInfo(BaoxianUnderwritingReportPayinfo payinfo);
}
