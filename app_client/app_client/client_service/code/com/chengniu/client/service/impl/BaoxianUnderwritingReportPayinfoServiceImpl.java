package com.chengniu.client.service.impl;

import java.util.Date;
import java.util.Map;

import com.chengniu.client.service.BaoxianOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.BaoxianUnderwritingReportPayinfoDAO;
import com.chengniu.client.pojo.BaoxianUnderwritingReportPayinfo;
import com.chengniu.client.service.BaoxianUnderwritingReportPayinfoService;
import com.google.common.reflect.TypeToken;

@Service("baoxianUnderwritingReportPayinfoService")
public class BaoxianUnderwritingReportPayinfoServiceImpl implements
		BaoxianUnderwritingReportPayinfoService {
	@Autowired
	private BaoxianUnderwritingReportPayinfoDAO baoxianUnderwritingReportPayinfoDAO;
	@Autowired
	public BaoxianOrderService baoxianOrderService;

	@Override
	public boolean save(BaoxianUnderwritingReportPayinfo info) {
		info.setId(SerializableUtils.serializable());
		info.setStatus(0);
		info.setCreateTime(new Date());
		return baoxianUnderwritingReportPayinfoDAO.insert(info) > 0;
	}

	@Override
	public void update(BaoxianUnderwritingReportPayinfo payInfo) {
		baoxianUnderwritingReportPayinfoDAO.update(payInfo);
	}

	@Override
	public BaoxianUnderwritingReportPayinfo queryByReportId(String id) {
		return this.baoxianUnderwritingReportPayinfoDAO.queryByReportId(id);
	}

	@Override
	public BaoxianUnderwritingReportPayinfo queryByPayedReportId(String id) {
		return this.baoxianUnderwritingReportPayinfoDAO
				.queryByPayedReportId(id);
	}

    @Override
    public BaoxianUnderwritingReportPayinfo query(String id) {
        return baoxianUnderwritingReportPayinfoDAO.query(id);
    }

    @Override
	public boolean updateStatus(String id, Integer status) {
		BaoxianUnderwritingReportPayinfo pay = new BaoxianUnderwritingReportPayinfo();
		pay.setId(id);
		pay.setStatus(status);
		return this.baoxianUnderwritingReportPayinfoDAO.updateStatus(pay) > 0;
	}

	@Override
	public boolean updateRequest(String id, String request) {
		BaoxianUnderwritingReportPayinfo pay = new BaoxianUnderwritingReportPayinfo();
		pay.setId(id);
		pay.setRequestInfo(request);
		return this.baoxianUnderwritingReportPayinfoDAO.updateRequest(pay) > 0;
	}

    @Override
    public boolean updateResponse(String id, String tradeNum, int status, String response) {
        BaoxianUnderwritingReportPayinfo pay = this.baoxianUnderwritingReportPayinfoDAO
                .query(id);
        pay.setResponseInfo(response);
        pay.setResponseTime(new Date());
        pay.setTradeNum(tradeNum);
        pay.setStatus(status);

        return this.baoxianUnderwritingReportPayinfoDAO.updateResponse(pay) > 0;
    }

	@Override
	public boolean updateTradeNum(String id, String tradeNum) {
		BaoxianUnderwritingReportPayinfo pay = new BaoxianUnderwritingReportPayinfo();
		pay.setId(id);
		pay.setTradeNum(tradeNum);
		return this.baoxianUnderwritingReportPayinfoDAO.updateTradeNum(pay) > 0;
	}

}