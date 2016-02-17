package com.chengniu.client.service.impl;

import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.BaoxianUnderwritingDateExceptionDAO;
import com.chengniu.client.dao.BaoxianUnderwritingOperationRecordDAO;
import com.chengniu.client.dao.BaoxianUnderwritingReportDAO;
import com.chengniu.client.event.UnderwritingSuccessEvent;
import com.chengniu.client.event.UnderwritingVerifiedEvent;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.BaoxianInformalQuoteService;
import com.chengniu.client.service.BaoxianUnderwritingReportService;
import com.chengniu.client.service.BaoxianUnderwritingService;
import com.chengniu.client.service.FanhuaResponseParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kplus.orders.rpc.common.DateUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

@Service("baoxianUnderwritingReportService")
public class BaoxianUnderwritingReportServiceImpl implements
		BaoxianUnderwritingReportService {
	protected static final Logger log = LogManager
			.getLogger(BaoxianUnderwritingReportServiceImpl.class);
	@Autowired
	private BaoxianUnderwritingReportDAO baoxianUnderwritingReportDAO;
	@Autowired
	private BaoxianUnderwritingService baoxianUnderwritingService;
	@Autowired
	private BaoxianInformalQuoteService baoxianInformalQuoteService;

	@Autowired
	private EventDispatcher eventDispatcher;

	@Override
	public BaoxianUnderwritingReport queryByUnderwritingId(String id) {
		return this.baoxianUnderwritingReportDAO.queryByUnderwritingId(id);
	}

	@Override
	public BaoxianUnderwritingReport query(String id) {
		return baoxianUnderwritingReportDAO.query(id);
	}

	@Override
	public BaoxianUnderwritingReport queryByOrderNum(String orderNum,
			Integer userType) {
		return baoxianUnderwritingReportDAO.queryByOrderNum(orderNum, userType);
	}

	@Override
	public boolean save(BaoxianUnderwritingReport report) {
		if (report == null)
			return false;
		if (StringUtils.hasText(report.getId()))
			return this.baoxianUnderwritingReportDAO.update(report) > 0;
		else {
			report.setId(SerializableUtils.serializable());
			return this.baoxianUnderwritingReportDAO.insert(report) > 0;
		}
	}

	@Override
	public int queryCount(String userId, Integer userType) {
		BaoxianUnderwritingReport t = new BaoxianUnderwritingReport();
		t.setUserId(userId);
		t.setUserType(userType);
		return this.baoxianUnderwritingReportDAO.queryCount(t);
	}

	public BaoxianUnderwritingReport disposeFanhuaReport(String baoxianUnderwritingId,
                                                         String quoteId,
                                                         JsonObject result) {
		String state = result.get("state").getAsString();
		if (!"VerifySuccess".equalsIgnoreCase(state)
				&& !"Payed".equalsIgnoreCase(state)
				&& !"Interrupted".equalsIgnoreCase(state)
				&& !"Cancelled".equalsIgnoreCase(state)
				&& !"PayIng".equalsIgnoreCase(state)
				&& !"Cancelled PayIng".equalsIgnoreCase(state)
				&& !"UnderWriteFailed".equalsIgnoreCase(state)
				&& !"Finished".equalsIgnoreCase(state))
			return null;

		BaoxianUnderwritingReport report = new BaoxianUnderwritingReport();
		report.setId(SerializableUtils.serializable());
		report.setBaoxianUnderwritingId(baoxianUnderwritingId);
		report.setPolicyStatus(state);
		String mid = result.get("mid").getAsString();
		report.setMainQuoteId(mid);
		report.setQuoteId(quoteId);
		report.setCreateTime(new Date());
		report.setStatus(0);
		report.setPayStatus(0);
		report.setUpdateTime(new Date());
		report.setOperatorName("泛华核保");
		report.setExpressStatus(0);
		BaoxianUnderwriting underwriting = baoxianUnderwritingService
				.query(report.getBaoxianUnderwritingId());
		report.setBaoxianIntentId(underwriting.getBaoxianIntentId());
		report.setBaoxianInformalReportId(underwriting
                .getBaoxianInformalReportId());
		report.setChannel(underwriting.getChannel());
		report.setEnableOrder(true);
		report.setCityName(underwriting.getCityName());
		report.setVehicleModelCode(underwriting.getVehicleModelCode());
		report.setVehicleModelName(underwriting.getVehicleModelName());

        if ("Interrupted".equalsIgnoreCase(state)
                || "PayIng".equalsIgnoreCase(state)
                || "UnderWriteSuccess".equalsIgnoreCase(state)
                || "Cancelled PayIng".equalsIgnoreCase(state)
                || "Finished".equalsIgnoreCase(state))
            report.setUnderwritingStatus(1);
        else if ("UnderWriteFailed".equalsIgnoreCase(state))
            report.setUnderwritingStatus(-1);
        else
            report.setUnderwritingStatus(0);

        FanhuaResponseParser.parseReport(result, report);

		report.setBaoxianCompanyCode(underwriting.getBaoxianCompanyCode());
		report.setMobile(underwriting.getMobile());
		report.setBaoxianCompanyName(underwriting.getBaoxianCompanyName());
		report.setUserType(underwriting.getUserType());
		report.setCityCode(underwriting.getCityCode());
		report.setOperatorTime(new Date());
		report.setVehicleModelPrice(underwriting.getVehicleModelPrice());
		report.setUserId(underwriting.getUserId());
		try {
			BaoxianUnderwritingReport oldReport = baoxianUnderwritingReportDAO
					.queryByUnderwritingId(underwriting.getId());
			if (oldReport == null) {
				this.baoxianUnderwritingReportDAO.insert(report);
			} else {
				// 支付过了就别改了
				if (oldReport.getPayStatus() != null
						&& oldReport.getPayStatus() == 1)
					return report;
				report.setId(oldReport.getId());
				report.setStatus(null);
				report.setBaoxianIntentId(underwriting.getBaoxianIntentId());
				report.setPayStatus(null);
				report.setBaoxianInformalReportId(underwriting
						.getBaoxianInformalReportId());
				report.setEnableOrder(null);
				report.setExpressStatus(null);
				this.baoxianUnderwritingReportDAO.update(report);
			}

            FanhuaResponseParser.parseRequest(result, underwriting);

			BaoxianUnderwritingRequest request = baoxianInformalQuoteService
					.queryRequestByIntentId(underwriting.getBaoxianIntentId());
			baoxianUnderwritingService.updateStatusReported(request,
					underwriting);
			if (request != null) {
				baoxianInformalQuoteService.updateQuoteRequest(request, false);
			}

			// 发送核保成功事件
			if (oldReport == null) {
                log.debug("发布核保成功事件 underwritingId:{}", underwriting.getId());
                eventDispatcher.publish(UnderwritingVerifiedEvent.create(
                        underwriting, report));
			}
		} catch (DuplicateKeyException e) {
			baoxianUnderwritingService.updateStatusReported(null, underwriting);
			return this.baoxianUnderwritingReportDAO
					.queryByUnderwritingId(baoxianUnderwritingId);
		}
		return report;
	}

}