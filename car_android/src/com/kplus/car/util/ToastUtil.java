package com.kplus.car.util;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.xmlpull.v1.XmlPullParserException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * 项目名称：工具包
 * 
 * 类名称：ToastUtil
 * 
 * 类描述： 弹出提示信息
 * 
 * @version
 * 
 */
public class ToastUtil {
	private static Toast toast = null;
	public static int LENGTH_LONG = Toast.LENGTH_LONG;
	public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

	/**
	 * 普通文本消息提示
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void TextToast(Context context, CharSequence text,
			int duration, int gravity) {
		// 创建一个Toast提示消息
		if (toast == null)
		{
			toast = Toast.makeText(context, text, duration);
			 //设置Toast提示消息在屏幕上的位置
			 toast.setGravity(gravity, 0, 0);
		}
		else
		{
			toast.setText(text);
			toast.setDuration(duration);
			// 设置Toast提示消息在屏幕上的位置
			 toast.setGravity(gravity, 0, 0);
		}
		// 显示消息
		toast.show();
	}

	public static void makeLongToast(Context context, String text) {
		if (toast == null)
		{
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		else
		{
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}

	public static void makeShortToast(Context context, String text) {

		if (toast == null)
		{
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		else
		{
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}

	public static void makeExceptionToast(Context context, Exception exception) {

		if (exception instanceof SocketTimeoutException)
		{
			makeShortToast(context, "请求网络超时");
		}
		else if (exception instanceof SocketException)
		{
			makeShortToast(context, "服务器没有响应");
		}
		else if (exception instanceof NetworkErrorException)
		{
			makeShortToast(context, "网络错误");
		}
		else if (exception instanceof IOException)
		{
			makeShortToast(context, "输入输出流错误");
		}
		else if (exception instanceof XmlPullParserException)
		{
			makeShortToast(context, "解析xml出错");
		}
		else
		{
			makeShortToast(context, "程序发生未知错误");
		}

	}
}
