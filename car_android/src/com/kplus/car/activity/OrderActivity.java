package com.kplus.car.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.payment.PayResultLisenter;
import com.kplus.car.payment.PaymentUtil;
import com.kplus.car.model.Account;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.OrderAgainst;
import com.kplus.car.model.OrderDetail;
import com.kplus.car.model.OrderInfo;
import com.kplus.car.model.OrderPayment;
import com.kplus.car.model.Refund;
import com.kplus.car.model.RefundInfo;
import com.kplus.car.model.UserInfo;
import com.kplus.car.model.json.GetOrderDetailJson;
import com.kplus.car.model.json.GetRefundInfoJson;
import com.kplus.car.model.response.CloseOrderResponse;
import com.kplus.car.model.response.GetOrderDetailResponse;
import com.kplus.car.model.response.GetRefundInfoResponse;
import com.kplus.car.model.response.GetStringValueResponse;
import com.kplus.car.model.response.GetUserInfoResponse;
import com.kplus.car.model.response.request.CloseOrderRequest;
import com.kplus.car.model.response.request.GetOrderDetailRequest;
import com.kplus.car.model.response.request.GetRefundInfoRequest;
import com.kplus.car.model.response.request.GetUserInfoRequest;
import com.kplus.car.model.response.request.OrderPayRequest;
import com.kplus.car.service.UpdateAgainstRecords;
import com.kplus.car.util.BroadcastReceiverUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.util.WebUtils;
import com.kplus.car.widget.BaseLoadList;
import com.kplus.car.wxapi.WXPayListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends BaseActivity implements OnClickListener, WXPayListener, PayResultLisenter, BroadcastReceiverUtil.BroadcastListener
{
	private static final int STATUS_PAY_ON_LINE = 1;
	private static final int STATUS_UPLOAD_LIENCE = 2;
	private static final int STATUS_WAIT_DISPOSE = 3;
	private static final int STATUS_DISPOSED_SUCCEED = 4;
	private static final int STATUS_DISPOSED_FAIL = 5;
	private static final int REQUEST_FOR_CLOSE_ORDER = 1;
	private static final int REQUEST_FOR_ORDER_INFO_EDIT = 2;
	
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View rightView;
	private ImageView ivRight;	
	private View orderTitleRoot;
	private TextView tvSubmitOrder;
	private ImageView ivNowSubmitOrder;
	private TextView tvNowSubmitOrder;
	private TextView tvPayOnline;
	private ImageView ivHavePayOnline,ivNowPayOnline;
	private TextView tvHavePayOnline,tvNowPayOnline;
	private TextView tvUploadLicence;
	private ImageView ivHaveUploadLicence,ivNowUploadLicence;
	private TextView tvHaveUploadLicence,tvNowUploadLicence;
	private TextView tvWaitDispose;
	private ImageView ivNowWaitDispose;
	private TextView tvNowWaitDispose;
	private View failReasonView;
	private TextView tvfailReason;
	private TextView tvOrderNumber;
	private TextView tvOrderState;
	private TextView tvOrderDate,tvOrderRefundDate, tvOrderRefundMessage, tvOrderRefundMessage1;
	private TextView tvOrderFee;
	private TextView tvOrderFine, tvOrderFineDeduction, tvOrderFineDeductionLabel;
	private TextView tvOrderServiceCharge;
	private TextView tvOrderServiceChargeDeduction, tvOrderServiceChargeDeductionLabel;
	private Button closeorderButton, orderButton,fillAccountButton,refundChange;
	private View orderPayDateView;
	private TextView tvOrderPayDate;
	private View orderCompleteDateView;
	private TextView tvOrderCompleteDate;
	private CheckBox use_online_pay_check;
	private TextView pay_money_text, pay_money_label_text, pay_balance_label_text, pay_balance_text;
	private View payOnlineCheckView;
	private View use_balance_check_view;
	private CheckBox use_balance_check;
	private TextView use_balance_text;
	private ProgressBar use_balance_pb;	
	private TextView tvListInfo;
	private ListView lvVehicleAgainst;	
	private View rlSelectPayType;
	private TextView timeRemindView, payTimeRemind,cardNumber,userName;
	private View operateView,orderRefundDateView,orderRefundInfoView,rlSelectPayFill, orderRefundInfoView1;
	private View paymentMethodView,firstTime,secondTime;
	private TextView tvpaymentMethod1, tvpayMoneyMethod1, tvpaymentMethod2, tvpayMoneyMethod2;
	
	private boolean isFirstLoad = true;
	private boolean verifyFail = false;
	private List<AgainstRecord> orderAgainsts;
	private long orderId;
	private int orderStatus;
	private String orderNum;
	private float totalPrice;
	private float orderServiceCharge;
	private float orderSeiviceChargeDeduction;
	private float orderPriceDeduvtion;
	private String vehicleNum;
	private OrderAgainstAdapter adapter;
	private DecimalFormat df;
	private String orderState;
	private String orderDate = "";
	private String orderPayDate = "";
	private String orderRefundDate = "";
	private String failreanson;
	private float fCashbalance;
	private SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd");
	private float againstMoney;
	private String orderCompleteDate;
	private String orderInfoFlag;
	private String drivingImageUrl;
	private String driverImageUrl;
	private String cardImageUrl;
	private String cardImageUrl2;
	private String refundName;
	private String refundCardNumber;
	private Integer refundType;
//	private View orderPayWayView;
	private TextView refundHintMessage;
	private IWXAPI iwxapi;
	private Refund rf;
	private boolean useRefund = true;
	private Handler mHander = new Handler();
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			getOrderDetail();
		}
	};
	private BroadcastReceiver paySuccessReceiver = BroadcastReceiverUtil.createBroadcastReceiver(this);

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		rightView = findViewById(R.id.rightView);
		rightView.setVisibility(View.GONE);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setImageResource(R.drawable.share_titlebar);		
		orderTitleRoot = findViewById(R.id.orderTitleRoot);
		tvSubmitOrder = (TextView) findViewById(R.id.tvSubmitOrder);
		ivNowSubmitOrder = (ImageView) findViewById(R.id.ivNowSubmitOrder);
		tvNowSubmitOrder = (TextView) findViewById(R.id.tvNowSubmitOrder);
		tvPayOnline = (TextView) findViewById(R.id.tvPayOnline);
		ivHavePayOnline = (ImageView) findViewById(R.id.ivHavePayOnline);
		ivNowPayOnline = (ImageView) findViewById(R.id.ivNowPayOnline);
		tvHavePayOnline = (TextView) findViewById(R.id.tvHavePayOnline);
		tvNowPayOnline = (TextView) findViewById(R.id.tvNowPayOnline);
		tvUploadLicence = (TextView) findViewById(R.id.tvUploadLicence);
		ivHaveUploadLicence = (ImageView) findViewById(R.id.ivHaveUploadLicence);
		ivNowUploadLicence = (ImageView) findViewById(R.id.ivNowUploadLicence);
		tvHaveUploadLicence = (TextView) findViewById(R.id.tvHaveUploadLicence);
		tvNowUploadLicence = (TextView) findViewById(R.id.tvNowUploadLicence);
		tvWaitDispose = (TextView) findViewById(R.id.tvWaitDispose);
		ivNowWaitDispose = (ImageView) findViewById(R.id.ivNowWaitDispose);
		tvNowWaitDispose = (TextView) findViewById(R.id.tvNowWaitDispose);		
		failReasonView = findViewById(R.id.failReasonView);
		tvfailReason = (TextView) findViewById(R.id.tvfailReason);		
		tvOrderNumber = (TextView) findViewById(R.id.tvOrderNumber);
		tvOrderState = (TextView) findViewById(R.id.tvOrderState);
		tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);
		tvOrderRefundDate = (TextView) findViewById(R.id.tvOrderRefundDate);
		tvOrderFee = (TextView) findViewById(R.id.tvOrderFee);
		tvOrderFine = (TextView) findViewById(R.id.orderFine);
		tvOrderFineDeduction = (TextView) findViewById(R.id.orderFineDeduction);
		tvOrderFineDeductionLabel = (TextView) findViewById(R.id.orderFineDeductionLabel);
		tvOrderServiceCharge = (TextView) findViewById(R.id.orderServiceCharge);
		tvOrderServiceChargeDeduction = (TextView) findViewById(R.id.orderServiceChargeDeduction);
		tvOrderServiceChargeDeductionLabel = (TextView) findViewById(R.id.orderServiceChargeDeductionLabel);
		closeorderButton = (Button) findViewById(R.id.closeorderButton);
		orderButton = (Button) findViewById(R.id.orderButton);
		orderPayDateView = findViewById(R.id.orderPayDateView);
		tvOrderPayDate = (TextView) findViewById(R.id.tvOrderPayDate);
		orderCompleteDateView = findViewById(R.id.orderCompleteDateView);
		tvOrderCompleteDate = (TextView) findViewById(R.id.tvOrderCompleteDate);
		use_online_pay_check = (CheckBox) findViewById(R.id.use_online_pay_check);
		pay_money_text = (TextView) findViewById(R.id.pay_money_text);
		pay_money_label_text = (TextView) findViewById(R.id.pay_money_label_text);
		pay_balance_text = (TextView) findViewById(R.id.pay_balance_text);
		pay_balance_label_text = (TextView) findViewById(R.id.pay_balance_label_text);
		payOnlineCheckView = findViewById(R.id.payOnlineCheckView);
		use_balance_check = (CheckBox) findViewById(R.id.use_balance_check);
		use_balance_text = (TextView) findViewById(R.id.use_balance_text);
		use_balance_check_view = findViewById(R.id.use_balance_check_view);
		use_balance_pb = (ProgressBar) findViewById(R.id.use_balance_pb);		
		tvListInfo = (TextView) findViewById(R.id.tvListInfo);
		lvVehicleAgainst = (ListView) findViewById(R.id.lvVehicleAgainst);		
		rlSelectPayType = findViewById(R.id.rlSelectPayType);
		timeRemindView = (TextView) findViewById(R.id.timeRemindView);
		payTimeRemind = (TextView) findViewById(R.id.payTimeRemind);
		operateView = findViewById(R.id.operateView);
		rlSelectPayFill = findViewById(R.id.rlSelectPayFill);		
		orderRefundDateView = findViewById(R.id.orderRefundDateView);
		orderRefundInfoView = findViewById(R.id.orderRefundInfoView);
		paymentMethodView = findViewById(R.id.paymentMethodView);
		tvpaymentMethod1 = (TextView) findViewById(R.id.tvpaymentMethod1);
		tvpayMoneyMethod1 = (TextView) findViewById(R.id.tvpayMoneyMethod1);
		tvpaymentMethod2 = (TextView) findViewById(R.id.tvpaymentMethod2);
		tvpayMoneyMethod2 = (TextView) findViewById(R.id.tvpayMoneyMethod2);
		fillAccountButton = (Button) findViewById(R.id.fillAccountButton);
		cardNumber = (TextView) findViewById(R.id.cardNumber);
		userName = (TextView) findViewById(R.id.userName);
		refundChange = (Button) findViewById(R.id.refundChange);
		firstTime = findViewById(R.id.firstTime);
		secondTime = findViewById(R.id.secondTime);
		refundHintMessage = (TextView) findViewById(R.id.refundHintMessage);
		tvOrderRefundMessage = (TextView) findViewById(R.id.tvOrderRefundMessage);
		tvOrderRefundMessage1 = (TextView) findViewById(R.id.tvOrderRefundMessage1);
		orderRefundInfoView1 = findViewById(R.id.orderRefundInfoView1);
	}

	@Override
	protected void loadData()
	{
		df = new DecimalFormat("#.##");
		df.setMinimumFractionDigits(2);
		orderId = getIntent().getLongExtra("orderId", 0);
		iwxapi = WXAPIFactory.createWXAPI(OrderActivity.this, KplusConstants.WECHAT_APPID, true);
		iwxapi.registerApp(KplusConstants.WECHAT_APPID);
		new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				try{
					return MobclickAgent.getConfigParams(OrderActivity.this, "useRefund");
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(String result) {
				if(!StringUtils.isEmpty(result) && result.equals("1")){
					useRefund = true;
				}
				else
					useRefund = false;
				new GetUserInfoTask().execute();
			}
		}.execute();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		orderId = intent.getLongExtra("orderId", 0);
		new GetUserInfoTask().execute();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		BroadcastReceiverUtil.registerReceiver(this, BroadcastReceiverUtil.ACTION_PAY_SUCCESS, paySuccessReceiver);
		if (isFirstLoad) {
			showloading(true);
			getOrderDetail();
		}
		rf = null;
		new GetRefundInfoTask().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		BroadcastReceiverUtil.unRegisterReceiver(this, paySuccessReceiver);
	}

	@Override
	protected void setListener()
	{	
		refundChange.setOnClickListener(this);
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		closeorderButton.setOnClickListener(this);
		orderButton.setOnClickListener(this);
		fillAccountButton.setOnClickListener(this);
		use_online_pay_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				float totalPriceTemp = totalPrice - orderSeiviceChargeDeduction - orderPriceDeduvtion;
				if(isChecked){
					orderButton.setEnabled(true);
					if(!(totalPriceTemp > fCashbalance) || Math.abs(fCashbalance) < 0.0001){
						use_balance_check.setChecked(false);
						pay_balance_label_text.setVisibility(View.GONE);
					}
					if(use_balance_check.isChecked()){
						pay_money_label_text.setVisibility(View.VISIBLE);
						pay_money_text.setText(df.format(totalPriceTemp - fCashbalance));
						pay_balance_label_text.setVisibility(View.VISIBLE);
						pay_balance_text.setText(df.format(fCashbalance));
					}
					else{
						pay_money_label_text.setVisibility(View.VISIBLE);
						pay_money_text.setText(df.format(totalPriceTemp > 0 ? totalPriceTemp : 0.01));
						pay_balance_label_text.setVisibility(View.GONE);
						pay_balance_text.setText("");
					}
				}
				else{
					if(use_balance_check.isChecked()){
						if(totalPriceTemp > fCashbalance){
							use_online_pay_check.setChecked(true);
						}
						else{
							pay_money_label_text.setVisibility(View.GONE);
							pay_money_text.setText("");
							pay_balance_label_text.setVisibility(View.VISIBLE);
							pay_balance_text.setText(df.format(totalPriceTemp > 0 ? totalPriceTemp : 0.01));
							orderButton.setEnabled(true);
						}
					}
					else{
						use_online_pay_check.setChecked(true);
					}
				}
			}
		});
		use_balance_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				float totalPriceTemp = totalPrice - orderSeiviceChargeDeduction - orderPriceDeduvtion;
				if (isChecked) {
					if (Math.abs(fCashbalance) < 0.0001)
						use_balance_check.setChecked(false);
					else {
						if (!(totalPriceTemp > fCashbalance)) {
							use_online_pay_check.setChecked(false);
							pay_money_label_text.setVisibility(View.GONE);
						}
						if (use_online_pay_check.isChecked()) {
							orderButton.setEnabled(true);
							pay_money_label_text.setVisibility(View.VISIBLE);
							pay_money_text.setText(df.format(totalPriceTemp - fCashbalance));
							pay_balance_label_text.setVisibility(View.VISIBLE);
							pay_balance_text.setText(df.format(fCashbalance));
						} else {
							orderButton.setEnabled(true);
							pay_money_label_text.setVisibility(View.GONE);
							pay_money_text.setText("");
							pay_balance_label_text.setVisibility(View.VISIBLE);
							pay_balance_text.setText(df.format(totalPriceTemp > 0 ? totalPriceTemp : 0.01));
						}
					}
				} else {
					if (use_online_pay_check.isChecked()) {
						orderButton.setEnabled(true);
						pay_money_label_text.setVisibility(View.VISIBLE);
						pay_money_text.setText(df.format(totalPriceTemp > 0 ? totalPriceTemp : 0.01));
						pay_balance_label_text.setVisibility(View.GONE);
						pay_balance_text.setText("");
					} else {
						use_balance_check.setChecked(true);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		if(v.equals(leftView)){
			finish();
		}else if(v.equals(fillAccountButton)){
			Intent intent = new Intent(OrderActivity.this,RefundActivity.class);
			startActivity(intent);
		}
		else if(v.equals(refundChange)){
			Intent intent = new Intent(OrderActivity.this,RefundActivity.class);
			if(rf != null)
				intent.putExtra("refund", rf);
			startActivity(intent);
		}
		else if(v.equals(rightView)){
			Intent intent = new Intent(OrderActivity.this, Share.class);
			intent.putExtra("type", KplusConstants.SHARE_ORDER);
			startActivity(intent);
		}
		else if(v.equals(closeorderButton)){
			EventAnalysisUtil.onEvent(OrderActivity.this, "pageJiaofei_closeOrder  ", "在线缴费订单页面关闭订单", null);
			Intent intent = new Intent(OrderActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
			intent.putExtra("message", "是否要关闭订单？");
			startActivityForResult(intent, REQUEST_FOR_CLOSE_ORDER);
		}
		else if(v.equals(orderButton)){
			int sta = (Integer) orderButton.getTag();
			if(sta == STATUS_PAY_ON_LINE){
				EventAnalysisUtil.onEvent(OrderActivity.this, "pageJiaofei_conformOrder ", "在线缴费订单页面确认，前往付款", null);
				if(use_online_pay_check.isChecked()){
					if(use_balance_check.isChecked()){
						if(rlSelectPayType.getVisibility() == View.GONE)
							rlSelectPayType.setVisibility(View.VISIBLE);
						else if(rlSelectPayType.getVisibility() == View.VISIBLE)
							rlSelectPayType.setVisibility(View.GONE);
					}
					else{
						if(rlSelectPayType.getVisibility() == View.GONE)
							rlSelectPayType.setVisibility(View.VISIBLE);
						else if(rlSelectPayType.getVisibility() == View.VISIBLE)
							rlSelectPayType.setVisibility(View.GONE);
					}					
				}
				else{
					aliPayRequest(KplusConstants.BALANCE_PAY);
					JSONObject prop = new JSONObject();
					try{
						prop.put("amount", totalPrice-orderSeiviceChargeDeduction - orderPriceDeduvtion);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			else if(sta == STATUS_UPLOAD_LIENCE){
				Intent intent = new Intent(this, OrderInfoAddActivity.class);
				intent.putExtra("orderId", orderId);
				intent.putExtra("isAdd", true);
				startActivityForResult(intent, REQUEST_FOR_ORDER_INFO_EDIT);
			}
			else if(sta == STATUS_WAIT_DISPOSE){
				Intent intent = new Intent(this, OrderInfoAddActivity.class);
				intent.putExtra("orderId", orderId);
				startActivityForResult(intent, REQUEST_FOR_ORDER_INFO_EDIT);
			}
		}
	}
	
	private void setStatus(int sta){
		String strTemp = mApplication.dbCache.containOrderId(orderId);
		switch(sta){
			case STATUS_PAY_ON_LINE:
				orderTitleRoot.setVisibility(View.VISIBLE);
				tvSubmitOrder.setTextColor(getResources().getColor(R.color.daze_blue3));
				ivNowSubmitOrder.setVisibility(View.GONE);
				tvNowSubmitOrder.setVisibility(View.GONE);
				tvPayOnline.setTextColor(getResources().getColor(R.color.daze_orangered4));
				ivNowPayOnline.setVisibility(View.VISIBLE);
				ivHavePayOnline.setVisibility(View.VISIBLE);
				tvNowPayOnline.setVisibility(View.VISIBLE);
				tvHavePayOnline.setVisibility(View.VISIBLE);
				tvUploadLicence.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
				tvNowUploadLicence.setVisibility(View.GONE);
				ivHaveUploadLicence.setVisibility(View.GONE);
				ivNowUploadLicence.setVisibility(View.GONE);
				tvHaveUploadLicence.setVisibility(View.GONE);
				tvWaitDispose.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
				ivNowWaitDispose.setVisibility(View.GONE);
				tvNowWaitDispose.setVisibility(View.GONE);
				failReasonView.setVisibility(View.GONE);
				orderState = "待支付";
				tvOrderState.setText(orderState);
				tvOrderState.setTextColor(getResources().getColor(R.color.daze_orangered4));
				closeorderButton.setVisibility(View.VISIBLE);
				orderButton.setVisibility(View.VISIBLE);
				orderButton.setText("确认，前往支付");
				orderButton.setTag(STATUS_PAY_ON_LINE);
				rightView.setVisibility(View.GONE);
				payOnlineCheckView.setVisibility(View.VISIBLE);
				use_balance_check_view.setVisibility(View.VISIBLE);
				operateView.setVisibility(View.VISIBLE);
				timeRemindView.setVisibility(View.VISIBLE);
				payTimeRemind.setVisibility(View.VISIBLE);
				paymentMethodView.setVisibility(View.GONE);
				orderCompleteDateView.setVisibility(View.GONE);
				orderRefundDateView.setVisibility(View.GONE);
				orderRefundInfoView.setVisibility(View.GONE);
				orderRefundInfoView1.setVisibility(View.GONE);
				if(strTemp != null && strTemp.equals("true")){
					tvOrderState.setText("支付确认中");
					closeorderButton.setVisibility(View.GONE);
					orderButton.setVisibility(View.GONE);
					mHander.postDelayed(runnable, 10000);
				}
				break;
			case STATUS_UPLOAD_LIENCE:
				orderTitleRoot.setVisibility(View.VISIBLE);
				tvSubmitOrder.setTextColor(getResources().getColor(R.color.daze_blue3));
				ivNowSubmitOrder.setVisibility(View.GONE);
				tvNowSubmitOrder.setVisibility(View.GONE);
				tvPayOnline.setTextColor(getResources().getColor(R.color.daze_blue3));
				ivNowPayOnline.setVisibility(View.GONE);
				ivHavePayOnline.setVisibility(View.VISIBLE);
				tvHavePayOnline.setVisibility(View.VISIBLE);
				tvNowPayOnline.setVisibility(View.GONE);
				tvUploadLicence.setTextColor(getResources().getColor(R.color.daze_orangered4));
				tvNowUploadLicence.setVisibility(View.VISIBLE);
				ivHaveUploadLicence.setVisibility(View.VISIBLE);
				ivNowUploadLicence.setVisibility(View.VISIBLE);
				tvHaveUploadLicence.setVisibility(View.VISIBLE);
				tvWaitDispose.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
				ivNowWaitDispose.setVisibility(View.GONE);
				tvNowWaitDispose.setVisibility(View.GONE);
				if(verifyFail){
					failReasonView.setVisibility(View.VISIBLE);
					tvfailReason.setText("证件审核失败：" + (StringUtils.isEmpty(failreanson) ? "" : failreanson));
				}
				else
					failReasonView.setVisibility(View.GONE);
				tvOrderState.setText(orderState);
				tvOrderState.setTextColor(getResources().getColor(R.color.daze_blue3));
				closeorderButton.setVisibility(View.GONE);
				if(StringUtils.isEmpty(orderInfoFlag) || orderInfoFlag.equals("0000")){
					orderButton.setVisibility(View.GONE);
				}
				else{
					orderButton.setVisibility(View.VISIBLE);
					orderButton.setText("上传相关证件");
					orderButton.setTag(STATUS_UPLOAD_LIENCE);
				}
				rightView.setVisibility(View.GONE);
				payOnlineCheckView.setVisibility(View.GONE);
				use_balance_check_view.setVisibility(View.GONE);
				operateView.setVisibility(View.VISIBLE);
				timeRemindView.setVisibility(View.GONE);
				payTimeRemind.setVisibility(View.GONE);
				orderCompleteDateView.setVisibility(View.GONE);
				orderRefundDateView.setVisibility(View.GONE);
				orderRefundInfoView.setVisibility(View.GONE);
				orderRefundInfoView1.setVisibility(View.GONE);
				if(strTemp != null && strTemp.equals("true"))
					mApplication.dbCache.deleteOrderId(orderId);
				break;
			case STATUS_WAIT_DISPOSE:
				orderTitleRoot.setVisibility(View.VISIBLE);
				tvSubmitOrder.setTextColor(getResources().getColor(R.color.daze_blue3));
				ivNowSubmitOrder.setVisibility(View.GONE);
				tvNowSubmitOrder.setVisibility(View.GONE);
				tvPayOnline.setTextColor(getResources().getColor(R.color.daze_blue3));
				ivNowPayOnline.setVisibility(View.GONE);
				ivHavePayOnline.setVisibility(View.VISIBLE);
				tvHavePayOnline.setVisibility(View.VISIBLE);
				tvNowPayOnline.setVisibility(View.GONE);
				tvUploadLicence.setTextColor(getResources().getColor(R.color.daze_blue3));
				tvNowUploadLicence.setVisibility(View.GONE);
				ivHaveUploadLicence.setVisibility(View.VISIBLE);
				ivNowUploadLicence.setVisibility(View.GONE);
				tvHaveUploadLicence.setVisibility(View.VISIBLE);
				tvWaitDispose.setTextColor(getResources().getColor(R.color.daze_orangered4));
				ivNowWaitDispose.setVisibility(View.VISIBLE);
				tvNowWaitDispose.setVisibility(View.VISIBLE);
				failReasonView.setVisibility(View.GONE);
				tvOrderState.setText(orderState);
				tvOrderState.setTextColor(getResources().getColor(R.color.daze_orangered4));
				closeorderButton.setVisibility(View.GONE);
				if(StringUtils.isEmpty(orderInfoFlag) || orderInfoFlag.equals("0000")){
					orderButton.setVisibility(View.GONE);
				}
				else if(StringUtils.isEmpty(drivingImageUrl) && StringUtils.isEmpty(driverImageUrl) && StringUtils.isEmpty(cardImageUrl) && StringUtils.isEmpty(cardImageUrl2)){
					orderButton.setVisibility(View.GONE);
				}
				else{
					orderButton.setVisibility(View.VISIBLE);
					orderButton.setText("查看已提交证件信息");
					orderButton.setTag(STATUS_WAIT_DISPOSE);
				}
				rightView.setVisibility(View.GONE);
				payOnlineCheckView.setVisibility(View.GONE);
				use_balance_check_view.setVisibility(View.GONE);
				operateView.setVisibility(View.VISIBLE);
				timeRemindView.setVisibility(View.GONE);
				payTimeRemind.setVisibility(View.GONE);
				orderCompleteDateView.setVisibility(View.GONE);
				if(orderState.equals("待退款")){
					operateView.setVisibility(View.GONE);
				}
				orderRefundDateView.setVisibility(View.GONE);
				orderRefundInfoView.setVisibility(View.GONE);
				orderRefundInfoView1.setVisibility(View.GONE);
				if(strTemp != null && strTemp.equals("true"))
					mApplication.dbCache.deleteOrderId(orderId);
				break;
			case STATUS_DISPOSED_SUCCEED:
				orderTitleRoot.setVisibility(View.GONE);
				failReasonView.setVisibility(View.GONE);
				tvOrderState.setText(orderState);
				tvOrderState.setTextColor(getResources().getColor(R.color.daze_black2));
				closeorderButton.setVisibility(View.GONE);
				orderButton.setVisibility(View.GONE);
				rightView.setVisibility(View.VISIBLE);
				payOnlineCheckView.setVisibility(View.GONE);
				use_balance_check_view.setVisibility(View.GONE);
				operateView.setVisibility(View.GONE);
				orderCompleteDateView.setVisibility(View.VISIBLE);
				tvOrderCompleteDate.setText(orderCompleteDate);
				orderRefundDateView.setVisibility(View.GONE);
				orderRefundInfoView.setVisibility(View.GONE);
				orderRefundInfoView1.setVisibility(View.GONE);
				if(orderState.equals("已退款")){
					orderRefundDateView.setVisibility(View.VISIBLE);
					orderRefundInfoView.setVisibility(View.VISIBLE);
					orderRefundInfoView1.setVisibility(View.VISIBLE);
				}
				if(strTemp != null && strTemp.equals("true"))
					mApplication.dbCache.deleteOrderId(orderId);
				break;
			case STATUS_DISPOSED_FAIL:
				orderTitleRoot.setVisibility(View.GONE);
				failReasonView.setVisibility(View.GONE);
				tvOrderState.setText(orderState);
				tvOrderState.setTextColor(getResources().getColor(R.color.daze_load_normal_end));
				closeorderButton.setVisibility(View.GONE);
				orderButton.setVisibility(View.GONE);
				rightView.setVisibility(View.GONE);
				payOnlineCheckView.setVisibility(View.GONE);
				use_balance_check_view.setVisibility(View.GONE);
				operateView.setVisibility(View.GONE);
				orderCompleteDateView.setVisibility(View.VISIBLE);
				tvOrderCompleteDate.setText(orderCompleteDate);
				orderRefundDateView.setVisibility(View.GONE);
				orderRefundInfoView.setVisibility(View.GONE);
				orderRefundInfoView1.setVisibility(View.GONE);
				if(orderState.equals("已退款")){
					orderRefundDateView.setVisibility(View.VISIBLE);
					orderRefundInfoView.setVisibility(View.VISIBLE);
					orderRefundInfoView1.setVisibility(View.VISIBLE);
				}
				if(strTemp != null && strTemp.equals("true"))
					mApplication.dbCache.deleteOrderId(orderId);
				break;
				default:
					break;
		}
	}
	
	private void getOrderDetail(){
		new AsyncTask<Void, Void, GetOrderDetailResponse>() {
			protected GetOrderDetailResponse doInBackground(Void... params) {
				GetOrderDetailRequest request = new GetOrderDetailRequest();
				request.setParams(orderId);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetOrderDetailResponse result) {
				showloading(false);
				if (result != null) {
					if (result.getCode() != null && result.getCode() == 0) {
						setData(result.getData());
					} else {
						Toast.makeText(OrderActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
						finish();
					}
				} else {
					Toast.makeText(OrderActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}.execute();
	}
	
	private void setData(GetOrderDetailJson detailJson){
		OrderInfo oi = detailJson.getUserInfo();
		if(oi != null){
			drivingImageUrl = oi.getDrivingLicenseImageUrl();
			driverImageUrl = oi.getDriverLicenseImageUrl();
			cardImageUrl = oi.getCardImageUrl();
			cardImageUrl2 = oi.getCardImageUrl2();
		}
		OrderDetail detail = detailJson.getOrderDetail();
		if (detail == null) {
			Toast.makeText(OrderActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		if(isFirstLoad){
			isFirstLoad = false;
			orderNum = detail.getOrderNum();
			tvOrderNumber.setText(orderNum);
			vehicleNum = detail.getVehicleNum();
			tvTitle.setText(vehicleNum);
			orderAgainsts = new ArrayList<AgainstRecord>();
			List<OrderAgainst> againsts = detail.getAgainsts();
			againstMoney = 0;
			for (OrderAgainst orderAgainst : againsts) {
				againstMoney += orderAgainst.getMoney();
				AgainstRecord arTemp = new AgainstRecord();
				arTemp.setAddress(orderAgainst.getAddress());
				arTemp.setMoney(orderAgainst.getMoney());
				arTemp.setScore(orderAgainst.getScore());
				arTemp.setBehavior(orderAgainst.getBehavior());
				arTemp.setTime(orderAgainst.getTime());
				orderAgainsts.add(arTemp);
			}
			adapter = new OrderAgainstAdapter(this);
			lvVehicleAgainst.setAdapter(adapter);
			tvListInfo.setText("订单含" + orderAgainsts.size() + "条违章(" + vehicleNum + ")");
			orderDate = detail.getOrderTime();
			tvOrderDate.setText(orderDate);
		}
		orderInfoFlag = detail.getOrderInfoFlag();
		orderCompleteDate = detail.getUpdateTime();
		totalPrice = detail.getTotalPrice();
		orderServiceCharge = detail.getServiceCost() == null ? 0 : detail.getServiceCost();
		orderServiceCharge = orderServiceCharge < 0 ? 0.00f : orderServiceCharge;
		tvOrderFine.setText(df.format(againstMoney));
		tvOrderServiceCharge.setText( df.format(orderServiceCharge));
		orderSeiviceChargeDeduction = detail.getBalancePay();
		if(orderSeiviceChargeDeduction < 0)
			orderSeiviceChargeDeduction = 0;
		if(orderSeiviceChargeDeduction > orderServiceCharge)
			orderSeiviceChargeDeduction = orderServiceCharge;
		orderPriceDeduvtion = detail.getCouponPay();
		if(orderPriceDeduvtion < 0)
			orderPriceDeduvtion = 0;
		if(orderPriceDeduvtion > againstMoney)
			orderPriceDeduvtion = againstMoney;
		float totalPriceTemp = totalPrice - orderSeiviceChargeDeduction - orderPriceDeduvtion;
		if(Math.abs(totalPriceTemp) > 0.00001){
			tvOrderFee.setText(df.format(totalPriceTemp));
			pay_money_text.setText(df.format(totalPriceTemp));
		}
		else{
			tvOrderFee.setText("0.01");
			pay_money_text.setText("0.01");
		}
		pay_money_label_text.setVisibility(View.VISIBLE);
		pay_balance_label_text.setVisibility(View.GONE);
		if(orderSeiviceChargeDeduction > 0){
			tvOrderServiceChargeDeduction.setText("-" + df.format(orderSeiviceChargeDeduction));
			tvOrderServiceChargeDeductionLabel.setVisibility(View.VISIBLE);
		}
		else{
			tvOrderServiceChargeDeduction.setText("");
			tvOrderServiceChargeDeductionLabel.setVisibility(View.GONE);
		}
		if(orderPriceDeduvtion > 0){
			tvOrderFineDeduction.setText("-" + df.format(orderPriceDeduvtion));
			tvOrderFineDeductionLabel.setVisibility(View.VISIBLE);
		}
		else{
			tvOrderFineDeduction.setText("");
			tvOrderFineDeductionLabel.setVisibility(View.GONE);
		}
		OrderPayment opm = detailJson.getPayment();
		rlSelectPayFill.setVisibility(View.GONE);
		if(!StringUtils.isEmpty(opm.getCreateTime())){
			paymentMethodView.setVisibility(View.VISIBLE);
			orderPayDateView.setVisibility(View.VISIBLE);
			orderPayDate = detailJson.getPayment().getCreateTime();
			tvOrderPayDate.setText(orderPayDate);
			switch(opm.getPayType().intValue()){
			case KplusConstants.ALI_PAY:
			case KplusConstants.ALI_WEBPAY:
				tvpaymentMethod1.setText("支付宝");
				tvpaymentMethod2.setVisibility(View.GONE);
				tvpayMoneyMethod1.setText("(支付¥" + df.format(opm.getMoney()) + ")");
				tvpayMoneyMethod2.setVisibility(View.GONE);	
				break;
			case KplusConstants.UPOMP_PAY:
			case KplusConstants.UPOMP_WEBPAY:
				tvpaymentMethod1.setText("银联支付");
				tvpaymentMethod2.setVisibility(View.GONE);
				tvpayMoneyMethod1.setText("(支付¥" + df.format(opm.getMoney()) + ")");
				tvpayMoneyMethod2.setVisibility(View.GONE);	
				if(useRefund && detail.getStatus() == 13)
					rlSelectPayFill.setVisibility(View.VISIBLE);
				break;
			case KplusConstants.BALANCE_ALI_PAY:
			case KplusConstants.BALANCE_ALI_WEB:
				tvpaymentMethod1.setText("余额支付");
				tvpaymentMethod2.setText("支付宝");
				tvpaymentMethod2.setVisibility(View.VISIBLE);
				tvpayMoneyMethod1.setText("(支付¥" + df.format(opm.getCash()) + ")");
				tvpayMoneyMethod2.setText("(支付¥" + df.format(opm.getMoney()) + ")");
				tvpayMoneyMethod2.setVisibility(View.VISIBLE);
				break;
			case KplusConstants.BALANCE_UPOMP_PAY:
			case KplusConstants.BALANCE_UPOMP_WEBPAY:
				tvpaymentMethod1.setText("余额支付");
				tvpaymentMethod2.setText("银联支付");
				tvpaymentMethod2.setVisibility(View.VISIBLE);
				tvpayMoneyMethod1.setText("(支付¥" + df.format(opm.getCash()) + ")");
				tvpayMoneyMethod2.setText("(支付¥" + df.format(opm.getMoney()) + ")");
				tvpayMoneyMethod2.setVisibility(View.VISIBLE);
				if(useRefund && detail.getStatus() == 13)
					rlSelectPayFill.setVisibility(View.VISIBLE);
				break;
			case KplusConstants.BALANCE_PAY:
				tvpaymentMethod1.setText("余额支付");
				tvpaymentMethod2.setText("");
				tvpaymentMethod2.setVisibility(View.GONE);
				tvpayMoneyMethod1.setText("(支付¥" + df.format(opm.getCash()) + ")");
				tvpayMoneyMethod2.setText("");
				tvpayMoneyMethod2.setVisibility(View.GONE);
				break;
			}
		}
		else{
			paymentMethodView.setVisibility(View.GONE);
			orderPayDateView.setVisibility(View.GONE);
		}
		switch(detail.getStatus()){
			case 1:
				orderButton.setEnabled(false);
				orderButton.setBackgroundResource(R.drawable.daze_btn_bg_light_gray_2);
				orderButton.setText("暂时无法支付，等待客服联系！");
				break;
			case 2:
				orderStatus = STATUS_PAY_ON_LINE;
				setStatus(orderStatus);
				break;
			case 3:
			case 4:
				orderState = "已支付";
				verifyFail = false;
				orderStatus = STATUS_UPLOAD_LIENCE;
				setStatus(orderStatus);
				break;
			case 6:
				orderState = "审核失败";
				verifyFail = true;
				failreanson = detailJson.getOrderDetail().getDesc();
				orderStatus = STATUS_UPLOAD_LIENCE;
				setStatus(orderStatus);
				break;
			case 5:
				orderState = "待审核";
				orderStatus = STATUS_WAIT_DISPOSE;
				setStatus(orderStatus);
				break;
			case 7:
				orderState = "审核通过";
				orderStatus = STATUS_WAIT_DISPOSE;
				setStatus(orderStatus);
				break;
			case 10:
				orderState = "处理中";
				orderStatus = STATUS_WAIT_DISPOSE;
				setStatus(orderStatus);
				break;
			case 13:
				orderState = "待退款";
				orderStatus = STATUS_WAIT_DISPOSE;
				setStatus(orderStatus);
				break;
			case 11:
				orderState = "处理失败";
				failreanson = detailJson.getOrderDetail().getDesc();
				orderStatus = STATUS_DISPOSED_FAIL;
				setStatus(orderStatus);
				break;
			case 12:
				orderState = "处理成功";
				orderStatus = STATUS_DISPOSED_SUCCEED;
				setStatus(orderStatus);
				break;
			case 14:
				orderState = "已退款";
				orderPayDate = detailJson.getPayment().getCreateTime();
				orderStatus = STATUS_DISPOSED_SUCCEED;
				setStatus(orderStatus);
				RefundInfo ri = detailJson.getRefund();
				if(ri != null){
					refundName = ri.getName();
					refundCardNumber = ri.getAccount();
					refundType = ri.getType();
					if(refundType != null && refundType == 2)
						tvOrderRefundMessage.setText("银行卡退款");
					else
						tvOrderRefundMessage.setText("支付宝退款");
					orderRefundDate = ri.getCreateDatetime();
					if(!StringUtils.isEmpty(orderRefundDate))
						tvOrderRefundDate.setText(orderRefundDate);
					if(!StringUtils.isEmpty(refundName)&&!StringUtils.isEmpty(refundCardNumber))
						tvOrderRefundMessage1.setText(refundCardNumber + "(" + refundName + ")");
				} 
				break;
			case 20:
				orderState = "已关闭";
				orderStatus = STATUS_DISPOSED_SUCCEED;
				setStatus(orderStatus);
				break;
			case -1:
				orderState = "已关闭";
				orderStatus = STATUS_DISPOSED_SUCCEED;
				setStatus(orderStatus);
				break;
		}
	}
	
	public void click(View v){
		if(v.getId() == R.id.btAlipay){
			rlSelectPayType.setVisibility(View.GONE);
			if(use_balance_check.isChecked()) {
//				aliPayRequest(KplusConstants.BALANCE_ALI_PAY);
				aliPayRequest(KplusConstants.BALANCE_OPENTRADE_PAY);
			}
			else {
//				aliPayRequest(KplusConstants.ALI_PAY);
				aliPayRequest(KplusConstants.OPENTRADE_PAY);
			}
		}
		else if(v.getId() == R.id.btYinlian){
			rlSelectPayType.setVisibility(View.GONE);
			if(use_balance_check.isChecked())
				aliPayRequest(KplusConstants.BALANCE_UPOMP_PAY);
			else
				aliPayRequest(KplusConstants.UPOMP_PAY);
		}
		else if(v.getId() == R.id.btWeiXin){
			rlSelectPayType.setVisibility(View.GONE);
			if(use_balance_check.isChecked())
				aliPayRequest(KplusConstants.BALANCE_WECHAT_PAY);
			else
				aliPayRequest(KplusConstants.WECHAT_PAY);
		}
        else if(v.getId() == R.id.btLianlian){
            rlSelectPayType.setVisibility(View.GONE);
            if(use_balance_check.isChecked())
                aliPayRequest(KplusConstants.BALANCE_LIANLIAN_PAY);
            else
                aliPayRequest(KplusConstants.LIANLIAN_PAY);
        }
		else if(v.getId() == R.id.btCancel){
			rlSelectPayType.setVisibility(View.GONE);
		}
	}
	
	private void aliPayRequest(final int payType) {
		if(payType == KplusConstants.BALANCE_PAY){
			showloading(true);
			new AsyncTask<Object, Object, GetStringValueResponse>() {
				@Override
				protected GetStringValueResponse doInBackground(Object... params) {
					OrderPayRequest request = new OrderPayRequest();
					request.setParams(orderId, payType);
					try {
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(GetStringValueResponse result) {
					showloading(false);
					if (result != null) {
						if (result.getCode() == 0) {
//							getOrderDetail();
							mApplication.dbCache.saveOrderId(orderId);
							tvOrderState.setText("支付中");
							closeorderButton.setVisibility(View.GONE);
							orderButton.setVisibility(View.GONE);
							Intent intent = new Intent(OrderActivity.this, AlertDialogActivity.class);
							intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
							intent.putExtra("message", "支付成功");
							startActivity(intent);
							mHander.postDelayed(runnable, 10000);
						} else {
							Toast.makeText(OrderActivity.this,	result.getMsg(), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(OrderActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
					}
				}

			}.execute();
		}
		else {
			PaymentUtil paymentUtil = new PaymentUtil(OrderActivity.this, orderId, findViewById(R.id.page_loading), null, null);
			paymentUtil.setPayResultLisenter(OrderActivity.this);
			paymentUtil.payRequest(payType);
		}
	}

	protected void onActivityResult( int requestCode,int resultCode,Intent data){
		if(requestCode == REQUEST_FOR_CLOSE_ORDER){
			if(resultCode == RESULT_OK){
				new CloseOrderTask().execute();
				return;
			}
		}
		if(requestCode == REQUEST_FOR_ORDER_INFO_EDIT && resultCode == RESULT_OK){
			showloading(true);
			getOrderDetail();
			return;
		}
		if(data == null) {
            return;
        }
		String msg = "";
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
			mApplication.dbCache.saveOrderId(orderId);
			tvOrderState.setText("支付中");
			closeorderButton.setVisibility(View.GONE);
			orderButton.setVisibility(View.GONE);
			mHander.postDelayed(runnable, 10000);
		} else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
		}
//        ToastUtil.TextToast(OrderActivity.this, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
        Intent intent = new Intent(OrderActivity.this, AlertDialogActivity.class);
        intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
        intent.putExtra("message", msg);
        startActivity(intent);
	}

	private void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }
	
	class OrderAgainstAdapter extends BaseLoadList<AgainstRecord>{
		public OrderAgainstAdapter(Activity context){
			super(context);
		}

		@Override
		public void initItem(AgainstRecord it, Map<String, Object> holder)
		{
			TextView tvAgainstDate = (TextView) holder.get("tvAgainstDate");
			TextView tvAgainstScore = (TextView) holder.get("tvAgainstScore");
			TextView tvAgainstMoney = (TextView) holder.get("tvAgainstMoney");
			TextView tvAgainstBehavor = (TextView) holder.get("tvAgainstBehavor");
			TextView tvAgainstAddress = (TextView) holder.get("tvAgainstAddress");
			String timeTemp = "";
			try{
				Date date = sdfold.parse(it.getTime());
				timeTemp = sdfnew.format(date);
			}catch(Exception e){
				e.printStackTrace();
			}
			tvAgainstDate.setText(timeTemp);
			tvAgainstScore.setText(it.getScore() + "分");
			tvAgainstMoney.setText(it.getMoney() + "元");
			if(it.getBehavior() != null && !it.getBehavior().trim().equals("")){
				tvAgainstBehavor.setText(it.getBehavior());
			}
			else{
				tvAgainstBehavor.setText("违反道路交通安全法");
			}
			if(it.getAddress() != null && !it.getAddress().trim().equals("")){
				tvAgainstAddress.setText(it.getAddress());
			}
			else{
				tvAgainstAddress.setText("未知地点");
			}
		}

		@Override
		public Map<String, Object> getHolder(View v)
		{
			Map<String, Object> root = new HashMap<String, Object>();
			TextView tvAgainstDate = (TextView) v.findViewById(R.id.tvAgainstDate);
			TextView tvAgainstScore = (TextView) v.findViewById(R.id.tvAgainstScore);
			TextView tvAgainstMoney = (TextView) v.findViewById(R.id.tvAgainstMoney);
			TextView tvAgainstBehavor = (TextView) v.findViewById(R.id.tvAgainstBehavor);
			TextView tvAgainstAddress = (TextView) v.findViewById(R.id.tvAgainstAddress);
			root.put("tvAgainstDate", tvAgainstDate);
			root.put("tvAgainstScore", tvAgainstScore);
			root.put("tvAgainstMoney", tvAgainstMoney);
			root.put("tvAgainstBehavor", tvAgainstBehavor);
			root.put("tvAgainstAddress", tvAgainstAddress);
			
			return root;
		}

		@Override
		public List<AgainstRecord> executeFirst() throws Exception
		{
			return orderAgainsts;
		}

		@Override
		public int getLayoutId(int index)
		{
			return R.layout.daze_order_against_list_item;
		}

		@Override
		public void showLoading(boolean show)
		{
			if(!show)
				setListViewHeightBasedOnChildren(lvVehicleAgainst);			
		}
	}
	
	class CloseOrderTask extends AsyncTask<Void, Void, CloseOrderResponse>{
		private String errorText = "网络中断，请稍后再试";
		@Override
		protected void onPreExecute() {
			showloading(true);
			super.onPreExecute();
		}

		@Override
		protected CloseOrderResponse doInBackground(Void... params) {
			try{
				CloseOrderRequest request = new CloseOrderRequest();
				request.setParams(orderId, mApplication.getpId());
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				errorText = e.toString();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(CloseOrderResponse result) {
			if(result != null){
				if(result.getCode() != null && result.getCode() == 0){
					if(result.getData().getResult()){
						mHander.postDelayed(new Runnable() {
							@Override
							public void run() {
								getOrderDetail();
							}
						}, 3000);
					}
					else {
						Toast.makeText(OrderActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
						showloading(false);
					}
				}
				else {
					Toast.makeText(OrderActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
					showloading(false);
				}
			}
			else {
				Toast.makeText(OrderActivity.this, errorText, Toast.LENGTH_SHORT).show();
				showloading(false);
			}
			super.onPostExecute(result);
		}
	}
	
	class GetUserInfoTask extends AsyncTask<Void, Void, GetUserInfoResponse>{

		@Override
		protected GetUserInfoResponse doInBackground(Void... params) {
			try{
				GetUserInfoRequest request = new GetUserInfoRequest();
				request.setParams(mApplication.getId());
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(GetUserInfoResponse result) {
			try{
				use_balance_pb.setVisibility(View.GONE);
				if(result != null && result.getCode() != null && result.getCode() == 0){
					UserInfo userInfo = result.getData().getUserInfo();
					Float cashbalance = userInfo.getCashBalance();
					fCashbalance = (cashbalance == null ? 0.00f :cashbalance.floatValue());
					use_balance_text.setText("(余额" + fCashbalance + ")");
					if(cashbalance != null && cashbalance.floatValue() > 0.001){
						use_balance_check.setEnabled(true);
					}

					// 设置用户余额，传到洗车那边
					mApplication.setCarWashingUserBalance(fCashbalance);

					mApplication.dbCache.saveUserInfo(userInfo);
					List<Account> accounts = userInfo.getAccounts();
					if(accounts != null && !accounts.isEmpty())
						mApplication.dbCache.saveAccounts(accounts);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}
	
	@Override
	protected void onDestroy() {
		Intent update = new Intent(OrderActivity.this, UpdateAgainstRecords.class);
		update.putExtra("vehicleNumber", vehicleNum);
		startService(update);
		mHander.removeCallbacks(runnable);
		super.onDestroy();
	}

	@Override
	public void onPaySuccess(BaseResp response) {
		mApplication.setWxPayListener(null);
//		ToastUtil.TextToast(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
		mApplication.dbCache.saveOrderId(orderId);
		tvOrderState.setText("支付中");
		closeorderButton.setVisibility(View.GONE);
		orderButton.setVisibility(View.GONE);
		mHander.postDelayed(runnable, 10000);
		Intent intent = new Intent(OrderActivity.this, AlertDialogActivity.class);
		intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
		intent.putExtra("message", "支付成功");
		startActivity(intent);
	}

	@Override
	public void onPayCancel(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(OrderActivity.this, "支付取消", Toast.LENGTH_SHORT, Gravity.CENTER);
	}

	@Override
	public void onPayFail(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(OrderActivity.this, StringUtils.isEmpty(response.errStr) ? "支付失败" : response.errStr, Toast.LENGTH_SHORT, Gravity.CENTER);
	}
	
	class GetRefundInfoTask extends AsyncTask<Void, Void, GetRefundInfoResponse>{
		@Override
		protected GetRefundInfoResponse doInBackground(Void... params) {//异步线程
			try{
				GetRefundInfoRequest request = new GetRefundInfoRequest();
				request.setParams(mApplication.getId());
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		@Override                    
		protected void onPostExecute(GetRefundInfoResponse result) {
			
			if(result != null && result.getCode() != null && result.getCode() == 0){//判定	
				GetRefundInfoJson data = result.getData();
				if(data != null){
					rf = data.getRefund();
					if(rf != null){
						if(rf.getType() != 0){
							if(rf.getType() == 1){
								refundHintMessage.setText("退款信息已提交，款项将退回至您的支付宝");
							}else if(rf.getType() == 2){
								refundHintMessage.setText("退款信息已提交，款项将退回至您的银行卡");
							}
							cardNumber.setText(rf.getAccount());
							userName.setText("("+rf.getName()+")");
						}
						firstTime.setVisibility(View.GONE);
						secondTime.setVisibility(View.VISIBLE);
					}
					else{
						firstTime.setVisibility(View.VISIBLE);
						secondTime.setVisibility(View.GONE);
					}
				}
			}else{
				firstTime.setVisibility(View.VISIBLE);
				secondTime.setVisibility(View.GONE);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onPaySuccess() {
		mApplication.dbCache.saveOrderId(orderId);
		tvOrderState.setText("支付中");
		closeorderButton.setVisibility(View.GONE);
		orderButton.setVisibility(View.GONE);
		mHander.postDelayed(runnable, 10000);
	}

	@Override
	public void onReceiverBroadcast(Intent data) {
		mHander.removeCallbacks(runnable);
		getOrderDetail();
	}
}
