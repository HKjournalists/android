package com.chengniu.client.service;

import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianOrdersDTO;
import com.chengniu.client.pojo.BaoxianUnderwritingExpress;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;
import com.chengniu.client.pojo.BaoxianUnderwritingReportPayinfo;
import com.daze.intergation.dto.VerityRequest;
import com.google.gson.JsonObject;
import com.kplus.orders.rpc.dto.OrderPaymentDTO;

import java.util.Date;
import java.util.Map;

/**
 * 保险订单管理
 */
public interface BaoxianOrderService {

    /**
     * 暴露订单
     * @param reportId
     * @param userId
     * @param userType
     * @return
     */
    BaoxianUnderwritingReport createOrder(String reportId, String userId,
                                          Integer userType);

    /**
     * 设置为已提交订单
     * @param report
     */
    void updateSubmitOrders(BaoxianUnderwritingReport report);

    /**
     * 处理订单状态
     *
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(String id, int status);

    /**
     * 保存支付状态
     * @param report
     * @return
     */
    boolean updatePayStatus(BaoxianUnderwritingReport report);

    /**
     * 配送状态
     *
     * @param id
     * @param info
     * @return
     */
    boolean updateExpressStatus(String id, BaoxianUnderwritingExpress info);

    /**
     * 修改投保单
     *
     * @param id
     * @return
     */
    boolean updatePropNum(String id, String JqxPropNum, String SyxPropNum);

    /**
     * 修改保单机构号
     *
     * @param id
     * @param policyNo
     * @return
     */
    boolean updatePolicyNo(String id, String policyNo);

    /**
     * 处理承保状态
     * @param baoxianUnderwritingId
     * @param quoteId
     * @param result
     * @return
     */
    boolean updatePolicyStatus(String baoxianUnderwritingId, String quoteId, JsonObject result);

    /**
     * 修改配送信息状态
     *
     * @param id
     * @param insuerInfoId
     * @return
     */
    boolean updateInsuerInfo(String id, String insuerInfoId);

    /**
     * 获取最近的工作日
     *
     * @param date
     * @return
     */
    Date queryLastPayTime(Date date);

    /**
     * 获取出单时间
     *
     * @param date
     * @return
     */
    Date queryPrintTime(Date date, Date expireTime);

    /**
     * 查询订单
     *
     * @param report
     * @return
     */
    BaoxianOrdersDTO queryOrderSummary(BaoxianUnderwritingReport report);

    /**
     * 查询订单支付信息
     * @param report
     * @return
     */
    BaoxianUnderwritingReportPayinfo queryOrderPayment(BaoxianUnderwritingReport report);

    /**
     * 查询订单支付状态
     * @param report
     * @return
     */
    boolean checkOrderPayable(BaoxianUnderwritingReport report);

    /**
     * 发起支付
     * @param underwritingReport
     * @param type
     * @param bankNo
     * @param bankCode
     * @param bankName
     * @return
     */
    Map<String, Object> initiatePayment(BaoxianUnderwritingReport underwritingReport, Operator operator,
                                  String type, String bankNo, String bankCode, String bankName);

    /**
     * 发起橙牛支付
     * @param report
     * @param payType
     * @param channel
     *@param remoteAddr  @return
     */
    Map<String,String> initiateChengniuPayment(BaoxianUnderwritingReport report,
                                               Integer payType, String channel, String remoteAddr);

    /**
     * 支付失败通知
     * @param veritify
     */
    void handleOrdersOfficialPayFailed(VerityRequest veritify);

    /**
     * 支付状态通知
     *
     * @param orderNum
     * @param userType
     * @param payway
     * @param success
     * @return
     */
    boolean disposeOrderPayment(String orderNum, Integer userType, String payway,
                             boolean success);

    /**
     * 通知支付状态改变
     * @param reportId
     * @param success
     * @return
     */
    boolean handleOrderPayStatusChanged(String reportId, boolean success);

    /**
     * 处理泛化回调
     * @param underwritingId
     * @param result
     * @return
     * @throws Exception
     */
    boolean handleFanhuaNotification(String underwritingId, JsonObject result) throws Exception;

    Map<String,Object> disposeCancel(BaoxianUnderwritingReport report);

    /**
     * 检查报价有效期
     * @param report
     * @return
     */
    boolean checkValidity(BaoxianUnderwritingReport report);
}
