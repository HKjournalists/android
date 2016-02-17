package com.kplus.car.container.module;

import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.contact.YWContactManager;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.login.YWLoginState;
import com.kplus.car.KplusApplication;
import com.kplus.car.activity.MoreFunctionActivity;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.model.ProviderInfo;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class DazeFeedbackModule extends DazeModule implements DazeModuleDelegate {
	private KplusApplication mApplication;
	private Context context;
	@Override
	public String methodsForJS() {
		return "[\"chat\",\"openIM\"]";
	}
	
	public void chat(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			mApplication = viewController.getmApplication();
			context = viewController.getContext();
			if(mApplication.getId() == 0){
				viewController.getContext().startActivity(new Intent(context, PhoneRegistActivity.class));
			}
			else if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
				getUserOpenIm();
			}
			else{
				mApplication.initTaobao();
				if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
					status = "成功";
					Intent intent = mApplication.mYWIMKIT.getChattingActivityIntent(new EServiceContact("橙牛汽车管家", 156887186));
					context.startActivity(intent);
				}
				else if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining ){
					ToastUtil.TextToast(context, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				else {
					ToastUtil.TextToast(context, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
					mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(!StringUtils.isEmpty(command.getCallbackId())){
				try {
					result.put("status", status);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendResult(result, command);
			}
		}
	}
	
	private void getUserOpenIm(){
		new AsyncTask<Void, Void, GetUserOpenImResponse>(){
			@Override
			protected GetUserOpenImResponse doInBackground(Void... params) {
				GetUserOpenImRequest request = new GetUserOpenImRequest();
				request.setParams(mApplication.getId());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(GetUserOpenImResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					UserOpenIm data = result.getData();
					if(data != null && !StringUtils.isEmpty(data.getOpenUserid()) && !StringUtils.isEmpty(data.getOpenPassword())){
						mApplication.initTaobao();
						mApplication.loginTaobao(data.getOpenUserid(), data.getOpenPassword());
						mApplication.setOpenImUserId(data.getOpenUserid());
						mApplication.setOpenImPassWord(data.getOpenPassword());
					}
					else{
						ToastUtil.TextToast(context, !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(context, result != null && !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}
	
	public void openIM(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			mApplication = viewController.getmApplication();
			context = viewController.getContext();
			if(mApplication.getId() == 0){
				viewController.getContext().startActivity(new Intent(context, PhoneRegistActivity.class));
			}
			else if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
				getUserOpenIm();
			}
			else{
				mApplication.initTaobao();
				if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
					String openUserId = arguments.optString("openUserId");
					String providerName = arguments.optString("providerName");
					providerName = URLDecoder.decode(providerName, "utf-8");
					status = "成功";
                    ProviderInfo providerInfo = new ProviderInfo();
                    providerInfo.setOpenUserId(openUserId);
                    providerInfo.setName(providerName);
                    mApplication.getOpenImUsers().put(openUserId, providerInfo);
                    mApplication.dbCache.saveProviderInfo(providerInfo);
					Intent intent = mApplication.mYWIMKIT.getChattingActivityIntent(openUserId, "23163397");
					context.startActivity(intent);
				}
				else if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining ){
					ToastUtil.TextToast(context, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				else {
					ToastUtil.TextToast(context, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
					mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(!StringUtils.isEmpty(command.getCallbackId())){
				try {
					result.put("status", status);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendResult(result, command);
			}
		}
	}
}
