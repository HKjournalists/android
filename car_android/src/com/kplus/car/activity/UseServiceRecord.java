package com.kplus.car.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.R;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetUseServiceRecordResponse;
import com.kplus.car.model.response.request.GetUseServiceRecordRequest;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.BaseLoadList;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UseServiceRecord extends BaseActivity
{
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View viewEmpty;
	private ListView listService;
	
	private List<com.kplus.car.model.UseServiceRecord> list;
	private UseServiceRecordAdapter adapter;
	private String vehicleNumber;
	

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_use_service_record);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("我的救援记录");
		viewEmpty = findViewById(R.id.use_service_empty);
		viewEmpty.setVisibility(View.GONE);
		listService = (ListView) findViewById(R.id.listService);
	}

	@Override
	protected void loadData()
	{
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		showloading(true);
		new GetDataTask().execute();		
	}

	@Override
	protected void setListener()
	{
		leftView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});		
	}
	
	class GetDataTask extends AsyncTask<Void, Void, GetUseServiceRecordResponse>{
		private String errortext = "网络中断，请稍后重试";
		@Override
		protected GetUseServiceRecordResponse doInBackground(Void... params)
		{
			try{
				GetUseServiceRecordRequest request = new GetUseServiceRecordRequest();
				VehicleAuth va = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicleNumber);
				if(va.getId() != null && va.getId() != 0)
					request.setParams(va.getId());	
				else{
					errortext = "参数错误";
					return null;
				}
				return mApplication.client.execute(request);
			}catch(Exception e){
				e.printStackTrace();
				errortext = e.toString();
				return null;
			}
		}
		 @Override
		protected void onPostExecute(GetUseServiceRecordResponse result)
		{
			if(result != null){
				if(result.getCode() != null && result.getCode() == 0){
					list = result.getData().getList();
					adapter = new UseServiceRecordAdapter(UseServiceRecord.this);
					listService.setAdapter(adapter);
				}
				else{
					ToastUtil.TextToast(UseServiceRecord.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
			else{
				ToastUtil.TextToast(UseServiceRecord.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
			}
			showloading(false);
			
			super.onPostExecute(result);
		}
	}
	
	class UseServiceRecordAdapter extends BaseLoadList<com.kplus.car.model.UseServiceRecord>{
		public UseServiceRecordAdapter(Activity context){
			super(context);
		}
		
		@Override
		public void initItem(com.kplus.car.model.UseServiceRecord it, Map<String, Object> holder)
		{
			TextView tvTime = (TextView) holder.get("tvTime");
			TextView tvService = (TextView) holder.get("tvService");
			TextView tvResult = (TextView) holder.get("tvResult");
			TextView tvFee = (TextView) holder.get("tvFee");
			if(it.getUseTime() != null)
				tvTime.setText(it.getUseTime());
			if(it.getServiceType() != null)
				tvService.setText(it.getServiceType());
			if(it.getServiceResult() != null)
				tvResult.setText(it.getServiceResult());
			if(it.getServiceFee() != null)
				tvFee.setText(it.getServiceFee());
		}

		@Override
		public Map<String, Object> getHolder(View v)
		{
			Map<String ,Object> root = new HashMap<String, Object>();
			TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
			TextView tvService = (TextView) v.findViewById(R.id.tvService);
			TextView tvResult = (TextView) v.findViewById(R.id.tvResult);
			TextView tvFee = (TextView) v.findViewById(R.id.tvFee);
			root.put("tvTime", tvTime);
			root.put("tvService", tvService);
			root.put("tvResult", tvResult);
			root.put("tvFee", tvFee);
			
			return root;
		}

		@Override
		public List<com.kplus.car.model.UseServiceRecord> executeFirst() throws Exception
		{
			// TODO Auto-generated method stub
			return list;
		}

		@Override
		public int getLayoutId(int index)
		{
			// TODO Auto-generated method stub
			return R.layout.daze_use_service_record_item;
		}

		@Override
		public void showLoading(boolean show)
		{
			if(!show){
				if(list == null || list.isEmpty())
					viewEmpty.setVisibility(View.VISIBLE);
			}
		}
		
	}
}
