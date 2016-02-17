package com.chengniu.client.action.baoxian;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.common.Operator;
import com.chengniu.client.pojo.BaoxianBaseInfo;
import com.chengniu.client.pojo.BaoxianBaseInfoMedia;
import com.chengniu.client.pojo.BaoxianIntent;
import com.chengniu.client.pojo.BaoxianIntentDTO;
import com.chengniu.client.pojo.BaoxianIntentSummaryDTO;
import com.chengniu.client.pojo.BaoxianOrdersDTO;
import com.chengniu.client.pojo.BaoxianPeisong;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianIntentService;
import com.chengniu.client.service.impl.BaoxianWorkflowController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.kplus.orders.rpc.dto.PageVO;

/**
 * 保险前端流程
 *
 * 1. 保存基本信息 1a. 保存信息 1b. 生成初步意愿
 *
 * 2. 补充信息
 *
 * 3. 提交报价方案 3a. 补全上年车船税缴税证明
 *
 * 4. 获取意向列表
 *
 * 5. 查看意向列表
 *
 * 6. 提交核保 6a. 保存配送地址 6b. 提交核保
 *
 * 7. 检查支付状态
 *
 * 8. 支付 8a. 发起支付 8b. 支付结果处理
 *
 */
public class BaoxianWorkflowAction extends SuperAction {

	private String vehicleNum;

	public String id;
	public String idCardName;
	public String mobile;
	public String idCardNum;
	public String idCardType;
	public String idCardUrl;
	public String guohu;
	public String guohuDate;

	private String medias;

	public String status;

	private String type;
	private String bankNo;
	private String bankCode;
	private String bankName;

	private String reportId;

	@Autowired
	private BaoxianIntentService baoxianIntentService;

	@Autowired
	private BaoxianBaseInfoService baoxianBaseInfoService;

	/**
	 * 按车牌号获取进行中的意向信息
	 *
	 * @return 意向ID
	 * @throws Exception
	 */
	public String fetchHistoryIntent() throws Exception {
		if (StringUtils.isEmpty(vehicleNum)) {
			return super.ajax("参数错误");
		}

		Map<String, Object> ret = Maps.newHashMap();

		Operator operator = getCurrentOperator();
		List<BaoxianIntent> intents = null;
		if (!"新车未上牌".equals(vehicleNum)) {
			intents = baoxianIntentService.fetchPendingValidIntent(
					operator.getUserType(), operator.getUserId(), vehicleNum);
		}

		if (intents != null && !intents.isEmpty()) {
			BaoxianIntent intent = intents.get(0);

			ret.put("id", intent.getId());
			ret.put("status", intent.getStatus());
		} else {
			BaoxianBaseInfo baseInfo = baoxianBaseInfoService
					.queryByVehicleNum(operator.getUserType(),
							operator.getUserId(), vehicleNum);
			ret.put("info", baseInfo);
		}

		return super.ajax(ret);
	}

	/*
	 * 提交基础信息
	 * 
	 * @return 意向ID
	 * 
	 * @throws Exception
	 */
	public String submitUserInfo() throws Exception {
		BaoxianBaseInfo info = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				.disableHtmlEscaping().create()
				.fromJson(this.getClientParam(), BaoxianBaseInfo.class);
		info.setUserType(this.getCurrentOperator().getUserType());
		info.setUserId(this.getCurrentOperator().getUserId());
		BaoxianIntent intent = null;
		String msg = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController();
			intent = controller.submitInfoAndCreateIntent(info);
			if (StringUtils.hasText(this.getMedias())) {
				this.id = intent.getId();
				List<BaoxianBaseInfoMedia> mediaList;
				Type listType = new com.google.gson.reflect.TypeToken<List<BaoxianBaseInfoMedia>>() {
				}.getType();
				mediaList = new GsonBuilder().setDateFormat("yyyy-MM-dd")
						.disableHtmlEscaping().create()
						.fromJson(medias, listType);
				controller.updateUserMediaInfo(mediaList);
			}
			resultMap.put("id", intent.getId());
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}

