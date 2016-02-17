package com.kplus.car.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.asynctask.JiazhaoAgainstListTask;
import com.kplus.car.asynctask.JiazhaoListTask;
import com.kplus.car.asynctask.RemindQueryTask;
import com.kplus.car.asynctask.RestrictListTask;
import com.kplus.car.asynctask.RestrictSaveTask;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.Vehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.GetRestrictNumResponse;
import com.kplus.car.model.response.GetVehicleListResponse;
import com.kplus.car.model.response.JiazhaoAgainstListResponse;
import com.kplus.car.model.response.JiazhaoListResponse;
import com.kplus.car.model.response.RemindQueryResponse;
import com.kplus.car.model.response.RestrictListResponse;
import com.kplus.car.model.response.RestrictSaveResponse;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.GetRestrictNumRequest;
import com.kplus.car.model.response.request.GetVehicleListRequest;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SynchronizationVehicle extends Service
{
	private int startId;
	private KplusApplication mApplication;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate()
	{
		mApplication = (KplusApplication) getApplication();
		super.onCreate();
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		this.startId = startId;
		String type = null;
		if(intent != null)
			type = intent.getStringExtra("type");
		if(type != null && type.equals("getAuthDetail")){
			getAuthDetail();
		}
		else
			getVehicleList();
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	private void getVehicleList(){
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				final List<UserVehicle> uservehicles = new ArrayList<UserVehicle>();
				try{
					GetVehicleListRequest request = new GetVehicleListRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId());
					GetVehicleListResponse response = mApplication.client.execute(request);
					if(response != null && response.getCode() != null && response.getCode() == 0){
						List<Vehicle> vehicles = response.getData().getList();
						if(vehicles != null && !vehicles.isEmpty()){
							final List<VehicleAuth> vehicleAuths = new ArrayList<VehicleAuth>();
							for(Vehicle vehicle : vehicles){
								UserVehicle uv = new UserVehicle();
								if(!StringUtils.isEmpty(vehicle.getVehicleNum())){
									uv.setVehicleNum(vehicle.getVehicleNum());
									if(!StringUtils.isEmpty(vehicle.getCityId())){
										uv.setCityId(vehicle.getCityId());
										if(!StringUtils.isEmpty(vehicle.getCityName())) {
											uv.setCityName(vehicle.getCityName());
											GetRestrictNumRequest restrictNumRequest = new GetRestrictNumRequest();
											restrictNumRequest.setParams(uv.getCityName());
											GetRestrictNumResponse restrictNumResponse = mApplication.client.execute(restrictNumRequest);
											if (restrictNumResponse != null && restrictNumResponse.getCode() == 0 && restrictNumResponse.getData() != null){
												uv.setRestrictNum(restrictNumResponse.getData().getNum());
												uv.setRestrictRegion(restrictNumResponse.getData().getMessage());
												uv.setRestrictNums(restrictNumResponse.getData().getRestrictNums());
											}
										}
										if(!StringUtils.isEmpty(vehicle.getMotorNum()))
											uv.setMotorNum(vehicle.getMotorNum());
										if(!StringUtils.isEmpty(vehicle.getFrameNum()))
											uv.setFrameNum(vehicle.getFrameNum());
										if(vehicle.getVehicleModelId() != null)
											uv.setVehicleModelId(vehicle.getVehicleModelId());
										if(!StringUtils.isEmpty(vehicle.getDesc()))
											uv.setDescr(vehicle.getDesc());
										if(!StringUtils.isEmpty(vehicle.getMotorNum()) || !StringUtils.isEmpty(vehicle.getFrameNum())){
											uservehicles.add(uv);
											if(vehicle.getVehicleAuth() != null)
												vehicleAuths.add(vehicle.getVehicleAuth());
										}
										if(!StringUtils.isEmpty(vehicle.getAccount())){
											uv.setAccount(vehicle.getAccount());
										}
										if(!StringUtils.isEmpty(vehicle.getPassword())){
											uv.setPassword(vehicle.getPassword());
										}
										if(vehicle.getVehicleType() != null){
											uv.setVehicleType(vehicle.getVehicleType());
										}
									}
								}
							}
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									mApplication.dbCache.saveUserVehicles(uservehicles);
									mApplication.dbCache.saveVehicleAuths(vehicleAuths);
								}
							});
						}
					}
					remindQuery();
				}catch(Exception e){
                    stopSelf(startId);
				}
			}
		}).start();
	}
	
	private void getAuthDetail(){
		try{
			final List<UserVehicle> listUVs = mApplication.dbCache.getVehicles();
			if(listUVs != null && !listUVs.isEmpty()){
				StringBuilder sb = new StringBuilder();
				for(UserVehicle uv : listUVs){
					if(!uv.isHiden()){
						String vn = uv.getVehicleNum();
						if(!StringUtils.isEmpty(vn))
							sb.append(vn + ",");
					}
				}
				if(!StringUtils.isEmpty(sb.toString())){
					final String strVehicles = sb.substring(0, sb.length()-1);
					new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
						@Override
						protected GetAuthDetaiResponse doInBackground(Void... params) {
							try{
								GetAuthDetaiRequest request = new GetAuthDetaiRequest();
								request.setParams(mApplication.getUserId(), mApplication.getId(), strVehicles);
								return mApplication.client.execute(request);
							}
							catch(Exception e){
								e.printStackTrace();
								return null;
							}
						}
						
						protected void onPostExecute(GetAuthDetaiResponse result) {
							if(result != null && result.getCode() != null && result.getCode() == 0){
								List<VehicleAuth> listVA = result.getData().getList();
								if(listVA != null && !listVA.isEmpty()){
									Collections.sort(listVA, new AuthComparator());
									mApplication.dbCache.saveVehicleAuths(listVA);
								}
							}
							stopSelf(startId);
						}
					}.execute();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			stopSelf(startId);
		}
	}
	
	class AuthComparator implements Comparator<VehicleAuth>{
		@Override
		public int compare(VehicleAuth arg0, VehicleAuth arg1) {
			if(arg0.getVehicleNum().compareToIgnoreCase(arg1.getVehicleNum()) == 0){
				if(arg0.getStatus() != null && arg0.getStatus() == 2){
					return 1;
				}
				if(arg1.getStatus() != null && arg1.getStatus() == 2){
					return -1;
				}
				if(arg0.getBelong() != null && arg0.getBelong())
					return 1;
				if(arg1.getBelong() != null && arg1.getBelong())
					return -1;
				return 0;
			}
			return arg0.getVehicleNum().compareToIgnoreCase(arg1.getVehicleNum());
		}		
	}

    private void remindQuery(){
		new RestrictListTask(mApplication){
			@Override
			protected void onPostExecute(RestrictListResponse response) {
				if (response != null && response.getCode() != null && response.getCode() == 0){
					RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_RESTRICT);
					List<RemindRestrict> list = response.getData().getList();
					mApplication.dbCache.saveRemindRestricts(list);
					RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_RESTRICT);
				}
				List<UserVehicle> userVehicleList = mApplication.dbCache.getVehicles();
				for (final UserVehicle userVehicle : userVehicleList){
					RemindRestrict restrict = mApplication.dbCache.getRemindRestrict(userVehicle.getVehicleNum());
					if (!StringUtils.isEmpty(userVehicle.getRestrictNums()) && restrict == null){
						final RemindRestrict remindRestrict = new RemindRestrict();
						remindRestrict.setVehicleNum(userVehicle.getVehicleNum());
						new RestrictSaveTask(mApplication){
							@Override
							protected void onPostExecute(RestrictSaveResponse response) {
								if (response != null && response.getCode() != null && response.getCode() == 0){
									remindRestrict.setId(response.getData().getId());
									remindRestrict.setVehicleCityId(userVehicle.getCityId());
									mApplication.dbCache.saveRemindRestrict(remindRestrict);
									RemindManager.getInstance(SynchronizationVehicle.this).set(Constants.REQUEST_TYPE_RESTRICT, userVehicle.getVehicleNum(), 0, null);
								}
							}
						}.execute(remindRestrict);
					}
				}
			}
		}.execute();

		new JiazhaoListTask(mApplication){
			@Override
			protected void onPostExecute(JiazhaoListResponse jiazhaoListResponse) {
				if (jiazhaoListResponse != null && jiazhaoListResponse.getCode() == 0){
					RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_JIAZHAOFEN);
					List<Jiazhao> list = jiazhaoListResponse.getData().getList();
					if (list != null && list.size() > 0){
						for(Jiazhao jiazhao : list){
							String startTime = jiazhao.getStartTime();
							if (startTime != null){
								int index = startTime.indexOf(' ');
								if (index != -1)
									jiazhao.setStartTime(startTime.substring(0, index));
							}
							String date = jiazhao.getDate();
							if (date != null){
								int index = date.indexOf(' ');
								if (index != -1)
									jiazhao.setDate(date.substring(0, index));
							}
						}
					}
					mApplication.dbCache.saveJiazhaos(list);
					RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_JIAZHAOFEN);
				}
			}
		}.execute();

		new JiazhaoAgainstListTask(mApplication){
			@Override
			protected void onPostExecute(JiazhaoAgainstListResponse jiazhaoAgainstListResponse) {
				if (jiazhaoAgainstListResponse != null && jiazhaoAgainstListResponse.getCode() == 0){
					mApplication.dbCache.saveJiazhaoAgainst(jiazhaoAgainstListResponse.getData().getList());
				}
			}
		}.execute();

        new RemindQueryTask(mApplication){
            @Override
            protected void onPostExecute(RemindQueryResponse remindQueryResponse) {
                if (remindQueryResponse != null && remindQueryResponse.getCode() != null && remindQueryResponse.getCode() == 0){
					try{
						String json = remindQueryResponse.getData().getValue();
						if(!StringUtils.isEmpty(json)){
							JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
							for(Iterator iter = jsonArray.iterator(); iter.hasNext();) {
								JsonObject obj = (JsonObject) iter.next();
								int type = obj.get("type").getAsInt();
								JsonElement element = obj.get("value");
								switch (type){
									case -1:
										List<BaoyangRecord> records = new Gson().fromJson(element, new TypeToken<List<BaoyangRecord>>(){}.getType());
										mApplication.dbCache.saveBaoyangRecord(records);
										break;
									case 0:
										RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_CUSTOM);
										List<RemindCustom> customs = new Gson().fromJson(element, new TypeToken<List<RemindCustom>>(){}.getType());
										mApplication.dbCache.saveRemindCustom(customs);
										RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_CUSTOM);
										break;
									case 2:
										RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_CHEXIAN);
										List<RemindChexian> chexians = new Gson().fromJson(element, new TypeToken<List<RemindChexian>>(){}.getType());
										mApplication.dbCache.saveRemindChexian(chexians);
										RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_CHEXIAN);
										break;
									case 3:
										RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_BAOYANG);
										List<RemindBaoyang> baoyangs = new Gson().fromJson(element, new TypeToken<List<RemindBaoyang>>(){}.getType());
										mApplication.dbCache.saveRemindBaoyang(baoyangs);
										RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_BAOYANG);
										break;
									case 4:
										RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_NIANJIAN);
										List<RemindNianjian> nianjians = new Gson().fromJson(element, new TypeToken<List<RemindNianjian>>(){}.getType());
										mApplication.dbCache.saveRemindNianjian(nianjians);
										RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_NIANJIAN);
										break;
									case 5:
										RemindManager.getInstance(SynchronizationVehicle.this).cancelAll(Constants.REQUEST_TYPE_CHEDAI);
										List<RemindChedai> chedais = new Gson().fromJson(element, new TypeToken<List<RemindChedai>>(){}.getType());
										mApplication.dbCache.saveRemindChedai(chedais);
										RemindManager.getInstance(SynchronizationVehicle.this).setAll(Constants.REQUEST_TYPE_CHEDAI);
										break;
									case 6:
										List<RemindInfo> infos = new Gson().fromJson(element, new TypeToken<List<RemindInfo>>(){}.getType());
										if (infos != null) {
											for (int i = infos.size() - 1; i >= 0; i--) {
												RemindInfo info = infos.get(i);
												if (info.getType() == Constants.REQUEST_TYPE_JIAZHAOFEN)
													infos.remove(info);
											}
										}
										mApplication.dbCache.saveRemindInfo(infos);
										break;
								}
							}
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
                }
                LocalBroadcastManager.getInstance(SynchronizationVehicle.this).sendBroadcastSync(new Intent(KplusConstants.ACTION_GET_SYN));
                stopSelf(startId);
            }
        }.execute();
    }
}
