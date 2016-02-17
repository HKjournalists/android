package com.kplus.car.container.module;

import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.kplus.car.container.command.DazeInvokedUrlCommand;

public class DazeNetworkModule extends DazeModule{
	
	public void getNetworkType(DazeInvokedUrlCommand command){
		try{
			JSONObject jo = new JSONObject();
			if(viewController.getmApplication().client.isNetWorkConnected()){
				jo.put("networkAvaliable", true);
				ConnectivityManager cm = (ConnectivityManager) viewController.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo ni = cm.getActiveNetworkInfo();
				int type = ni.getType();			
				if(type == ConnectivityManager.TYPE_WIFI)
					jo.put("networkType", "wifi");
				else if(type == ConnectivityManager.TYPE_MOBILE){
					TelephonyManager tm = (TelephonyManager) viewController.getContext().getSystemService(Context.TELEPHONY_SERVICE);
					String strType = "mobile";
					switch(tm.getNetworkType()){
					case TelephonyManager.NETWORK_TYPE_UNKNOWN:
						strType = "网络类型未知";
						break;
					case TelephonyManager.NETWORK_TYPE_GPRS:
						strType = "GPRS网络";
						break;
					case TelephonyManager.NETWORK_TYPE_EDGE:
						strType = "EDGE网络";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
						strType = "UMTS网络";
						break;
					case TelephonyManager.NETWORK_TYPE_HSDPA:
						strType = "HSDPA网络";
						break;
					case TelephonyManager.NETWORK_TYPE_HSUPA:
						strType = "HSUPA网络";
						break;
					case TelephonyManager.NETWORK_TYPE_HSPA:
						strType = "HSPA网络";
						break;
					case TelephonyManager.NETWORK_TYPE_CDMA:
						strType = "CDMA网络,IS95A 或 IS95B";
						break;
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
						strType = "EVDO网络, revision 0";
						break;
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
						strType = "EVDO网络, revision A";
						break;
					case TelephonyManager.NETWORK_TYPE_1xRTT:
						strType = "1xRTT网络";
						break;
					}
					jo.put("networkType", strType);
				}
			}
			else{
				jo.put("networkAvaliable", false);
				jo.put("networkType", "fail");
			}
			sendResult(jo, command);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
