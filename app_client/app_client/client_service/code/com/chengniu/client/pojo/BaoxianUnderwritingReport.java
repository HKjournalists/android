package com.chengniu.client.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class BaoxianUnderwritingReport {
	private String id;
	private String baoxianInformalReportId;
	private String baoxianIntentId;

	private String baoxianUnderwritingId;

	private String baoxianCompanyCode;

	private String baoxianCompanyName;

	private Integer status;

	private String mobile;

	private BigDecimal jqxPrice;

	private BigDecimal syxPrice;

	private BigDecimal ccsPrice;

	private BigDecimal szxPrice;

	private BigDecimal csxPrice;

	private BigDecimal ckxPrice;

	private BigDecimal dqxPrice;

	private BigDecimal sjzrxPrice;

	private BigDecimal ssxPrice;

	private BigDecimal blxPrice;

	private BigDecimal hfxPrice;

	private BigDecimal zrxPrice;

	private BigDecimal szxBjmpPrice;

	private BigDecimal csxBjmpPrice;

	private BigDecimal ckxBjmpPrice;

	private BigDecimal dqxBjmpPrice;

	private BigDecimal sjzrxBjmpPrice;

	private BigDecimal blxBjmpPrice;

	private BigDecimal hfxBjmpPrice;

	private BigDecimal ssxBjmpPrice;

	private BigDecimal zrxBjmpPrice;

	private Date createTime;

	private Date ordersTime;

	private String ordersNum;

	private BigDecimal totalPrice;

	private Date updateTime;

	private String message;

	private String cityCode;

	private String cityName;

	private Integer vehicleModelPrice;

	private String vehicleModelCode;

	private String vehicleModelName;

	private String policyNo;

	private Integer payStatus;

	private String payTime;

	private String operatorId;

	private String operatorName;

	private Date operatorTime;

	private String userId;

	private Integer userType;

	private String baoxianInsureInfoId;

	private String policyStatus;

	private Date jqxStartDate;

	private Date syxStartDate;

	private Boolean enableOrder;

	private Integer systemPayStatus;

	private Date systemPayTime;

	private BigDecimal tyxPrice;

	private BigDecimal zdzxcPrice;

	private String baoxianPolicyPayinfoId;

	private Integer expressStatus;

	private String baoxianUnderwritingExpressId;

	private BigDecimal marketPrice;

	private String syxPolicyNo;

	private String jqxPolicyNo;

	private Integer underwritingStatus;

	private Date expireTime;

	private String syxPropNum;

	private String jqxPropNum;

	private String quoteId;

	private String mainQuoteId;

	private BigDecimal syxBjmpPrice;

	private BigDecimal cpcssxPrice;

	private BigDecimal zrmctyPrice;

	private BigDecimal ssmctyPrice;

	private BigDecimal kxmptyPrice;

	private BigDecimal dcsgmptyPrice;

	private BigDecimal xzsbssxPrice;

	private BigDecimal xzsbssxBjmpPrice;

	private BigDecimal cshwzrxPrice;

	private BigDecimal cshwzrxBjmpPrice;

	private BigDecimal ssxlwpssxPrice;

	private BigDecimal fjcsryzrxBjmpPrice;

	private BigDecimal jdctsssxPrice;

	private BigDecimal jbxBjmpPrice;

	private BigDecimal fjxBjmpPrice;

	private BigDecimal dcjcdshxPrice;

	private BigDecimal jlctyPrice;

	private BigDecimal sgzrmpltyPrice;

	private BigDecimal tzckzxPrice;

	private BigDecimal jdcssbxwfzddsftyxPrice;

	private BigDecimal xlqjfybcxPrice;

	private BigDecimal jsshfwjzrxPirce;

	private BigDecimal otherPrice;

	private BigDecimal bjmpxPrice;

	private String channel;

	private Double un;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getBaoxianIntentId() {
		return baoxianIntentId;
	}

	public void setBaoxianIntentId(String baoxianIntentId) {
		this.baoxianIntentId = baoxianIntentId;
	}

	public String getBaoxianUnderwritingId() {
		return baoxianUnderwritingId;
	}

	public void setBaoxianUnderwritingId(String baoxianUnderwritingId) {
		this.baoxianUnderwritingId = baoxianUnderwritingId == null ? null
				: baoxianUnderwritingId.trim();
	}

	public String getBaoxianCompanyCode() {
		return baoxianCompanyCode;
	}

	public void setBaoxianCompanyCode(String baoxianCompanyCode) {
		this.baoxianCompanyCode = baoxianCompanyCode == null ? null
				: baoxianCompanyCode.trim();
	}

	public String getBaoxianCompanyName() {
		return baoxianCompanyName;
	}

	public void setBaoxianCompanyName(String baoxianCompanyName) {
		this.baoxianCompanyName = baoxianCompanyName == null ? null
				: baoxianCompanyName.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public BigDecimal getJqxPrice() {
		return jqxPrice;
	}

	public void setJqxPrice(BigDecimal jqxPrice) {
		this.jqxPrice = jqxPrice;
	}

	public BigDecimal getSyxPrice() {
		return syxPrice;
	}

	public void setSyxPrice(BigDecimal syxPrice) {
		this.syxPrice = syxPrice;
	}

	public BigDecimal getCcsPrice() {
		return ccsPrice;
	}

	public void setCcsPrice(BigDecimal ccsPrice) {
		this.ccsPrice = ccsPrice;
	}

	public BigDecimal getSzxPrice() {
		return szxPrice;
	}

	public void setSzxPrice(BigDecimal szxPrice) {
		this.szxPrice = szxPrice;
	}

	public BigDecimal getCsxPrice() {
		return csxPrice;
	}

	public void setCsxPrice(BigDecimal csxPrice) {
		this.csxPrice = csxPrice;
	}

	public BigDecimal getCkxPrice() {
		return ckxPrice;
	}

	public void setCkxPrice(BigDecimal ckxPrice) {
		this.ckxPrice = ckxPrice;
	}

	public BigDecimal getDqxPrice() {
		return dqxPrice;
	}

	public void setDqxPrice(BigDecimal dqxPrice) {
		this.dqxPrice = dqxPrice;
	}

	public BigDecimal getSjzrxPrice() {
		return sjzrxPrice;
	}

	public void setSjzrxPrice(BigDecimal sjzrxPrice) {
		this.sjzrxPrice = sjzrxPrice;
	}

	public BigDecimal getSsxPrice() {
		return ssxPrice;
	}

	public void setSsxPrice(BigDecimal ssxPrice) {
		this.ssxPrice = ssxPrice;
	}

	public BigDecimal getBlxPrice() {
		return blxPrice;
	}

	public void setBlxPrice(BigDecimal blxPrice) {
		this.blxPrice = blxPrice;
	}

	public BigDecimal getHfxPrice() {
		return hfxPrice;
	}

	public void setHfxPrice(BigDecimal hfxPrice) {
		this.hfxPrice = hfxPrice;
	}

	public BigDecimal getZrxPrice() {
		return zrxPrice;
	}

	public void setZrxPrice(BigDecimal zrxPrice) {
		this.zrxPrice = zrxPrice;
	}

	public BigDecimal getSzxBjmpPrice() {
		return szxBjmpPrice;
	}

	public void setSzxBjmpPrice(BigDecimal szxBjmpPrice) {
		this.szxBjmpPrice = szxBjmpPrice;
	}

	public BigDecimal getCsxBjmpPrice() {
		return csxBjmpPrice;
	}

	public void setCsxBjmpPrice(BigDecimal csxBjmpPrice) {
		this.csxBjmpPrice = csxBjmpPrice;
	}

	public BigDecimal getCkxBjmpPrice() {
		return ckxBjmpPrice;
	}

	public void setCkxBjmpPrice(BigDecimal ckxBjmpPrice) {
		this.ckxBjmpPrice = ckxBjmpPrice;
	}

	public BigDecimal getDqxBjmpPrice() {
		return dqxBjmpPrice;
	}

	public void setDqxBjmpPrice(BigDecimal dqxBjmpPrice) {
		this.dqxBjmpPrice = dqxBjmpPrice;
	}

	public BigDecimal getSjzrxBjmpPrice() {
		return sjzrxBjmpPrice;
	}

	public void setSjzrxBjmpPrice(BigDecimal sjzrxBjmpPrice) {
		this.sjzrxBjmpPrice = sjzrxBjmpPrice;
	}

	public BigDecimal getBlxBjmpPrice() {
		return blxBjmpPrice;
	}

	public void setBlxBjmpPrice(BigDecimal blxBjmpPrice) {
		this.blxBjmpPrice = blxBjmpPrice;
	}

	public BigDecimal getHfxBjmpPrice() {
		return hfxBjmpPrice;
	}

	public void setHfxBjmpPrice(BigDecimal hfxBjmpPrice) {
		this.hfxBjmpPrice = hfxBjmpPrice;
	}

	public BigDecimal getSsxBjmpPrice() {
		return ssxBjmpPrice;
	}

	public void setSsxBjmpPrice(BigDecimal ssxBjmpPrice) {
		this.ssxBjmpPrice = ssxBjmpPrice;
	}

	public BigDecimal getZrxBjmpPrice() {
		return zrxBjmpPrice;
	}

	public void setZrxBjmpPrice(BigDecimal zrxBjmpPrice) {
		this.zrxBjmpPrice = zrxBjmpPrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getOrdersTime() {
		return ordersTime;
	}

	public void setOrdersTime(Date ordersTime) {
		this.ordersTime = ordersTime;
	}

	public String getOrdersNum() {
		return ordersNum;
	}

	public void setOrdersNum(String ordersNum) {
		this.ordersNum = ordersNum == null ? null : ordersNum.trim();
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message == null ? null : message.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName == null ? null : cityName.trim();
	}

	public Integer getVehicleModelPrice() {
		return vehicleModelPrice;
	}

	public void setVehicleModelPrice(Integer vehicleModelPrice) {
		this.vehicleModelPrice = vehicleModelPrice;
	}

	public String getVehicleModelCode() {
		return vehicleModelCode;
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode == null ? null
				: vehicleModelCode.trim();
	}

	public String getVehicleModelName() {
		return vehicleModelName;
	}

	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName == null ? null
				: vehicleModelName.trim();
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo == null ? null : policyNo.trim();
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime == null ? null : payTime.trim();
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId == null ? null : operatorId.trim();
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName == null ? null : operatorName.trim();
	}

	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getBaoxianInsureInfoId() {
		return baoxianInsureInfoId;
	}

	public void setBaoxianInsureInfoId(String baoxianInsureInfoId) {
		this.baoxianInsureInfoId = baoxianInsureInfoId == null ? null
				: baoxianInsureInfoId.trim();
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus == null ? null : policyStatus.trim();
	}

	public Date getJqxStartDate() {
		return jqxStartDate;
	}

	public void setJqxStartDate(Date jqxStartDate) {
		this.jqxStartDate = jqxStartDate;
	}

	public Date getSyxStartDate() {
		return syxStartDate;
	}

	public void setSyxStartDate(Date syxStartDate) {
		this.syxStartDate = syxStartDate;
	}

	public Boolean getEnableOrder() {
		return enableOrder;
	}

	public void setEnableOrder(Boolean enableOrder) {
		this.enableOrder = enableOrder;
	}

	public Integer getSystemPayStatus() {
		return systemPayStatus;
	}

	public void setSystemPayStatus(Integer systemPayStatus) {
		this.systemPayStatus = systemPayStatus;
	}

	public Date getSystemPayTime() {
		return systemPayTime;
	}

	public void setSystemPayTime(Date systemPayTime) {
		this.systemPayTime = systemPayTime;
	}

	public BigDecimal getTyxPrice() {
		return tyxPrice;
	}

	public void setTyxPrice(BigDecimal tyxPrice) {
		this.tyxPrice = tyxPrice;
	}

	public BigDecimal getZdzxcPrice() {
		return zdzxcPrice;
	}

	public void setZdzxcPrice(BigDecimal zdzxcPrice) {
		this.zdzxcPrice = zdzxcPrice;
	}

	public String getBaoxianPolicyPayinfoId() {
		return baoxianPolicyPayinfoId;
	}

	public void setBaoxianPolicyPayinfoId(String baoxianPolicyPayinfoId) {
		this.baoxianPolicyPayinfoId = baoxianPolicyPayinfoId == null ? null
				: baoxianPolicyPayinfoId.trim();
	}

	public Integer getExpressStatus() {
		return expressStatus;
	}

	public void setExpressStatus(Integer expressStatus) {
		this.expressStatus = expressStatus;
	}

	public String getBaoxianUnderwritingExpressId() {
		return baoxianUnderwritingExpressId;
	}

	public void setBaoxianUnderwritingExpressId(
			String baoxianUnderwritingExpressId) {
		this.baoxianUnderwritingExpressId = baoxianUnderwritingExpressId == null ? null
				: baoxianUnderwritingExpressId.trim();
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getSyxPolicyNo() {
		return syxPolicyNo;
	}

	public void setSyxPolicyNo(String syxPolicyNo) {
		this.syxPolicyNo = syxPolicyNo == null ? null : syxPolicyNo.trim();
	}

	public String getJqxPolicyNo() {
		return jqxPolicyNo;
	}

	public void setJqxPolicyNo(String jqxPolicyNo) {
		this.jqxPolicyNo = jqxPolicyNo == null ? null : jqxPolicyNo.trim();
	}

	public Integer getUnderwritingStatus() {
		return underwritingStatus;
	}

	public void setUnderwritingStatus(Integer underwritingStatus) {
		this.underwritingStatus = underwritingStatus;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getSyxPropNum() {
		return syxPropNum;
	}

	public void setSyxPropNum(String syxPropNum) {
		this.syxPropNum = syxPropNum == null ? null : syxPropNum.trim();
	}

	public String getJqxPropNum() {
		return jqxPropNum;
	}

	public void setJqxPropNum(String jqxPropNum) {
		this.jqxPropNum = jqxPropNum == null ? null : jqxPropNum.trim();
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId == null ? null : quoteId.trim();
	}

	public String getMainQuoteId() {
		return mainQuoteId;
	}

	public void setMainQuoteId(String mainQuoteId) {
		this.mainQuoteId = mainQuoteId;
	}

	public BigDecimal getSyxBjmpPrice() {
		return syxBjmpPrice;
	}

	public void setSyxBjmpPrice(BigDecimal syxBjmpPrice) {
		this.syxBjmpPrice = syxBjmpPrice;
	}

	public BigDecimal getCpcssxPrice() {
		return cpcssxPrice;
	}

	public void setCpcssxPrice(BigDecimal cpcssxPrice) {
		this.cpcssxPrice = cpcssxPrice;
	}

	public BigDecimal getZrmctyPrice() {
		return zrmctyPrice;
	}

	public void setZrmctyPrice(BigDecimal zrmctyPrice) {
		this.zrmctyPrice = zrmctyPrice;
	}

	public BigDecimal getSsmctyPrice() {
		return ssmctyPrice;
	}

	public void setSsmctyPrice(BigDecimal ssmctyPrice) {
		this.ssmctyPrice = ssmctyPrice;
	}

	public BigDecimal getKxmptyPrice() {
		return kxmptyPrice;
	}

	public void setKxmptyPrice(BigDecimal kxmptyPrice) {
		this.kxmptyPrice = kxmptyPrice;
	}

	public BigDecimal getDcsgmptyPrice() {
		return dcsgmptyPrice;
	}

	public void setDcsgmptyPrice(BigDecimal dcsgmptyPrice) {
		this.dcsgmptyPrice = dcsgmptyPrice;
	}

	public BigDecimal getXzsbssxPrice() {
		return xzsbssxPrice;
	}

	public void setXzsbssxPrice(BigDecimal xzsbssxPrice) {
		this.xzsbssxPrice = xzsbssxPrice;
	}

	public BigDecimal getXzsbssxBjmpPrice() {
		return xzsbssxBjmpPrice;
	}

	public void setXzsbssxBjmpPrice(BigDecimal xzsbssxBjmpPrice) {
		this.xzsbssxBjmpPrice = xzsbssxBjmpPrice;
	}

	public BigDecimal getCshwzrxPrice() {
		return cshwzrxPrice;
	}

	public void setCshwzrxPrice(BigDecimal cshwzrxPrice) {
		this.cshwzrxPrice = cshwzrxPrice;
	}

	public BigDecimal getCshwzrxBjmpPrice() {
		return cshwzrxBjmpPrice;
	}

	public void setCshwzrxBjmpPrice(BigDecimal cshwzrxBjmpPrice) {
		this.cshwzrxBjmpPrice = cshwzrxBjmpPrice;
	}

	public BigDecimal getSsxlwpssxPrice() {
		return ssxlwpssxPrice;
	}

	public void setSsxlwpssxPrice(BigDecimal ssxlwpssxPrice) {
		this.ssxlwpssxPrice = ssxlwpssxPrice;
	}

	public BigDecimal getFjcsryzrxBjmpPrice() {
		return fjcsryzrxBjmpPrice;
	}

	public void setFjcsryzrxBjmpPrice(BigDecimal fjcsryzrxBjmpPrice) {
		this.fjcsryzrxBjmpPrice = fjcsryzrxBjmpPrice;
	}

	public BigDecimal getJdctsssxPrice() {
		return jdctsssxPrice;
	}

	public void setJdctsssxPrice(BigDecimal jdctsssxPrice) {
		this.jdctsssxPrice = jdctsssxPrice;
	}

	public BigDecimal getJbxBjmpPrice() {
		return jbxBjmpPrice;
	}

	public void setJbxBjmpPrice(BigDecimal jbxBjmpPrice) {
		this.jbxBjmpPrice = jbxBjmpPrice;
	}

	public BigDecimal getFjxBjmpPrice() {
		return fjxBjmpPrice;
	}

	public void setFjxBjmpPrice(BigDecimal fjxBjmpPrice) {
		this.fjxBjmpPrice = fjxBjmpPrice;
	}

	public BigDecimal getDcjcdshxPrice() {
		return dcjcdshxPrice;
	}

	public void setDcjcdshxPrice(BigDecimal dcjcdshxPrice) {
		this.dcjcdshxPrice = dcjcdshxPrice;
	}

	public BigDecimal getJlctyPrice() {
		return jlctyPrice;
	}

	public void setJlctyPrice(BigDecimal jlctyPrice) {
		this.jlctyPrice = jlctyPrice;
	}

	public BigDecimal getSgzrmpltyPrice() {
		return sgzrmpltyPrice;
	}

	public void setSgzrmpltyPrice(BigDecimal sgzrmpltyPrice) {
		this.sgzrmpltyPrice = sgzrmpltyPrice;
	}

	public BigDecimal getTzckzxPrice() {
		return tzckzxPrice;
	}

	public void setTzckzxPrice(BigDecimal tzckzxPrice) {
		this.tzckzxPrice = tzckzxPrice;
	}

	public BigDecimal getJdcssbxwfzddsftyxPrice() {
		return jdcssbxwfzddsftyxPrice;
	}

	public void setJdcssbxwfzddsftyxPrice(BigDecimal jdcssbxwfzddsftyxPrice) {
		this.jdcssbxwfzddsftyxPrice = jdcssbxwfzddsftyxPrice;
	}

	public BigDecimal getXlqjfybcxPrice() {
		return xlqjfybcxPrice;
	}

	public void setXlqjfybcxPrice(BigDecimal xlqjfybcxPrice) {
		this.xlqjfybcxPrice = xlqjfybcxPrice;
	}

	public BigDecimal getJsshfwjzrxPirce() {
		return jsshfwjzrxPirce;
	}

	public void setJsshfwjzrxPirce(BigDecimal jsshfwjzrxPirce) {
		this.jsshfwjzrxPirce = jsshfwjzrxPirce;
	}

	public BigDecimal getOtherPrice() {
		return otherPrice;
	}

	public void setOtherPrice(BigDecimal otherPrice) {
		this.otherPrice = otherPrice;
	}

	public BigDecimal getBjmpxPrice() {
		return bjmpxPrice;
	}

	public void setBjmpxPrice(BigDecimal bjmpxPrice) {
		this.bjmpxPrice = bjmpxPrice;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel == null ? null : channel.trim();
	}

	public Double getUn() {
		return un;
	}

	public void setUn(Double un) {
		this.un = un;
	}

	public boolean isManualUnderwriting() {
		return quoteId == null || quoteId.isEmpty();
	}

	public String getBaoxianInformalReportId() {
		return baoxianInformalReportId;
	}

	public void setBaoxianInformalReportId(String baoxianInformalReportId) {
		this.baoxianInformalReportId = baoxianInformalReportId;
	}
}