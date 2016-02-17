package com.chengniu.client.service.impl;

import com.chengniu.client.common.DateUtils;
import com.chengniu.client.dao.UserPushClientDAO;
import com.chengniu.client.domain.QuoteRequestStatus;
import com.chengniu.client.event.*;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianInsureInfoService;
import com.chengniu.client.service.BaoxianUnderwritingService;
import com.chengniu.client.service.UserNotificationService;
import com.daze.common.api.dto.PushResult;
import com.daze.common.api.dto.SendResult;
import com.daze.common.api.service.MessagePushService;
import com.daze.common.api.service.ShortMessageService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kplus.user.rpc.dto.UserLoginRecordDTO;
import com.kplus.user.rpc.service.UserRPCService;
import com.providers.rpc.service.ProviderRPCService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * C端消息推送
 */
@Service("userNotificationService")
public class UserNotificationServiceImpl implements UserNotificationService {
	protected static final Logger log = LogManager
			.getLogger(UserNotificationServiceImpl.class);

	@Resource(name = "userRPCService")
	private UserRPCService userRPCService;
	@Resource(name = "messagePushService")
	private MessagePushService messagePushService;
    @Resource(name = "providerRPCService")
    private ProviderRPCService providerRPCService;
    @Resource(name = "messageRPCService")
    private ShortMessageService phoneMessage;

	@Autowired
	private UserPushClientDAO userPushClientDAO;
    @Autowired
    private EventDispatcher eventDispatcher;
    @Autowired
    private BaoxianBaseInfoService baoxianBaseInfoService;
    @Autowired
    private BaoxianUnderwritingService baoxianUnderwritingService;
    @Autowired
    private BaoxianInsureInfoService baoxianInsureInfoService;

	private static final int PUSH_TYPE_GETUI = 1;
	private static final String MAIN_CLIENT_APPID = "C_Main_App";

    @Value("${push.message.quote.failed}")
    private String messageQuoteFailed;
    @Value("${push.message.quote.finished}")
    private String messageQuoteFinished;
    @Value("${push.message.report}")
    private String messageReport;
    @Value("${push.message.pay}")
    private String paymessage;
    @Value("${push.message.undewrite.success}")
    private String messageUnderwriteSuccess;
    @Value("${push.message.order.expressed}")
    private String messageExpressed;

	private static Map<String, Object> sNotificationData = Maps.newHashMap();
	static {
		Map<String, Object> params = Maps.newHashMap();
		params.put("motionType", "webapp");
		params.put("motionValue",
				"{\"appId\":\"10000018\",\"startPage\":\"index.html\"}");
		sNotificationData.put("data", params);
	}

    @PostConstruct
    public void init() {
        eventDispatcher.register(this);
    }

    /**
     * 报价完成通知
     * @param e
     */
    @Subscribe
    public void notifyQuoteFinished(QuoteFinishedEvent e) {
        final BaoxianUnderwritingRequest request = e.getRequest();

        if (QuoteRequestStatus.isQuoteFailed(request.getStatus())) {
            // 发送报价失败消息
            BaoxianBaseInfo baseinfo = this.baoxianBaseInfoService
                    .query(request.getBaoxianBaseInfoId(),
                            request.getUserId(),
                            request.getUserType());

            String reason = request.getFailMessage();
            String msg = new MessageFormat(messageQuoteFailed).format(new String[]{
                    reason,
                    baseinfo.getVehicleNum() != null ? baseinfo.getVehicleNum() : ""
            });

            if (request.getUserType() == 1) {
                Map<String, Object> params = Maps.newHashMap();
                params.put("id", request.getBaoxianIntentId());

                notifyBUserWithAppMessage(request.getUserId(), msg, params);
            } else {
                notifyCUserWithAppMessage(request.getUserId(), msg, null);
            }
        } else {
            // 发送报价成功消息
            int count = e.getCount();

            BaoxianBaseInfo baseinfo = this.baoxianBaseInfoService
                    .query(request.getBaoxianBaseInfoId(),
                            request.getUserId(),
                            request.getUserType());
            String msg = new MessageFormat(messageQuoteFinished).format(new Object[]{
                    count,
                    baseinfo.getVehicleNum() != null ? baseinfo.getVehicleNum() : ""
            });

            if (request.getUserType() == 1) {
                Map<String, Object> params = Maps.newHashMap();
                params.put("id", request.getBaoxianIntentId());

                notifyBUserWithAppMessage(request.getUserId(), msg, params);
            } else {
                notifyCUserWithAppMessage(request.getUserId(), msg, null);
            }
        }
    }

