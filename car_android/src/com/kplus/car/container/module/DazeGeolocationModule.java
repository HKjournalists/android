package com.kplus.car.container.module;

import org.json.JSONObject;

import android.os.Handler;

import com.kplus.car.KplusApplication;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class DazeGeolocationModule extends DazeModule implements DazeModuleDelegate{
	
	public void geolocation(final DazeInvokedUrlCommand command){
		try{
			KplusApplication mApplication = viewController.getmApplication();
			String cityName = mApplication.getCityName();
			if(!StringUtils.isEmpty(cityName)){
				JSONObject result = new JSONObject();
				result.put("city", cityName);
				String cityId = mApplication.getCityId();
				if(!StringUtils.isEmpty(cityId)){
					result.put("id", cityId);
				}
				String province = mApplication.getProvince();
				if(!StringUtils.isEmpty(province)){
					if(province.contains("省"))
						result.put("province", province.substring(0, province.indexOf("省")));
					else
						result.put("province", province);
				}
				if(mApplication.getLocation() != null){
					result.put("latitude", "" + mApplication.getLocation().getLatitude());
					result.put("longitude", "" + mApplication.getLocation().getLongitude());
				}
				sendResult(result, command);
			}
			else{
				JSONObject result = new JSONObject();
				sendResult(result, command);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getGPSCity(final DazeInvokedUrlCommand command){
		try{
			KplusApplication mApplication = viewController.getmApplication();
			String cityName = mApplication.getLocationCityName();
			if(!StringUtils.isEmpty(cityName)){
				JSONObject result = new JSONObject();
				result.put("city", cityName);
				String cityId = mApplication.getLocationCityId();
				if(!StringUtils.isEmpty(cityId)){
					result.put("id", cityId);
				}
				String province = mApplication.getLocationProvince();
				if(!StringUtils.isEmpty(province)){
					if(province.contains("省"))
						result.put("province", province.substring(0, province.indexOf("省")));
					else
						result.put("province", province);
				}
				if(mApplication.getLocation() != null){
					result.put("latitude", "" + mApplication.getLocation().getLatitude());
					result.put("longitude", "" + mApplication.getLocation().getLongitude());
				}
				sendResult(result, command);
			}
			else{
				JSONObject result = new JSONObject();
				sendResult(result, command);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getCityPrefix(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		try {
			String[] proviencePrefix = {"皖","闽","甘","粤","桂","贵","琼","冀","豫","黑",
					"鄂","湘","苏","赣","吉","辽","蒙","宁","青","鲁",
					"晋","陕","川","藏","新","云","浙"};
			String[] proviences = {"安徽","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江",
					"湖北","湖南","江苏","江西","吉林","辽宁","内蒙古","宁夏","青海","山东",
					"山西","陕西","四川","西藏","新疆","云南","浙江"};
			String prefix = null;
			String cityName = viewController.getmApplication().getCityName();
			if(!StringUtils.isEmpty(cityName)){
				if(cityName.contains("北京")){
					prefix = "京";
				}
				else if(cityName.contains("天津")){
					prefix = "津";
				}
				else if(cityName.contains("上海")){
					prefix = "沪";
				}
				else if(cityName.contains("重庆")){
					prefix = "渝";
				}
			}
			String provience = viewController.getmApplication().getProvince();
			if(!StringUtils.isEmpty(provience)){
				for(int i=0;i<proviences.length;i++){
					if(provience.contains(proviences[i])){
						prefix = proviencePrefix[i];
						break;
					}
				}
			}
			if(!StringUtils.isEmpty(prefix)){
				result.put("prefix", prefix);
			}
			sendResult(result, command, false);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String methodsForJS() {
		return "[\"geolocation\",\"getGPSCity\", \"getCityPrefix\"]";
	}
}
