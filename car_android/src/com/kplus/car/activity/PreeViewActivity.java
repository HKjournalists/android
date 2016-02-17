package com.kplus.car.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.widget.imageview.GestureImageView;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class PreeViewActivity extends BaseActivity implements OnClickListener
{
	private View leftView;
	private ImageView ivLeft;
	private View savePhoto;
	private View closePhoto;
	private ViewGroup content;
	private ImageView ivDefault;
	private GestureImageView view;
	
	private String imageUrl;
	private Handler mHandler = new Handler();

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_preview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		savePhoto = findViewById(R.id.savePhoto);
		closePhoto = findViewById(R.id.closePhoto);
		content = (ViewGroup) findViewById(R.id.content);
		ivDefault = (ImageView) findViewById(R.id.ivDefault);
	}

	@Override
	protected void loadData()
	{
		imageUrl = getIntent().getStringExtra("imageUrl");
		if(imageUrl != null)
			ivDefault.setVisibility(View.GONE);
		if (imageUrl != null) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			view = new GestureImageView(this);
			KplusApplication.getInstance().imageLoader.displayImage(imageUrl, view);
			view.setLayoutParams(params);
			content.addView(view);
		}
	}

	@Override
	protected void setListener()
	{
		leftView.setOnClickListener(this);
		savePhoto.setOnClickListener(this);
		closePhoto.setOnClickListener(this);		
	}

	@Override
	public void onClick(View v)
	{
		if(v.equals(leftView)){
			finish();
		}
		else if(v.equals(savePhoto)){
//			if(filename != null){
				showloading(true);
				new Thread(
						new Runnable()
						{
							@Override
							public void run()
							{
								downloadImage(imageUrl);
								mHandler.post(new Runnable()
								{
									@Override
									public void run()
									{
										showloading(false);
										Toast.makeText(PreeViewActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
									}
								});
							}
						}).start();
//			}
//			else{
//				Toast.makeText(PreeViewActivity.this, "图片已存在", Toast.LENGTH_SHORT).show();
//			}
		}
		else if(v.equals(closePhoto)){
			finish();
		}
	}
	
	private void downloadImage(String url) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(FileUtil.getParentDirectory(), new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg"));
			InputStream is = new URL(url).openStream();
			BufferedInputStream bin = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int data = -1;
			while ((data = bin.read(buffer)) != -1) {
				fos.write(buffer, 0, data);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