    /**
     * 核保成功通知
     * @param e
     */
    @Subscribe
    public void notifyUnderwritingVerified(UnderwritingVerifiedEvent e) {
        final BaoxianUnderwriting underwriting = e.getUnderwriting();
        final BaoxianUnderwritingReport report = e.getReport();


        BaoxianBaseInfo baseinfo = this.baoxianBaseInfoService
                .query(underwriting.getBaoxianBaseInfoId(),
                        underwriting.getUserId(),
                        underwriting.getUserType());
        String msg = new MessageFormat(messageReport)
                .format(new String[]{
                        baseinfo.getVehicleNum() != null ? baseinfo.getVehicleNum() : "",
                        report.getBaoxianCompanyName() != null ? report.getBaoxianCompanyName() : "",
                        report.getTotalPrice() != null ? report.getTotalPrice().toPlainString() : ""
                });

        try {
            if (underwriting.getUserType() == 1) {
                Map<String, Object> params = Maps.newHashMap();
                params.put("id", underwriting.getBaoxianIntentId());

                notifyBUserWithAppMessage(underwriting.getUserId(), msg, params);
            } else {
                notifyCUserWithAppMessage(underwriting.getUserId(), msg, sNotificationData);
            }
        } catch (Exception ex) {
            log.warn("消息推送{}出错", underwriting.getId(), ex);
        }

        try {
            // 信息
            SendResult ret = phoneMessage.sendAsyncMessage(underwriting.getMobile(), msg, 1);
            log.warn("短信推送{} 返回：{}", underwriting.getId(), ret.toString());
        } catch (Exception ex) {
            log.warn("短信推送{}出错", underwriting.getId(), ex);
        }
    }

    /**
     * 支付成功通知
     * @param e
     */
    @Subscribe
    public void notifyOrderPayStatusChanged(OrderPayStatusChangedEvent e) {
        final BaoxianUnderwritingReport report = e.getReport();
        boolean success = report.getPayStatus() != null && report.getPayStatus() == 1;
        if (!success) {
            return;
        }

        BaoxianUnderwriting underwriting = baoxianUnderwritingService
                .query(report.getBaoxianUnderwritingId());
        BaoxianBaseInfo baseinfo = this.baoxianBaseInfoService.query(
                underwriting.getBaoxianBaseInfoId(),
                report.getUserId(), report.getUserType());

        String message = new MessageFormat(paymessage)
                .format(new String[] {
                        report.getTotalPrice() != null ? report.getTotalPrice().toPlainString() : "",
                        report.getBaoxianCompanyName() != null ? report.getBaoxianCompanyName() : "",
                        baseinfo.getVehicleNum() != null ? baseinfo.getVehicleNum() : ""
                });

        if (report.getUserType() == 1) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("id", underwriting.getBaoxianIntentId());

            notifyBUserWithAppMessage(underwriting.getUserId(), message, params);
        } else {
            notifyCUserWithAppMessage(underwriting.getUserId(), message, null);
        }

