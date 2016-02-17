package com.kplus.car;

import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.kplus.car.comm.FileCache;
import com.kplus.car.model.response.request.BaseRequest;
import com.kplus.car.util.FileItem;

public class Client {

	private KplusClient kplusClient;

	private FileCache cache;
	private ConnectivityManager cm;
	private TelephonyManager tm;
	private Context context;

	public Client(KplusApplication context, FileCache fileCache) {
		kplusClient = new KplusClient();
		cache = fileCache;
		this.context = context;
	}

	public <T extends Response> T execute(BaseRequest<T> request)
			throws Exception {
		if(!isNetWorkConnected())
			return null;
		T root;
		boolean isAllowed = cache.isAllowed(request);
		if (isAllowed && cache.isCached(request)) {
			root = (T) cache.getCache(request);
		} else {
			root = kplusClient.execute(request);
//			cache.cache(root);
		}
		return root;
	}

	public <T extends Response> T executePost(BaseRequest<T> request)
			throws Exception {
		T root;
		boolean isAllowed = cache.isAllowed(request);
		if (isAllowed && cache.isCached(request)) {
			root = (T) cache.getCache(request);
		} else {
			root = kplusClient.executePost(request);
//			cache.cache(root);
		}
		return root;
	}

	public <T extends Response> T executePostWithParams(BaseRequest<T> request,
			Map<String, FileItem> fileParams) throws Exception {
		T root;
		boolean isAllowed = cache.isAllowed(request);
		if (isAllowed && cache.isCached(request)) {
			root = (T) cache.getCache(request);
		} else {
			root = kplusClient.executePostWithParams(request, fileParams);
//			cache.cache(root);
		}
		return root;
	}
	
	public boolean isNetWorkConnected(){
		try{
			if(cm == null)
				cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(tm == null)
				tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if(ni == null || !cm.getBackgroundDataSetting())
				return false;
			int type = ni.getType();
			int subType = ni.getSubtype();
			if(type == ConnectivityManager.TYPE_WIFI)
				return ni.isConnected();
//			else if(type == ConnectivityManager.TYPE_MOBILE && subType == TelephonyManager.NETWORK_TYPE_UMTS && !tm.isNetworkRoaming())
			else if(type == ConnectivityManager.TYPE_MOBILE && !tm.isNetworkRoaming())
				return ni.isConnected();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
