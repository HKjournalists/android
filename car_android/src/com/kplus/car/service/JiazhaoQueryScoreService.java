package com.kplus.car.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.asynctask.JiazhaoQueryScoreTask;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.json.JiazhaoQueryScoreJson;
import com.kplus.car.model.response.JiazhaoQueryScoreResponse;
import com.kplus.car.model.response.request.JiazhaoQueryScoreRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class JiazhaoQueryScoreService extends IntentService{
	private KplusApplication mApplication;

	public JiazhaoQueryScoreService(){
		super("UpdateAgainstRecords");
	}

	public JiazhaoQueryScoreService(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		mApplication = (KplusApplication) getApplication();
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Jiazhao jiazhao = (Jiazhao) intent.getSerializableExtra("jiazhao");
		if (jiazhao == null)
			return;
		new JiazhaoQueryScoreTask(mApplication){
			@Override
			protected void onPostExecute(JiazhaoQueryScoreResponse response) {
				if (response != null && response.getCode() != null) {
					switch (response.getCode()){
						case 0:
							boolean bChanged = false;
							JiazhaoQueryScoreJson json = response.getData();
							if (StringUtils.isEmpty(jiazhao.getXm())){
								jiazhao.setXm(json.getXm());
								bChanged = true;
							}
							if (json.getLjjf() != null && json.getLjjf().intValue() != jiazhao.getLjjf()){
								jiazhao.setLjjf(json.getLjjf());
								bChanged = true;
							}
							if (!StringUtils.isEmpty(json.getZjcx()) && !json.getZjcx().equals(jiazhao.getZjcx())){
								jiazhao.setZjcx(json.getZjcx());
								bChanged = true;
							}
							if (bChanged) {
								mApplication.dbCache.updateJiazhao(jiazhao);
							}
							StringBuilder sb = new StringBuilder();
							sb.append("驾驶证：");
							String jszh = jiazhao.getJszh();
							sb.append(jszh.substring(0, 4) + "****" + jszh.substring(jszh.length() - 4));
							if (!StringUtils.isEmpty(jiazhao.getXm()))
								sb.append("（" + jiazhao.getXm() + "）");
							sb.append("\n累计记分：" + jiazhao.getLjjf());
							showAlertDialog(sb.toString(), KplusConstants.ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_SUCCESS, jiazhao);
							break;
						case 401:
							sb = new StringBuilder();
							sb.append("驾驶证：");
							jszh = jiazhao.getJszh();
							sb.append(jszh.substring(0, 4) + "****" + jszh.substring(jszh.length() - 4));
							if (!StringUtils.isEmpty(jiazhao.getXm()))
								sb.append("（" + jiazhao.getXm() + "）");
							sb.append("\n原因：无此驾驶证信息");
							showAlertDialog(sb.toString(), KplusConstants.ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_FAILED, jiazhao);
							break;
						case 402:
							sb = new StringBuilder();
							sb.append("驾驶证：");
							jszh = jiazhao.getJszh();
							sb.append(jszh.substring(0, 4) + "****" + jszh.substring(jszh.length() - 4));
							if (!StringUtils.isEmpty(jiazhao.getXm()))
								sb.append("（" + jiazhao.getXm() + "）");
							sb.append("\n原因：驾驶证号码和档案编号不匹配");
							showAlertDialog(sb.toString(), KplusConstants.ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_FAILED, jiazhao);
							break;
						default:
							Intent alertIntent = new Intent(JiazhaoQueryScoreService.this, AlertDialogActivity.class);
							alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
							alertIntent.putExtra("title", "查询失败");
							alertIntent.putExtra("message", "查询不到哦，可以多试几次哦^_^");
							alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(alertIntent);
							break;
					}
				}
				else {
					Intent alertIntent = new Intent(JiazhaoQueryScoreService.this, AlertDialogActivity.class);
					alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
					alertIntent.putExtra("title", "查询失败");
					alertIntent.putExtra("message", "没有网络无法查询哦");
					alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(alertIntent);
				}
				Intent it = new Intent("com.kplus.car.jiazhao.finish");
				it.putExtra("id", jiazhao.getId());
				LocalBroadcastManager.getInstance(JiazhaoQueryScoreService.this).sendBroadcast(it);
			}
		}.execute(jiazhao.getId());
	}

	private void showAlertDialog(String message, int subType, Jiazhao jiazhao){
		Intent alertIntent = new Intent(this, AlertDialogActivity.class);
		alertIntent.putExtra("alertType", KplusConstants.ALERT_JIAZHAO_RESULT);
		alertIntent.putExtra("subAlertType", subType);
		alertIntent.putExtra("message", message);
		alertIntent.putExtra("jiazhao", jiazhao);
		alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(alertIntent);
	}
}
