package com.chengniu.client.action.baoxian;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.common.BitBooleanSerializer;
import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.domain.AutomaticStatus;
import com.chengniu.client.pojo.BaoxianPeisong;
import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.service.BaoxianUnderwritingService;
import com.chengniu.client.service.YangguangService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class YangguangBaoxianOrdersAction extends SuperAction {
	private static final long serialVersionUID = -2027055836778011409L;

	@Resource(name = "baoxianUnderwritingService")
	private BaoxianUnderwritingService baoxianUnderwritingService;
	@Resource(name = "yangguangService")
	private YangguangService yangguangService;

	private String mobile;
	public String vehicleNum;
	public String cityCode;
	public String idCardName;
	private String session;
	private String baseInfoId;
	private String id;

	/**
	 * 获取需要填写的信息100
	 * 
	 * @return
	 * @throws Exception
	 */
	public String preReport() throws Exception {
		Map<String, String> resultMap = this.yangguangService.disposePreReport(
				vehicleNum, idCardName, cityCode, mobile,
				this.getCurrentOperator());
		return super.ajax(resultMap);
	}

	/**
	 * 获取需要填写的信息105
	 * 
	 * @return
	 * @throws Exception
	 */
	public String commitBaseInfo() throws Exception {
		return super.ajax(this.yangguangService.disposeCommitBaseInfo(
				this.getClientParam(), session, this.getCurrentOperator()));
	}

	/**
	 * 获取需要填写的信息110
	 * 
	 * @return
	 * @throws Exception
	 */
	public String report() throws Exception {
		Gson gso = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				.registerTypeAdapter(Boolean.class, new BitBooleanSerializer())
				.disableHtmlEscaping().create();
		BaoxianUnderwriting info = gso.fromJson(this.getClientParam(),
				BaoxianUnderwriting.class);
		info.setId(id);
		info.setUserType(this.getCurrentOperator().getUserType());
		info.setUserId(this.getCurrentOperator().getUserId());
		boolean tryLoadHistoryFirst = false;
		if (StringUtils.hasText(id) && !StringUtils.hasText(session)) {
			session = this.baoxianUnderwritingService.querySession(id, 105);
			tryLoadHistoryFirst = true;
		}

		// 如果是报价完成，但是没有保存报价，直接返最后一次的报价请求
		BaoxianUnderwriting old = baoxianUnderwritingService.query(id);
		if (tryLoadHistoryFirst
				&& null != old.getAutomaticStatus()
				&& old.getAutomaticStatus() == AutomaticStatus.REPORT_SUCCEED
						.rawValue()
				&& (old.getStatus() == null || old.getStatus() == 0)) {
			Map<String, String> ret = this.yangguangService
					.disposeLatestReport(session);
			if (ret != null) {
				return super.ajax(ret);
			}
		}
		return super.ajax(this.yangguangService.disposeReport(info, session));
	}

	/**
	 * 提交核保115
	 *
	 * @return
	 * @throws Exception
	 */
	public String submitUnderwriting() throws Exception {
		if (StringUtils.hasText(id) && !StringUtils.hasText(session))
			session = this.baoxianUnderwritingService.querySession(id, 105);
		return super.ajax(this.yangguangService.disposeUnderwriting(session,
				this.getCurrentOperator()));
	}

	/**
	 * 提交订单120
	 *
	 * @return
	 * @throws Exception
	 */
	public String peisong() throws Exception {
		BaoxianPeisong info = CommonUtil.gson().fromJson(this.getClientParam(),
				BaoxianPeisong.class);
		info.setUserId(this.getCurrentOperator().getUserId());
		info.setUserType(this.getCurrentOperator().getUserType());
		if (StringUtils.hasText(id) && !StringUtils.hasText(session))
			session = this.baoxianUnderwritingService.querySession(id, 105);
		return super.ajax(this.yangguangService.disposePeisong(id, info,
				session, this.getCurrentOperator()));
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public void setBaseInfoId(String baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	public void setId(String id) {
		this.id = id;
	}
}