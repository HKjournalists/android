package com.kplus.car.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllVehiclesActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_FOR_ADD_VEHICLE = 1;
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView lvListview;
	private View emptyView;
	private Button btAddVehicle;
	private LayoutInflater mInflater;
	private VehicleAdapter adapter;
	private List<VehicleAuth> vehicleAuths;
	private List<UserVehicle> userVehicles;
	private Resources resources;
	private SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
	private SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			vehicleAuths = mApplication.dbCache.getVehicleAuths();
			userVehicles = mApplication.dbCache.getVehicles();
			if(userVehicles == null || userVehicles.isEmpty())
				emptyView.setVisibility(View.VISIBLE);
			else
				emptyView.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		vehicleAuths = mApplication.dbCache.getVehicleAuths();
		userVehicles = mApplication.dbCache.getVehicles();
		if(userVehicles == null || userVehicles.isEmpty())
			emptyView.setVisibility(View.VISIBLE);
		else
			emptyView.setVisibility(View.GONE);
		adapter.notifyDataSetChanged();
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, new IntentFilter(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE));
	}
	
	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
		super.onPause();
	}

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_listview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("我的车辆");
		lvListview = (ListView) findViewById(R.id.lvListview);
		adapter = new VehicleAdapter();
		lvListview.setAdapter(adapter);
		emptyView = findViewById(R.id.emptyView);
		btAddVehicle = (Button) findViewById(R.id.btAddVehicle);
	}

	@Override
	protected void loadData() {
		mInflater = LayoutInflater.from(this);
		resources = getResources();
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		btAddVehicle.setOnClickListener(this);
		lvListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  long arg3) {
				try{
					if(userVehicles != null && !userVehicles.isEmpty()){
						String vehicleNumber = userVehicles.get(arg2).getVehicleNum();
						if(!StringUtils.isEmpty(vehicleNumber)){
							VehicleAuth vaTemp = null;
							if(vehicleAuths != null && !vehicleAuths.isEmpty()){
								for(VehicleAuth va : vehicleAuths){
									if(va.getVehicleNum() != null && va.getVehicleNum().equals(vehicleNumber)){
										vaTemp = va;
										break;
									}
								}
							}
							if(vaTemp == null){
								Intent intent = new Intent(AllVehiclesActivity.this, EmergencyLicenseActivity.class);
								intent.putExtra("vehicleNumber", vehicleNumber);
								startActivity(intent);
							}
							else{
								if(vaTemp.getStatus() != null && vaTemp.getStatus() == 2){
									if(vaTemp.getBelong() != null && vaTemp.getBelong()){
										Intent intent = new Intent(AllVehiclesActivity.this, EmergencyLicenseActivity.class);
										intent.putExtra("vehicleNumber", vehicleNumber);
										startActivity(intent);
									}
									else{
										ToastUtil.TextToast(AllVehiclesActivity.this, "该车辆已被其他用户认证", Toast.LENGTH_SHORT, Gravity.CENTER);
									}
								}
								else{
									Intent intent = new Intent(AllVehiclesActivity.this, EmergencyLicenseActivity.class);
									intent.putExtra("vehicleNumber", vehicleNumber);
									startActivity(intent);
								}
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.equals(leftView)){
			finish();
		}
		else if(v.equals(btAddVehicle)){
			EventAnalysisUtil.onEvent(AllVehiclesActivity.this, "addCar", "添加车辆", null);
			startActivityForResult(new Intent(AllVehiclesActivity.this, VehicleAddNewActivity.class), REQUEST_FOR_ADD_VEHICLE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_ADD_VEHICLE){
			if(resultCode == KplusConstants.FLAG_CANCEL_ADD){
				lvListview.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(!sp.getBoolean("hasRemind", false)){
							Intent intent = new Intent();
							intent.setClass(AllVehiclesActivity.this,AlertDialogActivity.class);
							intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
							intent.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_REMIND);
							intent.putExtra("title", "行驶证没在身边？");
							intent.putExtra("message", "设置闹钟后我们将提醒您添加爱车");
							intent.putExtra("leftButtonText", "下次再说");
							intent.putExtra("rightButtonText", "去设置");
							startActivity(intent);	
						}
					}
				}, 100);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class VehicleAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(userVehicles == null)
				return 0;
			return userVehicles.size();
		}

		@Override
		public Object getItem(int arg0) {
			if(userVehicles == null || userVehicles.isEmpty())
				return null;
			return userVehicles.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			HashMap<String, Object> map = null;
			if(arg1 == null){
				map = new HashMap<String, Object>();
				arg1 = mInflater.inflate(R.layout.daze_listview_item, arg2, false);
				TextView textview1 = (TextView) arg1.findViewById(R.id.textview1);
				TextView textview2 = (TextView) arg1.findViewById(R.id.textview2);
				TextView textview3 = (TextView) arg1.findViewById(R.id.textview3);
				map.put("textview1", textview1);
				map.put("textview2", textview2);
				map.put("textview3", textview3);
				arg1.setTag(map);
			}
			else
				map = (HashMap<String, Object>) arg1.getTag();
			TextView textview1 = (TextView) map.get("textview1");
			TextView textview2 = (TextView) map.get("textview2");
			TextView textview3 = (TextView) map.get("textview3");
			String vehicleNumber = userVehicles.get(arg0).getVehicleNum();
			if(!StringUtils.isEmpty(vehicleNumber)){
				textview1.setText(vehicleNumber);
				if(vehicleAuths != null && !vehicleAuths.isEmpty()){
					for(VehicleAuth va : vehicleAuths){
						if(va.getVehicleNum() != null && va.getVehicleNum().equals(vehicleNumber)){
							switch(va.getStatus()){
							case 0:
								textview2.setText("");
								textview3.setText("未认证");
								textview3.setTextColor(resources.getColor(R.color.daze_orangered5));
//								if(va.getBelong() != null && va.getBelong())
//									textview3.setTextColor(resources.getColor(R.color.daze_orangered5));
//								else
//									textview3.setTextColor(resources.getColor(R.color.daze_darkgrey9));
								break;
							case 1:
								textview2.setText("");
								textview3.setText("认证中");
								textview3.setTextColor(resources.getColor(R.color.daze_orangered4));
//								if(va.getBelong() != null && va.getBelong())
//									textview3.setTextColor(resources.getColor(R.color.daze_orangered5));
//								else
//									textview3.setTextColor(resources.getColor(R.color.daze_darkgrey9));
								break;
							case 2:
								textview3.setText("已认证");
								if(va.getBelong() != null && va.getBelong()){
									textview3.setTextColor(resources.getColor(R.color.daze_orangered4));
									if(!StringUtils.isEmpty(va.getAuthDatetime())){
										String authDate = va.getAuthDatetime();
										try{
											Date date = sdfold.parse(authDate);
											authDate = sdfnew.format(date);
											textview2.setText(authDate);
										}
										catch(Exception e){
											e.printStackTrace();
											textview2.setText(va.getAuthDatetime());
										}
									}
								}
								else{
									textview3.setTextColor(resources.getColor(R.color.daze_white_smoke2));
									textview2.setText("已被其他用户认证");
								}
								break;
							case 3:
								textview2.setText("");
								textview3.setText("认证失败");
								textview3.setTextColor(resources.getColor(R.color.daze_orangered5));
//								if(va.getBelong() != null && va.getBelong())
//									textview3.setTextColor(resources.getColor(R.color.daze_orangered5));
//								else
//									textview3.setTextColor(resources.getColor(R.color.daze_darkgrey9));
								break;
								default:
									break;
							}
							break;
						}
					}
				}
			}
			return arg1;
		}
		
	}

}
