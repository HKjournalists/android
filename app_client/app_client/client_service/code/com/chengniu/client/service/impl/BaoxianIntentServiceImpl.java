package com.chengniu.client.service.impl;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.BaoxianCarLicenseDAO;
import com.chengniu.client.dao.BaoxianIntentDAO;
import com.chengniu.client.domain.IntentStatus;
import com.chengniu.client.event.*;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianInformalQuoteService;
import com.chengniu.client.service.BaoxianIntentService;
import com.chengniu.client.service.BaoxianUnderwritingService;
import com.google.common.eventbus.Subscribe;
import com.kplus.orders.rpc.dto.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 保险意向管理
 */
@Service("baoxianIntentService")
public class BaoxianIntentServiceImpl implements BaoxianIntentService {

	@Autowired
	private BaoxianBaseInfoService baoxianBaseInfoService;
	@Autowired
	private BaoxianInformalQuoteService baoxianInformalQuoteService;
    @Autowired
    private BaoxianUnderwritingService baoxianUnderwritingService;

    @Autowired
    private BaoxianIntentDAO baoxianIntentDAO;
    @Autowired
	private EventDispatcher eventDispatcher;
	@Resource
	private BaoxianCarLicenseDAO baoxianCarLicenseDAO;

	private EventHandler eventHandler = new EventHandler();

	@Override
	public Integer queryCount(String status, String userId, Integer userType) {
		return this.baoxianIntentDAO.queryCount(status, userId, userType);
	}

	@PostConstruct
	public void init() {
		eventDispatcher.register(eventHandler);
	}

	@Override
	public List<BaoxianIntent> fetchPendingValidIntent(int userType,
			String userId, String vehicleNum) {
		String status = IntentStatus.Quoting.getValue() + ","
				+ IntentStatus.QuoteFailed.getValue();

		return baoxianIntentDAO.queryByVehicleAndStatus(userType, userId,
				vehicleNum, status);
	}

	@Override
	public BaoxianIntent query(String intentId) {
		return baoxianIntentDAO.query(intentId);
	}

	@Override
	public PageVO<BaoxianIntent> fetchPaginateIntents(SearchVO vo) {
		return baoxianIntentDAO.queryByPage(vo);
	}

	@Override
	public BaoxianIntent submitIntent(BaoxianBaseInfo baseInfo)
			throws RuntimeException {
		// 一辆车只能有一个有效意向
        if (!"新车未上牌".equals(baseInfo.getVehicleNum())) {
            List<BaoxianIntent> historyIntentList = fetchPendingValidIntent(
                    baseInfo.getUserType(), baseInfo.getUserId(),
                    baseInfo.getVehicleNum());
            if (historyIntentList != null && !historyIntentList.isEmpty()) {
                throw new RuntimeException("该车有其它记录");
            }
        }
		boolean succ = baoxianBaseInfoService.save(baseInfo);
		if (!succ) {
			throw new RuntimeException("保存基本信息错误");
		}
		// 创建初步意向
		BaoxianIntent intent = new BaoxianIntent();
		intent.setUserType(baseInfo.getUserType());
		intent.setUserId(baseInfo.getUserId());
		intent.setBaoxianBaseInfoId(baseInfo.getId());
		intent.setStatus(IntentStatus.Intent.getValue());
		intent.setDeleted("N");
		intent.setCityCode(baseInfo.getCityCode());
		intent.setCreateTime(new Date());

		intent.setId(SerializableUtils.allocUUID());
		if (baoxianIntentDAO.insert(intent) > 0) {
			return intent;
		}

		return null;
	}

	@Override
	public boolean updateUserInfo(BaoxianIntent intent, BaoxianBaseInfo info) {
		// if (!IntentStatus.isUserInfoFixable(intent.getStatus())) {
		// throw new RuntimeException("当前无法补充资料");
		// }

		info.setId(intent.getBaoxianBaseInfoId());
		boolean ret = baoxianBaseInfoService.save(info);

		return ret;
	}

	@Override
	public boolean deleteIntent(BaoxianIntent intent) {
		if (StringUtils.isEmpty(intent.getId())) {
			return false;
		}
		return baoxianIntentDAO.delete(intent.getId()) > 0;
	}