		resultMap.put("result", intent != null);
		return super.ajax(resultMap, "0", msg);
	}

	/**
	 * 补充信息
	 *
	 * @return
	 * @throws Exception
	 */
	public String fixUserInfo() throws Exception {
		BaoxianBaseInfo info = new BaoxianBaseInfo();
		info.setMobile(mobile);
		info.setIdCardName(idCardName);
		info.setIdCardNum(idCardNum);
		info.setIdCardUrl(idCardUrl);
		if (idCardNum != null && idCardName != null) {
			info.setToubaoren(true);
			info.setBeibaoren(true);
		}
		try {
			info.setIdCardType(Integer.parseInt(idCardType));
		} catch (Exception e) {
		}
		if ("0".equals(guohu)) {
			info.setGuohu(false);
		} else {
			info.setGuohu(true);
		}
		if (info.getGuohu()) {
			info.setGuohuDate(guohuDate);
		}
		info.setUserId(this.getCurrentOperator().getUserId());
		info.setUserType(this.getCurrentOperator().getUserType());

		boolean ret = false;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			ret = controller.updateUserInfo(info);
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", ret);
		return super.ajax(map, "0", msg);
	}

	/**
	 * 补全影像资料
	 *
	 * @return
	 * @throws Exception
	 */
	public String fixQuoteMediaInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);

		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);

			List<BaoxianBaseInfoMedia> mediaList;
			Type listType = new com.google.gson.reflect.TypeToken<List<BaoxianBaseInfoMedia>>() {
			}.getType();
			mediaList = new GsonBuilder().setDateFormat("yyyy-MM-dd")
					.disableHtmlEscaping().create().fromJson(medias, listType);

			controller.updateUserMediaInfo(mediaList);

			resultMap.put("result", true);

		} catch (RuntimeException e) {
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
		}

		return super.ajax(resultMap);
	}

	/**
	 * 提交报价请求
	 *
	 * @return
	 * @throws Exception
	 */
	public String commitQuoteRequest() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean ret = false;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			BaoxianUnderwritingRequest info = CommonUtil.gson().fromJson(
					this.getClientParam(), BaoxianUnderwritingRequest.class);
			ret = controller.submitQuoteRequest(info);
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		resultMap.put("result", ret);
		return super.ajax(resultMap, "0", msg);
	}

	public String queryStaticInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("chengbao", this.baoxianIntentService.queryCount("2",
				this.getCurrentOperator().getUserId(), this
						.getCurrentOperator().getUserType()));
		resultMap.put("baojia", this.baoxianIntentService.queryCount("0", this
				.getCurrentOperator().getUserId(), this.getCurrentOperator()
				.getUserType()));
		resultMap.put("hebao", this.baoxianIntentService.queryCount("1", this
				.getCurrentOperator().getUserId(), this.getCurrentOperator()
				.getUserType()));
		return super.ajax(resultMap);
	}

	/**
	 * 获取有效意向列表
	 *
	 * @return
	 * @throws Exception
	 */
	public String fetchValidIntents() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", 0);
		resultMap.put("time",
				DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		SearchVO vo = this.getSearch();
		vo.setUserType(this.getCurrentOperator().getUserType());
		if (!StringUtils.hasText(this.getCurrentOperator().getUserId())) {
			return super.ajax("参数错误", "250");
		}
		List<String> stautsList = null;
		if ("0".equals(status)) {
			stautsList = new ArrayList<String>();
			vo.setListStatus(stautsList);
			stautsList.add("1");
			stautsList.add("2");
			stautsList.add("4");
			stautsList.add("6");
		} else if ("1".equals(status)) {
			stautsList = new ArrayList<String>();
			vo.setListStatus(stautsList);
			stautsList.add("10");
			stautsList.add("12");
			stautsList.add("14");
		} else if ("2".equals(status)) {
			stautsList = new ArrayList<String>();
			vo.setListStatus(stautsList);
			stautsList.add("20");
			stautsList.add("26");
			stautsList.add("22");
			stautsList.add("24");
			stautsList.add("21");
		} else
			vo.setStatus(status);
		vo.setUserId(this.getCurrentOperator().getUserId());
		PageVO<BaoxianIntent> pageVO = baoxianIntentService
				.fetchPaginateIntents(vo);
		if (pageVO != null && pageVO.getCountRecords() > 0) {
			resultMap.put("total", pageVO.getCountRecords());

			List<BaoxianIntentSummaryDTO> intentSummaryDTOList = Lists
					.newLinkedList();
			Iterator<BaoxianIntent> iter = pageVO.getEntityList().iterator();
			while (iter.hasNext()) {
				BaoxianIntent intent = iter.next();
				try {
					BaoxianWorkflowController controller = new BaoxianWorkflowController(
							intent);
					BaoxianIntentSummaryDTO ret = controller
							.loadIntentSummary();
					if (ret != null) {
						intentSummaryDTOList.add(ret);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			resultMap.put("list", intentSummaryDTOList);
		}
		return super.ajax(resultMap);
	}

	/**
	 * 获取意向详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fetchIntentDetail() throws Exception {
		BaoxianIntentDTO ret = null;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			ret = controller.loadIntentDetail();
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		return super.ajax(ret, "0", msg);
	}

	/**
	 * 提交配送地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String submitDeliveryAddress() throws Exception {
		BaoxianPeisong info = CommonUtil.gson().fromJson(this.getClientParam(),
				BaoxianPeisong.class);
		info.setId(null);
		info.setUserType(this.getCurrentOperator().getUserType());
		info.setUserId(this.getCurrentOperator().getUserId());

		Map<String, Object> map = new HashMap<String, Object>();

		boolean ret = false;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			controller.createUnderwriting(reportId);
			ret = controller.submitDeliveryAddress(info);
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		map.put("result", ret);
		map.put("id", info.getId());
		return super.ajax(map, "0", msg);
	}

	/**
	 * 创建订单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createOrder() throws Exception {
		BaoxianOrdersDTO ret = null;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			ret = controller.createOrder(getCurrentOperator());
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		return super.ajax(ret, "0", msg);
	}

	/**
	 * 检查支付状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkPayStatus() throws Exception {
		boolean ret = false;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			ret = controller.checkPayable();
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		return super.ajax(!ret, "0", msg);
	}

	/**
	 * 支付
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initiatePayment() throws Exception {
		Map<String, Object> ret = null;
		String msg = "";
		try {
			BaoxianWorkflowController controller = new BaoxianWorkflowController(
					id);
			ret = controller.initiatePayment(getCurrentOperator(), type,
					bankNo, bankCode, bankName);
		} catch (RuntimeException e) {
			msg = e.getMessage();
		}
		return super.ajax(ret, "0", msg);
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public void setIdCardUrl(String idCardUrl) {
		this.idCardUrl = idCardUrl;
	}

	public void setGuohu(String guohu) {
		this.guohu = guohu;
	}

	public void setGuohuDate(String guohuDate) {
		this.guohuDate = guohuDate;
	}

	public void setMedias(String medias) {
		this.medias = medias;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getId() {
		return id;
	}

	public String getMedias() {
		return medias;
	}
}
