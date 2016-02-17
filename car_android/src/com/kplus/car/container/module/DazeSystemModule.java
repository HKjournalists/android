package com.kplus.car.container.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.activity.EmergencyDetailActivity;
import com.kplus.car.activity.MemberPrivilegeActivity;
import com.kplus.car.activity.RescueVehicleSelectActivity;
import com.kplus.car.activity.VehicleServiceActivity;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.container.DazeContainerController;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DazeSystemModule extends DazeModule implements DazeModuleDelegate{

	@Override
	public String methodsForJS() {
		return "[\"addObserver\",\"postObserver\",\"carWash\",\"showProvienceWindow\",\"getUserId\",\"getAndroidSDKVersion\"]";
	}

	public DazeContainerController getViewController() {
		return viewController;
	}

	public void setViewController(DazeContainerController viewController) {
		this.viewController = viewController;
	}

	public void startApp(DazeInvokedUrlCommand command){
		viewController.bindModuleToJSBridge();
	}

	public void showAlert(DazeInvokedUrlCommand command){
		viewController.showAlert(command);
	}

	public void whetherDazeJSBridgeExist(DazeInvokedUrlCommand command){
		viewController.whetherDazeJSBridgeExist(command);
	}

	public void showMsg(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			String type = arguments.optString("type");
			if(!StringUtils.isEmpty(type)){
				if(type.equals("loading")){
					String text = arguments.optString("text");
					boolean visible = arguments.optBoolean("visible", false);
					viewController.getLoadingView().setVisibility(visible ? View.VISIBLE : View.GONE);
					if(!StringUtils.isEmpty(text)){
						viewController.getTvLoading().setText(text);
					}
					status = "成功";
				}
				else if(type.equals("msg")){
					if(viewController.getLoadingView().getVisibility() == View.VISIBLE){
						viewController.getLoadingView().setVisibility(View.GONE);
						viewController.getTvLoading().setText("");
					}
					String text = arguments.optString("text");
					int delay = arguments.optInt("delay", 0);
					if(!StringUtils.isEmpty(text)){
						status = "成功";
						ToastUtil.TextToast(viewController.getContext(), text, delay == 0 ? 2000 : delay*1000, Gravity.CENTER);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				result.put("status", status);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			sendResult(result, command);
		}
	}

	public void stats(DazeInvokedUrlCommand command){
		try{
			KplusApplication mApplication = viewController.getmApplication();
			JSONObject arguments = command.getArguments();
			String event = arguments.optString("event");
//			JSONObject props = arguments.getJSONObject("props");
			EventAnalysisUtil.onEvent(viewController.getContext(), "event", "", null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void rescue(DazeInvokedUrlCommand command){
		final Context context = viewController.getContext();
		final ArrayList<VehicleAuth> vehicleAuths = new ArrayList<VehicleAuth>();
		ArrayList<VehicleAuth> vehicleAuthsTemp = (ArrayList<VehicleAuth>) viewController.getmApplication().dbCache.getVehicleAuths();
		if(vehicleAuthsTemp != null && !vehicleAuthsTemp.isEmpty()){
			for(VehicleAuth va : vehicleAuthsTemp){
				if(va.getBelong() != null && va.getBelong()){
					if(va.getStatus() != null && va.getStatus() == 2){
						vehicleAuths.add(va);
					}
				}
			}
		}
		if(vehicleAuths.isEmpty()){
			Intent intent = new Intent(context, MemberPrivilegeActivity.class);
			intent.putExtra("title", "认证送免费道路救援");
			context.startActivity(intent);
		}
		else{
			if(vehicleAuths.size() == 1){
				if(vehicleAuths.get(0).getResidueDegree() <= 0){
					viewController.vehicleNumber = vehicleAuths.get(0).getVehicleNum();
					Intent intent = new Intent(context, AlertDialogActivity.class);
					intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
					intent.putExtra("message", "您本辆车的免费救援次数已经用完，使用该功能将会产生服务费用，是否继续？");
					((Activity)context).startActivityForResult(intent, VehicleServiceActivity.REQUEST_FOR_CHOOSE_RESCUE);
				}
				else{
					Intent intent = new Intent(context, EmergencyDetailActivity.class);
					intent.putExtra("vehicleNumber", vehicleAuths.get(0).getVehicleNum());
					context.startActivity(intent);
				}
			}
			else{
				context.startActivity(new Intent(context, RescueVehicleSelectActivity.class));
				if(viewController.getAppId() != null && viewController.getAppId().equals("10000001")){
					Activity parent = ((Activity)context).getParent();
					if(parent != null)
						parent.overridePendingTransition(R.anim.slide_in_from_bottom, 0);
				}
				else{
					((Activity)context).overridePendingTransition(R.anim.slide_in_from_bottom, 0);
				}
			}
		}
	}

	public void addObserver(DazeInvokedUrlCommand command){
		viewController.registerReceiver(command);
	}

	public void postObserver(DazeInvokedUrlCommand command){
		viewController.sendBroadcast(command);
	}

    /**
     * 上门洗车回调
     * @param command
     */
	public void carWash(DazeInvokedUrlCommand command){
		if (KplusApplication.getInstance().isUserLogin(true, "上门洗车需要绑定手机号")) {
            final Context context = viewController.getContext();
			CNCarWashingLogic.startCarWashingActivity(context, false);
		}
	}

	public void showProvienceWindow(DazeInvokedUrlCommand command){
		viewController.showProvienceWindow(command);
	}

	public void getUserId(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		try {
			if (viewController.getmApplication().getUserId() != 0) {
				result.put("isSuccess ",true);
				result.put("userId", viewController.getmApplication().getUserId());
			}
			else{
				result.put("isSuccess ",false);
			}
		}catch (Exception e){
			e.printStackTrace();
			try {
				result.put("isSuccess ", false);
			}catch (Exception e1){}
		}
		finally {
			sendResult(result, command, false);
		}
	}

	public void getAndroidSDKVersion(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		try {
			result.put("isSuccess ",true);
			result.put("sdkVersion", Build.VERSION.SDK_INT);
		}catch (Exception e){
			e.printStackTrace();
			try {
				result.put("isSuccess ", false);
			}catch (Exception e1){}
		}
		finally {
			sendResult(result, command, false);
		}
	}

}
