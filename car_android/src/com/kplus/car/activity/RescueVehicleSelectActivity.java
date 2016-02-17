package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.VehicleAuth;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RescueVehicleSelectActivity extends BaseActivity {
	private static final int REQUEST_FOR_ENQUIRE = 1;
	private ListView lvListview;
	private Button btCancel;
	private View tvEmpty;
	private LayoutInflater mInflater;
	private List<VehicleAuth> vehicleAuths;
	private VehicleAdapter mAdapter;
	private String vehicleNumber;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_select_vehicle_to_rescue);
		lvListview = (ListView) findViewById(R.id.lvListview);
		btCancel = (Button) findViewById(R.id.btCancel);
		tvEmpty = findViewById(R.id.tvEmpty);
		tvEmpty.setVisibility(View.GONE);
	}

	@Override
	protected void loadData() {
		mInflater = LayoutInflater.from(this);
		vehicleAuths = new ArrayList<VehicleAuth>();
		List<VehicleAuth> vehicleAuthsTemp = mApplication.dbCache.getVehicleAuths();
		if(vehicleAuthsTemp != null && !vehicleAuthsTemp.isEmpty()){
			for(VehicleAuth va : vehicleAuthsTemp){
				if(va.getBelong() != null && va.getBelong()){
					if(va.getStatus() != null && va.getStatus() == 2){
						vehicleAuths.add(va);
					}
				}
			}
		}
		mAdapter = new VehicleAdapter();
		lvListview.setAdapter(mAdapter);
	}

	@Override
	protected void setListener() {
		btCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(0, R.anim.slide_out_to_bottom);
			}
		});
		lvListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				if(vehicleAuths.get(arg2).getResidueDegree() <= 0){
					vehicleNumber = vehicleAuths.get(arg2).getVehicleNum();
					Intent intent = new Intent(RescueVehicleSelectActivity.this, AlertDialogActivity.class);
					intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
					intent.putExtra("message", "您本辆车的免费救援次数已经用完，使用该功能将会产生服务费用，是否继续？");
					startActivityForResult(intent, REQUEST_FOR_ENQUIRE);
				}
				else{
					Intent intent = new Intent(RescueVehicleSelectActivity.this, EmergencyDetailActivity.class);
					intent.putExtra("vehicleNumber", vehicleAuths.get(arg2).getVehicleNum());
					startActivity(intent);
					finish();
				}
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(0, R.anim.slide_out_to_bottom);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_ENQUIRE){
			if(resultCode == RESULT_OK){
				Intent intent = new Intent(RescueVehicleSelectActivity.this, EmergencyDetailActivity.class);
				intent.putExtra("vehicleNumber", vehicleNumber);
				startActivity(intent);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class VehicleAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(vehicleAuths == null || vehicleAuths.isEmpty())
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1 == null){
				arg1 = mInflater.inflate(R.layout.daze_listview_item2, arg2, false);
				TextView tvVehicleNumber = (TextView) arg1.findViewById(R.id.tvVehicleNumber);
				arg1.setTag(tvVehicleNumber);
			}
			TextView tvVehicleNumber = (TextView) arg1.getTag();
			tvVehicleNumber.setText(vehicleAuths.get(arg0).getVehicleNum());
			return arg1;
		}
		
	}

}
