package com.kplus.car.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.Response;
import com.kplus.car.comm.TaskInfo;
import com.kplus.car.fragment.AdvertFragment;
import com.kplus.car.model.Advert;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.Coupon;
import com.kplus.car.model.CouponList;
import com.kplus.car.model.ImageNames;
import com.kplus.car.model.Order;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAddResult;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.model.json.AuthDetailJson;
import com.kplus.car.model.json.CouponListJson;
import com.kplus.car.model.json.GetAgainstRecordListJson;
import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.model.response.GetAgainstRecordListResponse;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.GetClientLicenceCountResponse;
import com.kplus.car.model.response.GetPriceValueResponse;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.GetUserInviteContentResponse;
import com.kplus.car.model.response.GetValidCouponListResponse;
import com.kplus.car.model.response.OrderAddResponse;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.AgainstReportRequest;
import com.kplus.car.model.response.request.GetAdvertDataRequest;
import com.kplus.car.model.response.request.GetAgainstRecordListRequest;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.GetClientLicenceCountRequest;
import com.kplus.car.model.response.request.GetPriceValueRequest;
import com.kplus.car.model.response.request.GetUserInviteContentRequest;
import com.kplus.car.model.response.request.GetValidCouponListResquest;
import com.kplus.car.model.response.request.OrderAddRequest;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.model.response.request.VehicleDelRequest;
import com.kplus.car.model.response.request.VehicleRoportRequest;
import com.kplus.car.service.AsyncTaskService;
import com.kplus.car.service.UpdateAgainstRecords;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.BaseLoadList;
import com.kplus.car.widget.CashSelectAdapter;
import com.kplus.car.widget.ImageProgress;
import com.kplus.car.widget.antistatic.spinnerwheel.AbstractWheel;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelChangedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelClickedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;
import com.kplus.car.widget.pulltorefresh.library.LoadingLayoutProxy;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 51查违章首页
 * 
 * @author suyilei
 * 
 */
public class VehicleDetailActivity extends BaseActivity implements OnClickListener/*, OnTouchListener */{
	private static final int DELETE_REQUEST = 0x01;
	private static final int PHONE_REGIST_REQUEST = 0x02;
	private static final int SHOW_MSG_REQUEST = 0x03;
	private static final int EDIT_VEHICLE_REQUEST = 0x04;

	private View leftView;
	private ImageView ivLeft;
	private View rightView;
	private ImageView ivRight;
	private TextView tvTitle;
	private TextView tvSecondTitle;
	private View menuLayout;
	private View menuBlankLayout;
	private View editBtn;
	private View modifyBtn;
	private PullToRefreshListView mPullRefreshScrollView;
	private ListView listView;
	private TextView moneyText;
	private TextView scoreText;
	private TextView newNumText;
	private View listEmpty;
	private View chargingView;
	private TextView tvRecordNumber, tvRecordMoney;
	private Button btPay;
	private View orderTitleView, orderSubmitView;
	private View blankView;
	private TextView order_submit_selected_num_text;
	private TextView order_submit_money_text, order_submit_reduce_money_text;
	private TextView order_submit_price_text, order_submit_reduce_price_text;
	private ProgressBar order_submit_total_price_loading, order_submit_service_price_loading, fineCashUseInfo_loading, serviceChargeCashUseInfo_loading;
	private TextView order_submit_total_price_text;
	private TextView tvSubmitCommit;
	private Button shareNoAgainst;
	private TextView tvPayMemberNumber;
	private View fineCashView, serviceChargeCashView;
	private TextView tvFineCashUseInfo, tvServiceChargeCashUseInfo;
	private View cashSelectedView;
	private AbstractWheel cashView;
	private TextView tvUnuseCash, tvUseCash;
	private View cashBlankView;
	private View imageShare;
	private View updateInfoView;
	private TextView tvPreUpdateTime;
	private View ivRefreshAgainstRecords;
	private View vehicleErrorView;
	private TextView tvVehicleError;
	private Button btCorrectVehicleInfo;
	private View refreshingView, refreshingView180;
	private ImageProgress imageRefreshProgress, imageRefreshProgress180;
	private TextView tvRefreshingProgress, tvRefreshingProgress180;
	private View correctVehicleErrorLayout;
	private TextView errorTitle, errorInfo, frame_number_notice, motor_number_notice, owner_id_notice, driver_id_notice;
	private View frame_number_layout, motor_number_layout, owner_id_layout, driver_id_layout;
	private EditText frame_number_view, motor_number_view, owner_id_view, driver_id_view;
	private View tvCorrectNextTime,tvSaveCorrectInfoNow, tvCorrectBlank;
	private View cityUnsupportView;
	private TextView tvCityUnsupported;
	private Button btUnsupportEncourage, btUnsupportRemind;
	private View searchFailView, imCloseNoti;
	private Button btFailEncourage;
	private View free_emergency_layout;
	private TextView free_rescue_num;
	private View correctAganistInfoLayout, money_correct_layout, score_correct_layout, tvCorrectAgainstNextTime, tvSaveCorrectAgainstInfoNow, tvCorrectAgainstBlank;
	private TextView score0,score1,score2,score3,score6,score12;
	private EditText money_correct_view;
	private TextView cashTitle;
	private View transparentbg;
	private View vehicle_owner_layout, driver_name_layout;
	private EditText vehicle_owner_view, driver_name_view;
	private View website_account_layout, website_password_layout, vehicleRegCerNo_layout;
	private EditText website_account_view, website_password_view, vehicleRegCerNo_view;
	private TextView searchFailInfo;
	
