package com.chengniu.client.action.baoxian;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.service.BaoxianUnderwritingService;

/**
 * 开放接口
 */
public class OpenServiceApiAction extends SuperAction {

	private static final long serialVersionUID = -2858952828210430240L;

	@Resource(name = "baoxianUnderwritingService")
	private BaoxianUnderwritingService baoxianUnderwritingService;

	private String userId;
	private String fromDate;
	private String toDate;

	/**
	 * 查询个人用户核保统计数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String anySuccessUnderwritingOfUserInPeriod() throws Exception {
		if (StringUtils.isEmpty(userId)) {
			return super.ajax("参数错误", "250");
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Date fromDateRaw = null;
		if (!StringUtils.isEmpty(fromDate)) {
			fromDateRaw = DateUtils.parse(fromDate,
					DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		}

		Date toDateRaw = null;
		if (!StringUtils.isEmpty(toDate)) {
			toDateRaw = DateUtils.parse(toDate,
					DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		}

		if (fromDateRaw != null && toDateRaw != null
				&& fromDateRaw.after(toDateRaw)) {
			return super.ajax("参数错误: 开始时间不能晚于结束时间", "250");
		}

		// 核保成功数量
		int successCount = baoxianUnderwritingService.queryReportCount(1,
				userId, BaoxianBaseInfo.USER_TYPE.PERSON.value, fromDateRaw,
				toDateRaw);

		resultMap.put("hasSuccess", (successCount > 0) ? 1 : 0);
		return super.ajax(resultMap);
	}

	/**
	 * 查询个人用户核保请求统计数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String anyUnderwritingOfUserInPeriod() throws Exception {
		if (StringUtils.isEmpty(userId)) {
			return super.ajax("参数错误", "250");
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Date fromDateRaw = null;
		if (!StringUtils.isEmpty(fromDate)) {
			fromDateRaw = DateUtils.parse(fromDate,
					DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		}

		Date toDateRaw = null;
		if (!StringUtils.isEmpty(toDate)) {
			toDateRaw = DateUtils.parse(toDate,
					DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS);
		}

		if (fromDateRaw != null && toDateRaw != null
				&& fromDateRaw.after(toDateRaw)) {
			return super.ajax("参数错误: 开始时间不能晚于结束时间", "250");
		}

		// 核保成功数量
		int successCount = baoxianUnderwritingService.queryRequestCount(null,
				userId, BaoxianBaseInfo.USER_TYPE.PERSON.value, fromDateRaw,
				toDateRaw);

		resultMap.put("hasSuccess", (successCount > 0) ? 1 : 0);
		return super.ajax(resultMap);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}