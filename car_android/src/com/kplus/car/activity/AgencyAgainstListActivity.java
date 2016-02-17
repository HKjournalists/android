package com.kplus.car.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.Coupon;
import com.kplus.car.model.CouponList;
import com.kplus.car.model.Order;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.json.CouponListJson;
import com.kplus.car.model.response.GetPriceValueResponse;
import com.kplus.car.model.response.GetValidCouponListResponse;
import com.kplus.car.model.response.OrderAddResponse;
import com.kplus.car.model.response.request.GetPriceValueRequest;
import com.kplus.car.model.response.request.GetValidCouponListResquest;
import com.kplus.car.model.response.request.OrderAddRequest;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.BaseLoadList;
import com.kplus.car.widget.CashSelectAdapter;
import com.kplus.car.widget.antistatic.spinnerwheel.AbstractWheel;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelChangedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelClickedListener;

public class AgencyAgainstListActivity extends BaseActivity implements OnClickListener {

	public static final int PHONE_REGIST_RESULT = 0x11;

	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView listView;
	private View orderSubmitView, blankView;
	private TextView order_submit_selected_num_text;
	private TextView order_submit_money_text, order_submit_reduce_money_text;
	private TextView order_submit_price_text, order_submit_reduce_price_text;
	private ProgressBar order_submit_total_price_loading, order_submit_service_price_loading, fineCashUseInfo_loading, serviceChargeCashUseInfo_loading;
	private TextView order_submit_total_price_text;
	private TextView tvSubmitCommit;
	private View footer;
	private View fineCashView, serviceChargeCashView;
	private TextView tvFineCashUseInfo, tvServiceChargeCashUseInfo;
	private View cashSelectedView;
	private AbstractWheel cashView;
	private TextView tvUnuseCash, tvUseCash;
	private View cashBlankView;
	private TextView tvAgainstNumberSelected, tvNextStep;
	
