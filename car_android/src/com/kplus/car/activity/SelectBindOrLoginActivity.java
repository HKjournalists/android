package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.R;
import com.kplus.car.comm.DazeUserAccount;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.AccountAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectBindOrLoginActivity extends BaseActivity implements OnClickListener {
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ListView lvListview;
	private View notToBind;
	private AlertDialog alertDialog;
	private AccountAdapter adapter;
	private List<DazeUserAccount> list;
	private String phones = null;
	private int loginType;
	private String platform;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_select_bind_or_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("账号绑定");
		lvListview = (ListView) findViewById(R.id.lvListview);
		notToBind = findViewById(R.id.notToBind);
	}

	@Override
	protected void loadData() {
		loginType = getIntent().getIntExtra("loginType", 0);
		list = mApplication.dbCache.getDazeUserAccounts();
		if(list == null || list.isEmpty()){
			finish();
			return;
		}
		List<DazeUserAccount> listTemp = new ArrayList<DazeUserAccount>();
		for(DazeUserAccount dua : list){
			switch(loginType){
			case 0:
				if(!StringUtils.isEmpty(dua.getPhoneLoginName()))
					listTemp.add(dua);
				break;
			case 1:
				if(!StringUtils.isEmpty(dua.getQqLoginName()))
					listTemp.add(dua);
				break;
			case 2:
				if(!StringUtils.isEmpty(dua.getWechatLoginName()))
					listTemp.add(dua);
				break;
			case 3:
				if(!StringUtils.isEmpty(dua.getWeiboLoginName()))
					listTemp.add(dua);
				break;
			}
			if(listTemp != null && !listTemp.isEmpty())
				list.removeAll(listTemp);
		}
		if(loginType == 1)
			platform = "QQ";
		else if(loginType == 2)
			platform = "微信";
		else if(loginType == 3)
			platform = "微博";
		if(loginType != 0){
			for(DazeUserAccount dua : list){
				if(StringUtils.isEmpty(dua.getPhoneNumber())){
					if(phones == null){
						phones = dua.getPhoneNumber();
					}
					else{
						phones += ("," + dua.getPhoneNumber());
					}
				}
			}
		}
		adapter = new AccountAdapter(this, list);
		lvListview.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		notToBind.setOnClickListener(this);
		lvListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				long uid = list.get(arg2).getUid();
				Intent intent = getIntent();
				intent.putExtra("uid", uid);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.equals(leftView)){
			setResult(RESULT_CANCELED);
			finish();
		}
		else if(arg0.equals(notToBind)){
			if(loginType != 0 && !StringUtils.isEmpty(phones)){
				if(alertDialog == null){
					AlertDialog.Builder builder = new  AlertDialog.Builder(SelectBindOrLoginActivity.this);
					builder.setTitle("提醒");
					builder.setMessage("如使用" + platform + "用户信息创建新账户，该账户将无法绑定手机号" + phones + "进行在线缴费等订单相关功能。我们推荐您直接绑定到已有账户" + phones);
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							alertDialog.dismiss();
						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							alertDialog.dismiss();
							setResult(RESULT_OK, getIntent());
							finish();
						}
					});
					alertDialog = builder.create();
				}
				if(!alertDialog.isShowing())
					alertDialog.show();
			}
			else{
				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if(alertDialog != null && alertDialog.isShowing()){
			alertDialog.dismiss();
			alertDialog = null;
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
