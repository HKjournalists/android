package com.kplus.car.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.sgwhp.patchdroid.PatchClient;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.model.response.AppUpgradeResponse;
import com.kplus.car.model.response.CheckVersionResponse;
import com.kplus.car.model.response.request.AppUpdateRequest;
import com.kplus.car.model.response.request.CheckVersionRequest;

public class UpdateManager {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	private static final int PATCH_FINISH = 3;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	private String versionName;
	private String downloadUrl;
	private int updateType;

	private Activity mContext;
	/* 更新进度条 */
	private LayoutInflater inflater;
	private View view;
	private ProgressBar mProgress;
	private TextView tvProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				tvProgress.setText("" + progress + "%");
				break;
			case DOWNLOAD_FINISH:
				if(updateType == 1)
					installApk("wzgj" + versionName + ".apk");
				else if(updateType == 2){
					patch();
				}
				break;
			case PATCH_FINISH:
				if ((mDownloadDialog != null) && (mDownloadDialog.isShowing()))
		            mDownloadDialog.dismiss();
				if (msg.getData().getBoolean("isSuccess"))
					installApk("temp.apk");
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Activity context) {
		this.mContext = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		new AsyncTask<Object, Object, AppUpgradeResponse>()
		{
			@Override
			protected AppUpgradeResponse doInBackground(Object... params)
			{
				try{
					KplusApplication mApplication = (KplusApplication)mContext.getApplication();
					AppUpdateRequest request = new AppUpdateRequest();
					request.setParams(mApplication.getUserId());
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(AppUpgradeResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						if (result.getData().getHasNew()) {
							versionName = result.getData().getVersionName();
							downloadUrl = result.getData().getDownloadUrl();
							updateType = result.getData().getAppFileType();
							showNoticeDialog();
						} 
					}
				}
			}
		}.execute();
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoNewDialog() {
		Toast.makeText(mContext, "当前已是最新版本！", Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示软件最新版对话框
	 */
	private void showNoticeDialog() {
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("检查更新");
		builder.setMessage("有新版本 " + (StringUtils.isEmpty(versionName) ? "" : versionName) + ",是否下载更新？");
		// 更新
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});

		// 稍后更新
		builder.setNegativeButton("稍后更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		if(mDownloadDialog == null){
			// 构造软件下载对话框
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle("新版本下载中，请稍候...");
			// 给下载对话框增加进度条
			if(view == null){
				view = inflater.inflate(R.layout.daze_app_update, null);
				mProgress = (ProgressBar) view.findViewById(R.id.pbUpload);
				tvProgress = (TextView) view.findViewById(R.id.tvProgress);
			}
			builder.setView(view);
			// 取消更新
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// 设置取消状态
					cancelUpdate = true;
				}
			});
			mDownloadDialog = builder.create();
		}
		mDownloadDialog.show();
		// 下载文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(downloadUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = null;
					if(updateType == 1)
						apkFile = new File(mSavePath, "wzgj" + versionName + ".apk");
					else if(updateType == 2)
						apkFile = new File(mSavePath, "wzgjpatch" + versionName + ".patch");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk(String path) {
		File apkfile = new File(mSavePath, path);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
	
	private void patch(){
		if ((mDownloadDialog != null) && (!mDownloadDialog.isShowing()))
	    {
	      mDownloadDialog.show();
	      tvProgress.setText("正在准备安装，请稍候...");
	      mProgress.setVisibility(View.GONE);
	    }
		new Thread(new Runnable() {			
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = PATCH_FINISH;
		        Bundle data = new Bundle();
		        try{
		        	File file = new File(mSavePath, "temp.apk");
		            if (file.exists())
		            	file.delete();
		            PatchClient.loadLib();
		            PatchClient.applyPatchToOwn(mContext, mSavePath + File.separator + "temp.apk", mSavePath + File.separator + "wzgjpatch" + versionName + ".patch");
		            data.putBoolean("isSuccess", true);
		        }
		        catch(Exception e){
		        	e.printStackTrace();
		        	data.putBoolean("isSuccess", false);
		        	msg.setData(data);
		        }
		        finally
		        {
		        	msg.setData(data);
		        	mHandler.sendMessage(msg);
		        }
			}
		}).start();;
	}
}