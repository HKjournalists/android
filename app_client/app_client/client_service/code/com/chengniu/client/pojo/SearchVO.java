package com.chengniu.client.pojo;

import java.util.Date;
import java.util.List;

import com.chengniu.client.common.DateUtils;
import com.kplus.orders.rpc.dto.PageSearchVO;

public class SearchVO extends PageSearchVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6800982915088154557L;
	private String keyword;// 关键词
	private String userId;
	private Integer userType;
	private String timeType;// 时间类型
	private String keywordType;// 关键词
	private String cityCode;// 查询条件
	private List<String>listStatus;
	private String startTime;// 查询条件
	private String endTime;// 查询条件
	private String payStatus;// 查询条件
	private String operatorId;// 操作人id
	private String orderNum;
	private String id;
	private String phone;// 关键词
	private String statisticsType;
	private String status;// 查询条件状态
	public final Date getStartDate() {
		return DateUtils.parse(this.getStartTime(),
				DateUtils.DATE_FORMAT_YYYYMMDD);
	}

	public final Date getEndDate() {
		return DateUtils.addDay(this.getEndTime(),
				DateUtils.DATE_FORMAT_YYYYMMDD, 1);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStatisticsType() {
		return statisticsType;
	}

	public void setStatisticsType(String statisticsType) {
		this.statisticsType = statisticsType;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(String keywordType) {
		this.keywordType = keywordType;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public List<String> getListStatus() {
		return listStatus;
	}

	public void setListStatus(List<String> listStatus) {
		this.listStatus = listStatus;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

}