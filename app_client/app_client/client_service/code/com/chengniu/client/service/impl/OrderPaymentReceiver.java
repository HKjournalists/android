package com.chengniu.client.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.chengniu.client.service.BaoxianOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kplus.orders.rpc.dto.OrdersDTO;
import com.kplus.orders.rpc.service.OrdersRPCService;

/**
 * 接收订单支付结果通知
 *
 * 要求做到幂等 Created by haiyang on 15/6/30.
 */
@Service("orderPaymentReceiver")
@Scope("singleton")
public class OrderPaymentReceiver {
	private final Logger logger = LoggerFactory
			.getLogger(OrderPaymentReceiver.class);

	@Value("${ONS.payment.topic}")
	private String TOPIC;
	@Value("${ONS.payment.consumer.id}")
	private String CONSUMER_ID;
	@Value("${ONS.payment.consumer.access.key}")
	private String ACCESS_KEY;
	@Value("${ONS.payment.consumer.secret.key}")
	private String SECRET_KEY;

	@Resource(name = "ordersRPCService")
	private OrdersRPCService ordersRPCService;

	@Autowired
	private BaoxianOrderService baoxianOrderService;

	private final static Gson gson = new GsonBuilder().disableHtmlEscaping()
			.setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public OrderPaymentReceiver() {
		logger.info("loading");
	}

	private MessageListener messageListener = new MessageListener() {
		@Override
		public Action consume(Message message, ConsumeContext consumeContext) {
			PayOrderMessage msg;
			String msgContent;
			try {
				msgContent = new String(message.getBody(), "utf-8");
				msg = gson.fromJson(msgContent, PayOrderMessage.class);
			} catch (UnsupportedEncodingException e) {
				logger.error("Action:订单支付通知" + "\nResult: 失败" + "\nMessage:"
						+ message + "\nCause: 解析失败" + e.getMessage() + "\n");
				return Action.CommitMessage;
			}

			// 过滤掉非保养订单
			if (!isBusinessOrder(msg.getOrderType())) {
				return Action.CommitMessage;
			}

			logger.info("订单支付通知:" + "\nONS: " + message + "\nsg: " + msgContent);

			OrdersDTO formOrder = ordersRPCService.queryById(msg.getOrderId());
			if (formOrder != null) {
				baoxianOrderService.disposeOrderPayment(
						formOrder.getOrderNum(), 0,
						String.valueOf(msg.getPayType()),
						true);
			} else {
				logger.info("支付回调,无此订单, payNum:[" + msg.getTradeNo() + "]");
			}

			return Action.CommitMessage;
		}
	};

	private boolean isBusinessOrder(int orderType) {
		return orderType == 8;
	}

	@PostConstruct
	public void initialize() {
		logger.info("Initialize Order Payment Receiver");

		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ConsumerId, CONSUMER_ID);
		properties.put(PropertyKeyConst.AccessKey, ACCESS_KEY);
		properties.put(PropertyKeyConst.SecretKey, SECRET_KEY);
		properties.put(PropertyKeyConst.MessageModel,
				PropertyValueConst.CLUSTERING);
		try {
			Consumer consumer = ONSFactory.createConsumer(properties);
			consumer.subscribe(TOPIC, "*", messageListener);
			consumer.start();
			logger.debug("Order Payment Consumer Started.");
		} catch (Exception e) {
			logger.debug("Order Payment Consumer Start Failed."
					+ e.getMessage());
		}
	}

	static final class PayOrderMessage {
		private long orderId;
		private int orderType;
		private String tradeNo;

		private int payType;
		private Timestamp payTime;
		private String seqNo;
		private String buyerEmail;
		private float balance; /* 使用的余额 */
		private float amount; /* 支付的金额 */
		private int orderStatus;

		public long getOrderId() {
			return orderId;
		}

		public void setOrderId(long orderId) {
			this.orderId = orderId;
		}

		public int getOrderType() {
			return orderType;
		}

		public void setOrderType(int orderType) {
			this.orderType = orderType;
		}

		public int getPayType() {
			return payType;
		}

		public String getTradeNo() {
			return tradeNo;
		}

		public void setTradeNo(String tradeNo) {
			this.tradeNo = tradeNo;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		public Timestamp getPayTime() {
			return payTime;
		}

		public void setPayTime(Timestamp payTime) {
			this.payTime = payTime;
		}

		public String getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(String seqNo) {
			this.seqNo = seqNo;
		}

		public String getBuyerEmail() {
			return buyerEmail;
		}

		public void setBuyerEmail(String buyerEmail) {
			this.buyerEmail = buyerEmail;
		}

		public float getBalance() {
			return balance;
		}

		public void setBalance(float balance) {
			this.balance = balance;
		}

		public float getAmount() {
			return amount;
		}

		public void setAmount(float amount) {
			this.amount = amount;
		}

		public int getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(int orderStatus) {
			this.orderStatus = orderStatus;
		}
	}
}
