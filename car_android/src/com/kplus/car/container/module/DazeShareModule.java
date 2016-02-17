package com.kplus.car.container.module;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.kplus.car.R;
import com.kplus.car.activity.BindPhoneActivity;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.activity.ShareInService;
import com.kplus.car.activity.ShareMessage;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.model.response.GetUserInviteContentResponse;
import com.kplus.car.model.response.request.GetUserInviteContentRequest;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXShareListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

public class DazeShareModule extends DazeModule implements DazeModuleDelegate{
	public static int contentType;
	private String to;

	@Override
	public String methodsForJS() {
		return "\"share\"";
	}
	
	public void share(DazeInvokedUrlCommand command){
		try{
			viewController.setShareCommand(command);
			Context context = viewController.getContext();
			if(viewController.getmApplication().getId() == 0){
				Intent intent = new Intent(context, PhoneRegistActivity.class);
				intent.putExtra("isShare", true);
				context.startActivity(intent);
			}
			else{
				JSONObject arguments = command.getArguments();
				to = arguments.optString("to");
				String from = arguments.optString("from");
				String title = arguments.optString("title");
				String url = arguments.optString("url");
				String image = arguments.optString("image");
				if(!StringUtils.isEmpty(title) && !StringUtils.isEmpty(url)){
					if(to != null && to.length() == 1){
						if(to.equals("2")){
							shareToWechat(title, url, image);
						}
						else if(to.equals(3)){
							shareToWechat(title, url, image);
						}
					}
					else{
						Intent intent = new Intent(viewController.getContext(), ShareMessage.class);
						intent.putExtra("shareType", to);
						intent.putExtra("title", title);
						intent.putExtra("url", url);
						intent.putExtra("image", image);
						viewController.getContext().startActivity(intent);
					}
				}
				else if(!StringUtils.isEmpty(from)){
					contentType = Integer.parseInt(from);
					if(contentType != 0){
						new GetInviteContentTask().execute();
					}
				}
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class GetInviteContentTask extends AsyncTask<Void, Void, GetUserInviteContentResponse>{
		private String errorText = "网络中断，请稍后重试";
		@Override
		protected void onPreExecute() {
			viewController.getLoadingView().setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		@Override
		protected GetUserInviteContentResponse doInBackground(Void... params) {
			try{
				GetUserInviteContentRequest request = new GetUserInviteContentRequest();
				request.setParams(viewController.getmApplication().getId(), contentType);
				return viewController.getmApplication().client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				errorText = e.toString();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(GetUserInviteContentResponse result) {
			try{
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						Intent intent = new Intent(viewController.getContext(), ShareInService.class);
//						intent.putExtra("shareType", "1");
						intent.putExtra("shareType", to);
						intent.putExtra("title", result.getData().getTitle());
						intent.putExtra("summary", result.getData().getSummary());
						intent.putExtra("content", result.getData().getContent());
						intent.putExtra("inviteUrl", result.getData().getInviteUrl());
						intent.putExtra("imgUrl", result.getData().getImgUrl());
						viewController.getContext().startActivity(intent);
					}
					else{
						Toast.makeText(viewController.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(viewController.getContext(), errorText, Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(viewController.getContext(), "获取分享内容失败", Toast.LENGTH_SHORT).show();
			}
			viewController.getLoadingView().setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
	}

	private boolean isAppInstalled(String uri) {
		PackageManager pm = viewController.getContext().getPackageManager();
		boolean installed =false;
		try {
			pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
			installed =true;
		} catch(PackageManager.NameNotFoundException e) {
			installed =false;
		}
		return installed;
	}

	private void shareToWechat(final String title, final String url, final String image){
		if(isAppInstalled("com.tencent.mm")){
			new Thread(new Runnable() {
				@Override
				public void run() {
					viewController.getmApplication().setWxShareListener((WXShareListener)viewController.getContext());
					WXMediaMessage msg;
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = url;
					msg = new WXMediaMessage(webpage);
					msg.title = title;
					msg.description = "";
					if(viewController.shareBitmap != null && !viewController.shareBitmap.isRecycled())
						viewController.shareBitmap.recycle();
					if(!StringUtils.isEmpty(image)){
						viewController.shareBitmap = BMapUtil.getBitmapFromUrl(viewController.getContext(), image);
						msg.setThumbImage(viewController.shareBitmap);
					}
					else
					{
						viewController.shareBitmap = BitmapFactory.decodeResource(viewController.getContext().getResources(), R.drawable.daze_icon);
						msg.setThumbImage(viewController.shareBitmap);
					}
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = to.equals("3") ? SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
					viewController.getIwxapi().sendReq(req);
				}
			}).start();
		}
		else{
			ToastUtil.TextToast(viewController.getContext(), "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
		}
	}

}
