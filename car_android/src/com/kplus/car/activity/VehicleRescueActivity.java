package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.service.UpdateUserVehicleService;
import com.kplus.car.util.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VehicleRescueActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_FOR_WHERE_RESCUE = 1;
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView lvListview;
	private View tvEmpty;
	private LayoutInflater mInflater;
	private List<VehicleAuth> vehicleAuths;
	private VehicleRescueAdapter adapter;
	private String vehicleNumber;
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
				Collections.sort(vehicleAuths, new VehicleAuthComparator());
				adapter.notifyDataSetChanged();
			}
			if(vehicleAuths == null || vehicleAuths.isEmpty())
				tvEmpty.setVisibility(View.VISIBLE);
			else
				tvEmpty.setVisibility(View.GONE);
		}
	};
	
	@Override
	protected void onResume() {
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
			Collections.sort(vehicleAuths, new VehicleAuthComparator());
			adapter.notifyDataSetChanged();
		}
		if(vehicleAuths == null || vehicleAuths.isEmpty())
			tvEmpty.setVisibility(View.VISIBLE);
		else
			tvEmpty.setVisibility(View.GONE);
		getAuthDetail();
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE));
		super.onResume();
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
		tvTitle.setText("道路救援");
		lvListview = (ListView) findViewById(R.id.lvListview);
		tvEmpty = findViewById(R.id.tvEmpty);
		tvEmpty.setVisibility(View.GONE);
	}

	@Override
	protected void loadData() {
		mInflater = LayoutInflater.from(this);
		vehicleAuths = new ArrayList<VehicleAuth>();
		adapter = new VehicleRescueAdapter();
		lvListview.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		lvListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				if(vehicleAuths.get(arg2).getResidueDegree() <= 0){
					vehicleNumber = vehicleAuths.get(arg2).getVehicleNum();
					Intent intent = new Intent(VehicleRescueActivity.this, AlertDialogActivity.class);
					intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
					intent.putExtra("message", "您本辆车的免费救援次数已经用完，使用该功能将会产生服务费用，是否继续？");
					startActivityForResult(intent, REQUEST_FOR_WHERE_RESCUE);
				}
				else{
					Intent intent = new Intent(VehicleRescueActivity.this, EmergencyDetailActivity.class);
					intent.putExtra("vehicleNumber", vehicleAuths.get(arg2).getVehicleNum());
					startActivity(intent);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(leftView)){
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_WHERE_RESCUE){
			if(resultCode == RESULT_OK){
				Intent intent = new Intent(VehicleRescueActivity.this, EmergencyDetailActivity.class);
				intent.putExtra("vehicleNumber", vehicleNumber);
				startActivity(intent);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class VehicleRescueAdapter extends BaseAdapter{

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
			String vehicleNumber = vehicleAuths.get(arg0).getVehicleNum();
			if(!StringUtils.isEmpty(vehicleNumber)){
				textview1.setText(vehicleNumber);
			}
			textview2.setText("免费次数剩余");
			int number = vehicleAuths.get(arg0).getResidueDegree() == null ? 0 : vehicleAuths.get(arg0).getResidueDegree();
			textview3.setText(number + "次");
			return arg1;
		}
		
	}
	
	private void getAuthDetail(){
		if(mApplication.getUserId() != 0 && mApplication.getId() != 0){
			long lastAuthDetailUpdateTime = sp.getLong("lastAuthDetailUpdateTime", 0);
			if((System.currentTimeMillis() - lastAuthDetailUpdateTime) > 20*1000){
				sp.edit().putLong("lastAuthDetailUpdateTime", System.currentTimeMillis()).commit();
				final List<UserVehicle> listUVs = mApplication.dbCache.getVehicles();
				if(listUVs != null && !listUVs.isEmpty()){
					StringBuilder sb = new StringBuilder();
					for(UserVehicle uv : listUVs){
						if(!uv.isHiden()){
							String vn = uv.getVehicleNum();
							if(!StringUtils.isEmpty(vn))
								sb.append(vn + ",");
						}
					}
					if(!StringUtils.isEmpty(sb.toString())){
						final String strVehicles = sb.substring(0, sb.length()-1);
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
										Collections.sort(listVA, new Comparator<VehicleAuth>() {
											@Override
											public int compare(VehicleAuth arg0, VehicleAuth arg1) {
												if(arg0.getVehicleNum().compareToIgnoreCase(arg1.getVehicleNum()) == 0){
													if(arg0.getStatus() != null && arg0.getStatus() == 2){
														return 1;
													}
													if(arg1.getStatus() != null && arg1.getStatus() == 2){
														return -1;
													}
													if(arg0.getBelong() != null && arg0.getBelong())
														return 1;
													if(arg1.getBelong() != null && arg1.getBelong())
														return -1;
													return 0;
												}
												return arg0.getVehicleNum().compareToIgnoreCase(arg1.getVehicleNum());
											}
										});
										mApplication.dbCache.saveVehicleAuths(listVA);
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
											Collections.sort(vehicleAuths, new VehicleAuthComparator());
											adapter.notifyDataSetChanged();
											startService(new Intent(VehicleRescueActivity.this, UpdateUserVehicleService.class));
										}
										if(vehicleAuths == null || vehicleAuths.isEmpty())
											tvEmpty.setVisibility(View.VISIBLE);
										else
											tvEmpty.setVisibility(View.GONE);
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
