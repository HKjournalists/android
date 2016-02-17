package com.kplus.car;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.igexin.sdk.PushConsts;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.activity.ChedaiActivity;
import com.kplus.car.activity.GuanjiaHomeActivity;
import com.kplus.car.activity.LogoActivity;
import com.kplus.car.activity.MainUIActivity;
import com.kplus.car.activity.MessageBoxActivity;
import com.kplus.car.activity.OrderActivity;
import com.kplus.car.activity.VehicleServiceActivity;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.module.activites.CNOrderDetailsActivity;
import com.kplus.car.comm.TaskInfo;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.ImageNames;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.BindPushClientResponse;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.BindPushClientRequest;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.service.NotificationService;
import com.kplus.car.service.UpdateAgainstRecords;
import com.kplus.car.util.BroadcastReceiverUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GexinSdkMsgReceiver extends BroadcastReceiver {

	private KplusApplication mApplication = KplusApplication.getInstance();
	public static int nClientIdNUmber = 0;
	private Handler mHandler = new Handler();
	private String cid;
	private Context context;
	private AlarmManager alarmManager;
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		sp = context.getSharedPreferences("kplussp", Context.MODE_PRIVATE);
		Bundle bundle = intent.getExtras();
		if(bundle !=null){
			try{
				Log.d("GexinSdkDemo", "onReceive() action=" + bundle.getInt("action"));
				switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		
				case PushConsts.GET_MSG_DATA:
					// 获取透传数据
					// String appid = bundle.getString("appid");
					byte[] payload = bundle.getByteArray("payload");
		
					if (payload != null) {
						String data = new String(payload);
						Log.d("GexinSdkDemo", "Got Payload:" + data);
						processCustomMessage(context, data);
					}
					break;
				case PushConsts.GET_CLIENTID:
					// 获取ClientID(CID)
					// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
					if(nClientIdNUmber == 0){
						cid = bundle.getString("clientid");
						if(!StringUtils.isEmpty(cid))
							gotoBind();
					}
					nClientIdNUmber++;
					break;
		
//				case PushConsts.BIND_CELL_STATUS:
//					String cell = bundle.getString("cell");
//		
//					Log.d("GexinSdkDemo", "BIND_CELL_STATUS:" + cell);
//					break;
				case PushConsts.THIRDPART_FEEDBACK:
					// sendMessage接口调用回执
					String appid = bundle.getString("appid");
					String taskid = bundle.getString("taskid");
					String actionid = bundle.getString("actionid");
					String result = bundle.getString("result");
					long timestamp = bundle.getLong("timestamp");
		
					Log.d("GexinSdkDemo", "appid:" + appid);
					Log.d("GexinSdkDemo", "taskid:" + taskid);
					Log.d("GexinSdkDemo", "actionid:" + actionid);
					Log.d("GexinSdkDemo", "result:" + result);
					Log.d("GexinSdkDemo", "timestamp:" + timestamp);
					break;
				default:
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void gotoBind(){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mApplication.getUserId() == 0) {
					gotoBind();
				} else {
					bindPushClient(context, mApplication.getUserId(), cid);
				}
			}
		}, 5 * 1000);
	}
	
	private void bindPushClient(final Context context, final long userId, final String clientId){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try{
					Log.i("GexinSdkMsgReceiver", "CID=" + clientId);
					if (TextUtils.isEmpty(KplusConstants.CLIENT_APP_KEY))
						KplusConstants.initData(context);
					BindPushClientRequest request = new BindPushClientRequest();
					request.setParams(userId, clientId, 1);
					BindPushClientResponse result = mApplication.client.execute(request);
					if(result != null && result.getCode() != null && result.getCode() == 0){
						if(result.getData() != null){
							if(result.getData().getResult() != null && result.getData().getResult().booleanValue() == true){
								Log.i("GexinSdkMsgReceiver", "bindPushClient succeed");
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void processCustomMessage(Context context, String data) {
		try {
			JSONObject extraJson = new JSONObject(data);
			if(extraJson.opt("type") != null){
				if(extraJson.opt("type") instanceof Integer){
					Integer type = (Integer) extraJson.get("type");
					if (type == 1) {
						mApplication.dbCache.putValue(KplusConstants.DB_KEY_ORDER_STATUS_CHANGE, "1");
						long orderId = extraJson.optLong("orderId");
						int orderType = extraJson.optInt("orderType");
						String msg = extraJson.optString("msg");
						showNotification(msg, orderId, orderType);
						int status = extraJson.optInt("status");
						if(status > 2){
							mApplication.dbCache.deleteOrderId(orderId);
						}
						LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(new Intent(BroadcastReceiverUtil.ACTION_PAY_SUCCESS));
					}
					else if(type == 2){
						String vehicleNum = extraJson.optString("vehicleNum");
						if(!StringUtils.isEmpty(vehicleNum)){
							UserVehicle vehicle = mApplication.dbCache.getVehicle(vehicleNum);
							if(vehicle != null && mApplication.getId() != 0){
								getVehicleAuth(vehicleNum);
							}
						}
					}
					else if(type == 3){
						String vehicleNum = extraJson.optString("vehicleNum");
						if(!StringUtils.isEmpty(vehicleNum)){	
							UserVehicle vehicle = mApplication.dbCache.getVehicle(vehicleNum);
							if(vehicle == null){
								vehicle = new UserVehicle();
								vehicle.setVehicleNum(vehicleNum);
							}
							else
								vehicle.setHiden(false);
							if(vehicleNum.length() >=2){
								if(StringUtils.isEmpty(vehicle.getCityId())){
									String temp = vehicleNum.substring(0, 2);
									temp = temp.toUpperCase();
									if(mApplication.getCities() != null){
										for (CityVehicle cityVehicle : mApplication.getCities()){
											if (temp.equals(cityVehicle.getPrefix())){
												vehicle.setCityId("" + cityVehicle.getId());
												vehicle.setCityName(cityVehicle.getName());
												break;
											}
										}
									}
								}
							}
							String frameNum = extraJson.optString("frameNum");
							if(!StringUtils.isEmpty(frameNum))
								vehicle.setFrameNum(frameNum);
							String motorNum = extraJson.optString("motorNum");
							if(!StringUtils.isEmpty(motorNum))
								vehicle.setMotorNum(motorNum);
							if(!StringUtils.isEmpty(vehicle.getFrameNum()) && !StringUtils.isEmpty(vehicle.getMotorNum())){
								saveVehicle(vehicle);
							}
						}
						JSONObject prop = new JSONObject();
						try{
							prop.put("name", extraJson.opt("name"));
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
					else if(type == 4){
						mApplication.dbCache.putValue(KplusConstants.DB_KEY_TAB_SERVICE_CHANGE, "1");
						LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.kplus.car.GexinSdkMsgReceiver.tab.service.change"));
					}
					else if(type == 5){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						final String noticeContent = extraJson.optString("notice");
						String force = extraJson.optString("force");
						if(!StringUtils.isEmpty(force) && force.equalsIgnoreCase("true")){
							Intent it = new Intent(context, MessageBoxActivity.class);
							showNotification(noticeContent, it);
							String messageNumber = mApplication.dbCache.getValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER);
							int nMessageNumber = 0;
							if(!StringUtils.isEmpty(messageNumber)){
								try{
									nMessageNumber = Integer.parseInt(messageNumber);
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
							nMessageNumber++;
							mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER, String.valueOf(nMessageNumber));
							Intent i = new Intent("com.kplus.car.GexinSdkMsgReceiver.newmessage");
							i.putExtra("newMessage", nMessageNumber);
							LocalBroadcastManager.getInstance(context).sendBroadcast(i);
						}
						else{
							String strFrom = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_START_TIME);
							if (StringUtils.isEmpty(strFrom))
								strFrom = "08:00";
							String strTo = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_END_TIME);
							if (StringUtils.isEmpty(strTo))
								strTo = "23:00";
							Date nowDate = new Date();
							String strNow = sdf.format(nowDate);
							SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
							strFrom = sdfShort.format(nowDate) + " " + strFrom;
							strTo = sdfShort.format(nowDate) + " " + strTo;
							long nowTime = nowDate.getTime();
							if(strNow.compareTo(strFrom) < 0){
								long intervalTime = sdf.parse(strFrom).getTime() - nowTime;
								if(alarmManager == null)
									alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
								Intent intent = new Intent(context, NotificationService.class);
								intent.putExtra("newMessage", true);
								intent.putExtra("noticeContent", noticeContent);
								PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
								alarmManager.set(AlarmManager.RTC_WAKEUP, nowTime + intervalTime, pi);
							}
							else if(strNow.compareTo(strTo) > 0){
								long intervalTime = 24*60*60*1000 - (nowTime -sdf.parse(strTo).getTime());
								if(alarmManager == null)
									alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
								Intent intent = new Intent(context, NotificationService.class);
								intent.putExtra("newMessage", true);
								intent.putExtra("noticeContent", noticeContent);
								PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
								alarmManager.set(AlarmManager.RTC_WAKEUP, nowTime + intervalTime, pi);
							}
							else{
								Intent it = new Intent(context, MessageBoxActivity.class);
								showNotification(noticeContent, it);
								String messageNumber = mApplication.dbCache.getValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER);
								int nMessageNumber = 0;
								if(!StringUtils.isEmpty(messageNumber)){
									try{
										nMessageNumber = Integer.parseInt(messageNumber);
									}
									catch(Exception e){
										e.printStackTrace();
									}
								}
								nMessageNumber++;
								mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER, String.valueOf(nMessageNumber));
								Intent i = new Intent("com.kplus.car.GexinSdkMsgReceiver.newmessage");
								i.putExtra("newMessage", nMessageNumber);
								LocalBroadcastManager.getInstance(context).sendBroadcast(i);
							}
						}
					}
					else if(type == 7){
						if(extraJson.opt("actionType") != null){
							if(KplusConstants.isDebug)
								System.out.println("GexinSdkMsgReceiver===>extraJson = " + extraJson.toString());
							String temp = extraJson.toString();
							try{
								temp = temp.replaceAll("\\\\", "");
								temp = temp.replaceAll("\"\\{", "{");
								temp = temp.replaceAll("\"\\[\\{", "[{");
								temp = temp.replaceAll("\\}\"", "}");
								temp = temp.replaceAll("\\}\\]\"", "}]");
								extraJson = new JSONObject(temp);
							}
							catch(Exception e){
								e.printStackTrace();
							}
							String actionType = extraJson.optString("actionType");
							if(!StringUtils.isEmpty(actionType)){
								String strVehicleNumber = extraJson.optString("vehicleNum");
								if(!StringUtils.isEmpty(strVehicleNumber) && mApplication.containsTask(strVehicleNumber)){
									TaskInfo ti = mApplication.getTask(strVehicleNumber);
									if(KplusConstants.isDebug)
										System.out.println("GexinSdkMsgReceiver===>containsTask");
									UserVehicle uvTemp = mApplication.dbCache.getVehicle(strVehicleNumber);
									if(uvTemp != null){
										if(actionType.contains("1") || actionType.contains("2") || actionType.contains("3") || actionType.contains("5") || actionType.contains("6") || actionType.contains("7")){
											if(KplusConstants.isDebug)
												System.out.println("GexinSdkMsgReceiver===>removeTask");
											mApplication.removeTask(strVehicleNumber);
											uvTemp.setReturnTime(System.currentTimeMillis());
											uvTemp.setHasParamError(false);
											uvTemp.setHasRuleError(false);
//											uvTemp.setCityUnValid(null);
											uvTemp.setHasSearchFail(false);
											Intent intentComplete = new Intent(context, AlertDialogActivity.class);
											if(actionType.contains("1") || actionType.contains("2"))
											{
												EventAnalysisUtil.onEvent(context, "click_wz_success", "查询违章成功", null);
												JSONObject dataJSON = extraJson.optJSONObject("data");
												if(dataJSON != null){
//													intentComplete.putExtra("add", true);
													uvTemp.setNewRecordNumber(dataJSON.optInt("total"));
													parserRecords(dataJSON, uvTemp, "" + extraJson.optLong("postDateTime"));
												}
												else{
													intentComplete.putExtra("add", true);
												}
//												else{
//													String dataRecords = extraJson.optString("data");
//													if(data != null){
//														try{
//															parserRecords(new JSONObject(dataRecords));
//														}
//														catch(Exception e){
//															e.printStackTrace();
//														}
//													}														
//												}												
											}
											if(actionType.contains("5")){
												JSONObject dataRecords = extraJson.optJSONObject("data");
												uvTemp.setHasRuleError(true);
												parserRules(dataRecords);
											}
											if(actionType.contains("6")){
												uvTemp.setHasParamError(true);
											}
											if(actionType.contains("7")){
												uvTemp.setHasSearchFail(true);
												Intent intent = new Intent("com.kplus.car.getagainstRecords");
												intent.putExtra("vehicleNumber", strVehicleNumber);
												intent.putExtra("fail", true);
												LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
											}
											mApplication.dbCache.updateVehicle(uvTemp);
											Intent intent = new Intent("com.kplus.car.getagainstRecords");
											intent.putExtra("actionType", actionType);
											intent.putExtra("vehicleNumber", strVehicleNumber);
											LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);												
											intentComplete.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
											intentComplete.putExtra("vehicleNumber", strVehicleNumber);
											intentComplete.putExtra("title", "您的违章查询任务已完成");
											intentComplete.putExtra("message", "小牛很高兴地告诉您，你提交的[查询车辆" + strVehicleNumber + "违章]任务已经执行完毕");
											intentComplete.putExtra("leftButtonText", "关闭");
											intentComplete.putExtra("rightButtonText", "查看结果");
											intentComplete.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT);
											intentComplete.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											context.startActivity(intentComplete);
										}
										else if((actionType.contains("4") && ti != null && !ti.isHasEnterVerifyCode()) || actionType.contains("8")){
											if(actionType.contains("4")){
												JSONObject dataJSON = extraJson.optJSONObject("data");
												if(dataJSON != null){
													String sessionId = dataJSON.optString("sessionId");
													if(!StringUtils.isEmpty(sessionId)){
														ti.setSessionId(sessionId);
														Intent intent = new Intent(context, AlertDialogActivity.class);
														intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VERIFY_CODE);
														intent.putExtra("vehicleNumber", strVehicleNumber);
														intent.putExtra("sessionId", sessionId);
														if(!isAppRunning()){
															intent.setAction(Intent.ACTION_MAIN);
															intent.addCategory(Intent.CATEGORY_LAUNCHER);				
														}
														intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
														context.startActivity(intent);
													}
												}
											}
											else if(actionType.contains("8")){
												int verifyCodeErrorCount = ti.getVerifyCodeErrorCount();
												verifyCodeErrorCount++;
												if(verifyCodeErrorCount >= 2){
													mApplication.removeTask(strVehicleNumber);
													uvTemp.setHasSearchFail(true);
													mApplication.dbCache.updateVehicle(uvTemp);								
													Intent intent = new Intent("com.kplus.car.getagainstRecords");
													intent.putExtra("vehicleNumber", strVehicleNumber);
													intent.putExtra("fail", true);
													intent.putExtra("verifyCodeError", true);
													LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
													Intent intentComplete = new Intent(context, AlertDialogActivity.class);
													intentComplete.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
													intentComplete.putExtra("vehicleNumber", strVehicleNumber);
													intentComplete.putExtra("title", "您的违章查询任务已完成");
													intentComplete.putExtra("message", "小牛很高兴地告诉您，你提交的[查询车辆" + strVehicleNumber + "违章]任务已经执行完毕");
													intentComplete.putExtra("leftButtonText", "关闭");
													intentComplete.putExtra("rightButtonText", "查看结果");
													intentComplete.putExtra("verifyCodeError", true);
													intentComplete.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT);
													intentComplete.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
													context.startActivity(intentComplete);
												}
												else{
													if(ti != null){
														ti.setVerifyCodeErrorCount(verifyCodeErrorCount);
														String sessionId = ti.getSessionId();
														if(!StringUtils.isEmpty(sessionId)){
															Intent intent = new Intent(context, AlertDialogActivity.class);
															intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VERIFY_CODE);
															intent.putExtra("vehicleNumber", strVehicleNumber);
															intent.putExtra("sessionId", sessionId);
															if(!isAppRunning()){
																intent.setAction(Intent.ACTION_MAIN);
																intent.addCategory(Intent.CATEGORY_LAUNCHER);				
															}
															intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
															context.startActivity(intent);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					else if (type == 9){
						Intent it = new Intent("com.kplus.car.guanjia.refresh");
						LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(it);
						String noticeContent = extraJson.optString("msg");
						if (!StringUtils.isEmpty(noticeContent)) {
							Intent intent = new Intent(context, MainUIActivity.class);
							intent.putExtra("currentTab", 3);
							intent.putExtra("providerId", extraJson.optString("providerId"));
							intent.putExtra("serviceType", extraJson.optString("serviceType"));
							intent.putExtra("cityId", extraJson.optString("cityId"));
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
							showCustomNotification(noticeContent, intent);
						}
					}
					else if (type == 10){
						String noticeContent = extraJson.optString("msg");
						JSONObject dataJSON = extraJson.optJSONObject("data");
						String motionType = null;
						String motionValue = null;
						int messageId = 0;
						if (dataJSON != null){
							motionType = dataJSON.optString("motionType");
							motionValue = dataJSON.optString("motionValue");
							messageId = dataJSON.optInt("messageId");
						}
						Intent intent = new Intent(context, MainUIActivity.class);
						intent.putExtra("pushType", 10);
						intent.putExtra("motionType", motionType);
						intent.putExtra("motionValue", motionValue);
						if (messageId != 0)
							intent.putExtra("messageId", String.valueOf(messageId));
						showCustomNotification(noticeContent, intent);
					}
				}
			}
			else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				final String noticeContent = extraJson.optString("notice");
				String force = extraJson.optString("force");
				if(!StringUtils.isEmpty(force) && force.equalsIgnoreCase("true")){
					showNotification(noticeContent);
				}
				else{
					String strFrom = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_START_TIME);
					if (StringUtils.isEmpty(strFrom))
						strFrom = "08:00";
					String strTo = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_END_TIME);
					if (StringUtils.isEmpty(strTo))
						strTo = "23:00";
					Date nowDate = new Date();
					String strNow = sdf.format(nowDate);
					SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
					strFrom = sdfShort.format(nowDate) + " " + strFrom;
					strTo = sdfShort.format(nowDate) + " " + strTo;
					long nowTime = nowDate.getTime();
					if(strNow.compareTo(strFrom) < 0){
						long intervalTime = sdf.parse(strFrom).getTime() - nowTime;
						if(alarmManager == null)
							alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
						Intent intent = new Intent(context, NotificationService.class);
						intent.putExtra("noticeContent", noticeContent);
						PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
						alarmManager.set(AlarmManager.RTC_WAKEUP, nowTime + intervalTime, pi);
					}
					else if(strNow.compareTo(strTo) > 0){
						long intervalTime = 24*60*60*1000 - (nowTime -sdf.parse(strTo).getTime());
						if(alarmManager == null)
							alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
						Intent intent = new Intent(context, NotificationService.class);
						intent.putExtra("noticeContent", noticeContent);
						PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
						alarmManager.set(AlarmManager.RTC_WAKEUP, nowTime + intervalTime, pi);	
					}
					else
					{
						showNotification(noticeContent);					
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	private boolean isAppOnForeground() {
		Log.d("processName", mApplication.packageName);
		ComponentName componentName = mApplication.activityManager
				.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = componentName.getPackageName();
		if (currentPackageName.equals(mApplication.packageName)) {
			return true;
		}
		return false;
	}

	private void showCustomNotification(String content, Intent it){
		if(StringUtils.isEmpty(content))
			return;
		try {
			int id = (int) System.currentTimeMillis();
			PendingIntent pi = PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_UPDATE_CURRENT);
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification_view);
			rv.setImageViewResource(R.id.icon, R.drawable.daze_icon);
			rv.setTextViewText(R.id.title, "橙牛汽车管家");
			rv.setTextViewText(R.id.content, content);
			NotificationCompat.Builder n = new NotificationCompat.Builder(context);
			n.setSmallIcon(R.drawable.daze_icon).setTicker(content).setContent(rv).
					setDefaults(NotificationCompat.DEFAULT_ALL).setAutoCancel(true).setContentIntent(pi);
			mApplication.notificationManager.notify(id, n.build());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private void showNotification(String noticeContent) {
		if(StringUtils.isEmpty(noticeContent))
			return;
		// 定义Notification的各种属性
		Notification notification = new Notification();
		notification.icon = R.drawable.daze_icon;
		notification.tickerText = noticeContent;
		notification.when = System.currentTimeMillis();
		// 点击后自动清除Notification
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// 设置通知的事件消息
		CharSequence contentTitle = "橙牛汽车管家"; // 通知栏标题
		if (!isAppOnForeground()) {
			Intent notificationIntent = new Intent(mApplication, MainUIActivity.class);
			notificationIntent.setAction(Intent.ACTION_MAIN);
			notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			PendingIntent contentIntent = PendingIntent.getActivity(
					mApplication, 0, notificationIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(mApplication, contentTitle,
					noticeContent, contentIntent);
		} else {
			Intent notificationIntent = new Intent();
			PendingIntent contentIntent = PendingIntent.getActivity(mApplication, 0, notificationIntent,0);
			notification.setLatestEventInfo(mApplication, contentTitle,
					noticeContent, contentIntent);
		}
		// 把Notification传递给NotificationManager
		int notifyId = sp.getInt("notifyId", 1);
		mApplication.notificationManager.notify(notifyId, notification);
		sp.edit().putInt("notifyId", ++notifyId).commit();
	}

	private void showNotification(String noticeContent, Intent intent) {
		if(StringUtils.isEmpty(noticeContent))
			return;
		// 定义Notification的各种属性
		Notification notification = new Notification();
		notification.icon = R.drawable.daze_icon;
		notification.tickerText = noticeContent;
		notification.when = System.currentTimeMillis();
		// 点击后自动清除Notification
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// 设置通知的事件消息
		CharSequence contentTitle = "橙牛汽车管家"; // 通知栏标题
		PendingIntent contentIntent = PendingIntent.getActivity(
				mApplication, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(mApplication, contentTitle,
				noticeContent, contentIntent);
		// 把Notification传递给NotificationManager
		int notifyId = sp.getInt("notifyId", 1);
		mApplication.notificationManager.notify(notifyId, notification);
		sp.edit().putInt("notifyId", ++notifyId).commit();
	}

	private void showNotification(String noticeContent, long orderId, int orderType) {
		if(StringUtils.isEmpty(noticeContent))
			return;
		// 定义Notification的各种属性
		Notification notification = new Notification();
		notification.icon = R.drawable.daze_icon;
		notification.tickerText = noticeContent;
		notification.when = System.currentTimeMillis();
		// 点击后自动清除Notification
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// 设置通知的事件消息
		CharSequence contentTitle = "橙牛汽车管家"; // 通知栏标题
		if (!isAppRunning()) {
			Intent notificationIntent = new Intent(mApplication, LogoActivity.class);
			notificationIntent.putExtra("orderId", orderId);
			notificationIntent.putExtra("orderType", orderType);
			notificationIntent.setAction(Intent.ACTION_MAIN);
			notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			PendingIntent contentIntent = PendingIntent.getActivity(mApplication, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification_view);
            rv.setImageViewResource(R.id.icon, R.drawable.daze_icon);
            rv.setTextViewText(R.id.title, contentTitle);
            rv.setTextViewText(R.id.content, noticeContent);
            notification.contentView = rv;
            notification.contentIntent = contentIntent;
		} else {
			Intent notificationIntent = null;
			if(orderType == 1){
				notificationIntent = new Intent(mApplication, OrderActivity.class);
				notificationIntent.putExtra("orderId", orderId);
			}
			/**
			 * 洗车订单通知
			 */
			else if (orderType == 6) {
				notificationIntent = new Intent(mApplication, CNOrderDetailsActivity.class);
				notificationIntent.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS_ORDER_ID, orderId);
			}
			else{
				notificationIntent = new Intent(mApplication, VehicleServiceActivity.class);
				notificationIntent.putExtra("appId", "10000009");
				notificationIntent.putExtra("startPage", "detail.html?orderId=" + orderId);
			}
			PendingIntent contentIntent = PendingIntent.getActivity(mApplication, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification_view);
            rv.setImageViewResource(R.id.icon, R.drawable.daze_icon);
            rv.setTextViewText(R.id.title, contentTitle);
            rv.setTextViewText(R.id.content, noticeContent);
            notification.contentView = rv;
            notification.contentIntent = contentIntent;
		}
		int notifyId = sp.getInt("notifyId", 1);
		mApplication.notificationManager.notify(notifyId, notification);
		sp.edit().putInt("notifyId", ++notifyId).commit();
	}
	
	private void saveVehicle(final UserVehicle vehicle) {
		new AsyncTask<Void, Void, VehicleAddResponse>() {
			protected VehicleAddResponse doInBackground(Void... params) {
				VehicleAddRequest request = new VehicleAddRequest();
				request.setParams(mApplication.getUserId(),mApplication.getId(), vehicle);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(VehicleAddResponse result) {
				try{
				if (result != null) {
					if (result.getCode() == 0) {
						if (result.getData().getResult()) {
							if(result.getData().getId() != null)
								vehicle.setVehicleId(result.getData().getId());
							mApplication.dbCache.saveVehicle(vehicle);
							JSONObject prop = new JSONObject();
							prop.put("vehicle_num", vehicle.getVehicleNum());
							if(mApplication.getId() != 0)
								getVehicleAuth(vehicle.getVehicleNum());
							Intent update = new Intent(context, UpdateAgainstRecords.class);
							update.putExtra("vehicleNumber", vehicle.getVehicleNum());
							context.startService(update);
						}
					}
				}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	private void getVehicleAuth(final String vehicleNum){
		new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
			@Override
			protected GetAuthDetaiResponse doInBackground(Void... params) {
				try{
					GetAuthDetaiRequest request = new GetAuthDetaiRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleNum);
					return mApplication.client.execute(request);
				}catch(Exception e){
					return null;
				}
			}
			protected void onPostExecute(GetAuthDetaiResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					if(result.getData() != null){
						List<VehicleAuth> listVA = result.getData().getList();
						if(listVA != null && !listVA.isEmpty()){
							if(listVA.size() == 1){
								VehicleAuth va = listVA.get(0);
								if(va != null){
									if((va.getStatus() != null && va.getStatus() == 2) || (va.getBelong() != null && va.getBelong())){
										mApplication.dbCache.saveVehicleAuth(va);
									}
								}
								LocalBroadcastManager.getInstance(mApplication.getApplicationContext()).sendBroadcast(new Intent(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE));
								return;
							}
							for(VehicleAuth va : listVA){
								if(va.getStatus() != null && va.getStatus() == 2){
									mApplication.dbCache.saveVehicleAuth(va);
									LocalBroadcastManager.getInstance(mApplication.getApplicationContext()).sendBroadcast(new Intent(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE));
									return;
								}
							}
							for(VehicleAuth va : listVA){
								if(va.getBelong() != null && va.getBelong()){
									mApplication.dbCache.saveVehicleAuth(va);
									LocalBroadcastManager.getInstance(mApplication.getApplicationContext()).sendBroadcast(new Intent(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE));
									return;
								}
							}
						}
					}
				}
			}
		}.execute();
	}
	
	private boolean isAppRunning(){
		long begintime = System.currentTimeMillis();
		ActivityManager activityManager = (ActivityManager)mApplication.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rtis = activityManager.getRunningTasks(100);
		String packageName = mApplication.getPackageName();
		for(RunningTaskInfo rti : rtis){
			String topActivityPackageName = rti.topActivity.getPackageName();
			String baseActivityPackageName = rti.baseActivity.getPackageName();
			if(topActivityPackageName.equals(packageName) && baseActivityPackageName.equals(packageName)){
				if(KplusConstants.isDebug){
					Log.i(packageName, packageName + "is running");
					Log.i(packageName, "costtime===>" + (System.currentTimeMillis() - begintime));
				}
				return true;
			}
		}
		if(KplusConstants.isDebug){
			Log.i(packageName, packageName + "is not running");
			Log.i(packageName, "costtime===>" + (System.currentTimeMillis() - begintime));
		}
		return false;
	}
	
	private void parserRecords(JSONObject dataJSON, UserVehicle uservehicle, String time){
		if(dataJSON != null){
			JSONArray listJSON = dataJSON.optJSONArray("list");
			if(listJSON != null && listJSON.length() > 0){
				List<AgainstRecord> againstList = new ArrayList<AgainstRecord>();
				int len = listJSON.length();
				int resultType = dataJSON.optInt("resultType", 0);
				String vehicleNum = null;
				for(int i=0;i<len;i++){
					JSONObject itemJSON = listJSON.optJSONObject(i);
					if(itemJSON != null){
						if(vehicleNum == null)
							vehicleNum = itemJSON.optString("vehicleNum");
						AgainstRecord recordItem = new AgainstRecord();
						recordItem.setId(itemJSON.optLong("id"));
						recordItem.setVehicleNum(itemJSON.optString("vehicleNum"));
						recordItem.setCityId(itemJSON.optLong("cityId"));
						recordItem.setCityName(itemJSON.optString("cityName"));
						recordItem.setAddress(itemJSON.optString("address"));
						recordItem.setBehavior(itemJSON.optString("behavior"));
						recordItem.setTime(itemJSON.optString("time"));
						recordItem.setScore(itemJSON.optInt("score"));
						recordItem.setMoney(itemJSON.optInt("money"));
						recordItem.setStatus(itemJSON.optInt("status"));
						recordItem.setLat(itemJSON.optDouble("lat"));
						recordItem.setLng(itemJSON.optDouble("lng"));
						recordItem.setOrderStatus(itemJSON.optInt("orderStatus"));
						recordItem.setCanSubmit(itemJSON.optInt("canSubmit"));
						recordItem.setPId(itemJSON.optLong("pId"));
						recordItem.setOrderId(itemJSON.optLong("orderId"));
						recordItem.setSelfProcess(itemJSON.optInt("selfProcess"));
						recordItem.setDataSourceTitle(itemJSON.optString("dataSourceTitle"));
						recordItem.setPaymentStatus(itemJSON.optInt("paymentStatus"));
						recordItem.setResultType(resultType);
						recordItem.setOrderCode(itemJSON.optString("orderCode"));
						recordItem.setOrdertime(itemJSON.optString("ordertime"));
						recordItem.setRecordType(itemJSON.optInt("recordType"));
						recordItem.setSelfScore(itemJSON.optInt("selfScore"));
						recordItem.setSelfMoney(itemJSON.optInt("selfMoney"));
						JSONObject imageNameJson = itemJSON.optJSONObject("imageName");
						if(imageNameJson != null && StringUtils.isEmpty(imageNameJson.optString("img_0"))){
							ImageNames imageNames = new ImageNames();
							imageNames.setImg_0(imageNameJson.optString("img_0"));
							imageNames.setImg_1(imageNameJson.optString("img_1"));
							imageNames.setImg_2(imageNameJson.optString("img_2"));
							imageNames.setImg_3(imageNameJson.optString("img_3"));
							imageNames.setImg_4(imageNameJson.optString("img_4"));
							recordItem.setImageName(imageNames);
						}
//						String imageName = itemJSON.optString("imageName");
//						if(imageName != null){
//							try {
//								JSONObject imageNameJson = new JSONObject(imageName);
//								recordItem.setImg_0(imageNameJson.optString("img_0"));
//								recordItem.setImg_1(imageNameJson.optString("img_1"));
//								recordItem.setImg_2(imageNameJson.optString("img_2"));
//								recordItem.setImg_3(imageNameJson.optString("img_3"));
//								recordItem.setImg_4(imageNameJson.optString("img_4"));
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
						againstList.add(recordItem);
					}
				}
				if(againstList != null && !againstList.isEmpty()){
					uservehicle.setUpdateTime(time);
					mApplication.dbCache.updateVehicle(uservehicle);
					if(resultType == 1)
						mApplication.dbCache.deleteImageAgainstRecord(vehicleNum);
					mApplication.dbCache.saveAgainstRecords(againstList);
				}
			}
		}
	}
	
	private void parserRules(JSONObject dataJSON){
		if(dataJSON != null){
			JSONArray ruleJSON = dataJSON.optJSONArray("rule");
			if(ruleJSON != null && ruleJSON.length() > 0){
				List<CityVehicle> cvList = new ArrayList<CityVehicle>();
				int len = ruleJSON.length();
				for(int i=0;i<len;i++){
					JSONObject itemJSON = ruleJSON.optJSONObject(i);
					if(itemJSON != null){
						CityVehicle cvItem = new CityVehicle();
						cvItem.setId(itemJSON.optLong("id"));
						cvItem.setProvince(itemJSON.optString("province"));
						cvItem.setName(itemJSON.optString("name"));
						cvItem.setPrefix(itemJSON.optString("prefix"));
						cvItem.setFrameNumLen(itemJSON.optInt("frameNumLen"));
						cvItem.setMotorNumLen(itemJSON.optInt("motorNumLen"));
						cvItem.setPY(itemJSON.optString("PY"));
						cvItem.setValid(itemJSON.optBoolean("valid"));
						cvItem.setHot(itemJSON.optBoolean("hot"));
						cvItem.setOwner(itemJSON.optBoolean("owner"));
						cvItem.setAccountLen(itemJSON.optInt("accountLen"));
						cvItem.setPasswordLen(itemJSON.optInt("passwordLen"));
						cvItem.setMotorvehiclenumLen(itemJSON.optInt("motorvehiclenumLen"));
						cvItem.setOwnerIdNoLen(itemJSON.optInt("ownerIdNoLen"));
						cvItem.setDrivingLicenseName(itemJSON.optBoolean("drivingLicenseName"));
						cvItem.setDrivingLicenseNoLen(itemJSON.optInt("drivingLicenseNoLen"));
						cvList.add(cvItem);
					}
				}
				if(cvList != null && !cvList.isEmpty()){
					mApplication.dbCache.saveCityVehicles(cvList);
					mApplication.updateCities(cvList);
				}
			}
		}
	}

}
