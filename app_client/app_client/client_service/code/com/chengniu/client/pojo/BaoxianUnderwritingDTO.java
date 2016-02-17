package com.chengniu.client.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaoxianUnderwritingDTO extends BaoxianUnderwriting {
	private BigDecimal jqxPrice;
	private BigDecimal syxPrice;
	private BigDecimal ccsPrice;
	private BigDecimal szxPrice;
	private BigDecimal csxPrice;
	private String ordersNum;
	private BigDecimal ckxPrice;
	private String forecastTime;
	private BigDecimal dqxPrice;
	private BigDecimal tyxPrice;
	private BigDecimal zdzxcPrice;
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
	private BigDecimal bjmpxPrice;
	private BigDecimal fjxBjmpPrice;
	private BigDecimal dcjcdshxPrice;
	private BigDecimal jlctyPrice;
	private BigDecimal sgzrmpltyPrice;
	private BigDecimal tzckzxPrice;
	private BigDecimal jdcssbxwfzddsftyxPrice;
	private BigDecimal xlqjfybcxPrice;
	private BigDecimal jsshfwjzrxPirce;
	private BigDecimal otherPrice;
	private BigDecimal ssxPrice;
	private BigDecimal blxPrice;
	private BigDecimal hfxPrice;
	private BigDecimal zrxPrice;
	private BigDecimal sjzrxPrice;
	private BigDecimal marketPrice;
	private String policyStatus;
	private BigDecimal szxBjmpPrice;
	private BigDecimal sjzrxBjmpPrice;
	private BigDecimal csxBjmpPrice;
	private BigDecimal ckxBjmpPrice;
	private BigDecimal dqxBjmpPrice;
	private String baoxianUnderwritingReportId;
	private BigDecimal blxBjmpPrice;
	private Integer orderStatus;
	private BigDecimal hfxBjmpPrice;
	private String baoxianCompanyPic;
	private String vehicleNum;
	private String idCardName;
	private String startDate;
	private BigDecimal ssxBjmpPrice;
	private String syxPolicyNo;
	private String jqxPolicyNo;
	private String baoxianCompanyRemark;
	private String quoteId;
	private BigDecimal zrxBjmpPrice;
	private BaoxianUnderwritingExpress express;
	private Date expireTime;
	private BaoxianInsureInfo insureInfo;
	private String payType;

	private Map<String, BaoxianUnderwritingChangeLog> request;

	private String changesRemark; // 修改保险方案备注
	private Date ordersTime;
	private Date printTime;
	private BigDecimal totalPrice;
	private Integer reportStatus;
	private String policyNo;
	private Integer expressStatus;
	private Integer underwritingStatus;
	private BaoxianUnderwritingReportPayinfo payinfo;
	private Integer payStatus = 0;
	private List<BaoxinUnderwritingChangeLogDTO> underwritingChangeLogs;
	private List<BaoxianBaseInfoMediaDTO> mediaList;

	public Integer getReportStatus() {
		if (reportStatus == null)
			reportStatus = -1;
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
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

	public Integer getExpressStatus() {
		return expressStatus;
	}

	public void setExpressStatus(Integer expressStatus) {
		this.expressStatus = expressStatus;
	}

	public BigDecimal getDqxBjmpPrice() {
		return dqxBjmpPrice;
	}

	public void setDqxBjmpPrice(BigDecimal dqxBjmpPrice) {
		this.dqxBjmpPrice = dqxBjmpPrice;
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

	public Date getOrdersTime() {
		return ordersTime;
	}

	public void setOrdersTime(Date ordersTime) {
		this.ordersTime = ordersTime;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getBaoxianCompanyPic() {
		return baoxianCompanyPic;
	}

	public void setBaoxianCompanyPic(String baoxianCompanyPic) {
		this.baoxianCompanyPic = baoxianCompanyPic;
	}

	public String getBaoxianCompanyRemark() {
		return baoxianCompanyRemark;
	}

	public void setBaoxianCompanyRemark(String baoxianCompanyRemark) {
		this.baoxianCompanyRemark = baoxianCompanyRemark;
	}

	public String getBaoxianUnderwritingReportId() {
		return baoxianUnderwritingReportId;
	}

	public void setBaoxianUnderwritingReportId(
			String baoxianUnderwritingReportId) {
		this.baoxianUnderwritingReportId = baoxianUnderwritingReportId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BaoxianInsureInfo getInsureInfo() {
		return insureInfo;
	}

	public void setInsureInfo(BaoxianInsureInfo insureInfo) {
		this.insureInfo = insureInfo;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getSjzrxPrice() {
		return sjzrxPrice;
	}

	public void setSjzrxPrice(BigDecimal sjzrxPrice) {
		this.sjzrxPrice = sjzrxPrice;
	}

	public BigDecimal getSjzrxBjmpPrice() {
		return sjzrxBjmpPrice;
	}

	public void setSjzrxBjmpPrice(BigDecimal sjzrxBjmpPrice) {
		this.sjzrxBjmpPrice = sjzrxBjmpPrice;
	}

	public BaoxianUnderwritingExpress getExpress() {
		return express;
	}

	public void setExpress(BaoxianUnderwritingExpress express) {
		this.express = express;
	}

	public BaoxianUnderwritingReportPayinfo getPayinfo() {
		return payinfo;
	}

	public void setPayinfo(BaoxianUnderwritingReportPayinfo payinfo) {
		this.payinfo = payinfo;
	}

	public String getOrdersNum() {
		return ordersNum;
	}

	public void setOrdersNum(String ordersNum) {
		this.ordersNum = ordersNum;
	}

	public String getSyxPolicyNo() {
		return syxPolicyNo;
	}

	public void setSyxPolicyNo(String syxPolicyNo) {
		this.syxPolicyNo = syxPolicyNo;
	}

	public String getJqxPolicyNo() {
		return jqxPolicyNo;
	}

	public void setJqxPolicyNo(String jqxPolicyNo) {
		this.jqxPolicyNo = jqxPolicyNo;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getForecastTime() {
		return forecastTime;
	}

	public void setForecastTime(String forecastTime) {
		this.forecastTime = forecastTime;
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

	public List<BaoxinUnderwritingChangeLogDTO> getUnderwritingChangeLogs() {
		return underwritingChangeLogs;
	}

	public void setUnderwritingChangeLogs(
			List<BaoxinUnderwritingChangeLogDTO> underwritingChangeLogs) {
		this.underwritingChangeLogs = underwritingChangeLogs;
	}

	public Map<String, BaoxianUnderwritingChangeLog> getRequest() {
		return request;
	}

	public void setRequest(Map<String, BaoxianUnderwritingChangeLog> request) {
		this.request = request;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getChangesRemark() {
		return changesRemark;
	}

	public void setChangesRemark(String changesRemark) {
		this.changesRemark = changesRemark;
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

	public BigDecimal getBjmpxPrice() {
		return bjmpxPrice;
	}

	public void setBjmpxPrice(BigDecimal bjmpxPrice) {
		this.bjmpxPrice = bjmpxPrice;
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

	public List<BaoxianBaseInfoMediaDTO> getMediaList() {
		return mediaList;
	}

	public void setMediaList(List<BaoxianBaseInfoMediaDTO> mediaList) {
		this.mediaList = mediaList;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
}