package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.JiazhaoAgainstListTask;
import com.kplus.car.asynctask.JiazhaoListTask;
import com.kplus.car.asynctask.RemindQueryTask;
import com.kplus.car.asynctask.WeatherTask;
import com.kplus.car.fragment.AddVehicleFragment;
import com.kplus.car.fragment.UserVehicleFragment;
import com.kplus.car.model.Advert;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.Weather;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.JiazhaoAgainstListResponse;
import com.kplus.car.model.response.JiazhaoListResponse;
import com.kplus.car.model.response.RemindQueryResponse;
import com.kplus.car.model.response.WeatherResponse;
import com.kplus.car.model.response.request.GetAdvertDataRequest;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.CirclePageIndicator;
import com.kplus.car.widget.FadePageTransformer;
import com.kplus.car.widget.FixedSpeedScroller;
import com.nineoldandroids.animation.ObjectAnimator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HomeNewActivity extends BaseActivity implements ClickUtils.NoFastClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<UserVehicle> mListUserVehicle;
    private UserVehicleAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private View mBtnAdd;
    private View mBtnAddBg;
    private CirclePageIndicator mPageIndicator;
    private TextView mLocation;
    private ImageView mBg1;
    private RelativeLayout mRootView;
    private View mGuidePage;
    private int mTotal = 0;
    private int mBg1Height;
    private List<String> mListJiazhaoRefreshing;
    private long mPopupCloseTime;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_new);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setPageTransformer(true, new FadePageTransformer());
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), new LinearInterpolator());
            field.set(mViewPager, scroller);
        } catch(Exception e){
            e.printStackTrace();
        }
        mBg1 = (ImageView) findViewById(R.id.bg1);
        mBg1Height = dip2px(this, 185);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnAddBg = findViewById(R.id.btn_add_bg);
        mListUserVehicle = mApplication.dbCache.getVehicles();
        if (mListUserVehicle == null || mListUserVehicle.size() == 0) {
            mBtnAdd.setVisibility(View.GONE);
            mBtnAddBg.setVisibility(View.GONE);
        } else {
            UserVehicle vehicle = mListUserVehicle.get(0);
            mApplication.setCarWashingLicense(vehicle.getVehicleNum());
        }
        mAdapter = new UserVehicleAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPageIndicator.setViewPager(mViewPager);

        mLocation = (TextView) findViewById(R.id.location);
        mTotal = dip2px(this, 112);
        mListJiazhaoRefreshing = new ArrayList<>();
        showPopGuide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuthDetail();
        showPopGuide();
    }

    private void showPopGuide() {
        if (mListUserVehicle.size() > 0 && mApplication.dbCache.getValue("swipeGuide") == null) {
            mGuidePage = LayoutInflater.from(HomeNewActivity.this).inflate(R.layout.popup_guide, null, false);
            mRootView = (RelativeLayout) findViewById(R.id.root);
            mRootView.addView(mGuidePage, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ClickUtils.setNoFastClickListener(mGuidePage.findViewById(R.id.btn), HomeNewActivity.this);
            mApplication.dbCache.putValue("swipeGuide", "false");
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(vehicleSyncReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(vehicleAddReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weizhangStartReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weizhangFinishReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(jiazhaoStartReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(jiazhaoFinishReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tabChangedReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
        super.onDestroy();
    }

    @Override
    protected void loadData() {
        EventAnalysisUtil.onEvent(this, "pageView_wodeche", "我的车首页", null);
        LocalBroadcastManager.getInstance(this).registerReceiver(vehicleSyncReceiver, new IntentFilter(KplusConstants.ACTION_GET_SYN));
        LocalBroadcastManager.getInstance(this).registerReceiver(vehicleAddReceiver, new IntentFilter("com.kplus.car.vehicle.add"));
        LocalBroadcastManager.getInstance(this).registerReceiver(weizhangStartReceiver, new IntentFilter("com.kplus.car.weizhang.start"));
        LocalBroadcastManager.getInstance(this).registerReceiver(weizhangFinishReceiver, new IntentFilter("com.kplus.car.weizhang.finish"));
        LocalBroadcastManager.getInstance(this).registerReceiver(jiazhaoStartReceiver, new IntentFilter("com.kplus.car.jiazhao.start"));
        LocalBroadcastManager.getInstance(this).registerReceiver(jiazhaoFinishReceiver, new IntentFilter("com.kplus.car.jiazhao.finish"));
        LocalBroadcastManager.getInstance(this).registerReceiver(tabChangedReceiver, new IntentFilter("com.kplus.car.tab.changed"));
        LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, new IntentFilter("com.kplus.car.location.changed"));
        if(!StringUtils.isEmpty(mApplication.getCityName())) {
            mLocation.setText(mApplication.getCityName());
        }

        if (mApplication.getId() != 0){
            new JiazhaoListTask(mApplication){
                @Override
                protected void onPostExecute(JiazhaoListResponse jiazhaoListResponse) {
                    if (jiazhaoListResponse != null && jiazhaoListResponse.getCode() != null && jiazhaoListResponse.getCode() == 0){
                        RemindManager.getInstance(HomeNewActivity.this).cancelAll(Constants.REQUEST_TYPE_JIAZHAOFEN);
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
                        RemindManager.getInstance(HomeNewActivity.this).setAll(Constants.REQUEST_TYPE_JIAZHAOFEN);
                    }
                }
            }.execute();

            new JiazhaoAgainstListTask(mApplication){
                @Override
                protected void onPostExecute(JiazhaoAgainstListResponse jiazhaoAgainstListResponse) {
                    if (jiazhaoAgainstListResponse != null && jiazhaoAgainstListResponse.getCode() != null && jiazhaoAgainstListResponse.getCode() == 0){
                        mApplication.dbCache.saveJiazhaoAgainst(jiazhaoAgainstListResponse.getData().getList());
                    }
                }
            }.execute();

            new RemindQueryTask(mApplication){
                @Override
                protected void onPostExecute(RemindQueryResponse remindQueryResponse) {
                    if (remindQueryResponse != null && remindQueryResponse.getCode() != null && remindQueryResponse.getCode() == 0){
                        String json = remindQueryResponse.getData().getValue();
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
                                    RemindManager.getInstance(HomeNewActivity.this).cancelAll(Constants.REQUEST_TYPE_CUSTOM);
                                    List<RemindCustom> customs = new Gson().fromJson(element, new TypeToken<List<RemindCustom>>(){}.getType());
                                    mApplication.dbCache.saveRemindCustom(customs);
                                    RemindManager.getInstance(HomeNewActivity.this).setAll(Constants.REQUEST_TYPE_CUSTOM);
                                    break;
                                case 2:
                                    RemindManager.getInstance(HomeNewActivity.this).cancelAll(Constants.REQUEST_TYPE_CHEXIAN);
                                    List<RemindChexian> chexians = new Gson().fromJson(element, new TypeToken<List<RemindChexian>>(){}.getType());
                                    mApplication.dbCache.saveRemindChexian(chexians);
                                    RemindManager.getInstance(HomeNewActivity.this).setAll(Constants.REQUEST_TYPE_CHEXIAN);
                                    break;
                                case 3:
                                    RemindManager.getInstance(HomeNewActivity.this).cancelAll(Constants.REQUEST_TYPE_BAOYANG);
                                    List<RemindBaoyang> baoyangs = new Gson().fromJson(element, new TypeToken<List<RemindBaoyang>>(){}.getType());
                                    mApplication.dbCache.saveRemindBaoyang(baoyangs);
                                    RemindManager.getInstance(HomeNewActivity.this).setAll(Constants.REQUEST_TYPE_BAOYANG);
                                    break;
                                case 4:
                                    RemindManager.getInstance(HomeNewActivity.this).cancelAll(Constants.REQUEST_TYPE_NIANJIAN);
                                    List<RemindNianjian> nianjians = new Gson().fromJson(element, new TypeToken<List<RemindNianjian>>(){}.getType());
                                    mApplication.dbCache.saveRemindNianjian(nianjians);
                                    RemindManager.getInstance(HomeNewActivity.this).setAll(Constants.REQUEST_TYPE_NIANJIAN);
                                    break;
                                case 5:
                                    RemindManager.getInstance(HomeNewActivity.this).cancelAll(Constants.REQUEST_TYPE_CHEDAI);
                                    List<RemindChedai> chedais = new Gson().fromJson(element, new TypeToken<List<RemindChedai>>(){}.getType());
                                    mApplication.dbCache.saveRemindChedai(chedais);
                                    RemindManager.getInstance(HomeNewActivity.this).setAll(Constants.REQUEST_TYPE_CHEDAI);
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
                        if (mAdapter != null && !isFinishing())
                            mAdapter.notifyDataSetChanged();
                    }
                }
            }.execute();
        }
        getAdvertData();
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mBtnAdd, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.location_layout), this);
        mPageIndicator.setOnPageChangeListener(this);
    }

    private void showPopup(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(mBtnAdd, "Rotation", 0f, 90f);
        anim.setDuration(250);
        anim.start();
        ViewCompat.setAlpha(mBtnAdd, 0.5f);
        ViewCompat.setAlpha(mBtnAddBg, ViewCompat.getAlpha(mBtnAddBg) * 0.5f);
        View layout = LayoutInflater.from(this).inflate(R.layout.popup_add_vehicle, null, false);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.add_vehicle), this);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.add_alarm), this);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.edit_vehicle), this);
        mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupCloseTime = SystemClock.uptimeMillis();
                ObjectAnimator anim = ObjectAnimator.ofFloat(mBtnAdd, "Rotation", 90f, 0f);
                anim.setDuration(250);
                anim.start();
                ViewCompat.setAlpha(mBtnAdd, 1f);
                ViewCompat.setAlpha(mBtnAddBg, ViewCompat.getAlpha(mBtnAddBg) * 2);
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(mBtnAdd);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.location_layout:
                EventAnalysisUtil.onEvent(this, "click_City_Select", "首页城市选择被点击", null);
                Intent intent = new Intent(this, CitySelectActivity.class);
                intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_HOME);
                intent.putExtra("cityName", mApplication.getCityName());
                startActivityForResult(intent, Constants.REQUEST_TYPE_CITY);
                break;
            case R.id.btn_add:
                if (SystemClock.uptimeMillis() - mPopupCloseTime < 500)
                    return;
                EventAnalysisUtil.onEvent(this, "open_AddButton", "打开快捷菜单＋号", null);
                showPopup();
                break;
            case R.id.add_vehicle:
                // 添加车辆
                EventAnalysisUtil.onEvent(this, "click_AddCar_Menu", "快捷菜单进入添加车辆", null);
                mPopupWindow.dismiss();
                if(mApplication.getId() == 0){
                    ToastUtil.TextToast(this, "添加车辆需要绑定手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                    intent = new Intent(this, PhoneRegistActivity.class);
                    intent.putExtra("isMustPhone", true);
                    startActivityForResult(intent, Constants.REQUEST_TYPE_LOGIN);
                }
                else {
                    intent = new Intent(this, VehicleAddNewActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
                }
                break;
            case R.id.add_alarm:
                EventAnalysisUtil.onEvent(this, "click_AddNotify_Menu", "快捷菜单进入添加提醒", null);
                // 添加提醒
                mPopupWindow.dismiss();
                if (mApplication.isUserLogin(true, "添加提醒需要绑定手机号") && mViewPager.getCurrentItem() < mListUserVehicle.size()) {
                    intent = new Intent(this, AddRemindActivity.class);
                    intent.putExtra("vehicle", mListUserVehicle.get(mViewPager.getCurrentItem()));
                    startActivityForResult(intent, Constants.REQUEST_TYPE_REMIND);
                }
                break;
            case R.id.edit_vehicle:
                EventAnalysisUtil.onEvent(this, "click_EditCar_Menu", "快捷菜单进入编辑车辆", null);
                // 编辑车辆
                mPopupWindow.dismiss();
                if (mApplication.isUserLogin(true, "编辑车辆需要绑定手机号") && mViewPager.getCurrentItem() < mListUserVehicle.size()) {
                    // 用户登录
                    intent = new Intent(this, VehicleEditActivity.class);
                    intent.putExtra("vehicle", mListUserVehicle.get(mViewPager.getCurrentItem()));
                    startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
                }
                break;
            case R.id.btn: // close pop view
                mRootView.removeView(mGuidePage);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == mAdapter.getCount() - 1) {
            mBtnAdd.setVisibility(View.GONE);
            mBtnAddBg.setVisibility(View.GONE);
        }
        else {
            mBtnAdd.setVisibility(View.VISIBLE);
            mBtnAddBg.setVisibility(View.VISIBLE);
            onScrolled(0);
            Object o = mAdapter.instantiateItem(mViewPager, position);
            if (o instanceof UserVehicleFragment){
                UserVehicleFragment uv = (UserVehicleFragment) o;
                uv.resetScroller();
                uv.updateRestrictNum();
                uv.setAdverts();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class UserVehicleAdapter extends FragmentStatePagerAdapter {
        public UserVehicleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == getCount() - 1)
                return new AddVehicleFragment();
            else {
                UserVehicle uv = mListUserVehicle.get(position);
                UserVehicleFragment fragment = UserVehicleFragment.newInstance(uv, mApplication.dbCache.getVehicleAuthByVehicleNumber(uv.getVehicleNum()));
                fragment.setJiazhaoRefreshingList(mListJiazhaoRefreshing);
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return mListUserVehicle != null ? mListUserVehicle.size() + 1 : 1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                super.destroyItem(container, position, object);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            try {
                super.restoreState(state, loader);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TYPE_LOGIN){
            if (resultCode == RESULT_OK){
                Intent intent = new Intent(this, VehicleAddNewActivity.class);
                startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
            }
        }
        else if (requestCode == Constants.REQUEST_TYPE_CITY){
            if (resultCode == RESULT_OK){
                ArrayList<CityVehicle> listTemp = data.getParcelableArrayListExtra("selectedCity");
                if(listTemp != null && !listTemp.isEmpty()) {
                    CityVehicle cv = listTemp.get(0);
                    String cityName = cv.getName();
                    if (cityName != null) {
                        mApplication.setCityName(cityName);
                        mLocation.setText(cityName);
                    }
                    if(cv.getProvince() != null)
                        mApplication.setProvince(cv.getProvince());
                }
            }
        }
        else if (requestCode == Constants.REQUEST_TYPE_VEHICLE){
            if(resultCode == Constants.RESULT_TYPE_ADDED) {
                if (mListUserVehicle != null && mListUserVehicle.size() > 0){
                    Intent it = new Intent(this, VehicleDetailActivity.class);
                    it.putExtra("vehicleNumber", mListUserVehicle.get(0).getVehicleNum());
                    it.putExtra("add", true);
                    startActivityForResult(it, Constants.REQUEST_TYPE_WEIZHANG);
                }
            }
            else if(resultCode == Constants.RESULT_TYPE_REMOVED){
                mListUserVehicle = mApplication.dbCache.getVehicles();
                mAdapter.notifyDataSetChanged();
                onPageSelected(mViewPager.getCurrentItem());
            }
            else if (resultCode == Constants.RESULT_TYPE_CHANGED){
                UserVehicle uv = (UserVehicle) data.getSerializableExtra("vehicle");
                mListUserVehicle.set(mViewPager.getCurrentItem(), uv);
                Fragment f = (Fragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                if (f != null)
                    f.onActivityResult(requestCode, resultCode, data);
            }
        }
        else {
            if (resultCode == Constants.RESULT_TYPE_CHANGED){
                if (requestCode == Constants.REQUEST_TYPE_NIANJIAN){
                    UserVehicle uv = mApplication.dbCache.getVehicle(mListUserVehicle.get(mViewPager.getCurrentItem()).getVehicleNum());
                    mListUserVehicle.set(mViewPager.getCurrentItem(), uv);
                    data.putExtra("vehicle", uv);
                }
            }
            Fragment f = (Fragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
            if (f != null)
                f.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onScrolled(int scrollY) {
        ViewCompat.setTranslationY(mBtnAdd, scrollY > mTotal ? -mTotal / 2 : -scrollY / 2);
        ViewCompat.setTranslationY(mBtnAddBg, scrollY > mTotal ? -mTotal / 2 : -scrollY / 2);
        float percent = scrollY > mTotal / 2.5f ? 1 : (float) scrollY / mTotal * 2.5f;
        ViewCompat.setTranslationY(mPageIndicator, -scrollY / 2);
        ViewCompat.setAlpha(mPageIndicator, 1 - percent);
        ViewCompat.setAlpha(mBtnAddBg, 1 - percent);
    }

    public void onPullDown(int pullDownY){
        mBg1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mBg1Height + pullDownY));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private BroadcastReceiver vehicleSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListUserVehicle = mApplication.dbCache.getVehicles();
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(0);
            onPageSelected(0);
        }
    };

    private BroadcastReceiver vehicleAddReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListUserVehicle = mApplication.dbCache.getVehicles();
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);
            onPageSelected(0);
        }
    };

    private BroadcastReceiver weizhangStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleNum = intent.getStringExtra("vehicleNum");
            if (mListUserVehicle != null){
                int i = 0;
                for (; i < mListUserVehicle.size(); i++){
                    if (vehicleNum.equals(mListUserVehicle.get(i).getVehicleNum())) {
                        mListUserVehicle.set(i, mApplication.dbCache.getVehicle(vehicleNum));
                        break;
                    }
                }
                if (i < mListUserVehicle.size()){
                    UserVehicleFragment uv = (UserVehicleFragment) mAdapter.instantiateItem(mViewPager, i);
                    uv.setIsRefreshingWeizhang(true);
                }
            }
        }
    };

    private BroadcastReceiver weizhangFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleNum = intent.getStringExtra("vehicleNum");
            if (mListUserVehicle != null){
                int i = 0;
                for (; i < mListUserVehicle.size(); i++){
                    if (vehicleNum.equals(mListUserVehicle.get(i).getVehicleNum()))
                        break;
                }
                if (i < mListUserVehicle.size()){
                    UserVehicleFragment uv = (UserVehicleFragment) mAdapter.instantiateItem(mViewPager, i);
                    uv.setIsRefreshingWeizhang(false);
                }
            }
            new JiazhaoAgainstListTask(mApplication){
                @Override
                protected void onPostExecute(JiazhaoAgainstListResponse jiazhaoAgainstListResponse) {
                    if (jiazhaoAgainstListResponse != null && jiazhaoAgainstListResponse.getCode() == 0){
                        mApplication.dbCache.saveJiazhaoAgainst(jiazhaoAgainstListResponse.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }.execute();
        }
    };

    private BroadcastReceiver jiazhaoStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListJiazhaoRefreshing.add(intent.getStringExtra("id"));
            for (int i = 0; i < mListUserVehicle.size(); i++){
                UserVehicleFragment uv = (UserVehicleFragment) mAdapter.instantiateItem(mViewPager, i);
                uv.setJiazhaoRefreshingList(mListJiazhaoRefreshing);
            }
        }
    };

    private BroadcastReceiver jiazhaoFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListJiazhaoRefreshing.remove(intent.getStringExtra("id"));
            for (int i = 0; i < mListUserVehicle.size(); i++){
                UserVehicleFragment uv = (UserVehicleFragment) mAdapter.instantiateItem(mViewPager, i);
                uv.setJiazhaoRefreshingList(mListJiazhaoRefreshing);
            }
        }
    };

    private BroadcastReceiver tabChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListUserVehicle = mApplication.dbCache.getVehicles();
            if (mListUserVehicle == null || mViewPager.getCurrentItem() == mListUserVehicle.size()) {
                mBtnAdd.setVisibility(View.GONE);
                mBtnAddBg.setVisibility(View.GONE);
            }
            else{
                mBtnAdd.setVisibility(View.VISIBLE);
                mBtnAddBg.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver locationChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLocation.setText(mApplication.getCityName());
        }
    };

    private void getAuthDetail(){
        if(mApplication.getUserId() != 0 && mApplication.getId() != 0){
            long lastAuthDetailUpdateTime = sp.getLong("lastAuthDetailUpdateTime", 0);
            if((System.currentTimeMillis() - lastAuthDetailUpdateTime) > 20*1000){
                sp.edit().putLong("lastAuthDetailUpdateTime", System.currentTimeMillis()).commit();
                final List<UserVehicle> listUVs = mApplication.dbCache.getVehicles();
                if(listUVs != null && !listUVs.isEmpty()){
                    StringBuilder sb = new StringBuilder();
                    for(UserVehicle uv : listUVs){
                        if(!uv.isHiden()){
                            String vn = uv.getVehicleNum();
                            if(!StringUtils.isEmpty(vn))
                                sb.append(vn).append(",");
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
                                        Collections.sort(listVA, new Comparator<VehicleAuth>() {
                                            @Override
                                            public int compare(VehicleAuth arg0, VehicleAuth arg1) {
                                                if (arg0.getVehicleNum().compareToIgnoreCase(arg1.getVehicleNum()) == 0) {
                                                    if (arg0.getStatus() != null && arg0.getStatus() == 2) {
                                                        return 1;
                                                    }
                                                    if (arg1.getStatus() != null && arg1.getStatus() == 2) {
                                                        return -1;
                                                    }
                                                    if (arg0.getBelong() != null && arg0.getBelong())
                                                        return 1;
                                                    if (arg1.getBelong() != null && arg1.getBelong())
                                                        return -1;
                                                    return 0;
                                                }
                                                return arg0.getVehicleNum().compareToIgnoreCase(arg1.getVehicleNum());
                                            }
                                        });
                                        mApplication.dbCache.saveVehicleAuths(listVA);
                                    }
                                }
                            }
                        }.execute();
                    }
                }
            }
        }
    }

    private void getAdvertData(){
        new AsyncTask<Void, Void, GetAdvertDataResponse>(){
            @Override
            protected GetAdvertDataResponse doInBackground(Void... params) {
                GetAdvertDataRequest request = new GetAdvertDataRequest();
                if (!StringUtils.isEmpty(mApplication.getCityId())){
                    try {
                        request.setParams(Long.parseLong(mApplication.getCityId()), mApplication.getUserId(), mApplication.getId(),KplusConstants.ADVERT_VEHICLE_BODY);
                        return mApplication.client.execute(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(GetAdvertDataResponse getAdvertDataResponse) {
                super.onPostExecute(getAdvertDataResponse);
                if(getAdvertDataResponse != null && getAdvertDataResponse.getCode() != null && getAdvertDataResponse.getCode() == 0){
                    AdvertJson data = getAdvertDataResponse.getData();
                    if(data != null){
                        List<Advert> adverts = data.getVehicleBody();
                        if(adverts != null && !adverts.isEmpty()){
                            mApplication.setVehicleBodyAdvert((ArrayList<Advert>)adverts);
                            if (mViewPager.getCurrentItem() != mAdapter.getCount() - 1){
                                UserVehicleFragment uv = (UserVehicleFragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                                mAdapter.finishUpdate(mViewPager);
                                uv.setAdverts();
                            }
                        }
                    }
                }
            }
        }.execute();
    }
}
