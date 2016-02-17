package com.kplus.car.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.kplus.car.R;

public class PictureObtainTypeActivity extends BaseActivity implements OnClickListener{
	private Button btAlbum, btCamera, btCancel;

	@Override
	protected void initView() {
		setContentView(R.layout.daze_select_picture_layout);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(lp);
		btAlbum = (Button) findViewById(R.id.btAlbum);
		btCamera = (Button) findViewById(R.id.btCamera);
		btCancel = (Button) findViewById(R.id.btCancel);
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
		btAlbum.setOnClickListener(this);
		btCamera.setOnClickListener(this);
		btCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.btAlbum:
			intent.putExtra("type", 1);
			setResult(RESULT_OK, intent);
			break;
		case R.id.btCamera:
			intent.putExtra("type", 2);
			setResult(RESULT_OK, intent);
			break;
		case R.id.btCancel:
			setResult(RESULT_CANCELED);
			break;
		default:
			break;
		}
		finish();
	}

}
