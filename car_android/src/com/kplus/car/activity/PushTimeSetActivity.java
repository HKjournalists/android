package com.kplus.car.activity;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.TimeSetAdapter;
import com.kplus.car.widget.antistatic.spinnerwheel.AbstractWheel;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelChangedListener;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PushTimeSetActivity extends BaseActivity implements OnClickListener {
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View rightView;
	private TextView tvRight;
	private TextView tvFrom, tvTo;
	private AbstractWheel hourView, minuteView;
	private int flag = 0;//0 from 1 to
	private TimeSetAdapter hourAdapter, minuteAdapter;
	private String strFrom, strTo;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_puh_time_set);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("设置推送时间");
		rightView = findViewById(R.id.rightView);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("完成");
		tvFrom = (TextView) findViewById(R.id.tvFrom);
		tvTo = (TextView) findViewById(R.id.tvTo);
		hourView = (AbstractWheel) findViewById(R.id.hourView);
		minuteView = (AbstractWheel) findViewById(R.id.minuteView);
	}

	@Override
	protected void loadData() {
		hourAdapter = new TimeSetAdapter(this, 0, 23, "%02d");
		hourView.setViewAdapter(hourAdapter);
		hourView.setCyclic(true);
		hourView.setVisibleItems(5);
		minuteAdapter = new TimeSetAdapter(this, 0, 59, "%02d");
		minuteView.setViewAdapter(minuteAdapter);
		minuteView.setCyclic(true);
		minuteView.setVisibleItems(5);
		strFrom = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_START_TIME);
		if(StringUtils.isEmpty(strFrom))
			strFrom = "08:00";
		strTo = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_END_TIME);
		if(StringUtils.isEmpty(strTo))
			strTo = "23:00";
		tvFrom.setText("从" + strFrom);
		tvTo.setText("至" + strTo);
		hourView.postDelayed(new Runnable() {			
			@Override
			public void run() {
				if(flag == 0)
					updateHourAndMinute(strFrom);
				else if(flag == 1)
					updateHourAndMinute(strTo);
			}
		}, 100);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		tvFrom.setOnClickListener(this);
		tvTo.setOnClickListener(this);
		hourView.addChangingListener(new OnWheelChangedListener() {			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if(flag == 0){
					strFrom = ((TimeSetAdapter)hourView.getViewAdapter()).getItemText(newValue).toString()
								+ strFrom.substring(strFrom.indexOf(":"));
					tvFrom.setText("从" + strFrom);
				}
				else if(flag == 1){
					strTo = ((TimeSetAdapter)hourView.getViewAdapter()).getItemText(newValue).toString()
							+ strTo.substring(strFrom.indexOf(":"));
					tvTo.setText("至" + strTo);
				}
			}
		});
		minuteView.addChangingListener(new OnWheelChangedListener() {			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if(flag == 0){
					strFrom = strFrom.substring(0, strFrom.indexOf(":") + 1)
							    + ((TimeSetAdapter)minuteView.getViewAdapter()).getItemText(newValue).toString();
					tvFrom.setText("从" + strFrom);
				}
				else if(flag == 1){
					strTo = strTo.substring(0, strTo.indexOf(":") + 1)
							  + ((TimeSetAdapter)minuteView.getViewAdapter()).getItemText(newValue).toString();
					tvTo.setText("至" + strTo);
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.leftView:
			finish();
			break;
		case R.id.rightView:
			EventAnalysisUtil.onEvent(this, "click_recorePush_success", "违章消息推送完成", null);
			if(strFrom.compareTo(strTo) >= 0){
				ToastUtil.TextToast(PushTimeSetActivity.this, "设置错误，请重新设置！", Toast.LENGTH_SHORT, Gravity.CENTER);
			}
			else{
				mApplication.dbCache.putValue(KplusConstants.DB_KEY_PUSH_START_TIME, strFrom);
				mApplication.dbCache.putValue(KplusConstants.DB_KEY_PUSH_END_TIME, strTo);
				finish();
			}
			break;
		case R.id.tvFrom:
			flag = 0;
			updateFromAndTo(flag);
			updateHourAndMinute(strFrom);
			break;
		case R.id.tvTo:
			flag = 1;
			updateFromAndTo(flag);
			updateHourAndMinute(strTo);
			break;
		default:
			break;
		}
	}
	
	private void updateFromAndTo(int flag){
		if(flag == 0){
			tvFrom.setBackgroundColor(Color.rgb(230, 230, 230));//#e6e6e6
			tvFrom.setTextColor(getResources().getColor(R.color.daze_orangered5));
			tvTo.setBackgroundColor(getResources().getColor(R.color.daze_white));
			tvTo.setTextColor(getResources().getColor(R.color.daze_black2));
		}
		else if(flag == 1){
			tvFrom.setBackgroundColor(getResources().getColor(R.color.daze_white));
			tvFrom.setTextColor(getResources().getColor(R.color.daze_black2));
			tvTo.setBackgroundColor(Color.rgb(230, 230, 230));//#e6e6e6
			tvTo.setTextColor(getResources().getColor(R.color.daze_orangered5));			
		}		
	}
	
	private void updateHourAndMinute(String time){
		String[] params = time.split(":");
		if(params != null && params.length == 2){
			hourView.setCurrentItem(Integer.valueOf(params[0]), true);
			minuteView.setCurrentItem(Integer.valueOf(params[1]), true);
		}
	}

}
