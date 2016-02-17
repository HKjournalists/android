package com.kplus.car.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.AppLogsTask;
import com.kplus.car.asynctask.GetMessageTask;
import com.kplus.car.model.NoticeContent;
import com.kplus.car.model.response.GetMessageResponse;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.ServicesActionUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageBoxActivity extends BaseActivity implements OnClickListener {
	private View leftView;
	private ListView lvListview;
	private LayoutInflater mInflater;
	private List<NoticeContent> list;
	private MessageAdapter adapter;
	private DisplayImageOptions optionsPhoto;
	private View mEmptyView;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_notice_listview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvLeft.setText("返回");
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("车主福利");
		lvListview = (ListView) findViewById(R.id.lvListview);
		mEmptyView = findViewById(R.id.emptyView);
		lvListview.setEmptyView(mEmptyView);
	}

	@Override
	protected void loadData() {
		mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER, "0");
		Intent i = new Intent("com.kplus.car.GexinSdkMsgReceiver.newmessage");
		i.putExtra("newMessage", 0);
		LocalBroadcastManager.getInstance(this).sendBroadcast(i);
		optionsPhoto = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		adapter = new MessageAdapter();
		lvListview.setAdapter(adapter);
		lvListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NoticeContent nc = list.get(position);
				if (!StringUtils.isEmpty(nc.getMotionType()) && !StringUtils.isEmpty(nc.getMotionValue())){
					ServicesActionUtil servicesAction = new ServicesActionUtil(MessageBoxActivity.this);
					servicesAction.onClickAction(nc.getMotionType(), nc.getMotionValue());
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("messageId", String.valueOf(nc.getId()));
					params.put("userId", String.valueOf(mApplication.getUserId()));
					new AppLogsTask(mApplication, "clickMessageBox", params).execute();
				}
			}
		});
		getMessages();
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftView:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void getMessages(){
		new GetMessageTask(mApplication){
			@Override
			protected void onPostExecute(GetMessageResponse response) {
				showloading(false);
				if(response != null){
					if(response.getCode() != null && response.getCode() == 0 && response.getData() != null){
						list = response.getData().getList();
						adapter.notifyDataSetChanged();
					}
					else
						ToastUtil.TextToast(MessageBoxActivity.this, response.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				else
					ToastUtil.TextToast(MessageBoxActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
			}
		}.execute();
	}
	
	class MessageAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(list == null)
				return 0;
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			if(list == null)
				return null;
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder;
			if(convertView == null){
				holder = new HashMap<>();
				if(mInflater == null)
					mInflater = LayoutInflater.from(MessageBoxActivity.this);
				convertView = mInflater.inflate(R.layout.daze_listview_item6, parent, false);
				ImageView ivMessageImg = (ImageView) convertView.findViewById(R.id.ivMessageImg);
				TextView tvMessageInfo = (TextView) convertView.findViewById(R.id.tvMessageInfo);
				TextView tvMessageDate = (TextView) convertView.findViewById(R.id.tvMessageDate);
				holder.put("ivMessageImg", ivMessageImg);
				holder.put("tvMessageInfo", tvMessageInfo);
				holder.put("tvMessageDate", tvMessageDate);
				convertView.setTag(holder);
			}
			else{
				holder = (Map<String, Object>) convertView.getTag();
			}
			ImageView ivMessageImg = (ImageView) holder.get("ivMessageImg");
			TextView tvMessageInfo = (TextView) holder.get("tvMessageInfo");
			TextView tvMessageDate = (TextView) holder.get("tvMessageDate");
			NoticeContent nc = list.get(position);
			if(!StringUtils.isEmpty(nc.getImgUrl())) {
				ivMessageImg.setVisibility(View.VISIBLE);
				mApplication.imageLoader.displayImage(nc.getImgUrl(), ivMessageImg, optionsPhoto);
			}
			else {
				ivMessageImg.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty(nc.getContent())) {
				tvMessageInfo.setVisibility(View.VISIBLE);
				tvMessageInfo.setText(nc.getContent());
			}
			else {
				tvMessageInfo.setVisibility(View.GONE);
			}
			if(!StringUtils.isEmpty(nc.getNoticeTime())){
				String timeTemp = DateUtil.getShowTimeOnInterval(nc.getNoticeTime(), "yyyy-MM-dd HH:mm:ss");
				if(!StringUtils.isEmpty(timeTemp))
					tvMessageDate.setText(timeTemp);
			}
			return convertView;
		}
	}
}
