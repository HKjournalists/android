package com.kplus.car.container.module;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;

import com.kplus.car.R;
import com.kplus.car.activity.OrderListActivity;
import com.kplus.car.activity.VehicleServiceActivity;
import com.kplus.car.activity.WebViewActivity;
import com.kplus.car.container.DazeContainerController;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class DazeNavigationModule extends DazeModule implements DazeModuleDelegate{
	
	public DazeContainerController getViewController() {
		return viewController;
	}

	public void setViewcController(DazeContainerController viewController) {
		this.viewController = viewController;
	}

	public void popTo(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "成功";
		try{
			JSONObject arguments = command.getArguments();
			int step = arguments.optInt("step");
			if(step == -99){
				((Activity)viewController.getContext()).finish();
				((Activity)viewController.getContext()).overridePendingTransition(0, R.anim.slide_out_to_right);
			}
			else if(step != 0){
				if(step < 0){
					step = Math.abs(step);
					WebBackForwardList backList = viewController.getWebView().copyBackForwardList();
					if(backList != null && backList.getSize() > Math.abs(step)){
						WebHistoryItem currentItem = backList.getCurrentItem();
						int currentIndex = backList.getCurrentIndex();
						WebHistoryItem lastItem = currentItem;
						int i;
						for(i=currentIndex-1;i>=0;i--){
							WebHistoryItem itemTemp = backList.getItemAtIndex(i);
							if(!itemTemp.getOriginalUrl().equals(lastItem.getOriginalUrl())){
								lastItem = itemTemp;
								step--;
								if(step == 0){
									break;
								}
							}
						}
						if(i < 0)
							i = 0;
						if(viewController.getWebView().canGoBackOrForward(i-currentIndex)) {
							viewController.getWebView().goBackOrForward(i - currentIndex);
							viewController.getTvLeftButton().setTag(null);
							viewController.getRightButton().setTag(null);
							viewController.getTvLeftButton().setText("");
							viewController.getTvLeftButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_btn_back, 0);
							viewController.getTvLeftButton().setVisibility(View.VISIBLE);
							viewController.getRightButton().setText("");
							viewController.getRightButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_refresh_background, 0);
						}
					}
					else{
						((Activity)viewController.getContext()).finish();
						((Activity)viewController.getContext()).overridePendingTransition(0, R.anim.slide_out_to_right);
					}
				}
				else{
					if(viewController.getWebView().canGoBackOrForward(step)) {
						viewController.getWebView().goBackOrForward(step);
						viewController.getTvLeftButton().setTag(null);
						viewController.getRightButton().setTag(null);
						viewController.getTvLeftButton().setText("");
						viewController.getTvLeftButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_btn_back, 0);
						viewController.getTvLeftButton().setVisibility(View.VISIBLE);
						viewController.getRightButton().setText("");
						viewController.getRightButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_refresh_background, 0);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			status = "失败";
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
	
	public void setTitle(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			String title = arguments.optString("text");
			String type = arguments.optString("type");
			if(!StringUtils.isEmpty(type)){
				if(type.equals("title")){
					viewController.getTvTitle().setText(title);
					if(title.equals("支付完成"))
						viewController.getTvLeftButton().setVisibility(View.GONE);
				}
				else if(type.equals("subtitle")){
					viewController.getTvSubtitle().setText(title);
				}
				status = "成功";
			}
		}
		catch(Exception e){
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
	
	public void toggleTitleBar (DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			boolean visible = arguments.optBoolean("visible", false);
			if(visible){
				viewController.getTitleView().setVisibility(View.VISIBLE);
			}
			else{
				viewController.getTitleView().setVisibility(View.GONE);
			}
			status = "成功";
		}
		catch(Exception e){
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
	
	public void pushWindow(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			String appId = arguments.optString("appId");
			String url = arguments.optString("url");
			if(!StringUtils.isEmpty(appId)){
				Intent intent = new Intent(viewController.getContext(), VehicleServiceActivity.class);
				intent.putExtra("appId", appId);
				if(!StringUtils.isEmpty(url))
				intent.putExtra("startPage", url);
				viewController.getContext().startActivity(intent);
				Activity parent = ((Activity)viewController.getContext()).getParent();
				if(parent != null && parent instanceof TabActivity)
					parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
				else
					((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
					status = "成功";
			}
			else{
				if(!StringUtils.isEmpty(url)){
					if((url.indexOf("http://") != -1 || url.indexOf("https://") != -1) && !url.equals(viewController.startPage)){
						if(viewController.isUrlWhite(url)){
								Intent intent = new Intent(viewController.getContext(), VehicleServiceActivity.class);
								intent.putExtra("startPage", url);
								intent.putExtra("appId", "null");
								viewController.getContext().startActivity(intent);
								Activity parent = ((Activity)viewController.getContext()).getParent();
								if(parent != null && parent instanceof TabActivity)
									parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
								else
									((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
						}
						else if(url.contains("target=top")){
							Uri uri = Uri.parse(url);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							viewController.getContext().startActivity(intent);
						}
						else{
							Intent intent = new Intent(viewController.getContext(), WebViewActivity.class);
							intent.putExtra("url", url);
							viewController.getContext().startActivity(intent);
							Activity parent = ((Activity)viewController.getContext()).getParent();
							if(parent != null && parent instanceof TabActivity)
								parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
							else
								((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
						}
					}
					else
					{
						if(url.indexOf("://") != -1) {
							viewController.getWebView().loadUrl(url);
							viewController.getTvLeftButton().setTag(null);
							viewController.getRightButton().setTag(null);
							viewController.getTvLeftButton().setText("");
							viewController.getTvLeftButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_btn_back, 0);
							viewController.getTvLeftButton().setVisibility(View.VISIBLE);
							viewController.getRightButton().setText("");
							viewController.getRightButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_refresh_background, 0);
						}
						else{
							String appIdTemp = viewController.getAppId();
							if(appIdTemp != null && (appIdTemp.equals("10000012") || appIdTemp.equals("10000001")) && viewController.isRoot){
								Intent intent = new Intent(viewController.getContext(), VehicleServiceActivity.class);
								intent.putExtra("appId", appIdTemp);
								intent.putExtra("startPage", url);
								viewController.getContext().startActivity(intent);
								Activity parent = ((Activity)viewController.getContext()).getParent();
								if(parent != null && parent instanceof TabActivity)
									parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
								else
									((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
									status = "成功";
							}
							else{
								viewController.getWebView().loadUrl(viewController.mainUrl.substring(0, viewController.mainUrl.lastIndexOf("/")+1)+url);
								viewController.getTvLeftButton().setTag(null);
								viewController.getRightButton().setTag(null);
								viewController.getTvLeftButton().setText("");
								viewController.getTvLeftButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_btn_back, 0);
								viewController.getTvLeftButton().setVisibility(View.VISIBLE);
								viewController.getRightButton().setText("");
								viewController.getRightButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_refresh_background, 0);
							}
						}
					}
					status = "成功";
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			status = "失败";
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

	public void replaceWindow(DazeInvokedUrlCommand command){
		JSONObject result = new JSONObject();
		String status = "失败";
		try{
			JSONObject arguments = command.getArguments();
			String appId = arguments.optString("appId");
			String url = arguments.optString("url");
			if(!StringUtils.isEmpty(appId)){
				Intent intent = new Intent(viewController.getContext(), VehicleServiceActivity.class);
				intent.putExtra("appId", appId);
				if(!StringUtils.isEmpty(url))
					intent.putExtra("startPage", url);
				viewController.getContext().startActivity(intent);
				Activity parent = ((Activity)viewController.getContext()).getParent();
				if(parent != null && parent instanceof TabActivity)
					parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
				else
					((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
				status = "成功";
			}
			else{
				if(!StringUtils.isEmpty(url)){
					if((url.indexOf("http://") != -1 || url.indexOf("https://") != -1) && !url.equals(viewController.startPage)){
						if(viewController.isUrlWhite(url)){
							Intent intent = new Intent(viewController.getContext(), VehicleServiceActivity.class);
							intent.putExtra("startPage", url);
							intent.putExtra("appId", "null");
							viewController.getContext().startActivity(intent);
							Activity parent = ((Activity)viewController.getContext()).getParent();
							if(parent != null && parent instanceof TabActivity)
								parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
							else
								((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
						}
						else if(url.contains("target=top")){
							Uri uri = Uri.parse(url);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							viewController.getContext().startActivity(intent);
						}
						else{
							Intent intent = new Intent(viewController.getContext(), WebViewActivity.class);
							intent.putExtra("url", url);
							viewController.getContext().startActivity(intent);
							Activity parent = ((Activity)viewController.getContext()).getParent();
							if(parent != null && parent instanceof TabActivity)
								parent.overridePendingTransition(R.anim.slide_in_from_right, 0);
							else
								((Activity)viewController.getContext()).overridePendingTransition(R.anim.slide_in_from_right, 0);
						}
					}
					status = "成功";
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			status = "失败";
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
			((Activity)viewController.getContext()).finish();
		}
	}
	
	public void showOptionMenu(DazeInvokedUrlCommand command){
		viewController.addMenu(command);
	}
	
	public void gotoOrderList(DazeInvokedUrlCommand command){
		viewController.getContext().startActivity(new Intent(viewController.getContext(), OrderListActivity.class));
		((Activity)viewController.getContext()).finish();
	}

	@Override
	public String methodsForJS() {
		return "[\"gotoOrderList\",\"replaceWindow\"]";
	}
}
