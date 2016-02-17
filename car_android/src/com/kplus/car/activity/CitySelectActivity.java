package com.kplus.car.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.service.UpdateAgainstRecords;
import com.kplus.car.service.UpdateUserVehicleService;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitySelectActivity extends BaseActivity implements OnClickListener
{
	private String userLocation = null;
	private String cityIds = null;
	private UserVehicle vehicle = null;
	private boolean isSingleSelect = true;
	private int fromtype;
	
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View rightView;
	private ImageView ivRight;
	private TextView tvPrefix;
	
	private GridView gvCitySelected;
	private ArrayList<CityVehicle> listSelected;
	private CitySelectAdapter adapterSelected;
	
	private TextView tvLocate;
	private CityVehicle cvLocate;
	
	private View hotCityLabel;
	private GridView gvHotCity;
	private HotCityAdapter adapterHotCity;
	private ArrayList<CityVehicle> listHotCity;
	
	private ListView lvProvience;
	private AllCityAdapter provienceAdapter;
	private int[] prefixLoaction = null;
	private static final String[] proviencePrefix = {"京","渝","沪","津","皖","闽","甘","粤","桂","贵","琼","冀","豫","黑",
														"鄂","湘","苏","赣","吉","辽","蒙","宁","青","鲁",
														"晋","陕","川","藏","新","云","浙"};
	private static final String[] proviences = {"北京","重庆","上海","天津","安徽","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江",
												"湖北","湖南","江苏","江西","吉林","辽宁","内蒙古","宁夏","青海","山东",
												"山西","陕西","四川","西藏","新疆","云南","浙江"};
	private ListView lvCity;
	private ArrayList<CityVehicle> listCity;
	private CityAdapter adapterCity;
	private LayoutInflater inflater;
	private View subCityView;
	
	private List<CityVehicle> allList;
	private RelativeLayout.LayoutParams prefixLP;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				if(prefixLoaction == null)
					prefixLoaction = new int[2];
				tvPrefix.getLocationOnScreen(prefixLoaction);
				int[] location = new int[2];
				lvProvience.getLocationOnScreen(location);
				if(location[1] < (prefixLoaction[1] + dip2px(CitySelectActivity.this, 84))){
					tvPrefix.setVisibility(View.VISIBLE);
				}
				else{
					tvPrefix.setVisibility(View.GONE);
				}
				try{
					int nCount = lvProvience.getChildCount();
					for(int i=nCount-1;i>=0;i--){
						View v = lvProvience.getChildAt(i);
						int[] locationTemp = new int[2];
						v.getLocationOnScreen(locationTemp);
						if(locationTemp[1] > prefixLoaction[1] && locationTemp[1] < (prefixLoaction[1] + dip2px(CitySelectActivity.this, 84))){
							tvPrefix.setText(proviencePrefix[i]);
							break;
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				Message message = new Message();
				message.what = 1;
				mHandler.sendMessageDelayed(message, 200);
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_set_monitor_city);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		
		leftView = (findViewById(R.id.leftView));
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_goback_24);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("选择城市");
		rightView = (findViewById(R.id.rightView));
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setImageResource(R.drawable.daze_btn_ok);
		
		gvCitySelected = (GridView) findViewById(R.id.gvCitySelected);
		tvLocate = (TextView) findViewById(R.id.tvLocate);
		if(mApplication.getLocationCityName() != null)
			tvLocate.setText(mApplication.getLocationCityName());
		lvProvience = (ListView) findViewById(R.id.lvProvience);
		lvCity = (ListView) findViewById(R.id.lvCity);
		hotCityLabel = findViewById(R.id.hotCityLabel);
		gvHotCity = (GridView)findViewById(R.id.hotCitySelected);
		tvPrefix = (TextView) findViewById(R.id.tvPrefix);
		prefixLP = (RelativeLayout.LayoutParams)tvPrefix.getLayoutParams();
		prefixLP.topMargin = (int) (mApplication.getnScreenHeight()*0.3);
		tvPrefix.setLayoutParams(prefixLP);
		tvPrefix.setVisibility(View.GONE);
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessageDelayed(msg, 200);
		subCityView = findViewById(R.id.subCityView);
	}

	@Override
	protected void loadData()
	{
		inflater = LayoutInflater.from(this);
		cityIds = getIntent().getStringExtra("cityId");
		vehicle = (UserVehicle) getIntent().getSerializableExtra("vehicle");
		fromtype = getIntent().getIntExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD);
		if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_AUTHENTICATION){
			isSingleSelect = false;
			String vehicleNumber = getIntent().getStringExtra("vehicleNumber");
			if(!StringUtils.isEmpty(vehicleNumber)){
				vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
			}
			if(vehicle != null){
				cityIds = vehicle.getCityId();
			}
		}
		else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD){
			if(vehicle == null){
				isSingleSelect = true;
			}
			else{
				VehicleAuth vehicleAuth = null;
				if(!StringUtils.isEmpty(vehicle.getVehicleNum()))
					vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicle.getVehicleNum());
				if(vehicleAuth != null && vehicleAuth.getBelong() != null && vehicleAuth.getBelong() && vehicleAuth.getStatus() != null && vehicleAuth.getStatus() == 2)
					isSingleSelect = false;
				else
					isSingleSelect = true;
			}
		}
		else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL){
			VehicleAuth vehicleAuth = null;
			if(!StringUtils.isEmpty(vehicle.getVehicleNum()))
				vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicle.getVehicleNum());
			if(vehicleAuth != null && vehicleAuth.getStatus() != null && vehicleAuth.getStatus() == 2)
				isSingleSelect = false;
			else
				isSingleSelect = true;
		}
		else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_USERINFO){
			isSingleSelect = true;
			userLocation = getIntent().getStringExtra("cityName");
		}
        else if (fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_HOME){
            isSingleSelect = true;
            userLocation = getIntent().getStringExtra("cityName");
			if(mApplication.getLocationCityName() != null){
				findViewById(R.id.cur_loc_label).setVisibility(View.VISIBLE);
				findViewById(R.id.cur_loc).setVisibility(View.VISIBLE);
			}
			else {
				findViewById(R.id.cur_loc_label).setVisibility(View.GONE);
				findViewById(R.id.cur_loc).setVisibility(View.GONE);
			}
        }
		if(isSingleSelect)
			rightView.setVisibility(View.GONE);
		else
			rightView.setVisibility(View.VISIBLE);
		if (mApplication.getCities().isEmpty()){
			ToastUtil.TextToast(CitySelectActivity.this, "获取城市信息失败，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
		else{
			setData();
		}
	}

	@Override
	protected void setListener()
	{
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		tvLocate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if(v.equals(leftView)){
			if(subCityView.getVisibility() == View.VISIBLE){
				subCityView.startAnimation(AnimationUtils.loadAnimation(CitySelectActivity.this, R.anim.slide_out_to_right));
				subCityView.postDelayed(new Runnable() {					
					@Override
					public void run() {
						subCityView.setVisibility(View.GONE);
						tvTitle.setText("选择城市");
						if(!isSingleSelect)
							rightView.setVisibility(View.VISIBLE);
					}
				}, 300);
			}
			else
				finish();
		}
		else if(v.equals(rightView)){
			if(!isSingleSelect){
				if(listSelected != null && !listSelected.isEmpty()){
					if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_AUTHENTICATION){						
						saveVehicle();
					}
					else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD){
						Intent intent = new Intent();
						intent.setClass(this, VehicleAddNewActivity.class);
						ArrayList<CityVehicle> listresult = new ArrayList<CityVehicle>();
						listresult.addAll(listSelected);
						intent.putParcelableArrayListExtra("selectedCity", listresult);
						setResult(VehicleAddNewActivity.CITY_RESULT, intent);
						finish();
					}
					else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL){
						Intent intent = new Intent();
						intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
						ArrayList<CityVehicle> listresult = new ArrayList<CityVehicle>();
						listresult.addAll(listSelected);
						intent.putParcelableArrayListExtra("selectedCity", listresult);
						intent.putExtra("vehicle", vehicle);
						startActivity(intent);
						finish();
					}
				}
			}
		}
		else if(v.equals(tvLocate)){
			if(cvLocate != null){
				if(isSingleSelect){
					if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD){
						Intent intent = new Intent();
						intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
						ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
						listTemp.add(cvLocate);
						intent.putParcelableArrayListExtra("selectedCity", listTemp);
						setResult(VehicleAddNewActivity.CITY_RESULT, intent);
						finish();
					}
					else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL){
						Intent intent = new Intent();
						intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
						ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
						listTemp.add(cvLocate);
						intent.putParcelableArrayListExtra("selectedCity", listTemp);
						intent.putExtra("vehicle", vehicle);
						startActivity(intent);
						finish();
					}
					else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_USERINFO || fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_HOME){
						Intent intent = new Intent(CitySelectActivity.this, UserInfoActivity.class);
						ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
						listTemp.add(cvLocate);
						intent.putParcelableArrayListExtra("selectedCity", listTemp);
						setResult(RESULT_OK, intent);
						finish();
					}
				}
				else{
					if(!listSelected.contains(cvLocate)){
						if(listSelected.size() >= 5){
							ToastUtil.TextToast(CitySelectActivity.this, "查询城市最多只能设置5个", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
						else{
							listSelected.add(cvLocate);
							adapterSelected.notifyDataSetChanged();
							adapterHotCity.notifyDataSetChanged();
							setCitySelectGridViewHeight();
							if(adapterCity != null)
							adapterCity.notifyDataSetChanged();
						}
					}
				}
			}
		}
	}
	
	private void setData(){
		provienceAdapter = new AllCityAdapter();
		lvProvience.setAdapter(provienceAdapter);
		setListViewHeightBasedOnChildren(lvProvience);
		listSelected = new ArrayList<CityVehicle>();
		if (cityIds != null && !cityIds.trim().equals("")) {
			String[] ids = cityIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				listSelected.add(mApplication.getCityMap().get(Long.parseLong(ids[i])));
			}
		}
		else if(!StringUtils.isEmpty(userLocation)){
			for(CityVehicle cv :mApplication.getCities()){
				if(userLocation.contains(cv.getName()) || cv.getName().contains(userLocation)){
					listSelected.add(cv);
					break;
				}
			}
		}
		adapterSelected = new CitySelectAdapter();
		gvCitySelected.setAdapter(adapterSelected);
		setCitySelectGridViewHeight();
		allList = mApplication.getCities();
		listHotCity = new ArrayList<CityVehicle>();
		if(allList != null && !allList.isEmpty()){
			if(mApplication.getLocationCityName() != null){
				for(CityVehicle cv : allList){
					if(cv.getName().contains(mApplication.getLocationCityName()))
					{
						cvLocate = cv;
					}
					if(cv.getHot() != null && cv.getHot())
						listHotCity.add(cv);
				}
			}
			if(listHotCity != null && !listHotCity.isEmpty()){
				hotCityLabel.setVisibility(View.VISIBLE);
				gvHotCity.setVisibility(View.VISIBLE);
			}
			adapterHotCity = new HotCityAdapter();
			gvHotCity.setAdapter(adapterHotCity);
			setHotCitySelectGridViewHeight();
		}
	}
	
	class AllCityAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return proviences.length;
		}

		@Override
		public Object getItem(int position) {
			return proviences[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder;
			if(convertView == null){
				holder = new HashMap<String, Object>();
				convertView = inflater.inflate(R.layout.daze_city_select_item, parent, false);
				TextView text_cityName = (TextView) convertView.findViewById(R.id.text_cityName);
				ImageView ivTonext = (ImageView) convertView.findViewById(R.id.ivTonext);
				ImageView ivIsSelected = (ImageView) convertView.findViewById(R.id.ivIsSelected);
				holder.put("text_cityName", text_cityName);
				holder.put("ivTonext", ivTonext);
				holder.put("ivIsSelected", ivIsSelected);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			TextView text_cityName = (TextView) holder.get("text_cityName");
			text_cityName.setText(proviences[position]);
			ImageView ivTonext = (ImageView) holder.get("ivTonext");
			ImageView ivIsSelected = (ImageView) holder.get("ivIsSelected");
			if(isMunicipality(proviences[position])){
				ivTonext.setVisibility(View.GONE);
				ivIsSelected.setVisibility(View.GONE);
				if(listSelected != null && !listSelected.isEmpty()){
					for(CityVehicle cv : listSelected){
						if(cv.getName().contains(proviences[position])){
							ivIsSelected.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			else{
				ivTonext.setVisibility(View.VISIBLE);
				ivIsSelected.setVisibility(View.GONE);
			}
			text_cityName.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					listCity = new ArrayList<CityVehicle>();
					if(allList != null){
						if(isMunicipality(proviences[position])){
							CityVehicle cvTemp = null;
							for(CityVehicle cv : allList){
								if(cv.getName().contains(proviences[position])){
									cvTemp = cv;
									break;
								}
							}
							if(cvTemp == null){
								return;
							}
							if(isSingleSelect){
								if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD){
									Intent intent = new Intent();
									intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
									ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
									listTemp.add(cvTemp);
									intent.putParcelableArrayListExtra("selectedCity", listTemp);
									setResult(VehicleAddNewActivity.CITY_RESULT, intent);
									finish();
								}
								else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL){
									Intent intent = new Intent();
									intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
									ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
									listTemp.add(cvTemp);
									intent.putParcelableArrayListExtra("selectedCity", listTemp);
									intent.putExtra("vehicle", vehicle);
									startActivity(intent);
									finish();
								}
								else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_USERINFO || fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_HOME){
									Intent intent = new Intent();
									intent.setClass(CitySelectActivity.this, UserInfoActivity.class);
									ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
									listTemp.add(cvTemp);
									intent.putParcelableArrayListExtra("selectedCity", listTemp);
									setResult(RESULT_OK, intent);
									finish();
								}
							}
							else{
								if(!listSelected.contains(cvTemp)){
									if(listSelected.size() >= 5){
										ToastUtil.TextToast(CitySelectActivity.this, "查询城市最多只能设置5个", Toast.LENGTH_SHORT, Gravity.CENTER);
									}
									else{
										listSelected.add(cvTemp);
										notifyDataSetChanged();
										provienceAdapter.notifyDataSetChanged();
										adapterHotCity.notifyDataSetChanged();
										setCitySelectGridViewHeight();
									}
								}
							}
						}
						else{
							for(CityVehicle cv : allList){
								if(cv.getProvince().contains(proviences[position]))
									listCity.add(cv);
							}
							adapterCity = new CityAdapter(listCity);
							lvCity.setAdapter(adapterCity);
							tvTitle.setText(proviences[position]);
							subCityView.startAnimation(AnimationUtils.loadAnimation(CitySelectActivity.this, R.anim.slide_in_from_right));
							subCityView.postDelayed(new Runnable() {								
								@Override
								public void run() {
									subCityView.setVisibility(View.VISIBLE);
									rightView.setVisibility(View.GONE);
								}
							}, 300);
						}
					}
				}
			});
			return convertView;
		}
		
	}

	
	class CityAdapter extends BaseAdapter{
		private List<CityVehicle> list;
		
		public CityAdapter(List<CityVehicle> _list){
			list = _list;
		}
		
		@Override
		public int getCount()
		{
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			Map<String, Object> holder;
			if(convertView == null){
				holder = new HashMap<String, Object>();
				convertView = inflater.inflate(R.layout.daze_city_select_item, parent, false);
				TextView text_cityName = (TextView) convertView.findViewById(R.id.text_cityName);
				ImageView ivTonext = (ImageView) convertView.findViewById(R.id.ivTonext);
				ImageView ivIsSelected = (ImageView) convertView.findViewById(R.id.ivIsSelected);
				holder.put("text_cityName", text_cityName);
				holder.put("ivTonext", ivTonext);
				holder.put("ivIsSelected", ivIsSelected);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			TextView text_cityName = (TextView) holder.get("text_cityName");
			text_cityName.setText(list.get(position).getName());
			ImageView ivTonext = (ImageView) holder.get("ivTonext");
			ivTonext.setVisibility(View.GONE);
			ImageView ivIsSelected = (ImageView) holder.get("ivIsSelected");
			ivIsSelected.setVisibility(View.GONE);
			for(CityVehicle cv : listSelected){
				if(cv.getName().contains(list.get(position).getName())){
					ivIsSelected.setVisibility(View.VISIBLE);
				}
			}
			text_cityName.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(isSingleSelect){
						if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD){
							Intent intent = new Intent();
							intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
							ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
							listTemp.add(listCity.get(position));
							intent.putParcelableArrayListExtra("selectedCity", listTemp);
							setResult(VehicleAddNewActivity.CITY_RESULT, intent);
							finish();
						}
						else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL){
							Intent intent = new Intent();
							intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
							ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
							listTemp.add(listCity.get(position));
							intent.putParcelableArrayListExtra("selectedCity", listTemp);
							intent.putExtra("vehicle", vehicle);
							startActivity(intent);
							finish();
						}
						else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_USERINFO || fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_HOME){
							Intent intent = new Intent();
							intent.setClass(CitySelectActivity.this, UserInfoActivity.class);
							ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
							listTemp.add(listCity.get(position));
							intent.putParcelableArrayListExtra("selectedCity", listTemp);
							setResult(RESULT_OK, intent);
							finish();
						}
					}
					else{
						if(!listSelected.contains(listCity.get(position))){
							if(listSelected.size() >= 5){
								ToastUtil.TextToast(CitySelectActivity.this, "查询城市最多只能设置5个", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
							else
								listSelected.add(listCity.get(position));
						}
						else{
							listSelected.remove(listCity.get(position));
						}
						adapterSelected.notifyDataSetChanged();
						adapterCity.notifyDataSetChanged();
						setCitySelectGridViewHeight();
						adapterHotCity.notifyDataSetChanged();
					}
				}
			});
			return convertView;
		}
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
	
	private void setCitySelectGridViewHeight() {
        ViewGroup.LayoutParams params = gvCitySelected.getLayoutParams(); 
        int columNumbers = mApplication.getnScreenWidth()/(dip2px(CitySelectActivity.this, 108));
        gvCitySelected.setNumColumns(columNumbers);
        if(listSelected != null){
        	if(listSelected.size() == 0)
        		params.height = dip2px(CitySelectActivity.this, 48);
        	else if(listSelected.size()%columNumbers == 0)
        		params.height = dip2px(CitySelectActivity.this, 48)*(listSelected.size()/columNumbers);
        	else
        		params.height = dip2px(CitySelectActivity.this, 48)*(1 + listSelected.size()/columNumbers);  
        	gvCitySelected.setLayoutParams(params); 
        } 
    }
	private void setHotCitySelectGridViewHeight() {
        ViewGroup.LayoutParams hotparams = gvHotCity.getLayoutParams();
        if(listHotCity != null){
        	if(listHotCity.size() == 0)
        		hotparams.height = dip2px(CitySelectActivity.this, 48);
        	else if(listHotCity.size()%4 == 0)
        		hotparams.height = dip2px(CitySelectActivity.this, 48)*(listHotCity.size()/4)+2*(listHotCity.size()/4);
        	else
        		hotparams.height = dip2px(CitySelectActivity.this, 48)*(1 + listHotCity.size()/4)+2*(listHotCity.size()/4+1);  
        	gvHotCity.setLayoutParams(hotparams); 
        } 
    }
	
	private void saveVehicle() {
		showloading(true);
		new AsyncTask<Void, Void, VehicleAddResponse>() {
			private String errortext = "网络中断，请稍候重试";
			protected VehicleAddResponse doInBackground(Void... params) {
				VehicleAddRequest request = new VehicleAddRequest();
				StringBuilder sbId = new StringBuilder();
				StringBuilder sbName = new StringBuilder();
				for(CityVehicle cv : listSelected){
					sbId.append(cv.getId()).append(",");
					sbName.append(cv.getName()).append(",");
				}					
				cityIds = sbId.substring(0, sbId.length()-1);
				vehicle.setCityId(cityIds);
				vehicle.setCityName(sbName.substring(0, sbName.length()-1));
				request.setParams(mApplication.getUserId(),mApplication.getId(), vehicle);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					errortext = e.toString();
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(VehicleAddResponse result) {
				showloading(false);
				if (result != null) {
					if (result.getCode() != null && result.getCode() == 0) {
						if (result.getData().getResult()) {
							Toast.makeText(CitySelectActivity.this,	"修改监控城市成功", Toast.LENGTH_SHORT).show();
							if(result.getData().getId() != null)
								vehicle.setVehicleId(result.getData().getId());
							vehicle.setHiden(false);
							mApplication.dbCache.saveVehicle(vehicle);
							if(mApplication.getId() != 0)
								getVehicleAuth(vehicle.getVehicleNum());
							Intent update = new Intent(CitySelectActivity.this, UpdateAgainstRecords.class);
							update.putExtra("vehicleNumber", vehicle.getVehicleNum());
							startService(update);
						} else {
							Toast.makeText(CitySelectActivity.this,"修改监控城市失败", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(CitySelectActivity.this,	result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CitySelectActivity.this, errortext,Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}
	
	private void getVehicleAuth(final String vehicleNum){
		new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
			@Override
			protected GetAuthDetaiResponse doInBackground(Void... params) {
				try{
					GetAuthDetaiRequest request = new GetAuthDetaiRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleNum);
					return mApplication.client.execute(request);
				}catch(Exception e){
					return null;
				}
			}
			protected void onPostExecute(GetAuthDetaiResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					if(result.getData() != null){
						List<VehicleAuth> listVA = result.getData().getList();
						if(listVA != null && !listVA.isEmpty()){
							if(listVA.size() == 1){
								VehicleAuth va = listVA.get(0);
								if(va != null){
									if((va.getStatus() != null && va.getStatus() == 2) || (va.getBelong() != null && va.getBelong())){
										mApplication.dbCache.saveVehicleAuth(va);
										startService(new Intent(CitySelectActivity.this, UpdateUserVehicleService.class));
									}
								}
								finish();
								return;
							}
							for(VehicleAuth va : listVA){
								if(va.getStatus() != null && va.getStatus() == 2){
									mApplication.dbCache.saveVehicleAuth(va);
									startService(new Intent(CitySelectActivity.this, UpdateUserVehicleService.class));
									finish();
									return;
								}
							}
							for(VehicleAuth va : listVA){
								if(va.getBelong() != null && va.getBelong()){
									mApplication.dbCache.saveVehicleAuth(va);
									startService(new Intent(CitySelectActivity.this, UpdateUserVehicleService.class));
									finish();
									return;
								}
							}	
						}
					}
				}
				finish();
			}
		}.execute();
	}
	
	private boolean isMunicipality(String name){
		if(StringUtils.isEmpty(name))
			return false;
		if(name.contains("北京") || name.contains("上海") || name.contains("重庆") || name.contains("天津"))
			return true;
		return false;
	}
	
	class CitySelectAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(listSelected == null || listSelected.isEmpty())
				return 0;
			return listSelected.size();
		}

		@Override
		public Object getItem(int position) {
			if(listSelected == null || listSelected.isEmpty())
				return null;
			return listSelected.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder = null;
			if(convertView == null){
				holder = new HashMap<String, Object>();
				convertView = inflater.inflate(R.layout.daze_city_selected_item, parent, false);
				View citySelectLayout = convertView.findViewById(R.id.citySelectLayout);
				TextView tvCitySelected = (TextView) convertView.findViewById(R.id.tvCitySelected);
				holder.put("citySelectLayout", citySelectLayout);
				holder.put("tvCitySelected", tvCitySelected);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			final View citySelectLayout = (View) holder.get("citySelectLayout");
			citySelectLayout.setTag(listSelected.get(position));
			citySelectLayout.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					CityVehicle tag = (CityVehicle) citySelectLayout.getTag();
					listSelected.remove(tag);
					notifyDataSetChanged();
					setCitySelectGridViewHeight();
					provienceAdapter.notifyDataSetChanged();
				}
			});
			TextView tvCitySelected = (TextView) holder.get("tvCitySelected");
			tvCitySelected.setText(listSelected.get(position).getName());
			return convertView;
		}
		
	}
	
	class HotCityAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(listHotCity == null || listHotCity.isEmpty())
				return 0;
			return listHotCity.size();
		}

		@Override
		public Object getItem(int position) {
			if(listHotCity == null || listHotCity.isEmpty())
				return null;
			return listHotCity.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder = null;
			if(convertView == null){
				holder = new HashMap<String, Object>();
				convertView = inflater.inflate(R.layout.daze_city_selected_hotitem, parent, false);
				TextView hottvCitySelected = (TextView) convertView.findViewById(R.id.hottvCitySelected);
				View hotcitySelectLayout = convertView.findViewById(R.id.hotcitySelectLayout);
				holder.put("hottvCitySelected", hottvCitySelected);
				holder.put("hotcitySelectLayout", hotcitySelectLayout);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			TextView hottvCitySelected = (TextView) holder.get("hottvCitySelected");
			View hotcitySelectLayout = (View) holder.get("hotcitySelectLayout");
			hottvCitySelected.setText(listHotCity.get(position).getName());
			if(listSelected.contains(listHotCity.get(position))){
//				hotcitySelectLayout.setBackground(getResources().getDrawable(R.drawable.daze_btn_down));
				hotcitySelectLayout.setBackgroundResource(R.drawable.daze_btn_down);
			}
			else{
//				hotcitySelectLayout.setBackground(getResources().getDrawable(R.drawable.daze_btn_bg_white));
				hotcitySelectLayout.setBackgroundResource(R.drawable.daze_btn_bg_white);
			}
			hotcitySelectLayout.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(isSingleSelect){
						Intent intent = new Intent();
						if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD){						
							intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
							ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
							listTemp.add(listHotCity.get(position));
							intent.putParcelableArrayListExtra("selectedCity", listTemp);
							setResult(VehicleAddNewActivity.CITY_RESULT, intent);
							finish();
						}
						else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL){
							intent.setClass(CitySelectActivity.this, VehicleAddNewActivity.class);
							ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
							listTemp.add(listHotCity.get(position));
							intent.putParcelableArrayListExtra("selectedCity", listTemp);
							intent.putExtra("vehicle", vehicle);
							startActivity(intent);
							finish();
						}
						else if(fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_USERINFO || fromtype == KplusConstants.SELECT_CITY_FROM_TYPE_HOME){
							intent.setClass(CitySelectActivity.this, UserInfoActivity.class);
							ArrayList<CityVehicle> listTemp = new ArrayList<CityVehicle>();
							listTemp.add(listHotCity.get(position));
							intent.putParcelableArrayListExtra("selectedCity", listTemp);
							setResult(RESULT_OK, intent);
							finish();
						}
					}
					else{
						if(!listSelected.contains(listHotCity.get(position))){
							if(listSelected.size() >= 5){
								ToastUtil.TextToast(CitySelectActivity.this, "查询城市最多只能设置5个", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
							else{
							listSelected.add(listHotCity.get(position));
							adapterSelected.notifyDataSetChanged();
							setCitySelectGridViewHeight();
							provienceAdapter.notifyDataSetChanged();
							notifyDataSetChanged();
							}
						}
					}
				}
			});
			return convertView;
		}
		
	}
}
