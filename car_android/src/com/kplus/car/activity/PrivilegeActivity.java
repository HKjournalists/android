package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.GetServicePhoneResponse;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.GetServicePhoneRequest;
import com.kplus.car.service.UpdateUserVehicleService;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PrivilegeActivity extends BaseActivity implements OnClickListener{
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;	
	private View rlRoadrescue;
	private View rlMonitor;
	private View rlService;	
	private TextView tvRoadrescueLabel;
	private TextView tvRoadrescueNum;
	private TextView tvMonitorLabel, tvMonitor;
	private TextView tvService;
	
	private List<VehicleAuth> selfSuccessVehicleAuths;
	private List<VehicleAuth>  vehicleAuths;
	private List<UserVehicle>  userVehicles;	
	private int rescueCount;
	
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			updateUI();
		}
	};

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_user_privilege2);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("我的特权");		
		rlRoadrescue = findViewById(R.id.rlRoadrescue);
		rlMonitor = findViewById(R.id.rlMonitor);
		rlService = findViewById(R.id.rlService);		
		tvRoadrescueLabel = (TextView) findViewById(R.id.tvRoadrescueLabel);
		tvRoadrescueNum = (TextView) findViewById(R.id.tvRoadrescueNum);
		tvMonitorLabel = (TextView) findViewById(R.id.tvMonitorLabel);
		tvMonitor = (TextView) findViewById(R.id.tvMonitor);
		tvService = (TextView) findViewById(R.id.tvService);
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
	}	
	
	@Override
	protected void setListener() {
		rlRoadrescue.setOnClickListener(this); //道路救援
		rlMonitor.setOnClickListener(this);    //违章监控
		rlService.setOnClickListener(this);    //贴心美女秘书
		leftView.setOnClickListener(this);
	}

	
	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE);
		LocalBroadcastManager.getInstance(PrivilegeActivity.this).registerReceiver(mReceiver, filter);
		updateUI();		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(PrivilegeActivity.this).unregisterReceiver(mReceiver);
		super.onPause();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(leftView))
		{
			finish();
		}
		else if(v.equals(rlRoadrescue)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(PrivilegeActivity.this, MemberPrivilegeActivity.class));
			}
			else{
				if(selfSuccessVehicleAuths != null && !selfSuccessVehicleAuths.isEmpty()){
					if(selfSuccessVehicleAuths.size() == 1){
						Intent intentRescue = new Intent(PrivilegeActivity.this, EmergencyDetailActivity.class);
						intentRescue.putExtra("vehicleNumber", selfSuccessVehicleAuths.get(0).getVehicleNum());
						startActivity(intentRescue);
					}
					else
						startActivity(new Intent(PrivilegeActivity.this, VehicleRescueActivity.class));
				}
				else{
					startActivity(new Intent(PrivilegeActivity.this, MemberPrivilegeActivity.class));
				}
			}
		}
		else if(v.equals(rlMonitor)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(PrivilegeActivity.this, MemberPrivilegeActivity.class));
			}
			else{
				if(selfSuccessVehicleAuths != null && !selfSuccessVehicleAuths.isEmpty()){
					if(selfSuccessVehicleAuths.size() == 1){
						Intent intentCity = new Intent(PrivilegeActivity.this, CitySelectActivity.class);
						intentCity.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_AUTHENTICATION);
						intentCity.putExtra("vehicleNumber", selfSuccessVehicleAuths.get(0).getVehicleNum());
						startActivity(intentCity);
					}
					else
						startActivity(new Intent(PrivilegeActivity.this, MonitorCitiesActivity.class));
				}
				else{
					startActivity(new Intent(PrivilegeActivity.this, MemberPrivilegeActivity.class));
				}
			}	
		}
		else if(v.equals(rlService)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(PrivilegeActivity.this, PhoneRegistActivity.class));
			}
			else{
				getServicePhone();
			}
		}
		
	}
	
	private void getServicePhone(){
		new AsyncTask<Void, Void, GetServicePhoneResponse>(){

			@Override
			protected GetServicePhoneResponse doInBackground(Void... params) {
				try{
					GetServicePhoneRequest request = new GetServicePhoneRequest();
					return  mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}				
			}
			
			protected void onPostExecute(GetServicePhoneResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					String strPhone = result.getData().getPhoneNum();
					if(!StringUtils.isEmpty(strPhone)){
						tvService.setText(strPhone);
						sp.edit().putString("servicePhone", strPhone).commit();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+strPhone));
						startActivity(intent);
						return;
					}
				}
				Toast.makeText(PrivilegeActivity.this, "获取服务电话失败", Toast.LENGTH_LONG).show();
			}
			
		}.execute();
	}
	
	private void updateUI(){
		if(mApplication.getId() == 0){
			tvRoadrescueLabel.setText("每车限一次");
			tvRoadrescueNum.setText("未开启");
			tvMonitorLabel.setText("");
			tvMonitor.setText("未开启");
			tvService.setText("未开启");
		}
		else{
			if(!StringUtils.isEmpty(sp.getString("servicePhone", null))){
				tvService.setText(sp.getString("servicePhone", null));
			}
			vehicleAuths = mApplication.dbCache.getVehicleAuths();
			userVehicles = mApplication.dbCache.getVehicles();
			selfSuccessVehicleAuths = new ArrayList<VehicleAuth>();
			boolean hasRecorded = false;
			rescueCount = 0;
			if(vehicleAuths != null && !vehicleAuths.isEmpty()){
				for(VehicleAuth va : vehicleAuths){
					if(va.getStatus() == 2){
						if(va.getBelong() != null && va.getBelong() == true){
							selfSuccessVehicleAuths.add(va);
							if(va.getResidueDegree() != null)
								rescueCount += va.getResidueDegree().intValue();
							if(!StringUtils.isEmpty(va.getFrameNum()) && !StringUtils.isEmpty(va.getMotorNum())){
								hasRecorded = true;
							}
						}
					}
				}
			}
			if(!selfSuccessVehicleAuths.isEmpty()){
				if(selfSuccessVehicleAuths.size() == 1){
					tvRoadrescueLabel.setText("免费次数剩余");
				}
				else{
					tvRoadrescueLabel.setText("免费次数" + selfSuccessVehicleAuths.size() + "车共计剩余");
				}
				tvRoadrescueNum.setText(rescueCount + "次");
				Collections.sort(selfSuccessVehicleAuths, new VehicleAuthComparator());
				if(hasRecorded){
					if(userVehicles != null && !userVehicles.isEmpty()){
						for(VehicleAuth va : selfSuccessVehicleAuths){
							if(!StringUtils.isEmpty(va.getFrameNum()) && !StringUtils.isEmpty(va.getMotorNum())){
								for(UserVehicle uv : userVehicles){
									if(uv.getVehicleNum() != null && uv.getVehicleNum().equals(va.getVehicleNum())){
										String cityNames = uv.getCityName();
										if(!StringUtils.isEmpty(cityNames)){
											String[] cns = cityNames.split(",");
											if(cns.length <=2)
												tvMonitorLabel.setText(cityNames);
											else
												tvMonitorLabel.setText(cns[0] + "," + cns[1] + "等" + cns.length + "座城市");
										}
										else
											tvMonitorLabel.setText("");
										tvMonitor.setText(va.getVehicleNum());
										break;
									}
								}
								break;
							}
						}
					}	
				}
				else{
					tvMonitorLabel.setText("");
					tvMonitor.setText("开启中");
				}
			}
			else{
				tvRoadrescueLabel.setText("每车限一次");
				tvRoadrescueNum.setText("未开启");
				tvMonitorLabel.setText("");
				tvMonitor.setText("未开启");
			}
			getAuthDetail();
		}		
	}
	
	private void getAuthDetail(){
		if(mApplication.getUserId() != 0 && mApplication.getId() != 0){
			long lastAuthDetailUpdateTime = sp.getLong("lastAuthDetailUpdateTime", 0);
			if((System.currentTimeMillis() - lastAuthDetailUpdateTime) > 20*1000){
				sp.edit().putLong("lastAuthDetailUpdateTime", System.currentTimeMillis()).commit();
				final List<UserVehicle> listUVs = mApplication.dbCache.getVehicles();
				if(listUVs != null && !listUVs.isEmpty()){
					StringBuilder sb = null;
					for(UserVehicle uv : listUVs){
						if(!uv.isHiden()){
							String vn = uv.getVehicleNum();
							if(!StringUtils.isEmpty(vn)){
								if(sb == null){
									sb = new StringBuilder();
									sb.append(vn);
								}
								else
									sb.append("," + vn);
							}
						}
					}
					if(!StringUtils.isEmpty(sb.toString())){
						final String strVehicles = sb.toString();
						new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
							@Override
							protected GetAuthDetaiResponse doInBackground(Void... params) {
								try{
									GetAuthDetaiRequest request = new GetAuthDetaiRequest();
									request.setParams(mApplication.getUserId(), mApplication.getId(), strVehicles);
									return mApplication.client.execute(request);
								}
								catch(Exception e){
									e.printStackTrace();
									return null;
								}
							}
							
							protected void onPostExecute(GetAuthDetaiResponse result) {
								if(result != null && result.getCode() != null && result.getCode() == 0){
									List<VehicleAuth> listVA = result.getData().getList();
									if(listVA != null && !listVA.isEmpty()){
										mApplication.dbCache.saveVehicleAuths(listVA);
										updateUI();
										startService(new Intent(PrivilegeActivity.this, UpdateUserVehicleService.class));
									}
								}
							}
						}.execute();
					}
				}
			}
		}
	}
	

	class VehicleAuthComparator implements Comparator<VehicleAuth>{
		@Override
		public int compare(VehicleAuth lhs, VehicleAuth rhs) {
			if(lhs.getAdjustDate() == null && rhs.getAdjustDate() == null)
				return 0;
			else if(lhs.getAdjustDate() == null && rhs.getAdjustDate() != null)
				return 1;
			else if(lhs.getAdjustDate() != null && rhs.getAdjustDate() == null)
				return -1;
			else
				return lhs.getAdjustDate().compareTo(rhs.getAdjustDate());
		}		
	}	
}
