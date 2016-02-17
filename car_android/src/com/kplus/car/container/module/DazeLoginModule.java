package com.kplus.car.container.module;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.kplus.car.KplusApplication;
import com.kplus.car.activity.BindPhoneActivity;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.container.DazeContainerController;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.util.ToastUtil;

public class DazeLoginModule extends DazeModule implements DazeModuleDelegate{

	@Override
	public String methodsForJS() {
		return "\"login\"";
	}

	public void login(DazeInvokedUrlCommand command){
		KplusApplication mApplication = viewController.getmApplication();
		if(mApplication.getId() == 0){
			ToastUtil.TextToast(viewController.getContext(), "使用该功能需要绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
			Intent intent = new Intent(viewController.getContext(), PhoneRegistActivity.class);
					intent.putExtra("isMustPhone", true);
			((Activity) viewController.getContext()).startActivityForResult(intent, DazeContainerController.REQUEST_LOGIN);
		}
		else{
			try{
				JSONObject result = new JSONObject();
				result.put("isSuccess", true);
				result.put("pid", mApplication.getpId());
				result.put("uid", mApplication.getId());
				result.put("userId", mApplication.getUserId());
				sendResult(result, command);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