        // 发送消息
        try {
            SendResult ret = phoneMessage.sendAsyncMessage(underwriting.getMobile(), message, 1);
            log.warn("短信推送{} 返回：{}", underwriting.getId(), ret.toString());
        } catch (Exception ex) {
        }
    }

    /**
     * 承保成功
     * @param e
     */
    @Subscribe
    public void notifyOrderUnderwriteSuccess(UnderwritingSuccessEvent e) {
        final BaoxianUnderwritingReport report = e.getReport();

        BaoxianUnderwriting underwriting = baoxianUnderwritingService
                .query(report.getBaoxianUnderwritingId());
        BaoxianBaseInfo baseinfo = this.baoxianBaseInfoService.query(
                underwriting.getBaoxianBaseInfoId(),
                report.getUserId(), report.getUserType());

        String message = new MessageFormat(messageUnderwriteSuccess)
                .format(new String[] {
                        baseinfo.getVehicleNum() != null ? baseinfo.getVehicleNum() : "",
                        report.getBaoxianCompanyName() != null ? report.getBaoxianCompanyName() : "",
                        report.getJqxStartDate() != null ?
                                DateUtils.format(report.getJqxStartDate(), DateUtils.DATE_FORMAT_YYYYMMDD) : "",
                        report.getSyxStartDate() != null ?
                                DateUtils.format(report.getSyxStartDate(), DateUtils.DATE_FORMAT_YYYYMMDD) : "",
                });

        if (report.getUserType() == 1) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("id", underwriting.getBaoxianIntentId());

            notifyBUserWithAppMessage(underwriting.getUserId(), message, params);
        } else {
            notifyCUserWithAppMessage(underwriting.getUserId(), message, null);
        }

        // 发送消息
        try {
            SendResult ret = phoneMessage.sendAsyncMessage(underwriting.getMobile(), message, 1);
            log.warn("短信推送{} 返回：{}", underwriting.getId(), ret.toString());
        } catch (Exception ex) {
        }
    }

    /**
     * 已配送通知
     *
     * @param e
     */
    @Subscribe
    public void notifyUnderwritingExpressed(UnderwritingExpressEvent e) {
        final BaoxianUnderwritingReport report = e.getReport();

        BaoxianUnderwritingExpress express = baoxianInsureInfoService.queryByBaoxianUnderwritingReportId(report.getId());
        if (express == null) {
            log.error("找不到配送信息：" + report.getId());
            return;
        }

        BaoxianUnderwriting underwriting = baoxianUnderwritingService
                .query(report.getBaoxianUnderwritingId());
        BaoxianBaseInfo baseinfo = this.baoxianBaseInfoService.query(
                underwriting.getBaoxianBaseInfoId(),
                report.getUserId(), report.getUserType());

        String message = new MessageFormat(messageExpressed)
                .format(new String[] {
                        baseinfo.getVehicleNum() != null ? baseinfo.getVehicleNum() : "",
                        express.getCompany() != null ? express.getCompany() : "",
                        express.getOrderNum() != null ? express.getOrderNum() : ""
                });

        if (report.getUserType() == 1) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("id", underwriting.getBaoxianIntentId());

            notifyBUserWithAppMessage(underwriting.getUserId(), message, params);
        } else {
            notifyCUserWithAppMessage(underwriting.getUserId(), message, null);
        }

        // 发送消息
        try {
            SendResult ret = phoneMessage.sendAsyncMessage(underwriting.getMobile(), message, 1);
            log.warn("短信推送{} 返回：{}", underwriting.getId(), ret.toString());
        } catch (Exception ex) {
        }
    }


    /**
     * 给B端用户推送消息
     * @param uid
     * @param title
     * @param params
     */
    private void notifyBUserWithAppMessage(String uid, String title,
                                          Map<String, Object> params) {
        Map<String, Object> payload = Maps.newHashMap();
        payload.put("type", 11);
        payload.put("msg", title);
        payload.putAll(params);

        Gson gson = new GsonBuilder().disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String content = gson.toJson(payload);

        this.providerRPCService.push(uid, content);
    }

    /**
     * 给C端用户推送消息
     * @param uid
     * @param title
     * @param params
     */
	private void notifyCUserWithAppMessage(String uid, String title,
                                           Map<String, Object> params) {
		List<UserLoginRecordDTO> loginRecords = userRPCService
				.queryUserLoginRecordByUid(Long.parseLong(uid));
		if (loginRecords == null || loginRecords.isEmpty()) {
			log.info("用户(id=" + uid + ") 从未登陆过");
			return;
		}

		Collections.sort(loginRecords, new Comparator<UserLoginRecordDTO>() {
			@Override
			public int compare(UserLoginRecordDTO o1, UserLoginRecordDTO o2) {
				return o2.getCreateDatetime().compareTo(o1.getCreateDatetime());
			}
		});

		int count = 0;
		Set<Long> userIdSet = Sets.newHashSet();
		for (UserLoginRecordDTO r : loginRecords) {
			if (userIdSet.add(r.getUserId())) {
				count++;
			}

			if (count >= 2) {
				break;
			}
		}

		if (userIdSet.isEmpty()) {
			return;
		}

		List<UserPushClient> userPushClients = userPushClientDAO
				.selectPushClientByUserIds(Lists.newArrayList(userIdSet),
						PUSH_TYPE_GETUI, 3);
		if (userPushClients == null || userPushClients.isEmpty()) {
			log.info("用户(id=" + uid + ") 未绑定CID");
			return;
		}

		Set<String> cids = Sets.newHashSet();
		for (UserPushClient pushClient : userPushClients) {
			if (!StringUtils.isEmpty(pushClient.getClientId())) {
				cids.add(pushClient.getClientId());
			}
		}

		notifyWithPush(title, params, cids, MAIN_CLIENT_APPID, PUSH_TYPE_GETUI);
	}

	private void notifyWithPush(String title, Map<String, Object> params,
			Set<String> cids, String appId, int channelId) {

		Map<String, Object> payload = Maps.newHashMap();
		payload.put("type", 10);
		payload.put("msg", title);
        if (params != null) {
            payload.putAll(params);
        }

		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String content = gson.toJson(payload);
		if (cids.size() > 1) {
            String[] cidInArrary = new String[cids.size()];
            cidInArrary = cids.toArray(cidInArrary);
            PushResult ret = messagePushService.pushAsyncNoticeMessageToList(content,
                    appId, cidInArrary, channelId, null, title);
			log.info("push result:" + ret.toString());
		} else {
            PushResult ret = messagePushService.pushAsyncNoticeMessage(content, appId, cids
                    .iterator().next(), channelId, title);
            log.info("push result:" + ret.toString());
		}
	}

}
