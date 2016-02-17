package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class MonitorCitiesActivity extends BaseActivity {
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView lvListview;
	private View tvEmpty;
	private LayoutInflater mInflater;
	private MonitorCityAdapter adapter;
	private List<VehicleAuth> vehicleAuths;
	private List<UserVehicle> userVehicles;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			vehicleAuths.clear();
			List<VehicleAuth> listTemp = mApplication.dbCache.getVehicleAuths();
			if(listTemp != null && !listTemp.isEmpty()){
				for(VehicleAuth va : listTemp){
					if(va.getBelong() != null && va.getBelong()){
						if(va.getStatus() != null && va.getStatus() == 2){
							vehicleAuths.add(va);
						}
					}
				}
			}
			Collections.sort(vehicleAuths, new VehicleAuthComparator());
			userVehicles = mApplication.dbCache.getVehicles();
			adapter.notifyDataSetChanged();
			if(vehicleAuths == null || vehicleAuths.isEmpty())
				tvEmpty.setVisibility(View.VISIBLE);
			else
				tvEmpty.setVisibility(View.GONE);
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		vehicleAuths.clear();
		List<VehicleAuth> listTemp = mApplication.dbCache.getVehicleAuths();
		if(listTemp != null && !listTemp.isEmpty()){
			for(VehicleAuth va : listTemp){
				if(va.getBelong() != null && va.getBelong()){
					if(va.getStatus() != null && va.getStatus() == 2){
						vehicleAuths.add(va);
					}
				}
			}
		}
		Collections.sort(vehicleAuths, new VehicleAuthComparator());
		userVehicles = mApplication.dbCache.getVehicles();
		adapter.notifyDataSetChanged();
		if(vehicleAuths == null || vehicleAuths.isEmpty())
			tvEmpty.setVisibility(View.VISIBLE);
		else
			tvEmpty.setVisibility(View.GONE);
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE));
	}
	
	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
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
		tvTitle.setText("违章监控");
		lvListview = (ListView) findViewById(R.id.lvListview);
		tvEmpty = findViewById(R.id.tvEmpty);
		tvEmpty.setVisibility(View.GONE);
	}

	@Override
	protected void loadData() {
		mInflater = LayoutInflater.from(this);
		vehicleAuths = new ArrayList<VehicleAuth>();
		adapter = new MonitorCityAdapter();
		lvListview.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	class MonitorCityAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(vehicleAuths == null)
				return 0;
			return vehicleAuths.size();
		}

		@Override
		public Object getItem(int arg0) {
			if(vehicleAuths == null || vehicleAuths.isEmpty())
				return null;
			return vehicleAuths.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			final VehicleAuth va = vehicleAuths.get(arg0);
			HashMap<String, Object> map = null;
			if(arg1 == null){
				map = new HashMap<String, Object>();
				arg1 = mInflater.inflate(R.layout.daze_listview_item, arg2, false);
				View rootView = arg1.findViewById(R.id.rootView);
				TextView textview1 = (TextView) arg1.findViewById(R.id.textview1);
				TextView textview3 = (TextView) arg1.findViewById(R.id.textview3);
				map.put("rootView", rootView);
				map.put("textview1", textview1);
				map.put("textview3", textview3);
				arg1.setTag(map);
			}
			else
				map = (HashMap<String, Object>) arg1.getTag();
			View rootView = (View) map.get("rootView");
			TextView textview1 = (TextView) map.get("textview1");
			TextView textview3 = (TextView) map.get("textview3");
			final String vehicleNumber = vehicleAuths.get(arg0).getVehicleNum();
			rootView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(!StringUtils.isEmpty(va.getFrameNum()) && !StringUtils.isEmpty(va.getMotorNum())){
						Intent intent = new Intent(MonitorCitiesActivity.this, CitySelectActivity.class);
						intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_AUTHENTICATION);
						intent.putExtra("vehicleNumber", vehicleNumber);
						startActivity(intent);
					}
					else{
						ToastUtil.TextToast(MonitorCitiesActivity.this, "服务正在开启，请稍候", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
			});
			if(!StringUtils.isEmpty(va.getFrameNum()) && !StringUtils.isEmpty(va.getMotorNum())){
				if(!StringUtils.isEmpty(vehicleNumber)){
					textview1.setText(vehicleNumber);
					if(userVehicles != null && !userVehicles.isEmpty()){
						for(UserVehicle uv : userVehicles){
							if(uv.getVehicleNum() != null && uv.getVehicleNum().equals(vehicleNumber)){
								String cityNames = uv.getCityName();
								if(!StringUtils.isEmpty(cityNames)){
									String[] cns = cityNames.split(",");
									if(cns.length <=2)
										textview3.setText(cityNames);
									else
										textview3.setText(cns[0] + "," + cns[1] + "等" + cns.length + "座城市");
								}
								else
									textview3.setText("违章监控城市提醒");
							}
						}
					}
				}
			}
			else{
				textview1.setText(vehicleNumber);
				textview3.setText("开启中");
			}
			
			return arg1;
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
