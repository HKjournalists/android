package com.chengniu.client.service;

import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.pojo.BaoxianInformalReport;
import com.chengniu.client.pojo.BaoxianIntent;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * 试算服务
 */
public interface BaoxianInformalQuoteService {

	/**
	 * 提交多方报价
	 *
	 * @param intentId
	 * @param remark
	 * @return
	 */
	Map<String, Object> disposeRequestToPost(String intentId, String companys,
			String remark);

	/**
	 * 上传影像资料到合作方
	 * 
	 * @param requestId
	 */
	boolean disposeMedia(String requestId);

	/**
	 * 查询
	 *
	 * @param reportId
	 * @return
	 */
	boolean disposeQuery(String reportId);

	/**
	 * cancel
	 *
	 * @param requestId
     * @param id
	 * @return
	 */
	Map<String, Object> disposeCancel(String requestId, String id);

	/**
	 * 处理泛华回写数据
	 * 
	 * @param underwritingId
	 * @param result
	 */
	boolean handleFanhuaNotification(String underwritingId, JsonObject result);

	/**
	 * 按报价请求获取报价结果列表
	 * 
	 * @param requestId
	 * @return
	 */
	List<BaoxianInformalReport> fetchQuoteList(String requestId);

	/**
	 * 查看报价详情
	 *
	 * @param informalReportId
	 * @return
	 */
	BaoxianInformalReport queryInformalReport(String informalReportId);

	/**
	 * 保存报价请求
	 *
	 * @param info
	 * @return
	 */
	boolean createQuoteRequest(BaoxianIntent intent, BaoxianBaseInfo info);

	/**
	 * 按意向ID获取报价请求
	 *
	 * @param intentId
	 * @return
	 */
	BaoxianUnderwritingRequest queryRequestByIntentId(String intentId);

	/**
	 * 按请求ID获取报价请求
	 * 
	 * @param requestId
	 * @return
	 */
	BaoxianUnderwritingRequest queryRequest(String requestId);

	/**
	 * 更新请求
	 *
	 * @param request
	 * @param isRenewRequest
	 */
	boolean updateQuoteRequest(BaoxianUnderwritingRequest request,
			boolean isRenewRequest);

	/**
	 * 继续试算请求
	 * 
	 * @param underwritingRequest
	 */
	void resumeQuoteRequest(BaoxianUnderwritingRequest underwritingRequest);

	/**
	 * 校验试算报价的有效性
	 * 
	 * @param report
	 * @return
	 */
	boolean validateInformalReport(BaoxianInformalReport report);

	/**
	 * 保存手动报价
	 *
	 * @param report
	 * @return
	 */
	boolean saveManualQuoteReport(BaoxianInformalReport report);

	/**
	 * 标记报价失败
	 * 
	 * @param report
	 * @param remark
	 * @return
	 */
	boolean updateInformalReportFailed(BaoxianInformalReport report,
			String remark);

	void cancelPendingReport(String requestId);
}