	private AgainstRecordListAdapter adapter;
	private List<AgainstRecord> records, recordsChecked;
	private boolean add = false;
	private String vehicleNumber;
	private UserVehicle vehicle;
	private Order order;
	private int payMoney;
	private DecimalFormat df = new DecimalFormat("#.##");
	private float serviceCharge;
	private float totalMoney;
	private long payMemberCount;
	private SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private List<Coupon> fineCouponList, fineCouponListSave, serviceChargeCouponList, serviceChargeCouponListSave;
	private Coupon currentFineCoupon, currentServiceChargeCoupon;
	private CashSelectAdapter cashAdapter;
	private RecordTypeAdapter recordTypeAdapter;
	private boolean isFineCoupon;
	private int currentIndex;
	private int payMoneyReduction = 0;
	private int serviceChargeReduction = 0;
	private Coupon nullCoupon;
	private int shareType;
	private int nPayOnlineNum, nPayOnlineMoney, nDisposingNum, nDisposingMoney;
	private int nProgress;
	private float nStep;
	private int nDegrees;
	private int nFrameLength, nMotorLength, ownerIdLen, driverIdLen;
	private VehicleAuth vehicleAuth;
	private AgainstRecord currentRecord;
	private int nSelfScore = -1;
	private String[] recordsTypes = {"道路违章","城管违章","高速违章","铁路违章"};
	private DisplayImageOptions opt;
	private boolean needVehicleOwner = false;
	private boolean needDriverName = false;
	private boolean needAccountAndPassword = false;
	private boolean needVehicleRegCerNo = false;
	private boolean verifyCodeError = false;
	private Handler progressHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				nDegrees++;
				if(nDegrees < 180){
					tvRefreshingProgress180.setText(100*nDegrees/180 + "%");
					imageRefreshProgress180.setnRadius(16);
					imageRefreshProgress180.setnAngel(nDegrees*2);
					imageRefreshProgress180.postInvalidate();
					Message message = new Message();
					message.what = 1;
					progressHandler.sendMessageDelayed(message, 1000);
				}
				else{
					refreshingView180.setVisibility(View.GONE);
				}
			}
			else if(msg.what == 2){
				nProgress += 3;
				if(nProgress < 100){
					tvRefreshingProgress.setText(nProgress + "%");
					imageRefreshProgress.setnRadius(16);
					imageRefreshProgress.setnAngel(nProgress*3.6f);
					imageRefreshProgress.postInvalidate();
					Message message = new Message();
					message.what = 2;
					progressHandler.sendMessageDelayed(message, 200);
				}
				else{
					refreshingView.setVisibility(View.GONE);
				}
			}
			else if(msg.what == 3){
				nProgress += nStep;
				if(nProgress < 100){
					tvRefreshingProgress.setText(nProgress + "%");
					imageRefreshProgress.setnRadius(16);
					imageRefreshProgress.setnAngel(nProgress*3.6f);
					imageRefreshProgress.postInvalidate();
					Message message = new Message();
					message.what = 3;
					progressHandler.sendMessageDelayed(message, 100);
				}
				else{
					nProgress = 100;
					tvRefreshingProgress.setText(nProgress + "%");
					imageRefreshProgress.setnRadius(16);
					imageRefreshProgress.setnAngel(nProgress*3.6f);
					imageRefreshProgress.postInvalidate();
					Message message = new Message();
					message.what = 4;
					progressHandler.sendMessageDelayed(message, 100);
				}
			}
			else if(msg.what == 4)
				refreshingView.setVisibility(View.GONE);
			else if(msg.what == 5){
				nDegrees += nStep;
				if(nDegrees < 180){
					tvRefreshingProgress180.setText(100*nDegrees/180 + "%");
					imageRefreshProgress180.setnRadius(16);
					imageRefreshProgress180.setnAngel(nDegrees*2);
					imageRefreshProgress180.postInvalidate();
					Message message = new Message();
					message.what = 5;
					progressHandler.sendMessageDelayed(message, 100);
				}
				else{
					nProgress = 180;
					tvRefreshingProgress180.setText(100*nDegrees/180 + "%");
					imageRefreshProgress180.setnRadius(16);
					imageRefreshProgress180.setnAngel(nDegrees*2);
					imageRefreshProgress180.postInvalidate();
					Message message = new Message();
					message.what = 6;
					progressHandler.sendMessageDelayed(message, 100);
				}
			}
			else if(msg.what == 6)
				refreshingView180.setVisibility(View.GONE);
			super.handleMessage(msg);
		}
		
	};
	private BroadcastReceiver aganistRecordsReceiver = new BroadcastReceiver() {		
		@Override
		public void onReceive(Context context, final Intent intent) {
			if(intent != null){
				if(intent.hasExtra("vehicleNumber") && intent.getStringExtra("vehicleNumber") != null){
					if(vehicleNumber.equals(intent.getStringExtra("vehicleNumber"))){
						vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
						progressHandler.removeMessages(1);
						nStep = (180 -nDegrees)/5f;
						Message message = new Message();
						message.what = 5;
						progressHandler.sendMessageDelayed(message, 100);
						progressHandler.postDelayed(new Runnable() {					
							@Override
							public void run() {
								if(vehicle.isHasSearchFail()){
									searchFailView.setVisibility(View.VISIBLE);
									if(intent.getBooleanExtra("verifyCodeError", false)){
										searchFailInfo.setText("验证码错误，查询失败了");
									}
									else{
										searchFailInfo.setText("小牛遇到麻烦了，正在努力解决中...");
									}
								}
								if(vehicle.isHasParamError() || vehicle.isHasRuleError()){
									vehicleErrorView.setVisibility(View.VISIBLE);
									correctVehicleErrorLayout.setVisibility(View.VISIBLE);
									getFrameAndMotorLength();
									showCorrectVehicleInfoView();
								}
								updateUnSupportCity();
								updateUI();
							}
						}, 600);	
					}
				}			
			}
		}
	};
	
	private void updateUnSupportCity(){
		if(vehicle != null){
			String cids = vehicle.getCityId();
			if(StringUtils.isEmpty(cids)){
				List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles();
				String cityNames = null;
				if(cvs != null && !cvs.isEmpty()){
					for(CityVehicle cv : cvs){
						if(!(cv.getValid() != null && cv.getValid() == true)){
							if(cityNames == null)
								cityNames = cv.getName();
							else
								cityNames += ("," + cv.getName());
						}
					}
				}
				if(cityNames != null){
					tvCityUnsupported.setText(cityNames + "违章查询暂未开通");
				}
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		EventAnalysisUtil.onEvent(this, "pageView_jiaofei ", "在线缴费页面浏览量", null);
		vehicleNumber = intent.getStringExtra("vehicleNumber");
		verifyCodeError = intent.getBooleanExtra("verifyCodeError", false);
		if(StringUtils.isEmpty(vehicleNumber)){
			ToastUtil.TextToast(this, "车牌号错误",1500, Gravity.CENTER);
			finish();
			return;
		}
		tvTitle.setText(vehicleNumber);
		vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
		if(vehicle == null){
			ToastUtil.TextToast(this, "车辆信息错误",1500, Gravity.CENTER);
			finish();
			return;
		}
		add = intent.getBooleanExtra("add", false);
		if (!StringUtils.isEmpty(vehicle.getCityName())) {
			tvSecondTitle.setText("查询城市:" + vehicle.getCityName());
			tvSecondTitle.setVisibility(View.VISIBLE);
		}
		long updateTime = 0;
		if(!StringUtils.isEmpty(vehicle.getUpdateTime())){
			try{
				updateTime = Long.parseLong(vehicle.getUpdateTime());
			}
			catch(Exception e){
				updateTime = 0;
				e.printStackTrace();
			}
		}
		if(updateTime == 0)
			updateInfoView.setVisibility(View.GONE);
		else
			updateInfoView.setVisibility(View.VISIBLE);
		final String strTemp = "数据更新时间" + DateUtil.getShowTimeOnInterval(updateTime);
		if(vehicle.getNewRecordNumber() != 0){
			if(vehicle.getNewRecordNumber() == 1)
				tvPreUpdateTime.setText("您有1条违章信息有更新");
			else
				tvPreUpdateTime.setText("您有" + vehicle.getNewRecordNumber() + "条新的违章");
		}
		else{
//			tvPreUpdateTime.setText("恭喜您,没有查询到新的违章");
			tvPreUpdateTime.setText(strTemp);
		}
		tvPreUpdateTime.postDelayed(new Runnable() {			
			@Override
			public void run() {
				tvPreUpdateTime.setText(strTemp);
			}
		}, 5000);
	}

	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_vehicle_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		rightView = findViewById(R.id.rightView);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setImageResource(R.drawable.daze_vehicle_edit_btn);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvSecondTitle = (TextView) findViewById(R.id.tvSecondTitle);
		menuLayout = findViewById(R.id.vehicle_detail_setMenu_layout);
		menuBlankLayout = findViewById(R.id.vehicle_detail_setMenu_blank_Layout);
		editBtn = findViewById(R.id.vehicle_detail_setMenu_edit);
		modifyBtn = findViewById(R.id.vehicle_detail_setMenu_modify);		
		mPullRefreshScrollView = (PullToRefreshListView) findViewById(R.id.vehicle_detail_pull_refresh_scrollview);
		listView = mPullRefreshScrollView.getRefreshableView();
		try{
			((LoadingLayoutProxy)(mPullRefreshScrollView.getLoadingLayoutProxy())).setMyRefreshingLabel("");
			((LoadingLayoutProxy)(mPullRefreshScrollView.getLoadingLayoutProxy())).setMyEndLabel("");
			((LoadingLayoutProxy)(mPullRefreshScrollView.getLoadingLayoutProxy())).setLoadingDrawable(null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView){
				mPullRefreshScrollView.postDelayed(new Runnable() {						
					@Override
					public void run() {
						mPullRefreshScrollView.onRefreshComplete();
					}
				}, 200);
				if(refreshingView.getVisibility() == View.GONE && refreshingView180.getVisibility() == View.GONE && !mApplication.containsTask(vehicleNumber)){
					if(searchFailView.getVisibility() == View.VISIBLE)
						return;
					if(vehicle.isHasParamError()){
						ToastUtil.TextToast(VehicleDetailActivity.this, "车辆信息错误", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(vehicle.isHasParamError()){
						ToastUtil.TextToast(VehicleDetailActivity.this, "车辆信息不符合该城市查询规则", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					nProgress = 0;
					tvRefreshingProgress.setText(nProgress + "%");
					imageRefreshProgress.setnRadius(16);
					imageRefreshProgress.setnAngel(nProgress*3.6f);
					imageRefreshProgress.postInvalidate();
					refreshingView.setVisibility(View.VISIBLE);
					Message message = new Message();
					message.what = 2;
					progressHandler.sendMessageDelayed(message, 200);
					new GetDataTask().execute();
				}
			}
		});
		LayoutInflater in = LayoutInflater.from(this);
		View detailHeader = in.inflate(R.layout.daze_vehicle_detail_info, null);
		listView.addHeaderView(detailHeader, null, false);
		imageShare = detailHeader.findViewById(R.id.imageShare);
		moneyText = (TextView) detailHeader.findViewById(R.id.vehicle_detail_total_money);
		scoreText = (TextView) detailHeader.findViewById(R.id.vehicle_detail_total_score);
		newNumText = (TextView) detailHeader.findViewById(R.id.vehicle_detail_total_num);
		listEmpty = in.inflate(R.layout.daze_list_against_empty, null);
		chargingView = findViewById(R.id.chargingView);
		tvRecordNumber = (TextView) findViewById(R.id.tvRecordNumber);
		tvRecordMoney = (TextView) findViewById(R.id.tvRecordMoney);
		btPay = (Button) findViewById(R.id.btPay);
		orderTitleView = findViewById(R.id.orderTitleView);
		orderSubmitView = findViewById(R.id.orderSubmitView);
		blankView = findViewById(R.id.blankView);
		order_submit_selected_num_text = (TextView) findViewById(R.id.order_submit_selected_num_text);
		order_submit_money_text = (TextView) findViewById(R.id.order_submit_money_text);
		order_submit_reduce_money_text = (TextView) findViewById(R.id.order_submit_reduce_money_text);
		order_submit_price_text = (TextView) findViewById(R.id.order_submit_price_text);
		order_submit_reduce_price_text = (TextView) findViewById(R.id.order_submit_reduce_price_text);
		order_submit_total_price_loading = (ProgressBar) findViewById(R.id.order_submit_total_price_loading);
		order_submit_total_price_text = (TextView) findViewById(R.id.order_submit_total_price_text);
		tvSubmitCommit = (TextView) findViewById(R.id.tvSubmitCommit);
		shareNoAgainst = (Button) listEmpty.findViewById(R.id.shareNoAgainst);
		tvPayMemberNumber = (TextView) findViewById(R.id.tvPayMemberNumber);
		fineCashView = findViewById(R.id.fineCashView);
		tvFineCashUseInfo = (TextView) findViewById(R.id.tvFineCashUseInfo);
		serviceChargeCashView = findViewById(R.id.serviceChargeCashView);
		tvServiceChargeCashUseInfo = (TextView) findViewById(R.id.tvServiceChargeCashUseInfo);
		cashSelectedView = findViewById(R.id.cashSelectedView);
		cashView = (AbstractWheel)findViewById(R.id.cashView);
		tvUnuseCash = (TextView) findViewById(R.id.tvUnuseCash);
		tvUseCash = (TextView) findViewById(R.id.tvUseCash);
		cashBlankView = findViewById(R.id.cashBlankView);
		order_submit_service_price_loading = (ProgressBar) findViewById(R.id.order_submit_service_price_loading);
		fineCashUseInfo_loading = (ProgressBar) findViewById(R.id.fineCashUseInfo_loading);
		serviceChargeCashUseInfo_loading = (ProgressBar) findViewById(R.id.serviceChargeCashUseInfo_loading);
		updateInfoView = findViewById(R.id.updateInfoView);
		tvPreUpdateTime = (TextView) detailHeader.findViewById(R.id.tvPreUpdateTime);
		ivRefreshAgainstRecords = detailHeader.findViewById(R.id.ivRefreshAgainstRecords);
		vehicleErrorView = detailHeader.findViewById(R.id.vehicleErrorView);
		tvVehicleError = (TextView) detailHeader.findViewById(R.id.tvVehicleError);
		btCorrectVehicleInfo = (Button) detailHeader.findViewById(R.id.btCorrectVehicleInfo);
		refreshingView = detailHeader.findViewById(R.id.refreshingView);
		refreshingView180 = detailHeader.findViewById(R.id.refreshingView180);
		imageRefreshProgress = (ImageProgress) findViewById(R.id.imageRefreshProgress);
		imageRefreshProgress180 = (ImageProgress) findViewById(R.id.imageRefreshProgress180);
		tvRefreshingProgress = (TextView) detailHeader.findViewById(R.id.tvRefreshingProgress);
		tvRefreshingProgress180 = (TextView) detailHeader.findViewById(R.id.tvRefreshingProgress180);
		correctVehicleErrorLayout = findViewById(R.id.correctVehicleErrorLayout);
		errorTitle = (TextView) findViewById(R.id.errorTitle);
		errorInfo = (TextView) findViewById(R.id.errorInfo);
		frame_number_notice = (TextView) findViewById(R.id.frame_number_notice);
		motor_number_notice = (TextView) findViewById(R.id.motor_number_notice);
		owner_id_notice = (TextView) findViewById(R.id.owner_id_notice);
		driver_id_notice = (TextView) findViewById(R.id.driver_id_notice);
		frame_number_layout = findViewById(R.id.frame_number_layout);
		frame_number_view = (EditText) findViewById(R.id.frame_number_view);
		frame_number_view.setTransformationMethod(new AllCapTransformationMethod());
		motor_number_layout = findViewById(R.id.motor_number_layout);
		motor_number_view = (EditText) findViewById(R.id.motor_number_view);
		motor_number_view.setTransformationMethod(new AllCapTransformationMethod());
		owner_id_layout = findViewById(R.id.owner_id_layout);
		owner_id_view = (EditText) findViewById(R.id.owner_id_view);
		owner_id_view.setTransformationMethod(new AllCapTransformationMethod());
		driver_id_layout = findViewById(R.id.driver_id_layout);
		driver_id_view = (EditText) findViewById(R.id.driver_id_view);
		driver_id_view.setTransformationMethod(new AllCapTransformationMethod());
		tvCorrectNextTime = findViewById(R.id.tvCorrectNextTime);
		tvSaveCorrectInfoNow = findViewById(R.id.tvSaveCorrectInfoNow);
		tvCorrectBlank = findViewById(R.id.tvCorrectBlank);
		cityUnsupportView = in.inflate(R.layout.daze_city_unsupport, null);
		tvCityUnsupported = (TextView) cityUnsupportView.findViewById(R.id.tvCityUnsupported);
		btUnsupportEncourage = (Button) cityUnsupportView.findViewById(R.id.btUnsupportEncourage);
		btUnsupportRemind = (Button) cityUnsupportView.findViewById(R.id.btUnsupportRemind);
		searchFailView = detailHeader.findViewById(R.id.searchFailView);
		imCloseNoti = detailHeader.findViewById(R.id.imCloseNoti);
		btFailEncourage = (Button) detailHeader.findViewById(R.id.btFailEncourage);
		free_emergency_layout = findViewById(R.id.free_emergency_layout);
		free_rescue_num = (TextView) findViewById(R.id.free_rescue_num);
		correctAganistInfoLayout = findViewById(R.id.correctAganistInfoLayout);
		money_correct_layout = findViewById(R.id.money_correct_layout);
		score_correct_layout = findViewById(R.id.score_correct_layout);
		tvCorrectAgainstNextTime = findViewById(R.id.tvCorrectAgainstNextTime);
		tvSaveCorrectAgainstInfoNow = findViewById(R.id.tvSaveCorrectAgainstInfoNow);
		tvCorrectAgainstBlank = findViewById(R.id.tvCorrectAgainstBlank);
		score0 = (TextView) findViewById(R.id.score0);
		score1 = (TextView) findViewById(R.id.score1);
		score2 = (TextView) findViewById(R.id.score2);
		score3 = (TextView) findViewById(R.id.score3);
		score6 = (TextView) findViewById(R.id.score6);
		score12 = (TextView) findViewById(R.id.score12);
		money_correct_view = (EditText) findViewById(R.id.money_correct_view);
		cashTitle = (TextView) findViewById(R.id.cashTitle);
		transparentbg = findViewById(R.id.transparentbg);
		vehicle_owner_layout = findViewById(R.id.vehicle_owner_layout);
		vehicle_owner_view = (EditText) findViewById(R.id.vehicle_owner_view);
		driver_name_layout = findViewById(R.id.driver_name_layout);
		driver_name_view = (EditText) findViewById(R.id.driver_name_view);
		website_account_layout = findViewById(R.id.website_account_layout);
		website_account_view = (EditText) findViewById(R.id.website_account_view);
		website_password_layout = findViewById(R.id.website_password_layout);
		website_password_view = (EditText) findViewById(R.id.website_password_view);
		vehicleRegCerNo_layout = findViewById(R.id.vehicleRegCerNo_layout);
		vehicleRegCerNo_view = (EditText) findViewById(R.id.vehicleRegCerNo_view);
		searchFailInfo = (TextView) findViewById(R.id.searchFailInfo);
		EventAnalysisUtil.onEvent(this, "pageView_jiaofei ", "在线缴费页面浏览量", null);
	}

	protected void setListener() {
		errorInfo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
			}
		});
		errorTitle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
			}
		});
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		editBtn.setOnClickListener(this);
		modifyBtn.setOnClickListener(this);
		menuBlankLayout.setOnClickListener(this);
		btPay.setOnClickListener(this);
		blankView.setOnClickListener(this);
		tvSubmitCommit.setOnClickListener(this);
		shareNoAgainst.setOnClickListener(this);
		fineCashView.setOnClickListener(this);
		serviceChargeCashView.setOnClickListener(this);
		cashView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				currentIndex = newValue;
			}
		});
		cashView.addClickingListener(new OnWheelClickedListener() {
			@Override
			public void onItemClicked(AbstractWheel wheel, int itemIndex) {
				currentIndex = itemIndex;
				cashView.setCurrentItem(itemIndex);
			}
		});
		tvUnuseCash.setOnClickListener(this);
		tvUseCash.setOnClickListener(this);
		cashBlankView.setOnClickListener(this);
		imageShare.setOnClickListener(this);
		ivRefreshAgainstRecords.setOnClickListener(this);
		btCorrectVehicleInfo.setOnClickListener(this);
		tvCorrectNextTime.setOnClickListener(this);
		tvSaveCorrectInfoNow.setOnClickListener(this);
		tvCorrectBlank.setOnClickListener(this);
		btUnsupportEncourage.setOnClickListener(this);
		btUnsupportRemind.setOnClickListener(this);
		imCloseNoti.setOnClickListener(this);
		btFailEncourage.setOnClickListener(this);
		free_emergency_layout.setOnClickListener(this);
		tvCorrectAgainstNextTime.setOnClickListener(this);
		tvSaveCorrectAgainstInfoNow.setOnClickListener(this);
		tvCorrectAgainstBlank.setOnClickListener(this);
		score0.setOnClickListener(this);
		score1.setOnClickListener(this);
		score2.setOnClickListener(this);
		score3.setOnClickListener(this);
		score6.setOnClickListener(this);
		score12.setOnClickListener(this);
		frame_number_view.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					frame_number_notice.setVisibility(View.VISIBLE);
					String strFrameNum = frame_number_view.getText().toString();
					if (strFrameNum != null) {
						frame_number_view.setSelection(strFrameNum.length());
					}
					if (nFrameLength == 99) {
						if (strFrameNum.length() == 17)
							updateNotice(frame_number_notice, true);
						else
							updateNotice(frame_number_notice, false);
					} else if (nFrameLength > 0) {
						if (strFrameNum.length() == nFrameLength)
							updateNotice(frame_number_notice, true);
						else
							updateNotice(frame_number_notice, false);
					}
				}
			}
		});
		frame_number_view.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s == null)
					updateNotice(frame_number_notice, false);
				else {
					String strFrameNum = s.toString();
					if (nFrameLength == 99) {
						if (strFrameNum.length() == 17)
							updateNotice(frame_number_notice, true);
						else
							updateNotice(frame_number_notice, false);
					} else {
						if (strFrameNum.length() == nFrameLength)
							updateNotice(frame_number_notice, true);
						else
							updateNotice(frame_number_notice, false);
					}
				}
			}
		});
		motor_number_view.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					motor_number_notice.setVisibility(View.VISIBLE);
					String strMotorNum = motor_number_view.getText().toString();
					if(strMotorNum != null){
						motor_number_view.setSelection(strMotorNum.length());
					}
					if(nMotorLength == 99){
						updateNotice(motor_number_notice, true);
					}
					else if(nMotorLength > 0){
						if(strMotorNum.length() == nMotorLength)
							updateNotice(motor_number_notice, true);
						else
							updateNotice(motor_number_notice, false);
					}
				}
			}
		});
		motor_number_view.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s == null)
					updateNotice(motor_number_notice, false);
				else{
					String strMotorNum = s.toString();
					if(nMotorLength == 99)
						updateNotice(motor_number_notice, true);
					else{
						if(strMotorNum.length() == nMotorLength)
							updateNotice(motor_number_notice, true);
						else
							updateNotice(motor_number_notice, false);
					}
				}
			}
		});
		owner_id_view.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					owner_id_notice.setVisibility(View.VISIBLE);
					String ownerId = owner_id_view.getText().toString();
					if(ownerId != null){
						owner_id_view.setSelection(ownerId.length());
					}
					if(ownerIdLen == 99){
						updateNotice(owner_id_notice, true);
					}
					else if(ownerIdLen > 0){
						if(ownerId.length() == ownerIdLen)
							updateNotice(owner_id_notice, true);
						else
							updateNotice(owner_id_notice, false);
					}
				}
			}
		});
		owner_id_view.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s == null)
					updateNotice(owner_id_notice, false);
				else{
					String ownerId = s.toString();
					if(ownerIdLen == 99)
						updateNotice(owner_id_notice, true);
					else{
						if(ownerId.length() == ownerIdLen)
							updateNotice(owner_id_notice, true);
						else
							updateNotice(owner_id_notice, false);
					}
				}
			}
		});
		driver_id_view.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					driver_id_notice.setVisibility(View.VISIBLE);
					String driverId = driver_id_view.getText().toString();
					if(driverId != null){
						driver_id_view.setSelection(driverId.length());
					}
					if(driverIdLen == 99){
						updateNotice(driver_id_notice, true);
					}
					else if(driverIdLen > 0){
						if(driverId.length() == driverIdLen)
							updateNotice(driver_id_notice, true);
						else
							updateNotice(driver_id_notice, false);
					}
				}
			}
		});
		driver_id_view.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s == null)
					updateNotice(driver_id_notice, false);
				else{
					String driverId = s.toString();
					if(driverIdLen == 99)
						updateNotice(driver_id_notice, true);
					else{
						if(driverId.length() == driverIdLen)
							updateNotice(driver_id_notice, true);
						else
							updateNotice(driver_id_notice, false);
					}
				}
			}
		});
	}

	protected void loadData() {
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		verifyCodeError = getIntent().getBooleanExtra("verifyCodeError", false);
		if(StringUtils.isEmpty(vehicleNumber)){
			ToastUtil.TextToast(this, "车牌号错误",1500, Gravity.CENTER);
			finish();
			return;
		}
		vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
		if(vehicle == null){
			ToastUtil.TextToast(this, "车辆信息错误",1500, Gravity.CENTER);
			finish();
			return;
		}
		opt = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.daze_car_default)
		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		long updateTime = 0;
		if(!StringUtils.isEmpty(vehicle.getUpdateTime())){
			try{
				updateTime = Long.parseLong(vehicle.getUpdateTime());
			}
			catch(Exception e){
				updateTime = 0;
				e.printStackTrace();
			}
		}
		if(updateTime == 0)
			updateInfoView.setVisibility(View.GONE);
		else
			updateInfoView.setVisibility(View.VISIBLE);
		final String strTemp = "数据更新时间" + DateUtil.getShowTimeOnInterval(updateTime);
		if(vehicle.getNewRecordNumber() != 0){
			if(vehicle.getNewRecordNumber() == 1)
				tvPreUpdateTime.setText("您有1条违章信息有更新");
			else
				tvPreUpdateTime.setText("您有" + vehicle.getNewRecordNumber() + "条新的违章");
		}
		else{
//			tvPreUpdateTime.setText("恭喜您,没有查询到新的违章");
			tvPreUpdateTime.setText(strTemp);
		}		
		tvPreUpdateTime.postDelayed(new Runnable() {			
			@Override
			public void run() {
				tvPreUpdateTime.setText(strTemp);
			}
		}, 5000);
		
		add = getIntent().getBooleanExtra("add", false);
		tvTitle.setText(vehicleNumber);
		if (!StringUtils.isEmpty(vehicle.getCityName())) {
			tvSecondTitle.setText("查询城市:" + vehicle.getCityName());
			tvSecondTitle.setVisibility(View.VISIBLE);
		}
		recordsChecked = new ArrayList<AgainstRecord>();
		fineCouponList = new ArrayList<Coupon>();
		serviceChargeCouponList = new ArrayList<Coupon>();
		fineCouponListSave = new ArrayList<Coupon>();
		serviceChargeCouponListSave = new ArrayList<Coupon>();
		nullCoupon = new Coupon();
		nullCoupon.setName("不选择");
		cashAdapter = new CashSelectAdapter(VehicleDetailActivity.this);
		recordTypeAdapter = new RecordTypeAdapter(VehicleDetailActivity.this);
		String cityIds = null;
		if(vehicle != null){
			if(!StringUtils.isEmpty(vehicle.getCityId())){
				List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(vehicle.getCityId());
				if(cvs != null && !cvs.isEmpty()){
					for(CityVehicle cv : cvs){
						if(!(cv.getValid() != null && cv.getValid() == true)){
							if(cityIds == null)
								cityIds = "" + cv.getId();
							else
								cityIds += ("," + cv.getId());
						}
					}
				}
				
			}
		}
		if(!StringUtils.isEmpty(cityIds)){
			try{
				String savedCityId = sp.getString("savedCityId", "");
				if(savedCityId.contains(cityIds)){
					btUnsupportRemind.setText("已开通提醒功能");
				}
				else{
					btUnsupportRemind.setText("开通后提醒我");
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(!StringUtils.isEmpty(cityIds)){
			String savedCityId = sp.getString("savedCityId", "");
			if(savedCityId.contains(cityIds)){
				btUnsupportRemind.setText("已开通提醒功能");
			}
			else{
				btUnsupportRemind.setText("开通后提醒我");
			}
		}
		if (vehicleNumber.startsWith("粤")){
			TextView serviceRule = (TextView) findViewById(R.id.serviceRule);
			serviceRule.setText("广东地区车辆只能处理省内违章，跨省违章无法处理。\n\n" + serviceRule.getText());
		}
		getAdvertData();
	}

	private void setAdapter() {
		adapter = new AgainstRecordListAdapter(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if (v.equals(leftView)) {
			finish();
		} else if (v.equals(rightView) || v.equals(menuBlankLayout)) {
			if (menuLayout.isShown()) {
				menuLayout.setVisibility(View.GONE);
			} else {
				EventAnalysisUtil.onEvent(this, "click_editBtn_recordBarRight", "点击title右侧编辑按钮", null);
				menuLayout.setVisibility(View.VISIBLE);
			}
		} else if (v.equals(editBtn)) {
			EventAnalysisUtil.onEvent(this, "editCar_recordBarRight", "点击title右侧编辑车辆信息", null);
			menuLayout.setVisibility(View.GONE);
			intent.setClass(this, VehicleEditActivity.class);
			intent.putExtra("vehicle", vehicle);
            intent.putExtra("hideDel", true);
            startActivityForResult(intent, EDIT_VEHICLE_REQUEST);
		} else if (v.equals(modifyBtn)) {
			EventAnalysisUtil.onEvent(this, "modifyCity_recordBarRight", "点击title右侧修改查询城市", null);
			menuLayout.setVisibility(View.GONE);
			intent.setClass(this, CitySelectActivity.class);
			intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL);
			intent.putExtra("cityId", vehicle.getCityId());
			intent.putExtra("vehicle", vehicle);
			startActivity(intent);
		}
		else if(v.equals(btPay)){
			EventAnalysisUtil.onEvent(this, "imediaPay_record", "在线缴费订单页面的立即缴费", null);
			if(mApplication.getId() == 0){
				ToastUtil.TextToast(VehicleDetailActivity.this, "支付需要绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
				Intent i = new Intent(VehicleDetailActivity.this, PhoneRegistActivity.class);
				i.putExtra("isMustPhone", true);
				startActivity(i);
			}
			else{
				transparentbg.setVisibility(View.VISIBLE);
				orderTitleView.setVisibility(View.VISIBLE);
				orderTitleView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_in_from_top));
				orderSubmitView.setVisibility(View.VISIBLE);
				orderSubmitView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_in_from_bottom));
				order = new Order();
				order.setUserId(mApplication.getUserId());
				order.setVehicleNum(vehicleNumber);
				order.setContactName("user" + mApplication.getUserId());
				order.setContactPhone(mApplication.dbCache.getValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE));
				getPrice();
//				getBalance();
			}
		}
		else if(v.equals(blankView)){
			orderTitleView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_out_to_top));
			orderTitleView.postDelayed(new Runnable() {
				@Override
				public void run() {
					orderTitleView.setVisibility(View.GONE);
					transparentbg.setVisibility(View.GONE);
				}
			}, 500);
			orderSubmitView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_out_to_bottom));
			orderSubmitView.postDelayed(new Runnable() {
				@Override
				public void run() {
					orderSubmitView.setVisibility(View.GONE);
				}
			}, 500);
		}
		else if(v.equals(tvSubmitCommit)){
			EventAnalysisUtil.onEvent(this, "pay_confirmorder_record", "在线缴费订单页面的提交订单", null);
			if(recordsChecked == null || recordsChecked.isEmpty()){
				ToastUtil.TextToast(VehicleDetailActivity.this, "请选择违章后再提交订单", 2000, Gravity.CENTER);
			}
			else{
				submitOrder();
			}
		}
		else if(v.equals(shareNoAgainst)){
			EventAnalysisUtil.onEvent(this, "click_share_noRecord", "违章查询页面无违章时底部分享点击", null);
			if(mApplication.getId() == 0){
				intent.setClass(VehicleDetailActivity.this, PhoneRegistActivity.class);
				intent.putExtra("isShare", true);
				startActivity(intent);
			}
			else{
				shareType = 101;
				new GetInviteContentTask().execute();
			}
		}
		else if(v.equals(fineCashView)){
			if(fineCashUseInfo_loading.getVisibility() == View.VISIBLE){
				ToastUtil.TextToast(VehicleDetailActivity.this, "正在获取代金券，请稍候", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(fineCouponList.isEmpty()){
				ToastUtil.TextToast(VehicleDetailActivity.this, "没有代金券可以使用", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			isFineCoupon = true;
			fineCouponList.clear();
			fineCouponList.addAll(fineCouponListSave);
			if(currentServiceChargeCoupon != null && currentServiceChargeCoupon != nullCoupon ){
				for(Coupon coup : fineCouponList){
					if(coup != null && coup.getId().longValue() == currentServiceChargeCoupon.getId().longValue()){
						fineCouponList.remove(coup);
						break;
					}
				}
			}
			fineCouponList.add(0, nullCoupon);
			cashAdapter.setList(fineCouponList);
			cashTitle.setText("选择代金券");
			cashView.setViewAdapter(cashAdapter);
			cashView.setCurrentItem(fineCouponList.indexOf(currentFineCoupon));
			cashView.setVisibleItems(5);
			cashSelectedView.setVisibility(View.VISIBLE);
		}
		else if (v.equals(serviceChargeCashView)){
			if(serviceChargeCashUseInfo_loading.getVisibility() == View.VISIBLE){
				ToastUtil.TextToast(VehicleDetailActivity.this, "正在获取代金券，请稍候", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(!(serviceCharge >0)){
				ToastUtil.TextToast(VehicleDetailActivity.this, "服务费为零，不能使用代金券", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(serviceChargeCouponList.isEmpty()){
				ToastUtil.TextToast(VehicleDetailActivity.this, "没有代金券可以使用", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			isFineCoupon = false;
			serviceChargeCouponList.clear();
			serviceChargeCouponList.addAll(serviceChargeCouponListSave);
			if(currentFineCoupon != null && currentFineCoupon != nullCoupon){
				for(Coupon coup : serviceChargeCouponList){
					if(coup != null && coup.getId().longValue() == currentFineCoupon.getId().longValue()){
						serviceChargeCouponList.remove(coup);
						break;
					}
				}
			}
			serviceChargeCouponList.add(0, nullCoupon);
			cashAdapter.setList(serviceChargeCouponList);
			cashTitle.setText("选择代金券");
			cashView.setViewAdapter(cashAdapter);
			cashView.setCurrentItem(serviceChargeCouponList.indexOf(currentServiceChargeCoupon));
			cashView.setVisibleItems(5);
			cashSelectedView.setVisibility(View.VISIBLE);
		}
		else if(v.equals(tvUnuseCash)){
			cashSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(tvUseCash)){
			if(cashTitle.getText().toString().equals("设置违章类型")){
				correctRecordType();
			}
			else{
				if(isFineCoupon){
					cashSelectedView.setVisibility(View.GONE);
					currentFineCoupon = fineCouponList.get(currentIndex);				
					if(currentFineCoupon.getAmount() == null){
						payMoneyReduction = 0;
						tvFineCashUseInfo.setText(currentFineCoupon.getName());
						order_submit_reduce_money_text.setVisibility(View.GONE);
					}
					else{
						tvFineCashUseInfo.setText(currentFineCoupon.getName() + "¥" + currentFineCoupon.getAmount().intValue());
						if(totalMoney - serviceCharge > currentFineCoupon.getAmount().intValue()){
							payMoneyReduction = currentFineCoupon.getAmount().intValue();
							order_submit_reduce_money_text.setText("-" + payMoneyReduction);
						}
						else{
							payMoneyReduction = (int) (totalMoney - serviceCharge);
							order_submit_reduce_money_text.setText("-" + df.format(payMoneyReduction));
						}
						order_submit_reduce_money_text.setVisibility(View.VISIBLE);
					}
					if(Math.abs(totalMoney - payMoneyReduction - serviceChargeReduction) > 0.00001)
						order_submit_total_price_text.setText(df.format(totalMoney - payMoneyReduction - serviceChargeReduction));
					else
						order_submit_total_price_text.setText("0.01");
				}
				else{
					cashSelectedView.setVisibility(View.GONE);
					currentServiceChargeCoupon = serviceChargeCouponList.get(currentIndex);
					if(currentServiceChargeCoupon.getAmount() == null){
						serviceChargeReduction = 0;
						tvServiceChargeCashUseInfo.setText(currentServiceChargeCoupon.getName());
						order_submit_reduce_price_text.setVisibility(View.GONE);
					}
					else{
						tvServiceChargeCashUseInfo.setText(currentServiceChargeCoupon.getName() + "¥" + currentServiceChargeCoupon.getAmount().intValue());
						if(serviceCharge > currentServiceChargeCoupon.getAmount().intValue()){
							serviceChargeReduction = currentServiceChargeCoupon.getAmount().intValue();
							order_submit_reduce_price_text.setText("-" + serviceChargeReduction);
						}
						else{
							serviceChargeReduction = (int) serviceCharge;
							order_submit_reduce_price_text.setText("-" + df.format(serviceChargeReduction));
						}
						order_submit_reduce_price_text.setVisibility(View.VISIBLE);
					}
					if(Math.abs(totalMoney - payMoneyReduction - serviceChargeReduction) > 0.00001)
						order_submit_total_price_text.setText(df.format(totalMoney - payMoneyReduction - serviceChargeReduction));
					else
						order_submit_total_price_text.setText("0.01");
				}
			}
		}
		else if(v.equals(cashBlankView)){
			cashSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(imageShare)){
			EventAnalysisUtil.onEvent(this, "click_share_recordRight", "违章查询页面右侧分享点击", null);
			if(mApplication.getId() == 0){
				intent.setClass(VehicleDetailActivity.this, PhoneRegistActivity.class);
				intent.putExtra("isShare", true);
				startActivity(intent);
			}
			else{
				if(records == null || records.isEmpty())
					shareType = 101;
				else
					shareType = 102;
				new GetInviteContentTask().execute();
			}
		}
		else if(v.equals(ivRefreshAgainstRecords)){
			if(refreshingView.getVisibility() == View.GONE && refreshingView180.getVisibility() == View.GONE && !mApplication.containsTask(vehicleNumber)){
				nProgress = 0;
				tvRefreshingProgress.setText(nProgress + "%");
				imageRefreshProgress.setnRadius(16);
				imageRefreshProgress.setnAngel(nProgress*3.6f);
				imageRefreshProgress.postInvalidate();
				refreshingView.setVisibility(View.VISIBLE);
				Message message = new Message();
				message.what = 2;
				progressHandler.sendMessageDelayed(message, 200);
				new GetDataTask().execute();
			}
		}
		else if(v.equals(btCorrectVehicleInfo)){
			correctVehicleErrorLayout.setVisibility(View.VISIBLE);
			getFrameAndMotorLength();
			showCorrectVehicleInfoView();
		}
		else if(v.equals(tvCorrectNextTime)){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
			correctVehicleErrorLayout.setVisibility(View.GONE);
		}
		else if(v.equals(tvSaveCorrectInfoNow)){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
			String strFrameNum = null;
			if(frame_number_layout.getVisibility() == View.VISIBLE){
				strFrameNum = frame_number_view.getText().toString().toUpperCase();
				if(StringUtils.isEmpty(strFrameNum)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入车架号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				else if(nFrameLength == 99 && strFrameNum.length() != 17){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入完整车架号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				else if(nFrameLength != 99 && strFrameNum.length() != nFrameLength){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入车架号后" + nFrameLength + "位", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				vehicle.setFrameNum(strFrameNum);
			}
			String strMotorNum = null;
			if(motor_number_layout.getVisibility() == View.VISIBLE){
				strMotorNum = motor_number_view.getText().toString().toUpperCase();
				if(StringUtils.isEmpty(strMotorNum)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入发动机号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				else if(nMotorLength != 99 && strMotorNum.length() != nMotorLength){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入发动机号后" + nMotorLength + "位", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			String vehicleOwner = null;
			if(vehicle_owner_layout.getVisibility() == View.VISIBLE){
				vehicleOwner = vehicle_owner_view.getText().toString();
				if(StringUtils.isEmpty(vehicleOwner)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入机动车所有人", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			String ownerId = null;
			if(owner_id_layout.getVisibility() == View.VISIBLE){
				ownerId = owner_id_view.getText().toString().toUpperCase();
				if(StringUtils.isEmpty(ownerId)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入车主身份证号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				else if(ownerIdLen != 99 && ownerId.length() != ownerIdLen){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入车主身份证号后" + ownerIdLen + "位", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			String driverName = null;
			if(driver_name_layout.getVisibility() == View.VISIBLE){
				driverName = driver_name_view.getText().toString();
				if(StringUtils.isEmpty(driverName)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入驾驶证姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			String driverId = null;
			if(driver_id_layout.getVisibility() == View.VISIBLE){
				driverId = driver_id_view.getText().toString().toUpperCase();
				if(StringUtils.isEmpty(driverId)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入驾驶证号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				else if(driverIdLen != 99 && driverId.length() != driverIdLen){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入驾驶证号后" + driverIdLen + "位", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			String websiteAaccount = null;
			if(website_account_layout.getVisibility() == View.VISIBLE){
				websiteAaccount = website_account_view.getText().toString();
				if(StringUtils.isEmpty(websiteAaccount)){
					if(StringUtils.isEmpty(vehicleOwner)){
						ToastUtil.TextToast(VehicleDetailActivity.this, "请输入官网账号", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
				}
			}
			String websitePassword = null;
			if(website_password_layout.getVisibility() == View.VISIBLE){
				websitePassword = website_password_view.getText().toString();
				if(StringUtils.isEmpty(websitePassword)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入官网密码", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			String vehicleRegCerNo = null;
			if(vehicleRegCerNo_layout.getVisibility() == View.VISIBLE){
				vehicleRegCerNo = vehicleRegCerNo_view.getText().toString();
				if(StringUtils.isEmpty(vehicleRegCerNo)){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入机动车登记证书编号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			saveVehicle(strFrameNum, strMotorNum, vehicleOwner, ownerId, driverName, driverId, websiteAaccount, websitePassword, vehicleRegCerNo);
		}
		else if(v.equals(tvCorrectBlank)){
//			correctVehicleErrorLayout.setVisibility(View.GONE);
		}
		else if(v.equals(btUnsupportEncourage)){
			String cityIds = null;
			if(vehicle != null){
				if(!StringUtils.isEmpty(vehicle.getCityId())){
					List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(vehicle.getCityId());
					if(cvs != null && !cvs.isEmpty()){
						for(CityVehicle cv : cvs){
							if(!(cv.getValid() != null && cv.getValid() == true)){
								if(cityIds == null)
									cityIds = "" + cv.getId();
								else
									cityIds += ("," + cv.getId());
							}
						}
					}
					
				}
			}
			submitError("4", cityIds);
		}
		else if(v.equals(btUnsupportRemind)){
			String cityIds = null;
			if(vehicle != null){
				if(!StringUtils.isEmpty(vehicle.getCityId())){
					List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(vehicle.getCityId());
					if(cvs != null && !cvs.isEmpty()){
						for(CityVehicle cv : cvs){
							if(!(cv.getValid() != null && cv.getValid() == true)){
								if(cityIds == null)
									cityIds = "" + cv.getId();
								else
									cityIds += ("," + cv.getId());
							}
						}
					}
					
				}
			}
//			String strCity = vehicle.getCityUnValid();
			String savedCityId = sp.getString("savedCityId", "");
			savedCityId += cityIds;
			sp.edit().putString("savedCityId", savedCityId).commit();
			btUnsupportRemind.setText("已开通提醒功能");
			final String strParams = cityIds;
			new AsyncTask<Void, Void, Response>(){
				@Override
				protected Response doInBackground(Void... params) {
						
					VehicleRoportRequest request = new VehicleRoportRequest();
					request.setParams("7", strParams, vehicleNumber, ""+mApplication.getUserId());
					try {
						return mApplication.client.executePost(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute();
		}
		else if(v.equals(imCloseNoti)){
			searchFailView.setVisibility(View.GONE);
		}
		else if(v.equals(btFailEncourage)){
			submitError("5", null);
		}
		else if(v.equals(tvCorrectAgainstNextTime)){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
			correctAganistInfoLayout.setVisibility(View.GONE);
		}
		else if(v.equals(tvSaveCorrectAgainstInfoNow)){
			int nCorrectMoney = 0;
			String strKey = null;
			String strMsg = null;
			if(money_correct_layout.getVisibility() == View.VISIBLE){
				if(StringUtils.isEmpty(money_correct_view.getText().toString())){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请输入罚款金额", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				try{
					nCorrectMoney = Integer.parseInt(money_correct_view.getText().toString());
				}
				catch(Exception e){
					e.printStackTrace();
					ToastUtil.TextToast(VehicleDetailActivity.this, "罚款金额输入错误", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				strKey = "1";
				strMsg = "" + nCorrectMoney;
			}
			if(score_correct_layout.getVisibility() == View.VISIBLE){
				if(nSelfScore == -1){
					ToastUtil.TextToast(VehicleDetailActivity.this, "请选择扣分数", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				if(strKey == null)
					strKey = "2";
				else
					strKey += ",2";
				if(strMsg == null)
					strMsg = ("" + nSelfScore);
				else
					strMsg += ("," + nSelfScore);
			}
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
			correctAganistInfoLayout.setVisibility(View.GONE);
			if(money_correct_layout.getVisibility() == View.VISIBLE){
				currentRecord.setSelfMoney(nCorrectMoney);
			}
			currentRecord.setSelfScore(nSelfScore);
			mApplication.dbCache.saveAgainstRecord(currentRecord);
			adapter.notifyDataSetChanged();
			final String keyString = strKey;
			final String msgString = strMsg;
			new AsyncTask<Void, Void, Response>(){
				@Override
				protected Response doInBackground(Void... params) {
					AgainstReportRequest request = new AgainstReportRequest();
					request.setParams(keyString, msgString, ""+currentRecord.getId());
					try {
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute();
		}
		else if(v.equals(tvCorrectAgainstBlank)){
//			correctAganistInfoLayout.setVisibility(View.GONE);
		}
		else if(v.equals(score0)){
			nSelfScore = 0;
			score0.setBackgroundResource(R.color.daze_orangered5);
			score1.setBackgroundResource(R.color.daze_white);
			score2.setBackgroundResource(R.color.daze_white);
			score3.setBackgroundResource(R.color.daze_white);
			score6.setBackgroundResource(R.color.daze_white);
			score12.setBackgroundResource(R.color.daze_white);
			score0.setTextColor(getResources().getColor(R.color.daze_white));
			score1.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score2.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score3.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score6.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score12.setTextColor(getResources().getColor(R.color.daze_orangered5));
		}
		else if(v.equals(score1)){
			nSelfScore = 1;
			score0.setBackgroundResource(R.color.daze_white);
			score1.setBackgroundResource(R.color.daze_orangered5);
			score2.setBackgroundResource(R.color.daze_white);
			score3.setBackgroundResource(R.color.daze_white);
			score6.setBackgroundResource(R.color.daze_white);
			score12.setBackgroundResource(R.color.daze_white);
			score0.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score1.setTextColor(getResources().getColor(R.color.daze_white));
			score2.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score3.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score6.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score12.setTextColor(getResources().getColor(R.color.daze_orangered5));
		}
		else if(v.equals(score2)){
			nSelfScore = 2;
			score0.setBackgroundResource(R.color.daze_white);
			score1.setBackgroundResource(R.color.daze_white);
			score2.setBackgroundResource(R.color.daze_orangered5);
			score3.setBackgroundResource(R.color.daze_white);
			score6.setBackgroundResource(R.color.daze_white);
			score12.setBackgroundResource(R.color.daze_white);
			score0.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score1.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score2.setTextColor(getResources().getColor(R.color.daze_white));
			score3.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score6.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score12.setTextColor(getResources().getColor(R.color.daze_orangered5));
		}
		else if(v.equals(score3)){
			nSelfScore = 3;
			score0.setBackgroundResource(R.color.daze_white);
			score1.setBackgroundResource(R.color.daze_white);
			score2.setBackgroundResource(R.color.daze_white);
			score3.setBackgroundResource(R.color.daze_orangered5);
			score6.setBackgroundResource(R.color.daze_white);
			score12.setBackgroundResource(R.color.daze_white);
			score0.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score1.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score2.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score3.setTextColor(getResources().getColor(R.color.daze_white));
			score6.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score12.setTextColor(getResources().getColor(R.color.daze_orangered5));
		}
		else if(v.equals(score6)){
			nSelfScore = 6;
			score0.setBackgroundResource(R.color.daze_white);
			score1.setBackgroundResource(R.color.daze_white);
			score2.setBackgroundResource(R.color.daze_white);
			score3.setBackgroundResource(R.color.daze_white);
			score6.setBackgroundResource(R.color.daze_orangered5);
			score12.setBackgroundResource(R.color.daze_white);
			score0.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score1.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score2.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score3.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score6.setTextColor(getResources().getColor(R.color.daze_white));
			score12.setTextColor(getResources().getColor(R.color.daze_orangered5));
		}
		else if(v.equals(score12)){
			nSelfScore = 12;
			score0.setBackgroundResource(R.color.daze_white);
			score1.setBackgroundResource(R.color.daze_white);
			score2.setBackgroundResource(R.color.daze_white);
			score3.setBackgroundResource(R.color.daze_white);
			score6.setBackgroundResource(R.color.daze_white);
			score12.setBackgroundResource(R.color.daze_orangered5);
			score0.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score1.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score2.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score3.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score6.setTextColor(getResources().getColor(R.color.daze_orangered5));
			score12.setTextColor(getResources().getColor(R.color.daze_white));
		}
	}
	
	private void submitError(final String errorKey, final String errorMsg){
		searchFailView.setVisibility(View.GONE);
		Intent intent = new Intent(VehicleDetailActivity.this, AlertDialogActivity.class);
		intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
		intent.putExtra("title", "感谢您的支持");
		intent.putExtra("message", "小牛已经收到您的鼓励，心中充满了温暖和力量，下定决心，排除万难，一定尽快解决这个问题");
		intent.putExtra("ConfirmButtonText", "关闭");
		startActivity(intent);
		new AsyncTask<Void, Void, Response>(){
			@Override
			protected Response doInBackground(Void... params) {
				VehicleRoportRequest request = new VehicleRoportRequest();
				request.setParams(errorKey, errorMsg, vehicleNumber, "" + mApplication.getUserId());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private class AgainstRecordListAdapter extends BaseLoadList<AgainstRecord> {
		private boolean isImage = false;

		public AgainstRecordListAdapter(BaseActivity context) {
			super(context);
		}

		@Override
		public void initItem(final AgainstRecord it, Map<String, Object> holder) {
			if(isImage){
				ImageView againstImage1 = (ImageView) holder.get("againstImage1");
				ImageView againstImage2 = (ImageView) holder.get("againstImage2");
				ImageView againstImage3 = (ImageView) holder.get("againstImage3");
				ImageView againstImage4 = (ImageView) holder.get("againstImage4");
				ImageNames imageNames = it.getImageName();
				String imageUrl0 = null;
				String imageUrl4 = null;
				String imageUrl1 = null;
				String imageUrl3 = null;
				if(imageNames != null){
					imageUrl0 = imageNames.getImg_0();
					imageUrl4 = imageNames.getImg_4();
					imageUrl1 = imageNames.getImg_1();
					imageUrl3 = imageNames.getImg_3();
				}
				if(!StringUtils.isEmpty(imageUrl4)){
					mApplication.imageLoader.displayImage(KplusConstants.SERVER_URL + "/common/getImage.htm?imageName=" + imageUrl4, againstImage1, opt);
				}
				if(!StringUtils.isEmpty(imageUrl1)){
					mApplication.imageLoader.displayImage(KplusConstants.SERVER_URL + "/common/getImage.htm?imageName=" + imageUrl1, againstImage2, opt);
				}
				if(!StringUtils.isEmpty(imageUrl3)){
					mApplication.imageLoader.displayImage(KplusConstants.SERVER_URL + "/common/getImage.htm?imageName=" + imageUrl3, againstImage3, opt);
				}
				if(!StringUtils.isEmpty(imageUrl0)){
					mApplication.imageLoader.displayImage(KplusConstants.SERVER_URL + "/common/getImage.htm?imageName=" + imageUrl0, againstImage4, opt);
				}
			}
			else{
			View rootView = (View) holder.get("rootView");
			TextView time = (TextView) holder.get("time");
			TextView tvStatus = (TextView) holder.get("tvStatus");
			TextView address = (TextView) holder.get("address");
			TextView behavior = (TextView) holder.get("behavior");
			TextView score = (TextView) holder.get("score");
			TextView money = (TextView) holder.get("money");
			View serviceHead = (View) holder.get("serviceHead");
			View oldAgainstHead = (View) holder.get("oldAgainstHead");
			View alarmToUpload = (View) holder.get("alarmToUpload");
			TextView tvOrderNumber = (TextView) holder.get("tvOrderNumber");
			TextView tvOrderSubmitTime = (TextView) holder.get("tvOrderSubmitTime");
			View payOnlineHead = (View) holder.get("payOnlineHead");
			View disposingHead = (View) holder.get("disposingHead");
			View unSupportHead = (View) holder.get("unSupportHead");
			TextView payOnlineNum = (TextView) holder.get("payOnlineNum");
			TextView payOnlineMoney = (TextView) holder.get("payOnlineMoney");
			TextView disposingNum = (TextView) holder.get("disposingNum");
			TextView disposingMoney = (TextView) holder.get("disposingMoney");
			TextView listItem_payment_status = (TextView) holder.get("listItem_payment_status");
			final CheckBox checkView = (CheckBox) holder.get("checkView");
			TextView tvBottom = (TextView) holder.get("tvBottom");
			TextView data_source = (TextView) holder.get("data_source");
			TextView vehicle_detail_listItem_error_recovery = (TextView) holder.get("vehicle_detail_listItem_error_recovery");
			TextView tvOrderStatus = (TextView) holder.get("tvOrderStatus");
			TextView vehicle_detail_recordType = (TextView) holder.get("vehicle_detail_recordType");
			serviceHead.setVisibility(View.GONE);
			oldAgainstHead.setVisibility(View.GONE);
			alarmToUpload.setVisibility(View.GONE);
			payOnlineHead.setVisibility(View.GONE);
			disposingHead.setVisibility(View.GONE);
			unSupportHead.setVisibility(View.GONE);
			checkView.setVisibility(View.GONE);
			int nIndex = records.indexOf(it);
			if(nIndex == 0 || (nIndex > 0 && records.get(nIndex -1).getnType() != it.getnType())){
				switch(it.getnType()){
				case KplusConstants.TYPE_CAN_PAY_ONLINE:
					payOnlineHead.setVisibility(View.VISIBLE);
					payOnlineNum.setText("" + nPayOnlineNum);
					payOnlineMoney.setText("" + nPayOnlineMoney);
					break;
				case KplusConstants.TYPE_DISPOSING:
					disposingHead.setVisibility(View.VISIBLE);
					disposingNum.setText("" + nDisposingNum);
					disposingMoney.setText("" + nDisposingMoney);
					break;
				case KplusConstants.TYPE_CAN_TICKET_PAY:
					serviceHead.setVisibility(View.VISIBLE);
					break;
				case KplusConstants.TYPE_NOT_SUPPORT:
					unSupportHead.setVisibility(View.VISIBLE);
					break;
				case KplusConstants.TYPE_DISPOSED:
					oldAgainstHead.setVisibility(View.VISIBLE);
					break;
					default:
						break;
				}
			}
			if(it.getMoney() == -1 || it.getScore() == -1){
				vehicle_detail_listItem_error_recovery.setVisibility(View.VISIBLE);
				vehicle_detail_listItem_error_recovery.setTag(it);
				vehicle_detail_listItem_error_recovery.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						currentRecord = it;
						correctAganistInfoLayout.setVisibility(View.VISIBLE);
						if(it.getMoney() == -1)
							money_correct_layout.setVisibility(View.VISIBLE);
						else
							money_correct_layout.setVisibility(View.GONE);
						if(it.getScore() == -1)
							score_correct_layout.setVisibility(View.VISIBLE);
						else
							score_correct_layout.setVisibility(View.GONE);
					}
				});
			}
			else{
				vehicle_detail_listItem_error_recovery.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty(it.getCityName())){
				int length = it.getCityName().length();
				if (length <= 6)
					data_source.setText("来自 " + it.getCityName() + "交管局官网");
				else if (length <= 8)
					data_source.setText("来自 " + it.getCityName() + "交管局");
				else
					data_source.setText(it.getCityName() + "交管局");
			}
			else
				data_source.setText("");
			if(nIndex == records.size() -1)
				tvBottom.setVisibility(View.VISIBLE);
			else
				tvBottom.setVisibility(View.GONE);
			String timeTemp = "";
			try{
				Date date = sdfold.parse(it.getTime());
				timeTemp = sdfnew.format(date);
			}catch(Exception e){
				e.printStackTrace();
			}
			time.setText(timeTemp);
			if(it.getPaymentStatus() != null && it.getPaymentStatus() == 1){
				listItem_payment_status.setText("已缴费");
				listItem_payment_status.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
			}
			else{
				if(it.getSelfProcess() != null && it.getSelfProcess() == 1){
					listItem_payment_status.setText("已缴费");
					listItem_payment_status.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
				}
				else{
					listItem_payment_status.setText("未缴费");
					listItem_payment_status.setTextColor(getResources().getColor(R.color.daze_orangered5));
				}
			}
			if (it.getStatus() == 1) {
				if(it.getSelfProcess() != null && it.getSelfProcess() == 1){
					if(mApplication.getpId() != 0 && (it.getPId() != null && it.getPId().longValue() == mApplication.getpId())){
						alarmToUpload.setVisibility(View.VISIBLE);
						if(!StringUtils.isEmpty(it.getOrderCode())){
							tvOrderNumber.setText(it.getOrderCode());
							alarmToUpload.setVisibility(View.VISIBLE);
						}
						else{
							alarmToUpload.setVisibility(View.GONE);
						}
						if(!StringUtils.isEmpty(it.getOrdertime()))
							tvOrderSubmitTime.setText(it.getOrdertime());
						tvOrderStatus.setText(KplusConstants.orderStatus(it.getOrderStatus()));
						tvOrderStatus.setTextColor(getResources().getColor(R.color.daze_light_green500));
					}
//					tvStatus.setText("橙牛处理");
//					tvStatus.setTextColor(Color.WHITE);
//					tvStatus.setBackgroundColor(getResources().getColor(R.color.daze_orangered4));
				}
//				else
				{
					tvStatus.setText("已处理");
					tvStatus.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
					tvStatus.setBackgroundColor(Color.WHITE);
				}
			} else {
				int nOrderStatus = it.getOrderStatus();
				if(nOrderStatus == 0 || nOrderStatus == 2 || nOrderStatus == 14 || nOrderStatus == 20){
					tvStatus.setText("未处理");
					if(it.getCanSubmit() != null && it.getCanSubmit() == 0)
						checkView.setVisibility(View.VISIBLE);
				}
				else{
					if(it.getOrderStatus() == 1)
						tvStatus.setText("未处理");
				}
				if(it.getnType() == KplusConstants.TYPE_DISPOSING){
					tvStatus.setText("处理中");
					checkView.setVisibility(View.GONE);
					if(mApplication.getpId() != 0 && (it.getPId() != null && it.getPId().longValue() == mApplication.getpId())){
						alarmToUpload.setVisibility(View.VISIBLE);
						if(!StringUtils.isEmpty(it.getOrderCode())){
							tvOrderNumber.setText(it.getOrderCode());
							alarmToUpload.setVisibility(View.VISIBLE);
						}
						else
							alarmToUpload.setVisibility(View.GONE);
						if(!StringUtils.isEmpty(it.getOrdertime()))
							tvOrderSubmitTime.setText(it.getOrdertime());
						tvOrderStatus.setText(KplusConstants.orderStatus(it.getOrderStatus()));
						tvOrderStatus.setTextColor(getResources().getColor(R.color.daze_light_green500));
						if(nOrderStatus == 3 || nOrderStatus == 4){
							tvOrderStatus.setText("请上传证件");
							tvOrderStatus.setTextColor(getResources().getColor(R.color.daze_orange));
						}
					}
				}
//				if(it.getScore() != 0)
//					checkView.setVisibility(View.GONE);
				tvStatus.setTextColor(getResources().getColor(R.color.daze_orangered4));
				tvStatus.setBackgroundColor(Color.WHITE);
			}
			if(!StringUtils.isEmpty(it.getAddress()))
				address.setText(it.getAddress());
			else
				address.setText("未知地点");
			if(!StringUtils.isEmpty(it.getBehavior()))
				behavior.setText(it.getBehavior());
			else
				behavior.setText("违反道路交通安全法");
			if(it.getScore() == -1){
				if(it.getSelfScore() != -1)
					score.setText(it.getSelfScore() +"分");
				else
					score.setText("未知");
			}
			else
				score.setText(it.getScore() +"分");
			if(it.getMoney() == -1){
				if(it.getSelfMoney() != -1)
					money.setText(it.getSelfMoney() + "元");
				else
					money.setText("未知");
			}
			else
				money.setText(it.getMoney() + "元");
			if(againRecordChecked(it)){
				checkView.setChecked(true);
			}
			else
				checkView.setChecked(false);
			rootView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(it.getnType() == KplusConstants.TYPE_CAN_PAY_ONLINE){
						if(checkView.isChecked()){
							checkView.setChecked(false);
							if(againRecordChecked(it)){
								recordsChecked.remove(it);
								if(recordsChecked.isEmpty()){
									chargingView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_out_to_bottom));
									chargingView.postDelayed(new Runnable() {									
										@Override
										public void run() {
											chargingView.setVisibility(View.GONE);
										}
									}, 500);
								}
								tvRecordNumber.setText("已选"+ recordsChecked.size() + "条违章，罚款共计");
								payMoney -= it.getMoney();
								tvRecordMoney.setText("¥" + payMoney);
								order_submit_selected_num_text.setText("" + recordsChecked.size() + "条");
								order_submit_money_text.setText(df.format(payMoney));
							}
						}
						else{
							checkView.setChecked(true);
							if(!againRecordChecked(it)){
								if(recordsChecked.isEmpty()){
									chargingView.setVisibility(View.VISIBLE);
									chargingView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_in_from_bottom));
								}
								recordsChecked.add(it);
								tvRecordNumber.setText("已选"+ recordsChecked.size() + "条违章，罚款共计");
								payMoney += it.getMoney();
								tvRecordMoney.setText("¥" + payMoney);
								order_submit_selected_num_text.setText("" + recordsChecked.size() + "条");
								order_submit_money_text.setText(df.format(payMoney));
							}
						}
					}
					else if(it.getnType() == KplusConstants.TYPE_DISPOSING || (it.getStatus() == 1 && it.getSelfProcess() != null && it.getSelfProcess() == 1)){
						if(mApplication.getpId() != 0){
							if(it.getPId() != null && it.getPId().longValue() == mApplication.getpId()){
								Intent intent = new Intent(VehicleDetailActivity.this, OrderActivity.class);
								intent.putExtra("orderId", it.getOrderId());
								startActivity(intent);
							}
						}
					}
				}
			});
			serviceHead.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(VehicleDetailActivity.this, VehicleServiceActivity.class);
					intent.putExtra("appId", "10000004");
					startActivity(intent);
//					mApplication.isTabNeedSwitch = true;
//					finish();
				}
			});
			unSupportHead.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(VehicleDetailActivity.this, SelfServiceListActivity.class);
					intent.putExtra("vehicleNumber", it.getVehicleNum());
					startActivity(intent);
				}
			});
			if(!StringUtils.isEmpty(it.getRecordTypeLabel()))
				vehicle_detail_recordType.setText(it.getRecordTypeLabel());
			else
				vehicle_detail_recordType.setText("");
			vehicle_detail_recordType.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					currentRecord = it;
					cashTitle.setText("设置违章类型");
					cashView.setViewAdapter(recordTypeAdapter);
					if(it.getRecordType() != null)
						cashView.setCurrentItem(it.getRecordType());
					else
						cashView.setCurrentItem(0);
					cashView.setVisibleItems(3);
					cashSelectedView.setVisibility(View.VISIBLE);
				}
			});
			}
		}

		@Override
		public Map<String, Object> getHolder(View v) {
			Map<String, Object> root = new HashMap<String, Object>();
			if(isImage){
				ImageView againstImage1 = (ImageView) v.findViewById(R.id.againstImage1);
				ImageView againstImage2 = (ImageView) v.findViewById(R.id.againstImage2);
				ImageView againstImage3 = (ImageView) v.findViewById(R.id.againstImage3);
				ImageView againstImage4 = (ImageView) v.findViewById(R.id.againstImage4);
				root.put("againstImage1", againstImage1);
				root.put("againstImage2", againstImage2);
				root.put("againstImage3", againstImage3);
				root.put("againstImage4", againstImage4);
			}
			else{
				View rootView = v.findViewById(R.id.rootView);
				TextView time = (TextView) v.findViewById(R.id.vehicle_detail_listItem_time);
				TextView tvStatus = (TextView) v.findViewById(R.id.vehicle_detail_listItem_status);
				TextView address = (TextView) v.findViewById(R.id.vehicle_detail_listItem_address);
				TextView behavior = (TextView) v.findViewById(R.id.vehicle_detail_listItem_behavior);
				TextView score = (TextView) v.findViewById(R.id.vehicle_detail_listItem_score);
				TextView money = (TextView) v.findViewById(R.id.vehicle_detail_listItem_money);
				View serviceHead = v.findViewById(R.id.serviceHead);
				View oldAgainstHead = (View) v.findViewById(R.id.oldAgainstHead);
				CheckBox checkView = (CheckBox) v.findViewById(R.id.checkView);
				TextView tvBottom = (TextView) v.findViewById(R.id.tvBottom);
				View alarmToUpload = (View) v.findViewById(R.id.vehicle_detail_alert_to_upload);
				TextView tvOrderNumber = (TextView) v.findViewById(R.id.tvOrderNumber);
				TextView tvOrderSubmitTime = (TextView) v.findViewById(R.id.tvOrderSubmitTime);
				View payOnlineHead = v.findViewById(R.id.payOnlineHead);
				View disposingHead = v.findViewById(R.id.disposingHead);
				View unSupportHead = v.findViewById(R.id.unSupportHead);
				TextView payOnlineNum = (TextView) v.findViewById(R.id.payOnlineNum);
				TextView payOnlineMoney = (TextView) v.findViewById(R.id.payOnlineMoney);
				TextView disposingNum = (TextView) v.findViewById(R.id.disposingNum);
				TextView disposingMoney = (TextView) v.findViewById(R.id.disposingMoney);
				TextView listItem_payment_status = (TextView) v.findViewById(R.id.listItem_payment_status);
				TextView data_source = (TextView) v.findViewById(R.id.data_source);
				TextView vehicle_detail_listItem_error_recovery = (TextView) v.findViewById(R.id.vehicle_detail_listItem_error_recovery);
				TextView tvOrderStatus = (TextView) v.findViewById(R.id.tvOrderStatus);
				TextView vehicle_detail_recordType = (TextView) v.findViewById(R.id.vehicle_detail_recordType);
				root.put("rootView", rootView);
				root.put("time", time);
				root.put("tvStatus", tvStatus);
				root.put("address", address);
				root.put("behavior", behavior);
				root.put("score", score);
				root.put("money", money);
				root.put("serviceHead", serviceHead);
				root.put("oldAgainstHead", oldAgainstHead);
				root.put("checkView", checkView);
				root.put("tvBottom", tvBottom);
				root.put("alarmToUpload", alarmToUpload);
				root.put("tvOrderNumber", tvOrderNumber);
				root.put("tvOrderSubmitTime", tvOrderSubmitTime);
				root.put("payOnlineHead", payOnlineHead);
				root.put("disposingHead", disposingHead);
				root.put("unSupportHead", unSupportHead);
				root.put("payOnlineNum", payOnlineNum);
				root.put("payOnlineMoney", payOnlineMoney);
				root.put("disposingNum", disposingNum);
				root.put("disposingMoney", disposingMoney);
				root.put("listItem_payment_status", listItem_payment_status);
				root.put("data_source", data_source);
				root.put("vehicle_detail_listItem_error_recovery", vehicle_detail_listItem_error_recovery);
				root.put("tvOrderStatus", tvOrderStatus);
				root.put("vehicle_detail_recordType", vehicle_detail_recordType);
			}
			return root;
		}

		@Override
		public List<AgainstRecord> executeFirst() throws Exception {
			try {
				records = linkeList;
				List<AgainstRecord> listTemp = mApplication.dbCache.getAgainstRecordsByNum(vehicleNumber);
				payMoney = 0;
				if(listTemp != null && !listTemp.isEmpty()){
					List<AgainstRecord> listDisposed = mApplication.dbCache.getCompletedRecords(vehicleNumber);
					if(listDisposed != null && !listDisposed.isEmpty()){
						removeAgainstResords(listTemp, listDisposed);
						listTemp.addAll(listDisposed);
					}
					List<AgainstRecord> listTicketPay = mApplication.dbCache.getTicketPaymentAgainstRecords(vehicleNumber);
					if(listTicketPay != null && !listTicketPay.isEmpty()){
						removeAgainstResords(listTemp, listTicketPay);
						listTemp.addAll(0,listTicketPay);
					}
					List<AgainstRecord> listDisposing = mApplication.dbCache.getDisposingAgainstRecords(vehicleNumber);
					if(listDisposing != null && !listDisposing.isEmpty()){
						nDisposingNum = 0;
						nDisposingMoney = 0;
						for(AgainstRecord ar : listDisposing){
							nDisposingNum++;
							nDisposingMoney += ar.getMoney();
						}
						removeAgainstResords(listTemp, listDisposing);
						listTemp.addAll(0,listDisposing);
					}
					List<AgainstRecord> listPayOnline = mApplication.dbCache.getPayOnlineAgainstRecords(vehicleNumber);
					if(listPayOnline != null && !listPayOnline.isEmpty()){
						nPayOnlineNum = 0;
						nPayOnlineMoney = 0;
						for(AgainstRecord ar : listPayOnline){
							nPayOnlineNum++;
							nPayOnlineMoney += ar.getMoney();
						}
						removeAgainstResords(listTemp, listPayOnline);
						listTemp.addAll(0,listPayOnline);
					}
					recordsChecked.clear();
					for(AgainstRecord ar : listTemp){
						if(ar.getStatus() == 0 && ar.getCanSubmit() == 0 && ar.getScore() >= 0 && (ar.getOrderStatus() == 0 || ar.getOrderStatus() == 2 || ar.getOrderStatus() == 14 || ar.getOrderStatus() == 20) && ar.getMoney() > 0){
							recordsChecked.add(ar);
							payMoney += ar.getMoney();
						}
					}
				}
				return listTemp;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public int getLayoutId(int index) {
			if(linkeList.get(index) != null){
				if(linkeList.get(index).getResultType() != null && linkeList.get(index).getResultType() == 1){
					isImage = true;
					return R.layout.daze_vehicle_detail_item_image;
				}
			}
			return R.layout.daze_vehicle_detail_item;
		}

		@Override
		public void showLoading(boolean show) {
			if (show) {
				showloading(true);
			} else {
				String cityIds = null;
				if(vehicle != null){
					if(!StringUtils.isEmpty(vehicle.getCityId())){
						List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(vehicle.getCityId());
						if(cvs != null && !cvs.isEmpty()){
							for(CityVehicle cv : cvs){
								if(!(cv.getValid() != null && cv.getValid() == true)){
									if(cityIds == null)
										cityIds = "" + cv.getId();
									else
										cityIds += ("," + cv.getId());
								}
							}
						}
						
					}
				}
				shareNoAgainst.setVisibility(View.GONE);
//				if(!StringUtils.isEmpty(vehicle.getCityUnValid()))
				if(!StringUtils.isEmpty(cityIds))
				{
					updateUnSupportCity();
				}
				if(linkeList == null || linkeList.isEmpty()){
					if(!StringUtils.isEmpty(cityIds)){
						if(listView.indexOfChild(listEmpty) != -1){
							listView.removeFooterView(listEmpty);
						}
						if(listView.indexOfChild(cityUnsupportView) == -1){
							listView.addFooterView(cityUnsupportView, null, false);
						}
					}
					else{
						if(listView.indexOfChild(cityUnsupportView) != -1){
							listView.removeFooterView(cityUnsupportView);
						}
						if(listView.indexOfChild(listEmpty) == -1){
							listView.addFooterView(listEmpty, null, false);
						}
					}
				}
				else{
					if(listView.indexOfChild(listEmpty) != -1){
						listView.removeFooterView(listEmpty);
					}
					if(listView.indexOfChild(cityUnsupportView) != -1){
						listView.removeFooterView(cityUnsupportView);
					}	
				}
				int score = 0;
				int money = 0;
				int newNum = 0;
				int oldNum = 0;
				for (AgainstRecord record : linkeList) {
					if (record.getStatus() == 1) {
						oldNum++;
					} else {
						if (record.getScore() != -1)
							score += record.getScore();
						if (record.getMoney() != -1)
							money += record.getMoney();
						newNum++;
					}
				}
				vehicle.setScore(score);
				vehicle.setMoney(money);
				vehicle.setNewNum(newNum);
				vehicle.setOldNum(oldNum);
				mApplication.dbCache.updateVehicle(vehicle);
				moneyText.setText("" + money);
				scoreText.setText("" + score);
				newNumText.setText("" + newNum);
				if(add){
					if(searchFailView.getVisibility() == View.GONE && refreshingView.getVisibility() == View.GONE && refreshingView180.getVisibility() == View.GONE && !mApplication.containsTask(vehicleNumber))
						mPullRefreshScrollView.setRefreshing(false);
				}
				add = false;
				if(recordsChecked != null && !recordsChecked.isEmpty()){
					chargingView.setVisibility(View.VISIBLE);
					chargingView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_in_from_bottom));
					tvRecordNumber.setText("已选"+ recordsChecked.size() + "条违章，罚款共计");
					tvRecordMoney.setText("¥" + payMoney);
					order_submit_selected_num_text.setText("" + recordsChecked.size() + "条");
					order_submit_money_text.setText(df.format(payMoney));
				}
				showloading(false);
			}
		}
	}

	private void delVehicle() {
		showloading(true);
		JSONObject prop = new JSONObject();
		try {
			prop.put("vehicle_num", vehicle.getVehicleNum());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		new AsyncTask<Void, Void, GetResultResponse>() {
			protected GetResultResponse doInBackground(Void... params) {
				VehicleDelRequest request = new VehicleDelRequest();
				request.setParams(mApplication.getUserId(),mApplication.getId(), vehicleNumber);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetResultResponse result) {
				showloading(false);
				if (result != null) {
					if (result.getCode() != null && result.getCode() == 0) {
						if (result.getData().getResult()) {							
							mApplication.dbCache.deleteVehicle(vehicleNumber);
							mApplication.dbCache.deleteAgainstRecord(vehicleNumber);
							mApplication.dbCache.deleteVehicleAuthByVehicleNumber(vehicleNumber);
							Toast.makeText(VehicleDetailActivity.this,
									"删除车辆成功", Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(VehicleDetailActivity.this,
									"删除车辆失败", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(VehicleDetailActivity.this,
								result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(VehicleDetailActivity.this, "网络中断，请稍候重试",
							Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PHONE_REGIST_REQUEST:
			if (mApplication.getpId() != 0) {
				getAuthDetail();
			}
			break;
		case DELETE_REQUEST:
			if(resultCode == RESULT_OK)
				delVehicle();
			break;
		case EDIT_VEHICLE_REQUEST:
			if(resultCode == Constants.RESULT_TYPE_CHANGED){
				if(data != null){
					UserVehicle temp = (UserVehicle) data.getSerializableExtra("vehicle");
					if(temp != null)
						vehicle = temp;
				}
			}
			break;
		default:
			break;
		}
	}
	
	private void getAuthDetail(){
		new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
			@Override
			protected GetAuthDetaiResponse doInBackground(Void... params) {
				GetAuthDetaiRequest request = new GetAuthDetaiRequest();
				request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleNumber);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(GetAuthDetaiResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						AuthDetailJson data = result.getData();
						if(data != null){
							List<VehicleAuth> listAuth = data.getList();
							if(listAuth != null && !listAuth.isEmpty()){
								vehicleAuth = listAuth.get(0);
								if(vehicleAuth != null)
									mApplication.dbCache.saveVehicleAuth(vehicleAuth);
								else
									vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicleNumber);
								if(vehicleAuth.getResidueDegree() != null){
									free_rescue_num.setText("" + vehicleAuth.getResidueDegree().intValue());
								}
							}
						}
					}
				}
			}
		}.execute();
	}

	private class GetDataTask extends AsyncTask<Void, Void, GetAgainstRecordListResponse> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(listView.indexOfChild(listEmpty) != -1){
				listView.removeFooterView(listEmpty);
			}
			if(listView.indexOfChild(cityUnsupportView) != -1){
				listView.removeFooterView(cityUnsupportView);
			}
		}
		
		@Override
		protected GetAgainstRecordListResponse doInBackground(Void... params) {
			GetAgainstRecordListRequest request = new GetAgainstRecordListRequest();
			long updateTime = 0;
			try{
				updateTime = Long.parseLong(vehicle.getUpdateTime());
			}
			catch(Exception E){
				updateTime = 0;
				E.printStackTrace();
			}
			request.setParams(mApplication.getUserId(), vehicleNumber, updateTime);
			try {
				return mApplication.client.execute(request);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final GetAgainstRecordListResponse result) {
			progressHandler.removeMessages(2);
			nStep = (100 -nProgress)/5f;
			Message message = new Message();
			message.what = 3;
			progressHandler.sendMessageDelayed(message, 100);
			if(KplusConstants.isDebug){
				if(result != null && result.getBody() != null)
					System.out.println("VehicleDetailActivity===>GetDataTask:result = " + result.getBody());
			}
			progressHandler.postDelayed(new Runnable() {				
				@Override
				public void run() {
					dealwithAgainstResult(result);
				}
			}, 600);
			super.onPostExecute(result);
		}
	}

	
	class AgainstRecordComparator implements Comparator<AgainstRecord>{
		@Override
		public int compare(AgainstRecord arg0, AgainstRecord arg1)
		{
			if(arg0.getStatus().intValue() == arg1.getStatus().intValue()){
				if((arg0.getScore() == 0 && arg1.getScore() != 0) || (arg0.getScore() != 0 && arg1.getScore() == 0)){
					return Math.abs(arg0.getScore().intValue()) - Math.abs(arg1.getScore().intValue());
				}
				else if(arg0.getScore() == 0 && arg1.getScore() == 0){
					if((arg0.getOrderStatus() == 0 || arg0.getOrderStatus() == 2 || arg0.getOrderStatus() == 14 || arg0.getOrderStatus() == 20) && arg1.getOrderStatus() != 0 && arg1.getOrderStatus() != 2 && arg1.getOrderStatus() != 14 && arg1.getOrderStatus() != 20){
						return -1;
					}
					else if((arg1.getOrderStatus() == 0 || arg1.getOrderStatus() == 2 || arg1.getOrderStatus() == 14 || arg1.getOrderStatus() == 20) && arg0.getOrderStatus() != 0 && arg0.getOrderStatus() != 2 && arg0.getOrderStatus() != 14 && arg0.getOrderStatus() != 20){
						return 1;
					}
				}
				return arg1.getTime().compareTo(arg0.getTime());
			}			
			return arg0.getStatus().intValue() - arg1.getStatus().intValue();
		}		
	}

	@Override
	protected void onResume()
	{
		payMemberCount = sp.getLong("authenticationMemberCount", 0)/57;
		if(payMemberCount > 0)
			tvPayMemberNumber.setText("目前已有" + payMemberCount + "人在线交罚款成功");
		else{
			getClientLicenceCount();
		}
		long requestTime = vehicle.getRequestTime();
		long returnTime = vehicle.getReturnTime();
		if(requestTime > 0 && returnTime - requestTime < 0){
			nDegrees = (int) ((System.currentTimeMillis() - requestTime)/1000);
			if(nDegrees < 180 && mApplication.containsTask(vehicleNumber)){
				if(refreshingView.getVisibility() == View.GONE && refreshingView180.getVisibility() == View.GONE){
					tvRefreshingProgress180.setText(100*nDegrees/180 + "%");
					imageRefreshProgress180.setnRadius(16);
					imageRefreshProgress180.setnAngel(nDegrees*2);
					imageRefreshProgress180.postInvalidate();
					refreshingView180.setVisibility(View.VISIBLE);
					Message message = new Message();
					message.what = 1;
					progressHandler.sendMessageDelayed(message, 1000);
				}					
			}
			else{
//				if(!mApplication.containsTask(vehicleNumber))
				{
					progressHandler.removeMessages(1);
					refreshingView180.setVisibility(View.GONE);
				}
			}
		}
		else{
			progressHandler.removeMessages(1);
			refreshingView180.setVisibility(View.GONE);
		}
		if(vehicle.isHasSearchFail()){
			searchFailView.setVisibility(View.VISIBLE);
			if(verifyCodeError){
				searchFailInfo.setText("验证码错误，查询失败了");
			}
			else{
				searchFailInfo.setText("小牛遇到麻烦了，正在努力解决中...");
			}
		}
		else
			searchFailView.setVisibility(View.GONE);
		vehicle.setHasSearchFail(false);
		if(vehicle.isHasParamError() || vehicle.isHasRuleError()){
			getFrameAndMotorLength();
			showCorrectVehicleInfoView();
			vehicleErrorView.setVisibility(View.VISIBLE);
		}
		setAdapter();		
		if(mApplication.getpId() != 0)
			getAuthDetail();
		super.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		if(aganistRecordsReceiver != null)
			LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(aganistRecordsReceiver, new IntentFilter("com.kplus.car.getagainstRecords"));
		super.onStart();
	}

	@Override
	protected void onStop() {
		if(aganistRecordsReceiver != null)
			LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(aganistRecordsReceiver);
		super.onStop();
	}

	private boolean againRecordChecked(AgainstRecord record){
		boolean result = false;
		for(AgainstRecord ar : recordsChecked){
			if(record.getId().longValue() == ar.getId().longValue()){
				result = true;
				break;
			}
		}
		return result;
	}
	
	private void getPrice(){
		String recordIds = "";
		for (AgainstRecord record : recordsChecked) {
			if ("".equals(recordIds)) {
				recordIds = "" + record.getId();
			} else {
				recordIds += "," + record.getId();
			}
		}
		order.setRecordIds(recordIds);
		final String finalRecordIds = recordIds;
		new AsyncTask<Void, Void, GetPriceValueResponse>(){
			
			protected void onPreExecute() {
				payMoneyReduction = 0;
				serviceChargeReduction = 0;
				order_submit_total_price_text.setText("");
				order_submit_total_price_loading.setVisibility(View.VISIBLE);
				order_submit_service_price_loading.setVisibility(View.VISIBLE);
				serviceChargeCashUseInfo_loading.setVisibility(View.VISIBLE);
				fineCashUseInfo_loading.setVisibility(View.VISIBLE);
				tvServiceChargeCashUseInfo.setText("");
				tvFineCashUseInfo.setText("");
				order_submit_price_text.setText("");
				order_submit_reduce_price_text.setVisibility(View.GONE);
				order_submit_reduce_money_text.setVisibility(View.GONE);
				tvSubmitCommit.setEnabled(false);
				tvSubmitCommit.setBackgroundResource(R.drawable.daze_btn_bg_light_gray_2);
			}

			@Override
			protected GetPriceValueResponse doInBackground(Void... params) {
				try{
					GetPriceValueRequest request = new GetPriceValueRequest();
					request.setParams(finalRecordIds, mApplication.getpId());
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(GetPriceValueResponse result) {
				order_submit_total_price_loading.setVisibility(View.GONE);
				order_submit_service_price_loading.setVisibility(View.GONE);
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						serviceCharge = result.getData().getPrice() == null ? -1 : result.getData().getPrice();
						if(Math.abs(serviceCharge + 1) < 0.00001){
							tvSubmitCommit.setText("所选违章地暂未开通服务");
							order_submit_price_text.setText("0.00");
							order_submit_total_price_text.setText("0.00");
							serviceChargeCashUseInfo_loading.setVisibility(View.GONE);
							fineCashUseInfo_loading.setVisibility(View.GONE);
							return;
						}
						else{
							tvSubmitCommit.setEnabled(true);
							tvSubmitCommit.setText("提交订单");
							tvSubmitCommit.setBackgroundColor(getResources().getColor(R.color.daze_white));
							order_submit_price_text.setText(df.format(serviceCharge));
							totalMoney = payMoney + serviceCharge;
							order_submit_total_price_text.setText(df.format(totalMoney));
							order.setPrice(totalMoney);
						}
						if(result.getData().getReduced()){
							if(result.getData().getReducePrice() != null){
								float moneyReduce = result.getData().getReducePrice();
								if(moneyReduce < 0){
									moneyReduce = 0.00f;
								}
								float moneyTemp = payMoney - moneyReduce;
								if(moneyTemp < 0){
									moneyTemp = 0.00f;
									order_submit_money_text.setText("0");
								}
								else
									order_submit_money_text.setText(df.format(moneyTemp));
								totalMoney = moneyTemp + serviceCharge;
								order.setPrice(totalMoney);
								order_submit_total_price_text.setText(df.format(totalMoney));
							}
						}
						getValidCouponList();
					}
					else{
						order_submit_price_text.setText("获取服务费失败");
						ToastUtil.TextToast(VehicleDetailActivity.this, result.getMsg(), 1000, Gravity.CENTER);
					}
				}
				else{
					order_submit_price_text.setText("获取服务费失败");
					ToastUtil.TextToast(VehicleDetailActivity.this, "网络中断，请稍后重试", 1000, Gravity.CENTER);
				}
			}
			
		}.execute();
	}
	
	private void getValidCouponList(){
		new AsyncTask<Void, Void, GetValidCouponListResponse>(){
			protected void onPreExecute() {
				tvFineCashUseInfo.setText("");
				tvServiceChargeCashUseInfo.setText("");
			}
			@Override
			protected GetValidCouponListResponse doInBackground(Void... params) {
				try{
					GetValidCouponListResquest request = new GetValidCouponListResquest();
					request.setParams(1, mApplication.getId(), totalMoney);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(GetValidCouponListResponse result) {
				try{
					serviceChargeCashUseInfo_loading.setVisibility(View.GONE);
					fineCashUseInfo_loading.setVisibility(View.GONE);
					if(result != null){
						if(result.getCode() != null && result.getCode() == 0){
							CouponListJson data = result.getData();
							if(data != null){
								List<CouponList> couponlists = data.getList();
								if(couponlists != null && !couponlists.isEmpty()){
									fineCouponList.clear();
									serviceChargeCouponList.clear();
									fineCouponListSave.clear();
									serviceChargeCouponListSave.clear();
									for(CouponList item : couponlists){
										if(item.getName().contains("benjin")){
											if(item.getList() != null && !item.getList().isEmpty()){
												fineCouponList.addAll(item.getList());
												Collections.sort(fineCouponList, new CouponComparator());
												fineCouponListSave.addAll(fineCouponList);
												fineCouponList.add(0, nullCoupon);
												currentFineCoupon = fineCouponList.get(1);
												if(currentFineCoupon.getAmount() == null){
													tvFineCashUseInfo.setText(currentFineCoupon.getName());
													order_submit_reduce_money_text.setVisibility(View.GONE);
												}
												else{
													tvFineCashUseInfo.setText(currentFineCoupon.getName() + "¥" + currentFineCoupon.getAmount().intValue());
													if(totalMoney - serviceCharge > currentFineCoupon.getAmount().intValue()){
														payMoneyReduction = currentFineCoupon.getAmount().intValue();
														order_submit_reduce_money_text.setText("-" + payMoneyReduction);
													}
													else{
														payMoneyReduction = (int) (totalMoney - serviceCharge);
														order_submit_reduce_money_text.setText("-" + df.format(payMoneyReduction));
													}
													order_submit_reduce_money_text.setVisibility(View.VISIBLE);
												}
											}
											else{
												tvFineCashUseInfo.setText("无代金券可用");
											}
										}
										else if(item.getName().contains("fuwufei")){
											if(item.getList() != null && !item.getList().isEmpty()){
												if(serviceCharge > 0){
													serviceChargeCouponList.addAll(item.getList());
													Collections.sort(serviceChargeCouponList, new CouponComparator());
													serviceChargeCouponListSave.addAll(serviceChargeCouponList);
													serviceChargeCouponList.add(0, nullCoupon);
													currentServiceChargeCoupon = serviceChargeCouponList.get(1);
													if(currentFineCoupon != null && currentFineCoupon.getId().longValue() == currentServiceChargeCoupon.getId().longValue()){
														if(serviceChargeCouponList.size() > 2)
															currentServiceChargeCoupon = serviceChargeCouponList.get(2);
														else
															currentServiceChargeCoupon = serviceChargeCouponList.get(0);
													}
													if(currentServiceChargeCoupon.getAmount() == null){
														tvServiceChargeCashUseInfo.setText(currentServiceChargeCoupon.getName());
														order_submit_reduce_price_text.setVisibility(View.GONE);
													}
													else{
														tvServiceChargeCashUseInfo.setText(currentServiceChargeCoupon.getName() + "¥" + currentServiceChargeCoupon.getAmount().intValue());
														if(serviceCharge > currentServiceChargeCoupon.getAmount().intValue()){
															serviceChargeReduction = currentServiceChargeCoupon.getAmount().intValue();
															order_submit_reduce_price_text.setText("-" + serviceChargeReduction);
														}
														else{
															serviceChargeReduction = (int) serviceCharge;
															order_submit_reduce_price_text.setText("-" + df.format(serviceChargeReduction));
														}
														order_submit_reduce_price_text.setVisibility(View.VISIBLE);
													}
												}
												else{
													tvServiceChargeCashUseInfo.setText("不使用代金券");
												}
											}
											else{
												if(serviceCharge > 0)
													tvServiceChargeCashUseInfo.setText("无代金券可用");
												else
													tvServiceChargeCashUseInfo.setText("不使用代金券");
											}
										}
									}
									if(fineCouponList.isEmpty())
										tvFineCashUseInfo.setText("无代金券可用");
									if(serviceChargeCouponList.isEmpty())
										tvServiceChargeCashUseInfo.setText("无代金券可用");
									if(Math.abs(totalMoney - payMoneyReduction - serviceChargeReduction) > 0.00001)
										order_submit_total_price_text.setText(df.format(totalMoney - payMoneyReduction - serviceChargeReduction));
									else
										order_submit_total_price_text.setText("0.01");
								}
								else{
									tvFineCashUseInfo.setText("无代金券可用");
									tvServiceChargeCashUseInfo.setText("无代金券可用");
								}
							}
							else{
								tvFineCashUseInfo.setText("获取代金券失败");
								tvServiceChargeCashUseInfo.setText("获取代金券失败");
							}
						}
						else{
							tvFineCashUseInfo.setText("获取代金券失败");
							tvServiceChargeCashUseInfo.setText("获取代金券失败");
						}
					}
					else{
						tvFineCashUseInfo.setText("获取代金券失败");
						tvServiceChargeCashUseInfo.setText("获取代金券失败");
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	private void submitOrder(){
		new AsyncTask<Void, Void, OrderAddResponse>(){
			protected void onPreExecute() {
				showloading(true);
			}

			@Override
			protected OrderAddResponse doInBackground(Void... params) {
				try {
					OrderAddRequest request = new OrderAddRequest();
					order.setpId(mApplication.getpId());
					JSONObject jo = new JSONObject();
					if(currentFineCoupon != null && currentFineCoupon.getId() != null && currentFineCoupon.getId() != 0)
						jo.put("benjin", currentFineCoupon.getId().longValue());
					if(currentServiceChargeCoupon != null && currentServiceChargeCoupon.getId() != null && currentServiceChargeCoupon.getId() != 0)
						jo.put("fuwufei", currentServiceChargeCoupon.getId().longValue());
					request.setParams(order, mApplication.getId(), jo.toString());
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(OrderAddResponse result) {
				showloading(false);
				if (result != null) {
					if(result.getCode() != null && result.getCode() == 0){
						String orderNum = result.getData().getOrderNum();
						if (!StringUtils.isEmpty(orderNum)) {
							Intent update = new Intent(VehicleDetailActivity.this, UpdateAgainstRecords.class);
							update.putExtra("vehicleNumber", vehicleNumber);
							startService(update);
							long orderId = result.getData().getOrderId();
							Intent intent = new Intent(VehicleDetailActivity.this,OrderActivity.class);
							intent.putExtra("orderId", orderId);
							startActivity(intent);
							finish();
							JSONObject prop1 = new JSONObject();
							try {
								prop1.put("order_num", orderId);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {
							ToastUtil.TextToast(VehicleDetailActivity.this, "订单提交失败", 1000, Gravity.CENTER);
						}
					}
					else{
						ToastUtil.TextToast(VehicleDetailActivity.this, result.getMsg(), 1000, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(VehicleDetailActivity.this, "网络中断，请稍后重试", 1000, Gravity.CENTER);
				}
			}
			
		}.execute();
	}

	class GetInviteContentTask extends AsyncTask<Void, Void, GetUserInviteContentResponse>{
		private String errorText = "网络中断，请稍后重试";
		@Override
		protected void onPreExecute() {
			showloading(true);
			super.onPreExecute();
		}
		
		@Override
		protected GetUserInviteContentResponse doInBackground(Void... params) {
			try{
				GetUserInviteContentRequest request = new GetUserInviteContentRequest();
				request.setParams(mApplication.getId(), shareType);
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				errorText = e.toString();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(GetUserInviteContentResponse result) {
			try{
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						Intent intent = new Intent(VehicleDetailActivity.this, ShareInService.class);
						intent.putExtra("shareType", "123");
						String title = result.getData().getTitle();
						String summary = result.getData().getSummary();
						String content = result.getData().getContent();
						String inviteUrl = result.getData().getInviteUrl();
						String imgUrl = result.getData().getImgUrl();
						if(shareType == 102){
							String regex = "$1";
							content = content.replace(regex, ""+ records.size());
							summary = summary.replace(regex, ""+ records.size());
						}
						intent.putExtra("title", title);
						intent.putExtra("summary", summary);
						intent.putExtra("content", content);
						intent.putExtra("inviteUrl", inviteUrl);
						intent.putExtra("imgUrl", imgUrl);
						intent.putExtra("contentType", shareType);
						startActivity(intent);
					}
					else{
						Toast.makeText(VehicleDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(VehicleDetailActivity.this, errorText, Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(VehicleDetailActivity.this, "获取分享内容失败", Toast.LENGTH_SHORT).show();
			}
			showloading(false);
			super.onPostExecute(result);
		}
		
	}
	
	private void getClientLicenceCount(){
		new Thread(){
			public void run(){
				try{
					GetClientLicenceCountRequest request = new GetClientLicenceCountRequest();
					GetClientLicenceCountResponse result = mApplication.client.execute(request);
					if(result != null){
						if(result.getCode() != null && result.getCode() == 0){
							long count = result.getData().getCount();
							payMemberCount = count/57;
							sp.edit().putLong("authenticationMemberCount", count).commit();
							tvPayMemberNumber.post(new Runnable() {								
								@Override
								public void run() {
									tvPayMemberNumber.setText("目前已有" + payMemberCount + "人在线交罚款成功");
								}
							});
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void updateUI(){
		if(records != null && !records.isEmpty())
			records.clear();
		else if(records == null)
			records = new ArrayList<AgainstRecord>();
		payMoney = 0;
		if(recordsChecked != null && !recordsChecked.isEmpty())
			recordsChecked.clear();
		else if(recordsChecked == null)
			recordsChecked = new ArrayList<AgainstRecord>();
		int score = 0;
		int money = 0;
		int newNum = 0;
		int oldNum = 0;
		List<AgainstRecord> listTemp = mApplication.dbCache.getAgainstRecordsByNum(vehicleNumber);
		if(listTemp != null && !listTemp.isEmpty()){
			List<AgainstRecord> listDisposed = mApplication.dbCache.getCompletedRecords(vehicleNumber);
			if(listDisposed != null && !listDisposed.isEmpty()){
				removeAgainstResords(listTemp, listDisposed);
				listTemp.addAll(listDisposed);
			}
			List<AgainstRecord> listTicketPay = mApplication.dbCache.getTicketPaymentAgainstRecords(vehicleNumber);
			if(listTicketPay != null && !listTicketPay.isEmpty()){
				removeAgainstResords(listTemp, listTicketPay);
				listTemp.addAll(0,listTicketPay);
			}
			List<AgainstRecord> listDisposing = mApplication.dbCache.getDisposingAgainstRecords(vehicleNumber);
			if(listDisposing != null && !listDisposing.isEmpty()){
				nDisposingNum = 0;
				nDisposingMoney = 0;
				for(AgainstRecord ar : listDisposing){
					nDisposingNum++;
					nDisposingMoney += ar.getMoney();
				}
				removeAgainstResords(listTemp, listDisposing);
				listTemp.addAll(0,listDisposing);
			}
			List<AgainstRecord> listPayOnline = mApplication.dbCache.getPayOnlineAgainstRecords(vehicleNumber);
			if(listPayOnline != null && !listPayOnline.isEmpty()){
				nPayOnlineNum = 0;
				nPayOnlineMoney = 0;
				for(AgainstRecord ar : listPayOnline){
					nPayOnlineNum++;
					nPayOnlineMoney += ar.getMoney();
				}
				removeAgainstResords(listTemp, listPayOnline);
				listTemp.addAll(0,listPayOnline);
			}
			records.addAll(listTemp);
			if(listView.indexOfChild(listEmpty) != -1){
				listView.removeFooterView(listEmpty);
			}
			if(listView.indexOfChild(cityUnsupportView) != -1){
				listView.removeFooterView(cityUnsupportView);
			}
			try{
				for(AgainstRecord ar : listTemp){
					if (ar.getStatus() == 1) {
						oldNum++;
					}
					else{
						if(ar.getStatus() == 0 && ar.getCanSubmit() == 0 && ar.getScore() == 0 && (ar.getOrderStatus() == 0 || ar.getOrderStatus() == 2 || ar.getOrderStatus() == 14 || ar.getOrderStatus() == 20) && ar.getMoney() > 0){
							recordsChecked.add(ar);
							payMoney += ar.getMoney();
						}
						if (ar.getScore() != -1)
							score += ar.getScore();
						if (ar.getMoney() != -1)
							money += ar.getMoney();
						newNum++;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			String cityIds = null;
			if(vehicle != null){
				if(!StringUtils.isEmpty(vehicle.getCityId())){
					List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(vehicle.getCityId());
					if(cvs != null && !cvs.isEmpty()){
						for(CityVehicle cv : cvs){
							if(!(cv.getValid() != null && cv.getValid() == true)){
								if(cityIds == null)
									cityIds = "" + cv.getId();
								else
									cityIds += ("," + cv.getId());
							}
						}
					}
					
				}
			}
			if(!StringUtils.isEmpty(cityIds)){
				if(listView.indexOfChild(listEmpty) != -1)
					listView.removeFooterView(listEmpty);
				if(listView.indexOfChild(cityUnsupportView) == -1)
					listView.addFooterView(cityUnsupportView, null, false);
			}
			else{
				if(listView.indexOfChild(cityUnsupportView) != -1)
					listView.removeFooterView(cityUnsupportView);
				if(listView.indexOfChild(listEmpty) == -1)
					listView.addFooterView(listEmpty, null, false);
			}			
		}
		adapter.notifyDataSetChanged();
		vehicle.setScore(score);
		vehicle.setMoney(money);
		vehicle.setNewNum(newNum);
		vehicle.setOldNum(oldNum);
		mApplication.dbCache.updateVehicle(vehicle);
		moneyText.setText("" + money);
		scoreText.setText("" + score);
		newNumText.setText("" + newNum);
		tvRecordNumber.setText("已选"+ recordsChecked.size() + "条违章，罚款共计");
		tvRecordMoney.setText("¥" + payMoney);
		order_submit_selected_num_text.setText("" + recordsChecked.size() + "条");
		order_submit_money_text.setText(df.format(payMoney));
		if(recordsChecked != null && !recordsChecked.isEmpty()){
			if(chargingView.getVisibility() == View.GONE){
				chargingView.setVisibility(View.VISIBLE);
				chargingView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_in_from_bottom));
			}
		}
		else{
			if(chargingView.getVisibility() == View.VISIBLE){
				chargingView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_out_to_bottom));
				chargingView.postDelayed(new Runnable() {							
					@Override
					public void run() {
						chargingView.setVisibility(View.GONE);
					}
				}, 500);
			}
		}
		long updateTime = 0;
		if(!StringUtils.isEmpty(vehicle.getUpdateTime())){
			try{
				updateTime = Long.parseLong(vehicle.getUpdateTime());
			}
			catch(Exception e){
				updateTime = 0;
				e.printStackTrace();
			}
		}
		if(updateTime == 0)
			updateInfoView.setVisibility(View.GONE);
		else
			updateInfoView.setVisibility(View.VISIBLE);
		if(vehicle.getNewRecordNumber() != 0){
			if(vehicle.getNewRecordNumber() == 1)
				tvPreUpdateTime.setText("您有1条违章信息有更新");
			else
				tvPreUpdateTime.setText("您有" + vehicle.getNewRecordNumber() + "条新的违章");
		}
		else{
			tvPreUpdateTime.setText("恭喜您,没有查询到新的违章");
		}
		final String strTemp = "数据更新时间" + DateUtil.getShowTimeOnInterval(updateTime);
		tvPreUpdateTime.postDelayed(new Runnable() {			
			@Override
			public void run() {
				tvPreUpdateTime.setText(strTemp);
			}
		}, 5000);
//		vehicle.setHasSearchFail(false);
		vehicle.setNewRecordNumber(0);
		mApplication.dbCache.updateVehicle(vehicle);
	}
	
	private void removeAgainstResords(List<AgainstRecord> all, List<AgainstRecord> del){
		for(AgainstRecord ardel : del){
			for(AgainstRecord arAll : all){
				if(arAll.getId().longValue() == ardel.getId().longValue()){
					all.remove(arAll);
					break;
				}
			}
		}
	}
	
	private void getFrameAndMotorLength(){
		String cityId = vehicle.getCityId();
		if(!StringUtils.isEmpty(cityId)){
			List<CityVehicle> cityVehicles = mApplication.dbCache.getCityVehicles(cityId);
			if(cityVehicles != null && !cityVehicles.isEmpty()){
				needVehicleOwner = false;
				needDriverName = false;
				needAccountAndPassword = false;
				needVehicleRegCerNo = false;
				nFrameLength = 0;
				nMotorLength = 0;
				ownerIdLen = 0;
				driverIdLen = 0;
				for(CityVehicle cityVehicle : cityVehicles){
					int nFrameLengthTemp = cityVehicle.getFrameNumLen();
					if(nFrameLengthTemp == -1)
						nFrameLengthTemp = 99;
					if(nFrameLengthTemp > nFrameLength)
						nFrameLength = nFrameLengthTemp;
					int nMotorLengthTemp = cityVehicle.getMotorNumLen();
					if(nMotorLengthTemp == -1)
						nMotorLengthTemp = 99;
					if(nMotorLengthTemp > nMotorLength)
						nMotorLength = nMotorLengthTemp;
					int ownerIdNoLen = cityVehicle.getOwnerIdNoLen() != null ? cityVehicle.getOwnerIdNoLen() : 0;
					if (ownerIdNoLen == -1) {
						ownerIdLen = 99;
					} else if (ownerIdNoLen > ownerIdLen) {
						ownerIdLen = ownerIdNoLen;
					}
					int drivingLicenseNoLen = cityVehicle.getDrivingLicenseNoLen() != null ? cityVehicle.getDrivingLicenseNoLen() : 0;
					if (drivingLicenseNoLen == -1) {
						driverIdLen = 99;
					} else if (drivingLicenseNoLen > driverIdLen) {
						driverIdLen = drivingLicenseNoLen;
					}
					if(cityVehicle.getOwner() != null && cityVehicle.getOwner()){
						needVehicleOwner = true;
					}
					if (cityVehicle.isDrivingLicenseName() != null && cityVehicle.isDrivingLicenseName())
						needDriverName = true;
					if(cityVehicle.getAccountLen() != null && cityVehicle.getAccountLen() != 0){
						needAccountAndPassword = true;
						if(!StringUtils.isEmpty(cityVehicle.getName())){
							website_account_view.setHint("请输入" + cityVehicle.getName() + "官网账号");
							website_password_view.setHint("请输入" + cityVehicle.getName() + "官网密码");
						}
					}
					if(cityVehicle.getMotorvehiclenumLen() != null && cityVehicle.getMotorvehiclenumLen() != 0)
						needVehicleRegCerNo = true;
				}
			}
		}
	}
	
	private void showCorrectVehicleInfoView(){
		frame_number_notice.setVisibility(View.GONE);
		motor_number_notice.setVisibility(View.GONE);
		owner_id_notice.setVisibility(View.GONE);
		driver_id_notice.setVisibility(View.GONE);
		frame_number_layout.setVisibility(View.GONE);
		motor_number_layout.setVisibility(View.GONE);
		vehicle_owner_layout.setVisibility(View.GONE);
		owner_id_layout.setVisibility(View.GONE);
		driver_name_layout.setVisibility(View.GONE);
		driver_id_layout.setVisibility(View.GONE);
		website_account_layout.setVisibility(View.GONE);
		website_password_layout.setVisibility(View.GONE);
		vehicleRegCerNo_layout.setVisibility(View.GONE);
//		String strError = "";
		if(nFrameLength > 0){
			String strMaxFrameNum = vehicle.getMaxFrameNum();
			if(strMaxFrameNum == null)
				strMaxFrameNum = "";
			String strFrameNum = vehicle.getFrameNum();
			if(strFrameNum == null)
				strFrameNum = "";
			frame_number_layout.setVisibility(View.VISIBLE);
			if(nFrameLength == 99){
				frame_number_view.setHint("请输入完整车架号");
				frame_number_notice.setText("请输入完整车架号");
				if(strFrameNum.length() != 17){
//					if(StringUtils.isEmpty(strError))
//						strError += "车架号";
//					else
//						strError += "、车架号";
					if(strMaxFrameNum.length() == 17){
						frame_number_view.setText(strFrameNum);
					}
					else if(strMaxFrameNum.length() > strFrameNum.length()){
						frame_number_view.setText(strMaxFrameNum);
						updateNotice(frame_number_notice, false);
					}
					else{
						frame_number_view.setText(strFrameNum);
						updateNotice(frame_number_notice, false);
					}
				}
				else{
					frame_number_view.setText(strFrameNum);
				}
			}
			else{
				frame_number_view.setHint("请输入车架号后" + nFrameLength + "位");
				frame_number_notice.setText("请输入车架号后" + nFrameLength + "位");
				if(strFrameNum.length() < nFrameLength){
//					if(StringUtils.isEmpty(strError))
//						strError += "车架号";
//					else
//						strError += "、车架号";
					if(strMaxFrameNum.length() > nFrameLength){
						frame_number_view.setText(strMaxFrameNum.substring(strMaxFrameNum.length() - nFrameLength));
					}
					else if(strMaxFrameNum.length() == nFrameLength){
						frame_number_view.setText(strMaxFrameNum);
					}
					else{
						frame_number_view.setText(strMaxFrameNum.length() > strFrameNum.length() ? strMaxFrameNum : strFrameNum);
						updateNotice(frame_number_notice, false);
					}
				}
				else if(strFrameNum.length() == nFrameLength){
					frame_number_view.setText(strFrameNum);
				}
				else{
//					if(StringUtils.isEmpty(strError))
//						strError += "车架号";
//					else
//						strError += "、车架号";
					frame_number_view.setText(strFrameNum.substring(strFrameNum.length() - nFrameLength));
				}
			}
		}
		if(nMotorLength > 0){
			String strMaxMotorNum = vehicle.getMaxMotorNum();
			if(strMaxMotorNum == null)
				strMaxMotorNum = "";
			String strMotorNum = vehicle.getMotorNum();
			if(strMotorNum == null)
				strMotorNum = "";
			motor_number_layout.setVisibility(View.VISIBLE);
			if(nMotorLength == 99){
				motor_number_view.setHint("请输入完整发动机号");
				motor_number_notice.setText("请输入完整发动机号");
				if(strMaxMotorNum.length() > strMotorNum.length()){
					motor_number_view.setText(strMaxMotorNum);
				}
				else{
					motor_number_view.setText(strMotorNum);
					if(strMotorNum.equals(""))
						updateNotice(motor_number_notice, false);
				}
			}
			else{
				motor_number_view.setHint("请输入发动机号后" + nMotorLength + "位");
				motor_number_notice.setText("请输入发动机号后" + nMotorLength + "位");
				if(strMotorNum.length() < nMotorLength){
//					if(StringUtils.isEmpty(strError))
//						strError += "发动机号";
//					else
//						strError += "、发动机号";
					if(strMaxMotorNum.length() > nMotorLength)
						motor_number_view.setText(strMaxMotorNum.substring(strMaxMotorNum.length() - nMotorLength));
					else if(strMaxMotorNum.length() == nMotorLength)
						motor_number_view.setText(strMaxMotorNum);
					else{
						motor_number_view.setText(strMaxMotorNum.length() > strMotorNum.length() ? strMaxMotorNum : strMotorNum);
						updateNotice(motor_number_notice, false);
					}
				}
				else if(strMotorNum.length() == nMotorLength){
					motor_number_view.setText(strMotorNum);
				}
				else{
//					if(StringUtils.isEmpty(strError))
//						strError += "发动机号";
//					else
//						strError += "、发动机号";
					motor_number_view.setText(strMotorNum.substring(strMotorNum.length() - nMotorLength));
				}
			}
		}
		if(needVehicleOwner){
			vehicle_owner_layout.setVisibility(View.VISIBLE);
			if(!StringUtils.isEmpty(vehicle.getVehicleOwner()))
				vehicle_owner_view.setText(vehicle.getVehicleOwner());
		}
		if(ownerIdLen > 0){
			String ownerId = vehicle.getOwnerId();
			if(ownerId == null)
				ownerId = "";
			owner_id_layout.setVisibility(View.VISIBLE);
			if(ownerIdLen == 99){
				owner_id_view.setHint("请输入完整车主身份证号");
				owner_id_notice.setText("请输入完整车主身份证号");
				owner_id_view.setText(ownerId);
				if(ownerId.equals(""))
					updateNotice(owner_id_notice, false);
			}
			else{
				owner_id_view.setHint("请输入车主身份证号后" + ownerIdLen + "位");
				owner_id_notice.setText("请输入车主身份证号后" + ownerIdLen + "位");
				if(ownerId.length() < ownerIdLen){
					owner_id_view.setText(ownerId);
					updateNotice(owner_id_notice, false);
				}
				else if(ownerId.length() == ownerIdLen){
					owner_id_view.setText(ownerId);
				}
				else{
					owner_id_view.setText(ownerId.substring(ownerId.length() - ownerIdLen));
				}
			}
		}
		if(needDriverName){
			driver_name_layout.setVisibility(View.VISIBLE);
			if(!StringUtils.isEmpty(vehicle.getDriverName()))
				driver_name_view.setText(vehicle.getDriverName());
		}
		if(driverIdLen > 0){
			String driverId = vehicle.getDriverId();
			if(driverId == null)
				driverId = "";
			driver_id_layout.setVisibility(View.VISIBLE);
			if(driverIdLen == 99){
				driver_id_view.setHint("请输入完整驾驶证号");
				driver_id_notice.setText("请输入完整驾驶证号");
				driver_id_view.setText(driverId);
				if(driverId.equals(""))
					updateNotice(driver_id_notice, false);
			}
			else{
				driver_id_view.setHint("请输入驾驶证号后" + driverIdLen + "位");
				driver_id_notice.setText("请输入驾驶证号后" + driverIdLen + "位");
				if(driverId.length() < driverIdLen){
					driver_id_view.setText(driverId);
					updateNotice(driver_id_notice, false);
				}
				else if(driverId.length() == driverIdLen){
					driver_id_view.setText(driverId);
				}
				else{
					driver_id_view.setText(driverId.substring(driverId.length() - driverIdLen));
				}
			}
		}
		if(needAccountAndPassword){
			website_account_layout.setVisibility(View.VISIBLE);
			website_password_layout.setVisibility(View.VISIBLE);
			if(!StringUtils.isEmpty(vehicle.getAccount()))
				website_account_view.setText(vehicle.getAccount());
			if(!StringUtils.isEmpty(vehicle.getPassword()))
				website_password_view.setText(vehicle.getPassword());
		}
		if(needVehicleRegCerNo){
			vehicleRegCerNo_layout.setVisibility(View.VISIBLE);
			if(!StringUtils.isEmpty(vehicle.getVehicleRegCerNo()))
				vehicleRegCerNo_view.setText(vehicle.getVehicleRegCerNo());
		}
//		tvVehicleError.setText(StringUtils.isEmpty(strError) ? "车辆信息" : strError + "错误");
//		errorTitle.setText(StringUtils.isEmpty(strError) ? "车辆信息" : strError + "错误");
//		errorInfo.setText(StringUtils.isEmpty(strError) ? "车辆" : strError + "信息错误，需要您更正信息才能正常查询违章");
		if(vehicle.isHasRuleError()){
			tvVehicleError.setText("车辆信息不符合查询规则");
			errorTitle.setText("车辆信息不符合查询规则");
			errorInfo.setText("车辆信息不符合查询规则，需要您更正信息才能正常查询违章");
		}
		else if(vehicle.isHasParamError()){
			tvVehicleError.setText("车辆信息错误");
			errorTitle.setText("车辆信息错误");
			errorInfo.setText("车辆信息错误，需要您更正信息才能正常查询违章");
		}
	}
	
	private void saveVehicle(final String strFrameNum, final String strMotorNum, final String vehicleOwner, final String ownerId, final String driverName, final String driverId, final String websiteAaccount, final String websitePassword, final String vehicleRegCerNo){
		new AsyncTask<Void, Void, VehicleAddResponse>(){
			protected void onPreExecute() {
				showloading(true);
			}
			@Override
			protected VehicleAddResponse doInBackground(Void... params) {
				try{
					VehicleAddRequest request = new VehicleAddRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId(), vehicle, strFrameNum, strMotorNum, vehicleOwner, ownerId, driverName, driverId, websiteAaccount, websitePassword, vehicleRegCerNo);
					return mApplication.client.execute(request);
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(VehicleAddResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						VehicleAddResult data = result.getData();
						if(data.getResult() != null && data.getResult()){
							ToastUtil.TextToast(VehicleDetailActivity.this, "更正车辆信息成功", Toast.LENGTH_SHORT, Gravity.CENTER);
							if(!StringUtils.isEmpty(strFrameNum))
								vehicle.setFrameNum(strFrameNum);
							if(!StringUtils.isEmpty(strMotorNum))
								vehicle.setMotorNum(strMotorNum);
							if(!StringUtils.isEmpty(vehicleOwner))
								vehicle.setVehicleOwner(vehicleOwner);
							if(!StringUtils.isEmpty(websiteAaccount))
								vehicle.setAccount(websiteAaccount);
							if(!StringUtils.isEmpty(websitePassword))
								vehicle.setPassword(websitePassword);
							if(!StringUtils.isEmpty(vehicleRegCerNo))
								vehicle.setVehicleRegCerNo(vehicleRegCerNo);
							vehicle.setHasRuleError(false);
							vehicle.setHasParamError(false);
							mApplication.dbCache.updateVehicle(vehicle);
							vehicleErrorView.setVisibility(View.GONE);
							correctVehicleErrorLayout.setVisibility(View.GONE);
						}
						else{
							if(result.getData().getType() != null && result.getData().getType() == 1){
								ToastUtil.TextToast(VehicleDetailActivity.this,"车辆信息不符合该城市查询规则", Toast.LENGTH_SHORT, Gravity.CENTER);
								List<CityVehicle> cvs = result.getData().getRule();
								if(cvs != null && !cvs.isEmpty()){
									mApplication.dbCache.saveCityVehicles(cvs);
									mApplication.updateCities(cvs);
									vehicleErrorView.setVisibility(View.GONE);
									correctVehicleErrorLayout.setVisibility(View.GONE);
								}
							}
							else if(result.getData().getType() != null && result.getData().getType() == 2){
								ToastUtil.TextToast(VehicleDetailActivity.this,"车辆信息错误", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
							else
								ToastUtil.TextToast(VehicleDetailActivity.this,StringUtils.isEmpty(result.getMsg()) ? "修改车辆信息失败" : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
						}
					}
					else
						ToastUtil.TextToast(VehicleDetailActivity.this,StringUtils.isEmpty(result.getMsg()) ? "修改车辆信息失败" : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				else{
					ToastUtil.TextToast(VehicleDetailActivity.this, "网络异常", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				showloading(false);
			}
		}.execute();
	}
	
	private void updateNotice(TextView tv, boolean isCorrect){
		if(isCorrect){
			tv.setBackgroundResource(R.color.daze_light_green50);
			tv.setTextColor(getResources().getColor(R.color.daze_light_green500));
		}
		else{
			tv.setVisibility(View.VISIBLE);
			tv.setBackgroundResource(R.color.daze_red50);
			tv.setTextColor(getResources().getColor(R.color.daze_red500));
		}
	}
	
	class CouponComparator implements Comparator<Coupon>{
		@Override
		public int compare(Coupon o1, Coupon o2) {
			if(o1.getEndTime().equals(o2.getEndTime())){
				return o1.getAmount().intValue() - o2.getAmount().intValue();
			}
			return o1.getEndTime().compareTo(o2.getEndTime());
		}		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(cashSelectedView.getVisibility() == View.VISIBLE){
				cashSelectedView.setVisibility(View.GONE);
				return true;
			}
			else if(orderSubmitView.getVisibility() == View.VISIBLE){
				orderTitleView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_out_to_top));
				orderTitleView.postDelayed(new Runnable() {				
					@Override
					public void run() {
						transparentbg.setVisibility(View.GONE);
						orderTitleView.setVisibility(View.GONE);
					}
				}, 500);
				orderSubmitView.startAnimation(AnimationUtils.loadAnimation(VehicleDetailActivity.this, R.anim.slide_out_to_bottom));
				orderSubmitView.postDelayed(new Runnable() {				
					@Override
					public void run() {
						orderSubmitView.setVisibility(View.GONE);
					}
				}, 500);
				return true;
			}
			else if(correctVehicleErrorLayout.getVisibility() == View.VISIBLE){
				correctVehicleErrorLayout.setVisibility(View.GONE);
				return true;
			}
			else{
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class AllCapTransformationMethod extends ReplacementTransformationMethod {
		protected char[] getOriginal() {
			char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
					'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
					'w', 'x', 'y', 'z' };
			return aa;
		}

		protected char[] getReplacement() {
			char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z' };
			return cc;
		}
	}
	
	private void dealwithAgainstResult(final GetAgainstRecordListResponse result){
		if(result != null){
			if(result.getCode() != null && result.getCode() == 0){
				GetAgainstRecordListJson data = result.getData();
				if(data != null){
					String dataType = data.getType();
					if(dataType != null){
						if(dataType.contains("0")){
							vehicle.setUpdateTime("" + result.getPostDateTime());
							List<AgainstRecord> listResult = data.getList();
							EventAnalysisUtil.onEvent(VehicleDetailActivity.this, "click_wz_success", "查询违章成功", null);
							if(listResult != null && !listResult.isEmpty()){
								if(result.getData().getResultType() != null && result.getData().getResultType() == 1){
									mApplication.dbCache.deleteImageAgainstRecord(vehicleNumber);
									for(AgainstRecord ar : listResult)
										ar.setResultType(1);
								}
								vehicle.setNewRecordNumber(listResult.size());
								mApplication.dbCache.updateVehicle(vehicle);
								mApplication.dbCache.saveAgainstRecords(listResult);
							}
							else
								mApplication.dbCache.updateVehicle(vehicle);
							updateUI();
							if(listView.indexOfChild(listEmpty) != -1){
								shareNoAgainst.setVisibility(View.VISIBLE);
							}
                            Intent it = new Intent("com.kplus.car.weizhang.finish");
                            it.putExtra("vehicleNum", vehicleNumber);
                            LocalBroadcastManager.getInstance(VehicleDetailActivity.this).sendBroadcast(it);
						}
						if(dataType.contains("1")){//参数合法性错误
							correctVehicleErrorLayout.setVisibility(View.VISIBLE);
							vehicleErrorView.setVisibility(View.VISIBLE);
							List<CityVehicle> cityVehicles = data.getRule();
							if(cityVehicles != null){
								vehicle.setHasRuleError(true);
								mApplication.dbCache.updateVehicle(vehicle);
								mApplication.dbCache.saveCityVehicles(cityVehicles);
								mApplication.updateCities(cityVehicles);
								getFrameAndMotorLength();
								showCorrectVehicleInfoView();
							}
						}
						if(dataType.contains("2")){//数据错误
							vehicle.setHasParamError(true);
							mApplication.dbCache.updateVehicle(vehicle);
							correctVehicleErrorLayout.setVisibility(View.VISIBLE);
							vehicleErrorView.setVisibility(View.VISIBLE);
							getFrameAndMotorLength();
							showCorrectVehicleInfoView();
						}
						if(dataType.contains("3")){//当前城市未开通
							String strCity = result.getData().getCity();
							strCity = strCity.replaceAll(" ", "");
							strCity = strCity.replaceAll("，", ",");
							strCity = strCity.replaceAll("：", "\\:");
							strCity = strCity.replace("{", "");
							strCity = strCity.replace("}", "");
							strCity = strCity.replaceAll("=", "\\:");
//							vehicle.setCityUnValid(strCity);
//							mApplication.dbCache.updateVehicle(vehicle);
							String cityIds = null;
							try{
								String[] cityParams = strCity.split(",");
								if(cityParams != null && cityParams.length > 0){
									for(int i=0;i<cityParams.length;i++){
										if(cityParams[i] != null){
											String[] subCityParams = cityParams[i].split("\\:");
											if(subCityParams != null && subCityParams.length > 1){
												if(cityIds == null)
													cityIds = subCityParams[0];
												else
													cityIds += ("," + subCityParams[0]);
											}
										}
									}
								}
							}
							catch(Exception e){
								e.printStackTrace();
							}
							List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(cityIds);
							if(cvs != null && !cvs.isEmpty()){
								for(CityVehicle cv : cvs){
									cv.setValid(false);
								}
								mApplication.dbCache.saveCityVehicles(cvs);
							}
							updateUnSupportCity();
							updateUI();
						}
						if(dataType.contains("4")){//缓冲中不存在，已经开始拉取
							vehicle.setRequestTime(System.currentTimeMillis());
							mApplication.dbCache.updateVehicle(vehicle);
//							Intent intent = new Intent(VehicleDetailActivity.this, AlertDialogActivity.class);
//							intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
//							intent.putExtra("message", "本次查询预计时间将超过20秒，已转入后台查询，查询结果将通过推送等方式通知您");
//							startActivityForResult(intent, SHOW_MSG_REQUEST);
							nDegrees = 0;
							imageRefreshProgress180.setnRadius(16);
							imageRefreshProgress180.setnAngel(0);
							imageRefreshProgress180.postInvalidate();
							refreshingView180.setVisibility(View.VISIBLE);
							tvRefreshingProgress180.setText(100*nDegrees/180 + "%");
							Message msg = new Message();
							msg.what = 1;
							progressHandler.sendMessageDelayed(msg, 1000);
							long updateTime = 0;
							try{
								updateTime = Long.parseLong(vehicle.getUpdateTime());
							}
							catch(Exception E){
								updateTime = 0;
								E.printStackTrace();
							}
							mApplication.addTasks(vehicleNumber, updateTime);
							if(!AsyncTaskService.isRunning){
								startService(new Intent(VehicleDetailActivity.this, AsyncTaskService.class));
							}
                            Intent it = new Intent("com.kplus.car.weizhang.start");
                            it.putExtra("vehicleNum", vehicleNumber);
                            LocalBroadcastManager.getInstance(VehicleDetailActivity.this).sendBroadcast(it);
						}
						if(dataType.contains("5")){
							TaskInfo ti = mApplication.getTask(vehicleNumber);
							if(ti != null && !ti.isHasEnterVerifyCode()){
								String sessionId = data.getSessionId();
								if(!StringUtils.isEmpty(sessionId)){
									Intent intent = new Intent(VehicleDetailActivity.this, AlertDialogActivity.class);
									intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VERIFY_CODE);
									intent.putExtra("vehicleNumber", vehicleNumber);
									intent.putExtra("sessionId", sessionId);
	//								if(!isAppRunning()){
	//									intent.setAction(Intent.ACTION_MAIN);
	//									intent.addCategory(Intent.CATEGORY_LAUNCHER);				
	//								}
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							}
							else if(ti == null){
								vehicle.setRequestTime(System.currentTimeMillis());
								nDegrees = 0;
								imageRefreshProgress180.setnRadius(16);
								imageRefreshProgress180.setnAngel(0);
								imageRefreshProgress180.postInvalidate();
								refreshingView180.setVisibility(View.VISIBLE);
								tvRefreshingProgress180.setText(100*nDegrees/180 + "%");
								Message msg = new Message();
								msg.what = 1;
								progressHandler.sendMessageDelayed(msg, 1000);
								long updateTime = 0;
								try{
									updateTime = Long.parseLong(vehicle.getUpdateTime());
								}
								catch(Exception E){
									updateTime = 0;
									E.printStackTrace();
								}
								mApplication.addTasks(vehicleNumber, updateTime);
								if(!AsyncTaskService.isRunning){
									startService(new Intent(VehicleDetailActivity.this, AsyncTaskService.class));
								}
							}
						}
					}
				}
			}
			else{
				searchFailView.setVisibility(View.VISIBLE);
				ToastUtil.TextToast(VehicleDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
			}
		}
		else{
			searchFailView.setVisibility(View.VISIBLE);
			ToastUtil.TextToast(VehicleDetailActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
		}
	}
	
	class RecordTypeAdapter extends AbstractWheelTextAdapter{

		public RecordTypeAdapter(Context context) {
			super(context, R.layout.daze_cash_select_item, NO_RESOURCE);
			setItemTextResource(R.id.tvCashInfo);
		}

		@Override
		public int getItemsCount() {
			if(recordsTypes == null)
				return 0;
			return recordsTypes.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			if(recordsTypes == null || recordsTypes.length == 0)
				return null;
			return recordsTypes[index];
		}
		
		@Override
	    public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
	        return view;
	    }
	}
	
	private void correctRecordType(){
		currentRecord.setRecordType(currentIndex);
		mApplication.dbCache.saveAgainstRecord(currentRecord);
		adapter.notifyDataSetChanged();
		cashSelectedView.setVisibility(View.GONE);
		new AsyncTask<Void, Void, Response>(){
			@Override
			protected Response doInBackground(Void... params) {
				AgainstReportRequest request = new AgainstReportRequest();
				request.setParams("3", "" + currentIndex, ""+currentRecord.getId());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private void getAdvertData(){
		new AsyncTask<Void, Void, GetAdvertDataResponse>(){
			@Override
			protected GetAdvertDataResponse doInBackground(Void... params) {
				GetAdvertDataRequest request = new GetAdvertDataRequest();
				if (!StringUtils.isEmpty(mApplication.getCityId())){
					try {
						request.setParams(Long.parseLong(mApplication.getCityId()), mApplication.getUserId(), mApplication.getId(),KplusConstants.ADVERT_VEHICLE_DETAIL_HEAD);
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(GetAdvertDataResponse getAdvertDataResponse) {
				super.onPostExecute(getAdvertDataResponse);
				if(getAdvertDataResponse != null && getAdvertDataResponse.getCode() != null && getAdvertDataResponse.getCode() == 0){
					AdvertJson data = getAdvertDataResponse.getData();
					if(data != null){
						List<Advert> adverts = data.getVehicleDetailHead();
						if(adverts != null && !adverts.isEmpty()){
							mApplication.setVehicleDetailHeadAdvert((ArrayList<Advert>) adverts);
							try {
								AdvertFragment adf = AdvertFragment.newInstance(KplusConstants.ADVERT_VEHICLE_DETAIL_HEAD);
								getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, 0).add(R.id.activityView, adf).commit();
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
			}
		}.execute();
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
	}
	
}