	@Override
	public boolean updateIntentStatus(BaoxianIntent intent) {
		return baoxianIntentDAO.updateStatus(intent) > 0;
	}

	private final class EventHandler {

		@Subscribe
		public void didInformalQuoteFinished(QuoteFinishedEvent e) {
			final BaoxianUnderwritingRequest request = e.getRequest();
			BaoxianIntent intent = query(request.getBaoxianIntentId());
			if (intent != null) {
				if (IntentStatus.isQuoteStage(intent.getStatus())) {
					intent.setStatus(IntentStatus.QuoteFinished.getValue());
					updateIntentStatus(intent);

					// 报价认证车辆
					if (intent.getStatus() != null
							&& IntentStatus.QuoteFinished.getValue() == intent
									.getStatus().intValue()) {
						BaoxianBaseInfo baseInfo = baoxianBaseInfoService
								.query(intent.getBaoxianBaseInfoId(),
										intent.getUserId(),
										intent.getUserType());
						try {
							BaoxianCarLicense carList = CommonUtil
									.simpleValueCopy(baseInfo,
											BaoxianCarLicense.class);
							baoxianCarLicenseDAO.insert(carList);
						} catch (Exception e1) {
						}
					}
				}
			}
		}

		@Subscribe
		public void didUnderwritingSubmited(UnderwritingSubmitedEvent e) {
			final BaoxianUnderwriting underwriting = e.getUnderwriting();

			BaoxianIntent intent = query(underwriting.getBaoxianIntentId());
			if (intent != null) {
				if (IntentStatus.isQuoteStage(intent.getStatus())) {
					// 修改状态为核保中
					intent.setStatus(IntentStatus.UnderwritingPending
							.getValue());
					updateIntentStatus(intent);
				}
			}
		}

//		@Subscribe
//		public void didUnderwritingFailed(UnderwritingVerifyFailedEvent e) {
//			final BaoxianUnderwriting underwriting = e.getUnderwriting();
//
//			BaoxianIntent intent = query(underwriting.getBaoxianIntentId());
//			if (intent != null) {
//				if (IntentStatus.isUnderwritingState(intent.getStatus())) {
//					// 修改状态为核保失败
//					intent.setStatus(IntentStatus.UnderwritingFailed.getValue());
//					updateIntentStatus(intent);
//				}
//			}
//		}

		@Subscribe
		public void didUnderwritingVerified(UnderwritingVerifiedEvent e) {
			final BaoxianUnderwriting underwriting = e.getUnderwriting();

			BaoxianIntent intent = query(underwriting.getBaoxianIntentId());
			if (intent != null) {
				if (IntentStatus.isUnderwritingState(intent.getStatus())) {
					// 修改状态为核保成功
					intent.setStatus(IntentStatus.UnderwritingSuccess
							.getValue());
					updateIntentStatus(intent);
				}
			}
		}

		@Subscribe
		public void didOrderPaid(OrderPayStatusChangedEvent e) {
			final BaoxianUnderwritingReport report = e.getReport();
			if (report.getPayStatus() != null && report.getPayStatus() == 1) {
				BaoxianIntent intent = query(report.getBaoxianIntentId());
				if (intent != null) {
//					if (IntentStatus.isOrderStage(intent.getStatus())) {
						// 修改状态为承保中
						intent.setStatus(IntentStatus.Paid.getValue());
						updateIntentStatus(intent);
//					}
				}
			}
		}

		@Subscribe
		public void didOrderExpress(UnderwritingExpressEvent e) {
			final BaoxianUnderwritingReport report = e.getReport();
			if (report.getPayStatus() != null && report.getExpressStatus() == 1) {
				BaoxianIntent intent = query(report.getBaoxianIntentId());
				if (intent != null) {
					if (IntentStatus.isOrderStage(intent.getStatus())) {
						// 修改状态为已配送
						intent.setStatus(IntentStatus.PolicyDelivered
								.getValue());
						updateIntentStatus(intent);
					}
				}
			}
		}

	}
}
