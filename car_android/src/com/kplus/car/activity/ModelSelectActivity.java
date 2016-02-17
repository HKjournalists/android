package com.kplus.car.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.VehicleBrand;
import com.kplus.car.model.VehicleModel;
import com.kplus.car.model.response.GetVehicleModelListResponse;
import com.kplus.car.model.response.request.GetVehicleModelListRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.MyLetterListView;
import com.kplus.car.widget.MyLetterListView.OnTouchingLetterChangedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ModelSelectActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private static final int REQUEST_FOR_ADD_MODE = 1;
	private static final int REQUEST_FOR_EDIT_MODE = 2;

	private View backBtn;
	private ProgressBar dataLoading;

	private View brandLayout;
	private View modelLayout;
	private ListView brandListView;
	private ListView modelListView;
	private MyLetterListView letterListView;
	private View llHide;
	private TextView tvNoFind, tvModeTitle;
	private ListView listSelfMode;

	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private SelfDefineModeAdapter selfdefineAdapter;
	private VehicleModel currentMode;
	private VehicleBrand brand;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_model_select);
		backBtn = findViewById(R.id.model_select_backBtn);
		dataLoading = (ProgressBar) findViewById(R.id.brand_select_loading);
		brandLayout = findViewById(R.id.brand_select_layout);
		brandListView = (ListView) findViewById(R.id.brand_select_list);
		modelLayout = findViewById(R.id.model_select_layout);
		modelListView = (ListView) findViewById(R.id.model_select_list);
		letterListView = (MyLetterListView) findViewById(R.id.brand_select_list_letter);
		llHide = findViewById(R.id.llHide);
		tvNoFind = (TextView) findViewById(R.id.tvNoFind);
		tvModeTitle = (TextView) findViewById(R.id.model_select_item_title);
		listSelfMode = (ListView)findViewById(R.id.listSelfMode); 
	}

	@Override
	protected void loadData() {
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.daze_car_default)
		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		if (mApplication.getBrands() == null || mApplication.getBrands().isEmpty()) {
			initBrandModelData();
		} else {
			setData();
		}
	}

	private void setData() {
		dataLoading.setVisibility(View.GONE);
		letterListView.setVisibility(View.VISIBLE);
		BrandListAdapter selectedAdapter = new BrandListAdapter(this,
				mApplication.getBrands());
		brandListView.setAdapter(selectedAdapter);
	}

	private void initBrandModelData() {
		new AsyncTask<Void, Void, GetVehicleModelListResponse>() {
			protected GetVehicleModelListResponse doInBackground(Void... params) {
				GetVehicleModelListRequest request = new GetVehicleModelListRequest();
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetVehicleModelListResponse result) {
				if (result != null && result.getCode() != null && result.getCode() == 0) {
					mApplication.setBrands(result.getData().getList());
					setData();
				}
			}
		}.execute();
	}

	@Override
	protected void setListener() {
		backBtn.setOnClickListener(this);
		brandListView.setOnItemClickListener(this);
		modelListView.setOnItemClickListener(this);
		letterListView.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			public void onTouchingLetterChanged(final String s) {
				if (alphaIndexer.get(s) != null) {
					int position = alphaIndexer.get(s);
					brandListView.setSelection(position);
				}
			}
		});
		llHide.setOnClickListener(this);
		tvNoFind.setOnClickListener(this);
		listSelfMode.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(backBtn)) {
			if (brand == null)
				finish();
			else {
				brand = null;
//				brandLayout.setVisibility(View.VISIBLE);
				modelLayout.setVisibility(View.GONE);
			}
		}
		else if(v.equals(llHide)){
			modelLayout.setVisibility(View.GONE);
		}
		else if(v.equals(tvNoFind)){
			Intent intent = new Intent(ModelSelectActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_VEHICLE_MODE_SELF_DEFINE);
			intent.putExtra("vehicleBrand", brand.getName());
			intent.putExtra("brandId", brand.getId());
			startActivityForResult(intent, REQUEST_FOR_ADD_MODE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View v, int position,
			long arg3) {
		if (listView.equals(brandListView)) {
			try{
				brand = (VehicleBrand) listView.getAdapter().getItem(position);
				modelListView.setAdapter(new ModelListAdapter(this, brand.getModels()));
	//			brandLayout.setVisibility(View.GONE);
				modelLayout.setVisibility(View.VISIBLE);
				tvModeTitle.setText(brand.getName());
				List<VehicleModel> listvehiclemodes = mApplication.dbCache.getSelfDefineModesByBrandId(brand.getId());
				selfdefineAdapter = new SelfDefineModeAdapter(this, listvehiclemodes);
				listSelfMode.setAdapter(selfdefineAdapter);
				currentMode = null;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} else if (listView.equals(modelListView)) {
			VehicleModel model = (VehicleModel) listView.getAdapter().getItem(
					position);
			Intent intent = new Intent(this, VehicleAddNewActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("vehicleModel", model);
			intent.putExtras(bundle);
			setResult(VehicleAddNewActivity.MODEL_RESULT, intent);
			finish();
		}
		else if(listView.equals(listSelfMode)){
			VehicleModel model = (VehicleModel) listView.getAdapter().getItem(
					position);
			Intent intent = new Intent(this, VehicleAddNewActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("vehicleModel", model);
			intent.putExtras(bundle);
			setResult(VehicleAddNewActivity.MODEL_RESULT, intent);
			finish();
		}
	}

	private class BrandListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<VehicleBrand> list;

		public BrandListAdapter(Context context, List<VehicleBrand> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = list.get(i).getCode();
				// 上一个汉语拼音首字母，如果不存在为" "
				String previewStr = (i - 1) >= 0 ? list.get(i - 1).getCode() : " ";
				if (!previewStr.equals(currentStr)) {
					alphaIndexer.put(currentStr, i);
					sections[i] = currentStr;
				}
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public VehicleBrand getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.daze_model_select_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.model_select_item_title);
				holder.name = (TextView) convertView.findViewById(R.id.model_select_item_name);
				holder.image = (ImageView) convertView.findViewById(R.id.model_select_item_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(getItem(position).getName());
			String currentStr = getItem(position).getCode();
			String previewStr = (position - 1) >= 0 ? getItem(position - 1).getCode() : " ";
			if (!previewStr.equals(currentStr)) {
				holder.title.setVisibility(View.VISIBLE);
				holder.title.setText(currentStr);
			} else {
				holder.title.setVisibility(View.GONE);
			}

			final ImageView imageView = holder.image;
			mApplication.imageLoader.displayImage(getItem(position).getLogo(), imageView, options);
			return convertView;
		}

		private class ViewHolder {
			TextView title;
			ImageView image;
			TextView name;
		}

	}

	private class ModelListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<VehicleModel> list;

		public ModelListAdapter(Context context, List<VehicleModel> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public VehicleModel getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.daze_model_select_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.model_select_item_name);
				holder.image = (ImageView) convertView.findViewById(R.id.model_select_item_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(getItem(position).getName());

			final ImageView imageView = holder.image;
			mApplication.imageLoader.displayImage(getItem(position).getImage(),	imageView, options);
			return convertView;
		}

		private class ViewHolder {
			ImageView image;
			TextView name;
		}

	}
	
	private class SelfDefineModeAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private List<VehicleModel> list;

		public SelfDefineModeAdapter(Context context, List<VehicleModel> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		public List<VehicleModel> getList() {
			return list;
		}

		public void setList(List<VehicleModel> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if(list == null)
				return 0;
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			if(list == null)
				return null;
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			HashMap<String, Object> holder = null;
			if(arg1 == null){
				arg1 = inflater.inflate(R.layout.daze_self_define_vehicle_mode_item, arg2, false);
				TextView tvSelfDefineVehicleModeLabel = (TextView) arg1.findViewById(R.id.tvSelfDefineVehicleModeLabel);
				TextView tvModifySelfdefineMode = (TextView) arg1.findViewById(R.id.tvModifySelfdefineMode);
				holder = new HashMap<String, Object>();
				holder.put("tvSelfDefineVehicleModeLabel", tvSelfDefineVehicleModeLabel);
				holder.put("tvModifySelfdefineMode", tvModifySelfdefineMode);
				arg1.setTag(holder);
			}
			else
				holder = (HashMap<String, Object>) arg1.getTag();
			TextView tvSelfDefineVehicleModeLabel = (TextView) holder.get("tvSelfDefineVehicleModeLabel");
			TextView tvModifySelfdefineMode = (TextView) holder.get("tvModifySelfdefineMode");
			tvSelfDefineVehicleModeLabel.setText(list.get(arg0).getName());
			tvModifySelfdefineMode.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View view) {
					currentMode = list.get(arg0);
					Intent intent = new Intent(ModelSelectActivity.this, AlertDialogActivity.class);
					intent.putExtra("alertType", KplusConstants.ALERT_VEHICLE_MODE_SELF_DEFINE);
					intent.putExtra("vehicleBrand", brand.getName());
					intent.putExtra("brandId", brand.getId());
					intent.putExtra("modeName", currentMode.getName());
					startActivityForResult(intent, REQUEST_FOR_ADD_MODE);
				}
			});
			return arg1;
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(modelLayout.getVisibility() == View.VISIBLE){
				modelLayout.setVisibility(View.GONE);
				return true;
			}
			else{
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case REQUEST_FOR_ADD_MODE:
			if(resultCode == RESULT_OK){
				if(data != null){
					String modeName = data.getStringExtra("modeName");
					if(!StringUtils.isEmpty(modeName)){
						if(currentMode != null){
							mApplication.dbCache.deleteDefineVehicleMode(currentMode);
						}						
						VehicleModel vm = new VehicleModel();
						vm.setBrandId(brand.getId());
						vm.setName(modeName);
						vm.setImage(brand.getLogo());
						mApplication.dbCache.saveSelfDefineVehicleMode(vm);
						if(selfdefineAdapter != null){
							List<VehicleModel> listTemp = mApplication.dbCache.getSelfDefineModesByBrandId(brand.getId());
							selfdefineAdapter.setList(listTemp);
						}
						Intent intent = new Intent(ModelSelectActivity.this, VehicleAddNewActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("vehicleModel", vm);
						intent.putExtras(bundle);
						setResult(VehicleAddNewActivity.MODEL_RESULT, intent);
						finish();
					}
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
