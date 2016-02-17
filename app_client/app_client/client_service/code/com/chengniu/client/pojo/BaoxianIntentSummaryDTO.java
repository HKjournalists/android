package com.chengniu.client.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 意向单信息
 * 不同阶段关注的信息
 */
public class BaoxianIntentSummaryDTO implements Serializable {

    private String id;      // 意向ID
    private int status;     // 意向状态

    private String vehicleNum;
    private String cityName;
    private String submitTime;
    private Integer failType;
    private String failMessage;
    private String forecastTime;    // 预计完成时间

    // 报价信息
    private List<BaoxianInformalReportSummaryDTO> reportList;

    // 核保信息
    private String underwritingCompanyCode;
    private String underwritingCompanyName;
    private String underwritingCompanyUrl;
    private String underwritingCompanyRemark;
    private Integer automaticStatus;         // 自动核保状态
    private Integer mediaStatus;             // 影像资料状态
    private Integer underwritingStatus;      // 核保状态
    private String informalQuotePrice;       // 初步报价
    private String quotePrice;               // 正式报价

    // 出单信息
    private Integer orderStatus;
    private Integer payStatus;
    private Integer expressStatus;
    private String expireTime;
    private String actualPrice;             // 实际出单价格
    private String printTime;               // 出单时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getFailType() {
        return failType;
    }

    public void setFailType(Integer failType) {
        this.failType = failType;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

    public String getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(String forecastTime) {
        this.forecastTime = forecastTime;
    }

    public List<BaoxianInformalReportSummaryDTO> getReportList() {
        return reportList;
    }

    public void setReportList(List<BaoxianInformalReportSummaryDTO> reportList) {
        this.reportList = reportList;
    }

    public String getUnderwritingCompanyCode() {
        return underwritingCompanyCode;
    }

    public void setUnderwritingCompanyCode(String underwritingCompanyCode) {
        this.underwritingCompanyCode = underwritingCompanyCode;
    }

    public String getUnderwritingCompanyName() {
        return underwritingCompanyName;
    }

    public void setUnderwritingCompanyName(String underwritingCompanyName) {
        this.underwritingCompanyName = underwritingCompanyName;
    }

    public String getUnderwritingCompanyUrl() {
        return underwritingCompanyUrl;
    }

    public void setUnderwritingCompanyUrl(String underwritingCompanyUrl) {
        this.underwritingCompanyUrl = underwritingCompanyUrl;
    }

    public String getUnderwritingCompanyRemark() {
        return underwritingCompanyRemark;
    }

    public void setUnderwritingCompanyRemark(String underwritingCompanyRemark) {
        this.underwritingCompanyRemark = underwritingCompanyRemark;
    }

    public Integer getAutomaticStatus() {
        return automaticStatus;
    }

    public void setAutomaticStatus(Integer automaticStatus) {
        this.automaticStatus = automaticStatus;
    }

    public Integer getMediaStatus() {
        return mediaStatus;
    }

    public void setMediaStatus(Integer mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public Integer getUnderwritingStatus() {
        return underwritingStatus;
    }

    public void setUnderwritingStatus(Integer underwritingStatus) {
        this.underwritingStatus = underwritingStatus;
    }

    public String getInformalQuotePrice() {
        return informalQuotePrice;
    }

    public void setInformalQuotePrice(String informalQuotePrice) {
        this.informalQuotePrice = informalQuotePrice;
    }

    public String getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(String quotePrice) {
        this.quotePrice = quotePrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(Integer expressStatus) {
        this.expressStatus = expressStatus;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }
}
