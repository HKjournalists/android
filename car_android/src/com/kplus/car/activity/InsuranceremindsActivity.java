package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.AdjustIssueDateResponse;
import com.kplus.car.model.response.request.AdjustIssueDateRequest;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class InsuranceremindsActivity extends BaseActivity implements OnClickListener {
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView lvListview;
	private View tvEmpty;
	private LayoutInflater mInflater;
	private List<VehicleAuth> vehicleAuths;
	private InsuranceremindsAdapter adapter;
	private int currentIndex;
	private DatePickerDialog  dpd = null;
	private DatePickerDialog.OnDateSetListener dpListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			Calendar cTemp = Calendar.getInstance();
			cTemp.set(year, monthOfYear, dayOfMonth);
			if(DateUtil.getDaysFromNow(cTemp) < 0)
				Toast.makeText(InsuranceremindsActivity.this, "日期设置错误", Toast.LENGTH_SHORT).show();
			else
				saveAdjustDate(cTemp);
		}
	};
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
			Collections.sort(vehicleAuths, new VehicleAuthComparator());
			adapter.notifyDataSetChanged();
		}
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
		tvTitle.setText("保险提醒");
		lvListview = (ListView) findViewById(R.id.lvListview);
		tvEmpty = findViewById(R.id.tvEmpty);
		tvEmpty.setVisibility(View.GONE);
	}

	@Override
	protected void loadData() {
		mInflater = LayoutInflater.from(this);
		vehicleAuths = new ArrayList<VehicleAuth>();
		adapter = new InsuranceremindsAdapter();
		lvListview.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(leftView)){
			finish();
		}
	}
	
	private void saveAdjustDate(final Calendar calendar){
		new AsyncTask<Void, Void, AdjustIssueDateResponse>() {
			@Override
			protected AdjustIssueDateResponse doInBackground(Void... params) {
				try{
					AdjustIssueDateRequest request = new AdjustIssueDateRequest();
					String adjustDate = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
					request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleAuths.get(currentIndex).getVehicleNum(), adjustDate);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(AdjustIssueDateResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					try{
						vehicleAuths.get(currentIndex).setAdjustDate(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
						ToastUtil.TextToast(InsuranceremindsActivity.this, "修改成功", Toast.LENGTH_SHORT, Gravity.CENTER);
						adapter.notifyDataSetChanged();
						mApplication.dbCache.saveVehicleAuth(vehicleAuths.get(currentIndex));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				else{
					ToastUtil.TextToast(InsuranceremindsActivity.this, "修改失败", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}
	
	class InsuranceremindsAdapter extends BaseAdapter{

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
			HashMap<String, Object> map = null;
			if(arg1 == null){
				map = new HashMap<String, Object>();
				arg1 = mInflater.inflate(R.layout.daze_listview_item, arg2, false);
				View rootView = arg1.findViewById(R.id.rootView);
				TextView textview1 = (TextView) arg1.findViewById(R.id.textview1);
				TextView textview2 = (TextView) arg1.findViewById(R.id.textview2);
				TextView textview3 = (TextView) arg1.findViewById(R.id.textview3);
				map.put("rootView", rootView);
				map.put("textview1", textview1);
				map.put("textview2", textview2);
				map.put("textview3", textview3);
				arg1.setTag(map);
			}
			else
				map = (HashMap<String, Object>) arg1.getTag();
			View rootView = (View) map.get("rootView");
			TextView textview1 = (TextView) map.get("textview1");
			final TextView textview2 = (TextView) map.get("textview2");
			final TextView textview3 = (TextView) map.get("textview3");
			String vehicleNumber = vehicleAuths.get(arg0).getVehicleNum();
			rootView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					try{
						String adjustDate = vehicleAuths.get(arg0).getAdjustDate();
						if(dpd == null){
							if(!StringUtils.isEmpty(adjustDate)){
								String[] dateTemp = adjustDate.split("-");
								dpd = new DatePickerDialog(InsuranceremindsActivity.this, dpListener, Integer.parseInt(dateTemp[0]), Integer.parseInt(dateTemp[1])-1, Integer.parseInt(dateTemp[2]));
							}
							else{
								Calendar calendar = Calendar.getInstance();
								dpd = new DatePickerDialog(InsuranceremindsActivity.this, dpListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
							}
						}
						else{
							if(!StringUtils.isEmpty(adjustDate)){
								String[] dateTemp = adjustDate.split("-");
								dpd.updateDate(Integer.parseInt(dateTemp[0]), Integer.parseInt(dateTemp[1])-1, Integer.parseInt(dateTemp[2]));
							}
							else{
								Calendar calendar = Calendar.getInstance();
								dpd.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
							}
						}
						dpd.show();
						currentIndex = arg0;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			if(!StringUtils.isEmpty(vehicleNumber)){
				textview1.setText(vehicleNumber);
			}
			String adjustDate = vehicleAuths.get(arg0).getAdjustDate();
			if(!StringUtils.isEmpty(adjustDate)){
				try{
					String[] dates = adjustDate.split("-");
					Calendar ctemp = Calendar.getInstance();
					ctemp.set(Integer.parseInt(dates[0]), Integer.parseInt(dates[1])-1, Integer.parseInt(dates[2]));
					long days = DateUtil.getDaysFromNow(ctemp);
					if(days <= 0){
						textview2.setText("");
						textview3.setText("保险已到期");
					}
					else{
						textview2.setText("距离" + adjustDate + "还有");
						textview3.setText(days + "天");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			else{
				textview2.setText("");
				textview3.setText("");
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
