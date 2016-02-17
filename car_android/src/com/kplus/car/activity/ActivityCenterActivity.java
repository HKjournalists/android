package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.ActivityInfo;
import com.kplus.car.model.json.ActivityInfoJson;
import com.kplus.car.model.response.GetActvityListResponse;
import com.kplus.car.model.response.request.GetActivityListRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCenterActivity extends BaseActivity implements OnClickListener {
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView lvListview;
	private TextView tvEmpty;
	private LayoutInflater mInflater;
	private List<ActivityInfo> list;
	private DazeActivityAdapter adapter;
	private int total;
	private int padding8, padding16;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_listview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("活动中心");
		lvListview = (ListView) findViewById(R.id.lvListview);
		tvEmpty = (TextView) findViewById(R.id.tvEmpty);
		tvEmpty.setVisibility(View.GONE);
	}

	@Override
	protected void loadData() {
		padding8 = dip2px(this, 8);
		padding16 = dip2px(this, 16);
//		list = mApplication.dbCache.getActivities();
		if(list == null)
			list = new ArrayList<ActivityInfo>();
		adapter = new DazeActivityAdapter();
		lvListview.setAdapter(adapter);
		if(list == null || list.isEmpty()){
			tvEmpty.setVisibility(View.VISIBLE);
		}
		else{
			tvEmpty.setVisibility(View.GONE);
		}
		getActivities();
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);	
		lvListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (list != null) {
					ActivityInfo ai = list.get(position);
					if (ai != null && !StringUtils.isEmpty(ai.getLink())) {
						if (ai.getLink().contains("appId") && ai.getLink().contains("startPage")) {
							try {
								JSONObject jsonObject = new JSONObject(ai.getLink());
								String startPage = jsonObject.optString("startPage");
								String appId = jsonObject.optString("appId");
								Intent vehicleServiceIntent = new Intent(ActivityCenterActivity.this, VehicleServiceActivity.class);
								vehicleServiceIntent.putExtra("appId", appId);
								vehicleServiceIntent.putExtra("startPage", startPage);
								startActivity(vehicleServiceIntent);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							Intent intent = new Intent(ActivityCenterActivity.this, VehicleServiceActivity.class);
							intent.putExtra("appId", "null");
							intent.putExtra("startPage", ai.getLink());
							startActivity(intent);
						}
					}
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.leftView:
			finish();
			break;
		case R.id.tvLoadMore:
			if(((TextView)arg0).getText().toString().equals("点击加载更多"))
				getActivities();
			break;
		default:
			break;
		}
	}
	
	private void getActivities(){
		new AsyncTask<Void, Void, GetActvityListResponse>(){
			protected void onPreExecute() {
				showloading(true);
			}
			@Override
			protected GetActvityListResponse doInBackground(Void... arg0) {
				try{
					GetActivityListRequest request = new GetActivityListRequest();
//					request.setParams(0, 0, mApplication.dbCache.getValue(KplusConstants.DB_KEY_ACTIVITY_LIST_UPDATE_TIME));
                    request.setParams(0, 0, null);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(GetActvityListResponse result) {
				showloading(false);
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						ActivityInfoJson data = result.getData();
						if(data != null){
							if(data.getTotal() != null)
								total = data.getTotal();
//							String updateTime = data.getUpdateTime();
//							if(!StringUtils.isEmpty(updateTime))
//								mApplication.dbCache.putValue(KplusConstants.DB_KEY_ACTIVITY_LIST_UPDATE_TIME, updateTime);
							if(data.getList() != null && !data.getList().isEmpty()){
								List<ActivityInfo> aisTemp = data.getList();
								list.addAll(aisTemp);
								adapter.notifyDataSetChanged();
								mApplication.dbCache.saveActivities(aisTemp);
							}
							if(list == null || list.isEmpty()){
								tvEmpty.setVisibility(View.VISIBLE);
							}
							else{
								tvEmpty.setVisibility(View.GONE);
							}
						}
					}
					else
						ToastUtil.TextToast(ActivityCenterActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				else
					ToastUtil.TextToast(ActivityCenterActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
			}
		}.execute();
	}
	
	private class DazeActivityAdapter extends BaseAdapter{

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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Map<String, Object> holder = null;
			if(arg1 == null){
				holder = new HashMap<String, Object>();
				if(mInflater == null)
					mInflater = LayoutInflater.from(ActivityCenterActivity.this);
				arg1 = mInflater.inflate(R.layout.daze_listview_item3, arg2, false);
				View rootView = arg1.findViewById(R.id.rootView);
				ImageView topBigImage = (ImageView) arg1.findViewById(R.id.topBigImage);
				TextView tvPartakeNum = (TextView) arg1.findViewById(R.id.tvPartakeNum);
				TextView tvActivityTime = (TextView) arg1.findViewById(R.id.tvActivityTime);
				TextView tvLoadMore = (TextView) arg1.findViewById(R.id.tvLoadMore);
				holder.put("rootView", rootView);
				holder.put("topBigImage", topBigImage);
				holder.put("tvPartakeNum", tvPartakeNum);
				holder.put("tvActivityTime", tvActivityTime);
				holder.put("tvLoadMore", tvLoadMore);
				arg1.setTag(holder);
			}
			else
				holder = (Map<String, Object>) arg1.getTag();
			View rootView = (View) holder.get("rootView");
			ImageView topBigImage = (ImageView) holder.get("topBigImage");
			TextView tvPartakeNum = (TextView) holder.get("tvPartakeNum");
			TextView tvActivityTime = (TextView) holder.get("tvActivityTime");
			TextView tvLoadMore = (TextView) holder.get("tvLoadMore");
			tvLoadMore.setVisibility(View.GONE);
			ActivityInfo ai = list.get(arg0);
			android.widget.LinearLayout.LayoutParams lp =  (android.widget.LinearLayout.LayoutParams) topBigImage.getLayoutParams();
			lp.height  = (int) (mApplication.getnScreenWidth() *3/8);
			topBigImage.setLayoutParams(lp);
			if(arg0 == 0){
				rootView.setPadding(padding16, padding16, padding16, 0);
			}
			else{
				rootView.setPadding(padding16, padding8, padding16, 0);
			}
			if(!StringUtils.isEmpty(ai.getImg())){
				setImageView(topBigImage, ai.getImg());
			}
			String strTime = "";
			if(!StringUtils.isEmpty(ai.getStartTime())){
				strTime += ai.getStartTime();
			}
			if(!StringUtils.isEmpty(ai.getEndTime())){
				strTime += ("至" + ai.getStartTime());
			}
			tvActivityTime.setText(strTime);
			if(arg0 == list.size() -1){
				tvLoadMore.setVisibility(View.VISIBLE);
//				if(list.size() < total)
//					tvLoadMore.setText("点击加载更多");
//				else
					tvLoadMore.setText("没有更多活动了");
			}
//			tvLoadMore.setOnClickListener(ActivityCenterActivity.this);
			if(ai.getVisitCount() != null && ai.getVisitCount() > 0)
				tvPartakeNum.setText("已经有" + ai.getVisitCount() + "人参与");
			else
				tvPartakeNum.setText("");
			return arg1;
		}
		
	}

}