	private AgainstRecordListAdapter adapter;
	private UserVehicle vehicle;
	private Order order;
	private int money;
	private float price;
	private float totalPrice;
	private String vehicleNumber;
	private DecimalFormat df = new DecimalFormat("#.##");
	private ArrayList<AgainstRecord> recordList = new ArrayList<AgainstRecord>();
	private List<Coupon> fineCouponList, fineCouponListSave, serviceChargeCouponList, serviceChargeCouponListSave;
	private Coupon currentFineCoupon, currentServiceChargeCoupon;
	private CashSelectAdapter cashAdapter;
	private boolean isFineCoupon;
	private int currentIndex;
	private int payMoneyReduction = 0;
	private int serviceChargeReduction = 0;
	private Coupon nullCoupon;

	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_agency_against_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle); 
		tvTitle.setText("在线缴费");
		listView = (ListView) findViewById(R.id.agency_against_list_body);
		orderSubmitView = findViewById(R.id.orderSubmitView);
		order_submit_selected_num_text = (TextView) findViewById(R.id.order_submit_selected_num_text);
		order_submit_money_text = (TextView) findViewById(R.id.order_submit_money_text);
		order_submit_price_text = (TextView) findViewById(R.id.order_submit_price_text);
		order_submit_total_price_text = (TextView) findViewById(R.id.order_submit_total_price_text);
		order_submit_total_price_loading = (ProgressBar) findViewById(R.id.order_submit_total_price_loading);
		order_submit_reduce_money_text = (TextView) findViewById(R.id.order_submit_reduce_money_text);
		order_submit_reduce_price_text = (TextView) findViewById(R.id.order_submit_reduce_price_text);
		tvSubmitCommit = (TextView) findViewById(R.id.tvSubmitCommit);
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
		tvAgainstNumberSelected = (TextView) findViewById(R.id.tvAgainstNumberSelected);
		tvNextStep = (TextView) findViewById(R.id.tvNextStep);
		blankView = findViewById(R.id.blankView);
	}

	protected void setListener() {
		leftView.setOnClickListener(this);
		tvSubmitCommit.setOnClickListener(this);
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
		tvNextStep.setOnClickListener(this);
		blankView.setOnClickListener(this);
	}

	protected void loadData() {
		df.setMinimumFractionDigits(2);
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
		tvTitle.setText("在线缴费-" + vehicleNumber);
		if (order == null) {
			order = new Order();
			order.setUserId(mApplication.getUserId());
			order.setVehicleNum(vehicle.getVehicleNum());
			order.setContactName("user" + mApplication.getUserId());
			order.setContactPhone(mApplication.dbCache.getValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE));
		}
		setAdapter();
		fineCouponList = new ArrayList<Coupon>();
		serviceChargeCouponList = new ArrayList<Coupon>();
		fineCouponListSave = new ArrayList<Coupon>();
		serviceChargeCouponListSave = new ArrayList<Coupon>();
		nullCoupon = new Coupon();
		nullCoupon.setName("不选择");
		cashAdapter = new CashSelectAdapter(AgencyAgainstListActivity.this);
		cashView.setViewAdapter(cashAdapter);
		cashView.setVisibleItems(5);
	}

	private void setAdapter() {
		adapter = new AgainstRecordListAdapter(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(leftView)) {
			finish();
		}
		else if(v.equals(tvSubmitCommit)){
			submitOrder();
		}
		else if(v.equals(fineCashView)){
			if(fineCashUseInfo_loading.getVisibility() == View.VISIBLE){
				ToastUtil.TextToast(AgencyAgainstListActivity.this, "正在获取代金券，请稍候", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(fineCouponList.isEmpty()){
				ToastUtil.TextToast(AgencyAgainstListActivity.this, "没有代金券可以使用", Toast.LENGTH_SHORT, Gravity.CENTER);
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
			cashView.setViewAdapter(cashAdapter);
			cashView.setCurrentItem(fineCouponList.indexOf(currentFineCoupon));
			cashSelectedView.setVisibility(View.VISIBLE);
		}
		else if (v.equals(serviceChargeCashView)){
			if(serviceChargeCashUseInfo_loading.getVisibility() == View.VISIBLE){
				ToastUtil.TextToast(AgencyAgainstListActivity.this, "正在获取代金券，请稍候", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(!(price >0)){
				ToastUtil.TextToast(AgencyAgainstListActivity.this, "服务费为零，不能使用代金券", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(serviceChargeCouponList.isEmpty()){
				ToastUtil.TextToast(AgencyAgainstListActivity.this, "没有代金券可以使用", Toast.LENGTH_SHORT, Gravity.CENTER);
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
			cashView.setViewAdapter(cashAdapter);
			cashView.setCurrentItem(serviceChargeCouponList.indexOf(currentServiceChargeCoupon));
			cashSelectedView.setVisibility(View.VISIBLE);
		}
		else if(v.equals(tvUnuseCash)){
			cashSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(tvUseCash)){
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
					if(totalPrice - price > currentFineCoupon.getAmount().intValue()){
						payMoneyReduction = currentFineCoupon.getAmount().intValue();
						order_submit_reduce_money_text.setText("-" + payMoneyReduction);
					}
					else{
						payMoneyReduction = (int) (totalPrice - price);
						order_submit_reduce_money_text.setText("-" + df.format(payMoneyReduction));
					}
					order_submit_reduce_money_text.setVisibility(View.VISIBLE);
				}
				if(Math.abs(totalPrice - payMoneyReduction - serviceChargeReduction) > 0.00001)
					order_submit_total_price_text.setText(df.format(totalPrice - payMoneyReduction - serviceChargeReduction));
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
					if(price > currentServiceChargeCoupon.getAmount().intValue()){
						serviceChargeReduction = currentServiceChargeCoupon.getAmount().intValue();
						order_submit_reduce_price_text.setText("-" + serviceChargeReduction);
					}
					else{
						serviceChargeReduction = (int) price;
						order_submit_reduce_price_text.setText("-" + df.format(serviceChargeReduction));
					}
					order_submit_reduce_price_text.setVisibility(View.VISIBLE);
				}
				if(Math.abs(totalPrice - payMoneyReduction - serviceChargeReduction) > 0.00001)
					order_submit_total_price_text.setText(df.format(totalPrice - payMoneyReduction - serviceChargeReduction));
				else
					order_submit_total_price_text.setText("0.01");
			}
		}
		else if(v.equals(cashBlankView)){
			cashSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(tvNextStep)){
			genPrice();
			orderSubmitView.setVisibility(View.VISIBLE);
			orderSubmitView.startAnimation(AnimationUtils.loadAnimation(AgencyAgainstListActivity.this, R.anim.slide_in_from_bottom));
		}
		else if(v.equals(blankView)){
			orderSubmitView.startAnimation(AnimationUtils.loadAnimation(AgencyAgainstListActivity.this, R.anim.slide_out_to_bottom));
			orderSubmitView.postDelayed(new Runnable() {				
				@Override
				public void run() {
					orderSubmitView.setVisibility(View.GONE);
				}
			}, 500);
		}
	}


	private void genPrice() {
		String recordIds = "";
		for (AgainstRecord record : recordList) {
			if ("".equals(recordIds)) {
				recordIds = "" + record.getId();
			} else {
				recordIds += "," + record.getId();
			}
		}
		order.setRecordIds(recordIds);
		final String finalRecordIds = recordIds;
		new AsyncTask<Void, Void, GetPriceValueResponse>() {			
			protected void onPreExecute() {
				payMoneyReduction = 0;
				serviceChargeReduction = 0;
				order_submit_total_price_loading.setVisibility(View.VISIBLE);
				order_submit_service_price_loading.setVisibility(View.VISIBLE);
				serviceChargeCashUseInfo_loading.setVisibility(View.VISIBLE);
				fineCashUseInfo_loading.setVisibility(View.VISIBLE);
				tvServiceChargeCashUseInfo.setText("");
				tvFineCashUseInfo.setText("");
				order_submit_price_text.setText("");
				order_submit_total_price_text.setText("");
				order_submit_reduce_price_text.setVisibility(View.GONE);
				order_submit_reduce_money_text.setVisibility(View.GONE);
				tvSubmitCommit.setBackgroundResource(R.drawable.daze_btn_bg_light_gray_2);
				tvSubmitCommit.setEnabled(false);
			}
			
			protected GetPriceValueResponse doInBackground(Void... params) {
				GetPriceValueRequest request = new GetPriceValueRequest();
				request.setParams(finalRecordIds,mApplication.getpId());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetPriceValueResponse result) {
				order_submit_total_price_loading.setVisibility(View.GONE);
				order_submit_service_price_loading.setVisibility(View.GONE);
				if (result != null) {
					if (result.getCode() != null && result.getCode() == 0) {
						price = result.getData().getPrice() == null ? -1 : result.getData().getPrice();
						if (price == -1) {
							tvSubmitCommit.setText("所选违章地暂未开通服务");
							order_submit_price_text.setText("0.00");
							order_submit_total_price_text.setText("0.00");
							serviceChargeCashUseInfo_loading.setVisibility(View.GONE);
							fineCashUseInfo_loading.setVisibility(View.GONE);
							return;
						} else {
							order_submit_price_text.setText("" + df.format(price));
							totalPrice = money + price;
							order.setPrice(totalPrice);
							order_submit_total_price_text.setText("" + df.format(totalPrice));
							tvSubmitCommit.setText("提交订单");
							tvSubmitCommit.setBackgroundColor(getResources().getColor(R.color.daze_white));
							tvSubmitCommit.setEnabled(true);
						}
						if(result.getData().getReduced()){
							if(result.getData().getReducePrice() != null){ 
								float moneyReduce = result.getData().getReducePrice();
								if(moneyReduce < 0){
									moneyReduce = 0.00f;
								}
								float moneyTemp = money - moneyReduce;
								if(moneyTemp < 0){
									moneyTemp = 0.00f;
									order_submit_money_text.setText("0");
								}
								else
									order_submit_money_text.setText("" + df.format(moneyTemp));
								totalPrice = moneyTemp + price;
								order.setPrice(totalPrice);
								order_submit_total_price_text.setText("" + df.format(totalPrice));
							}
						}
						getValidCouponList();
					} else {
						order_submit_price_text.setText("获取服务费失败");
						Toast.makeText(AgencyAgainstListActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} else {
					order_submit_price_text.setText("获取服务费失败");
					Toast.makeText(AgencyAgainstListActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
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
					request.setParams(1, mApplication.getId(), totalPrice);
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
													if(totalPrice - price > currentFineCoupon.getAmount().intValue()){
														payMoneyReduction = currentFineCoupon.getAmount().intValue();
														order_submit_reduce_money_text.setText("-" + payMoneyReduction);
													}
													else{
														payMoneyReduction = (int) (totalPrice - price);
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
												if(price > 0){
													if(currentServiceChargeCoupon.getAmount() == null){
														tvServiceChargeCashUseInfo.setText(currentServiceChargeCoupon.getName());
														order_submit_reduce_price_text.setVisibility(View.GONE);
													}
													else{
														tvServiceChargeCashUseInfo.setText(currentServiceChargeCoupon.getName() + "¥" + currentServiceChargeCoupon.getAmount().intValue());
														if(price > currentServiceChargeCoupon.getAmount().intValue()){
															serviceChargeReduction = currentServiceChargeCoupon.getAmount().intValue();
															order_submit_reduce_price_text.setText("-" + serviceChargeReduction);
														}
														else{
															serviceChargeReduction = (int) price;
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
												if(price > 0)
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
									if(Math.abs(totalPrice - payMoneyReduction - serviceChargeReduction) > 0.00001)
										order_submit_total_price_text.setText(df.format(totalPrice - payMoneyReduction - serviceChargeReduction));
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

	private void submitOrder() {
		showloading(true);
		new AsyncTask<Void, Void, OrderAddResponse>() {
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

			@Override
			protected void onPostExecute(OrderAddResponse result) {
				showloading(false);
				if (result != null) {
					if (result.getCode() != null && result.getCode() == 0) {
						String orderNum = result.getData().getOrderNum();
						if (orderNum != null) {
							long orderId = result.getData().getOrderId();
							Intent intent = new Intent(AgencyAgainstListActivity.this,OrderActivity.class);
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
							Toast.makeText(AgencyAgainstListActivity.this, "订单提交失败", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(AgencyAgainstListActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(AgencyAgainstListActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}

	private class AgainstRecordListAdapter extends BaseLoadList<AgainstRecord> {

		public AgainstRecordListAdapter(BaseActivity context) {
			super(context);
		}

		@Override
		public void initItem(final AgainstRecord it, Map<String, Object> holder) {
			TextView time = (TextView) holder.get("time");
			TextView tvStatus = (TextView) holder.get("tvStatus");
			TextView address = (TextView) holder.get("address");
			TextView behavior = (TextView) holder.get("behavior");
			TextView score = (TextView) holder.get("score");
			TextView money = (TextView) holder.get("money");
			View serviceHead = (View) holder.get("serviceHead");
			final CheckBox checkView = (CheckBox) holder.get("checkView");
			TextView tvBottom = (TextView) holder.get("tvBottom");
			View rootView = (View) holder.get("rootView");
			if(linkeList.indexOf(it) == linkeList.size() -1)
				tvBottom.setVisibility(View.VISIBLE);
			else
				tvBottom.setVisibility(View.GONE);
			time.setText(it.getTime());
			tvStatus.setTextColor(Color.rgb(255, 183, 24));
			tvStatus.setText("未处理");
			checkView.setVisibility(View.VISIBLE);
			if(recordList.contains(it))
				checkView.setChecked(true);
			else
				checkView.setChecked(false);
			serviceHead.setVisibility(View.GONE);
			
			if(it.getAddress() != null && !it.getAddress().equals(""))
				address.setText(it.getAddress());
			else
				address.setText("未知地点");
			if(it.getBehavior() != null && !it.getBehavior().equals(""))
				behavior.setText(it.getBehavior());
			else
				behavior.setText("违反道路交通安全法");
			score.setText(it.getScore() == -1 ? "未知" : "" + it.getScore() +"分");
			money.setText(it.getMoney() == -1 ? "未知" : "" + it.getMoney() + "元");
			rootView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					if(checkView.isChecked()){
						checkView.setChecked(false);
						if(recordList.contains(it)){
							recordList.remove(it);
							AgencyAgainstListActivity.this.money -= it.getMoney();
							order_submit_selected_num_text.setText("" + recordList.size() + "条");
							order_submit_money_text.setText(df.format(AgencyAgainstListActivity.this.money));
							tvAgainstNumberSelected.setText("你已经选择" + recordList.size() + "条违章");
						}
					}
					else{
						checkView.setChecked(true);
						if(!recordList.contains(it)){
							recordList.add(it);
							AgencyAgainstListActivity.this.money += it.getMoney();
							order_submit_selected_num_text.setText("" + recordList.size() + "条");
							order_submit_money_text.setText(df.format(AgencyAgainstListActivity.this.money));
							tvAgainstNumberSelected.setText("你已经选择" + recordList.size() + "条违章");
						}
					}
				}
			});
		}

		@Override
		public Map<String, Object> getHolder(View v) {
			Map<String, Object> root = new HashMap<String, Object>();
			TextView time = (TextView) v.findViewById(R.id.vehicle_detail_listItem_time);
			TextView tvStatus = (TextView) v.findViewById(R.id.vehicle_detail_listItem_status);
			TextView address = (TextView) v.findViewById(R.id.vehicle_detail_listItem_address);
			TextView behavior = (TextView) v.findViewById(R.id.vehicle_detail_listItem_behavior);
			TextView score = (TextView) v.findViewById(R.id.vehicle_detail_listItem_score);
			TextView money = (TextView) v.findViewById(R.id.vehicle_detail_listItem_money);
			View serviceHead = v.findViewById(R.id.serviceHead);
			CheckBox checkView = (CheckBox) v.findViewById(R.id.checkView);
			TextView tvBottom = (TextView) v.findViewById(R.id.tvBottom);
			View rootView = v.findViewById(R.id.rootView);
			root.put("time", time);
			root.put("tvStatus", tvStatus);
			root.put("address", address);
			root.put("behavior", behavior);
			root.put("score", score);
			root.put("money", money);
			root.put("serviceHead", serviceHead);
			root.put("checkView", checkView);
			root.put("tvBottom", tvBottom);
			root.put("rootView", rootView);

			return root;
		}

		@Override
		public List<AgainstRecord> executeFirst() throws Exception {
			try {
				List<AgainstRecord> records = mApplication.dbCache.getCanSubmitAgainstRecords(vehicleNumber, 0, 0);
				recordList.addAll(records);
				return records;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public int getLayoutId(int index) {
			return R.layout.daze_vehicle_detail_item;
		}

		@Override
		public void showLoading(boolean show) {
			if (show) {
				LayoutInflater in = LayoutInflater.from(context);
				footer = in.inflate(R.layout.daze_list_foot, null);
				listView.addFooterView(footer, null, false);
			} else {
				listView.removeFooterView(footer);
				if(recordList != null && !recordList.isEmpty()){
					for(AgainstRecord ar : recordList){
						money += ar.getMoney();
					}
					order_submit_selected_num_text.setText("" + recordList.size() + "条");
					order_submit_money_text.setText(df.format(money));
					tvAgainstNumberSelected.setText("你已经选择" + recordList.size() + "条违章");
				}
			}
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
				orderSubmitView.startAnimation(AnimationUtils.loadAnimation(AgencyAgainstListActivity.this, R.anim.slide_out_to_bottom));
				orderSubmitView.postDelayed(new Runnable() {				
					@Override
					public void run() {
						orderSubmitView.setVisibility(View.GONE);
					}
				}, 500);
				return true;
			}
			else{
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


}
