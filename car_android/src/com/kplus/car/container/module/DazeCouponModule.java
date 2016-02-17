package com.kplus.car.container.module;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import com.kplus.car.KplusApplication;
import com.kplus.car.activity.BindPhoneActivity;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.container.DazeContainerController;
import com.kplus.car.container.command.DazeInvokedUrlCommand;

public class DazeCouponModule extends DazeModule implements DazeModuleDelegate {
	@Override
	public String methodsForJS() {
		return "\"showSelectWin\"";
	}

	public void showSelectWin(DazeInvokedUrlCommand command){
		viewController.showCashSelectWin(command);
	}
}
