package com.kplus.car.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.kplus.car.R;
import com.kplus.car.model.CityRegion;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.SelfService;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.response.GetCityRegionListResponse;
import com.kplus.car.model.response.GetSelfServiceListResponse;
import com.kplus.car.model.response.request.GetCityRegionListRequest;
import com.kplus.car.model.response.request.GetSelfServiceListRequest;
import com.kplus.car.util.SelfServiceComparator;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.BaseLoadList;

/**
 * 违章自助办理点列表
 * 
 * @author suyilei
 * 
 */
public class SelfServiceListActivity extends BaseActivity implements
		OnClickListener {

	private DecimalFormat df = new DecimalFormat("#.#");

	private View regionSelect;
	private View toLeft;
	private View toRight;
	private Gallery gallery;
	private RegionAdapter regionAdapter;

	private List<CityRegion> regions;

	private ListView listView;
	private View listEmpty;
	private List<SelfService> servicesAll;
	private List<SelfService> services;
	private SelfServiceListAdapter adapter;
	private LayoutInflater inflater;

	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	
	private View footer;

	private String cityId;
	private String cityName;
	private String region;
	private String vehicleNumber;
	private UserVehicle vehicle;
	private String[] cityIds = null;
	private String[] cityNames = null;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_self_service_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

		regionSelect = findViewById(R.id.self_service_list_column);
		toLeft = findViewById(R.id.llToLeft);
		toRight = findViewById(R.id.llToRight);
		gallery = (Gallery) findViewById(R.id.gallery);

		listView = (ListView) findViewById(R.id.self_service_list_context);
		listEmpty = findViewById(R.id.list_empty);

		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("违章处理点");
	}

	@Override
	protected void loadData() {
		inflater = LayoutInflater.from(this);
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		if(StringUtils.isEmpty(vehicleNumber)){
			Toast.makeText(this, "参数错误", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
		if(vehicle == null){
			Toast.makeText(this, "参数错误", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		cityId = vehicle.getCityId();
		cityName = vehicle.getCityName();
		if(StringUtils.isEmpty(cityId)){
			Toast.makeText(this, "参数错误", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		try{
			cityIds = cityId.split(",");
			cityNames = cityName.split(",");
		}catch(Exception e){
			e.printStackTrace();
		}
		tvTitle.setText(cityNames[0] + "违章处理点");
		new AsyncTask<Void, Void, GetCityRegionListResponse>() {
			protected GetCityRegionListResponse doInBackground(Void... params) {
				try {
					GetCityRegionListRequest request = new GetCityRegionListRequest();
					request.setParams(Long.parseLong(cityIds[0]));				
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetCityRegionListResponse result) {
				if (result != null && result.getCode() != null && result.getCode() == 0) {
					regions = result.getData().getList();
					setRegionData();
				}
			}
		}.execute();

		new AsyncTask<Void, Void, GetSelfServiceListResponse>() {
			protected GetSelfServiceListResponse doInBackground(Void... params) {
				GetSelfServiceListRequest request = new GetSelfServiceListRequest();
				request.setParams(cityNames[0], null);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetSelfServiceListResponse result) {
				if (result != null && result.getCode() != null && result.getCode() == 0) {
					servicesAll = result.getData().getList();
					if (servicesAll != null && servicesAll.size() > 0) {
						try {
							LatLng startLatlng = new LatLng(mApplication.getLocation().getLatitude(), mApplication.getLocation().getLongitude());
							for (SelfService service : servicesAll) {
								LatLng endLatlng = new LatLng(service.getLat(), service.getLng());
								float distanceValue = (float) DistanceUtil.getDistance(startLatlng, endLatlng);
								service.setDistanceValue(distanceValue);
							}
							Collections.sort(servicesAll,new SelfServiceComparator());
						} catch (Exception e) {
						}
						setData();
					} else {
						listEmpty.setVisibility(View.VISIBLE);
					}
				}
			}
		}.execute();
	}

	private void setRegionData() {
		if (regions != null && !regions.isEmpty()) {
			regionSelect.setVisibility(View.VISIBLE);
			regionAdapter = new RegionAdapter();
			gallery.setAdapter(regionAdapter);
			gallery.setSelection(regions.size()*100);
			region = regions.get(0).getName();
			refreshData(region);
		}
	}

	private void setData() {
		if (adapter != null) {
//			adapter.showLoading(false);
			adapter.notifyDataSetChanged();
		} else {
			adapter = new SelfServiceListAdapter(this);
			listView.setAdapter(adapter);
		}
	}

	@Override
	protected void setListener() {
		toLeft.setOnClickListener(this);
		toRight.setOnClickListener(this);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				regionAdapter.setnSelected(arg2);
				region = regions.get(arg2%regions.size()).getName();
				refreshData(region);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
		leftView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
//		String region = null;
		Intent intent = null;
		
		if (v.equals(toLeft)){
			int nSelected = regionAdapter.getnSelected();
			nSelected--;
			gallery.setSelection(nSelected);
			region = regions.get(nSelected%regions.size()).getName();
			refreshData(region);
		}
		else if (v.equals(toRight)){
			int nSelected = regionAdapter.getnSelected();
			nSelected++;
			gallery.setSelection(nSelected);
			region = regions.get(nSelected%regions.size()).getName();
			refreshData(region);
		}
		else if (v.getId() == R.id.self_service_listItem_address) {
			SelfService selfService = (SelfService) v.getTag();
			intent = new Intent(this, MapRoutePlanActivity.class);
			intent.putExtra("lat", selfService.getLat().doubleValue());
			intent.putExtra("lng", selfService.getLng().doubleValue());
			startActivity(intent);
		} else if (v.getId() == R.id.self_service_listItem_callphone) {
			String phone = (String) v.getTag();
			intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
			startActivity(intent);
		} else if (v.equals(leftView)) {
			finish();
		}

	}

	private void refreshData(String region) {
		List<SelfService> listtemp = new ArrayList<SelfService>();
		if(servicesAll != null && !servicesAll.isEmpty()){
			for(SelfService ss : servicesAll){
				if(ss.getRegionName() != null && ss.getRegionName().equals(region)){
					listtemp.add(ss);
				}
			}
			if (listtemp.isEmpty()){
				listEmpty.setVisibility(View.VISIBLE);
			}
			else{
				listEmpty.setVisibility(View.GONE);
				if(services != null && !services.isEmpty())
					services.clear();
				else if(services == null)
					services = new ArrayList<SelfService>();
				services.addAll(listtemp);
				setData();
			}
		}
	}

	private class SelfServiceListAdapter extends BaseLoadList<SelfService> {

		public SelfServiceListAdapter(BaseActivity context) {
			super(context);
		}

		@Override
		public void initItem(final SelfService it, Map<String, Object> holder) {
			TextView name = (TextView) holder.get("name");
			TextView address = (TextView) holder.get("address");
			TextView distance = (TextView) holder.get("distance");
			TextView callphone = (TextView) holder.get("callphone");

			name.setText(it.getName());
			address.setText(it.getAddress());
			callphone.setText(it.getPhone());

			if (it.getDistanceValue() >= 1000) {
				distance.setText(df.format(it.getDistanceValue() / 1000) + "km");
			} else {
				distance.setText((int) it.getDistanceValue() + "m");
			}
			address.setTag(it);
			address.setOnClickListener(SelfServiceListActivity.this);
			if (StringUtils.isEmpty(it.getPhone())) {
				callphone.setVisibility(View.GONE);
			} else {
				callphone.setVisibility(View.VISIBLE);
				callphone.setTag(it.getPhone());
				callphone.setOnClickListener(SelfServiceListActivity.this);
			}
		}

		@Override
		public Map<String, Object> getHolder(View v) {
			Map<String, Object> root = new HashMap<String, Object>();
			TextView name = (TextView) v.findViewById(R.id.self_service_listItem_name);
			TextView address = (TextView) v.findViewById(R.id.self_service_listItem_address);
			TextView distance = (TextView) v.findViewById(R.id.self_service_listItem_distance);
			TextView callphone = (TextView) v.findViewById(R.id.self_service_listItem_callphone);

			root.put("name", name);
			root.put("address", address);
			root.put("distance", distance);
			root.put("callphone", callphone);

			return root;
		}

		@Override
		public List<SelfService> executeFirst() throws Exception {
			services = linkeList;
			return servicesAll;
		}

		@Override
		public int getLayoutId(int index) {
			return R.layout.daze_self_service_list_item;
		}

		@Override
		public void showLoading(boolean show) {
			if (show) {
				LayoutInflater in = LayoutInflater.from(context);
				footer = in.inflate(R.layout.daze_list_foot, null);
				((ListView) listView).addFooterView(footer, null, false);
			} else {
				((ListView) listView).removeFooterView(footer);
			}
		}
	}
	
	class RegionAdapter extends BaseAdapter{
		private int nSelected = -1;

		@Override
		public int getCount()
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return regions.get(position%regions.size());
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView tvregion = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.daze_gallery_item_region, parent,false);
				tvregion = (TextView) convertView.findViewById(R.id.tvregion);
				convertView.setTag(tvregion);
			}
			else
				tvregion = (TextView) convertView.getTag();
			tvregion.setText(regions.get(position%regions.size()).getName());
			if(nSelected == position)
				convertView.setBackgroundColor(getResources().getColor(R.color.daze_darkgrey8));
			else
				convertView.setBackgroundColor(Color.WHITE);
			
			return convertView;
		}

		public int getnSelected()
		{
			return nSelected;
		}

		public void setnSelected(int nSelected)
		{
			this.nSelected = nSelected;
			notifyDataSetChanged();
		}
		
	}
}