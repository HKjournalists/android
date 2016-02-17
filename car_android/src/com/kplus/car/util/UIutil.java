package com.kplus.car.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIutil {
	
	
	
	/****
	 * 分享
	 * 
	 * @param activity
	 * @param title
	 * @param text
	 */
	public static void share(Activity activity, String title, String text) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
		activity.startActivity(Intent.createChooser(shareIntent, title));
	}

	// public static ProgressDialog showProgressDialog(Context context){
	// ProgressDialog mpDialog = new ProgressDialog(context);
	// mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
	// mpDialog.setTitle("提示");//设置标题
	// mpDialog.setIcon(R.drawable.icon);//设置图标
	// mpDialog.setMessage("这是一个圆形进度条");
	// mpDialog.setIndeterminate(false);//设置进度条是否为不明确
	// mpDialog.setCancelable(true);//设置进度条是否可以按退回键取消
	// mpDialog.setButton("确定", new DialogInterface.OnClickListener(){
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	//
	// }
	//
	// });
	// mpDialog.show();
	// return ProgressDialog;
	// }
	/***
	 * 检查网络
	 */
	public static boolean detect(Context act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}
	
	/**   
	   * 按下这个按钮进行的颜色过滤   
	   */    
	  public final static float[] BT_SELECTED=new float[] {1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};     
	       
	  /**   
	   * 按钮恢复原状的颜色过滤   
	   */    
	  public final static float[] BT_NOT_SELECTED=new float[] {      
	      1, 0, 0, 0, 0,      
	      0, 1, 0, 0, 0,      
	      0, 0, 1, 0, 0,      
	      0, 0, 0, 1, 0 };  


	/***
	 * 显示系统输入法
	 */
	public static void showInput(Context act, View v) {
		InputMethodManager imm = (InputMethodManager) act
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

	}

	public static void hidInput(Context act,IBinder ib) {
		InputMethodManager imm = (InputMethodManager) act
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ib, InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
