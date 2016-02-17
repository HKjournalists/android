package com.kplus.car.activity;

import com.kplus.car.R;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditTextActivity extends BaseActivity implements OnClickListener
{
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View rightView;
	private ImageView ivRight;
	private EditText etEdit;
	
	private int flag;//0昵称1个性签名
	private String content;

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_edittext);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		rightView = findViewById(R.id.rightView);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setImageResource(R.drawable.daze_btn_ok);
		etEdit = (EditText) findViewById(R.id.etEdit);
	}

	@Override
	protected void loadData()
	{
		flag = getIntent().getIntExtra("flag", 0);
		if(flag == 0)
			tvTitle.setText("编辑昵称");
		else if(flag == 1)
			tvTitle.setText("编辑签名");
		content = getIntent().getStringExtra("content");
		if(!StringUtils.isEmpty(content))
		{
			etEdit.setText(content);
			etEdit.setSelection(content.length());
		}

	}

	@Override
	protected void setListener()
	{
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if(v.equals(leftView)){
			setResult(RESULT_CANCELED);
			finish();
		}
		else if(v.equals(rightView)){
			if(flag == 0){
				if(StringUtils.isEmpty(etEdit.getText().toString())){
					ToastUtil.TextToast(EditTextActivity.this, "昵称不能为空！", 2000, Gravity.CENTER);
					return;
				}
			}
			getIntent().putExtra("content", etEdit.getText().toString());
			setResult(RESULT_OK, getIntent());
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
