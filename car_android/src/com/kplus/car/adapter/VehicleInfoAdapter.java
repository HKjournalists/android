package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.Weather;
import com.kplus.car.receiver.RemindManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/9 0009.
 */
public class VehicleInfoAdapter extends RecyclerView.Adapter implements AdViewHolder.ADRemoveListener {
    private KplusApplication mApp;
    private Context mContext;
    private String mVehicleNum;
    private String mNianjianDate;
    private String mIssueDate;
    private List<RemindInfo> mListRemindInfo;
    private final static String[] TITLES = {"车险提醒", "保养提醒", "年检提醒", "车贷提醒"};
    private final static String[] DESCS = {"开启后汽车车险到期自动提醒",
            "开启后保养到期自动提醒", "开启后年检到期自动提醒", "开启后每月车贷到期自动提醒"};
    private final static int[] TYPES = {Constants.REQUEST_TYPE_CHEXIAN,
            Constants.REQUEST_TYPE_BAOYANG, Constants.REQUEST_TYPE_NIANJIAN, Constants.REQUEST_TYPE_CHEDAI};
    private final static boolean[] HIDDENS = {false, false, false, true};
    private boolean mIsRefreshingWeizhang = false;
    private String mCompany, mPhone;
    private List<String> mListJiazhaoRefreshing;

    public VehicleInfoAdapter(Context context, UserVehicle userVehicle, VehicleAuth vehicleAuth, List<String> refreshingList){
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        setUserVehicle(userVehicle);
        if (vehicleAuth != null)
            mIssueDate = vehicleAuth.getIssueDate();
        mListRemindInfo = mApp.dbCache.getRemindInfo(mVehicleNum);
        if (mListRemindInfo == null){
            mListRemindInfo = new ArrayList<RemindInfo>();
            for (int i = 0; i < TITLES.length; i++){
                RemindInfo info = new RemindInfo();
                info.setVehicleNum(mVehicleNum);
                info.setTitle(TITLES[i]);
                info.setDesc(DESCS[i]);
                info.setType(TYPES[i]);
                info.setHidden(HIDDENS[i]);
                mListRemindInfo.add(info);
            }
            mApp.dbCache.saveRemindInfo(mListRemindInfo, mVehicleNum);
            if (mApp.getId() != 0)
                new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_REMIND);
        }
        mListRemindInfo = fitlerHiddenRemind(mListRemindInfo);
        if (userVehicle.getRequestTime() > 0 && userVehicle.getReturnTime() < userVehicle.getRequestTime()){
            int degree = (int) ((System.currentTimeMillis() - userVehicle.getRequestTime())/1000);
            if(degree < 180 && mApp.containsTask(mVehicleNum)) {
                mIsRefreshingWeizhang = true;
            }
        }
        mListJiazhaoRefreshing = refreshingList;
    }

    private List<RemindInfo> fitlerHiddenRemind(List<RemindInfo> list){
        List<RemindInfo> newList = new ArrayList<RemindInfo>();
        for (RemindInfo info : list){
            if (!info.isHidden())
                newList.add(info);
        }
        return newList;
    }

    public void reloadRemindInfo(){
        mListRemindInfo = mApp.dbCache.getRemindInfo(mVehicleNum);
        mListRemindInfo = fitlerHiddenRemind(mListRemindInfo);
    }

    public void closeRemind(int position){
        int pos = getRemindInfoPosition(position);
        if (pos >= 0 && mListRemindInfo != null && pos < mListRemindInfo.size()){
            RemindInfo info = mListRemindInfo.get(pos);
            info.setHidden(true);
            mListRemindInfo.remove(pos);
            mApp.dbCache.updateRemindInfo(info);
            RemindManager.getInstance(mContext).cancel(mVehicleNum, info.getType(), info.getTitle());
            if (mApp.getId() != 0)
                new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_REMIND);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case Constants.REQUEST_TYPE_WEIZHANG:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_weizhang, parent, false);
                WeizhangViewHolder weizhangViewHolder = new WeizhangViewHolder(v, mContext);
                return weizhangViewHolder;
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_jiazhao, parent, false);
                JiazhaoViewHolder jiazhaoViewHolder = new JiazhaoViewHolder(v, mContext);
                return jiazhaoViewHolder;
            case Constants.REQUEST_TYPE_CHEXIAN:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chexian, parent, false);
                ChexianViewHolder chexianViewHolder = new ChexianViewHolder(v, mContext);
                return chexianViewHolder;
            case Constants.REQUEST_TYPE_BAOYANG:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_baoyang, parent, false);
                BaoyangViewHolder baoyangViewHolder = new BaoyangViewHolder(v, mContext);
                return baoyangViewHolder;
            case Constants.REQUEST_TYPE_NIANJIAN:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_nianjian, parent, false);
                NianJianViewHolder nianJianViewHolder = new NianJianViewHolder(v, mContext);
                return nianJianViewHolder;
            case Constants.REQUEST_TYPE_CHEDAI:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chedai, parent, false);
                ChedaiViewHolder chedaiViewHolder = new ChedaiViewHolder(v, mContext);
                return chedaiViewHolder;
            case Constants.REQUEST_TYPE_CUSTOM:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_custom_remind, parent, false);
                CustomViewHolder customViewHolder = new CustomViewHolder(v, mContext);
                return customViewHolder;
            case Constants.REQUEST_TYPE_AD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
                AdViewHolder adViewHolder = new AdViewHolder(v, mContext, KplusConstants.ADVERT_VEHICLE_BODY, this);
                return adViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case Constants.REQUEST_TYPE_WEIZHANG:
                WeizhangViewHolder weizhangViewHolder = (WeizhangViewHolder) holder;
                weizhangViewHolder.bind(mVehicleNum, mIsRefreshingWeizhang);
                break;
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                JiazhaoViewHolder jiazhaoViewHolder = (JiazhaoViewHolder) holder;
                jiazhaoViewHolder.bind(mListJiazhaoRefreshing);
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                ChexianViewHolder chexianViewHolder = (ChexianViewHolder) holder;
                chexianViewHolder.bind(mVehicleNum, mCompany, mPhone, mIssueDate);
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                NianJianViewHolder nianJianViewHolder = (NianJianViewHolder) holder;
                nianJianViewHolder.bind(mVehicleNum, mNianjianDate, mIssueDate);
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                BaoyangViewHolder baoyangViewHolder = (BaoyangViewHolder) holder;
                baoyangViewHolder.bind(mVehicleNum);
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                ChedaiViewHolder chedaiViewHolder = (ChedaiViewHolder) holder;
                chedaiViewHolder.bind(mVehicleNum);
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                CustomViewHolder customViewHolder = (CustomViewHolder) holder;
                int pos = getRemindInfoPosition(position);
                if (pos >= 0) {
                    RemindInfo info = mListRemindInfo.get(pos);
                    if (customViewHolder.bind(mVehicleNum, info.getTitle()))
                        notifyDataSetChanged();
                }
                break;
            case Constants.REQUEST_TYPE_AD:
                AdViewHolder adViewHolder = (AdViewHolder) holder;
                adViewHolder.bind(mApp.getVehicleBodyAdvert());
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = mListRemindInfo != null ? mListRemindInfo.size() : 0;
        boolean hasAd = mApp.getVehicleBodyAdvert() != null && mApp.getVehicleBodyAdvert().size() > 0;
        if (hasAd)
            return count + 3;
        else
            return count + 2;
    }

    @Override
    public int getItemViewType(int position) {
        boolean hasAd = mApp.getVehicleBodyAdvert() != null && mApp.getVehicleBodyAdvert().size() > 0;
        switch (position){
            case 0:
                return Constants.REQUEST_TYPE_WEIZHANG;
            case 1:
                return Constants.REQUEST_TYPE_JIAZHAOFEN;
            case 2:
                if (hasAd)
                    return Constants.REQUEST_TYPE_AD;
                else
                    return mListRemindInfo.get(0).getType();
            default:
                if (hasAd)
                    return mListRemindInfo.get(position - 3).getType();
                else
                    return mListRemindInfo.get(position - 2).getType();
        }
    }

    private int getRemindInfoPosition(int position){
        boolean hasAd = mApp.getVehicleBodyAdvert() != null && mApp.getVehicleBodyAdvert().size() > 0;
        if (hasAd)
            return position - 3;
        else
            return position - 2;
    }

    public void setAdverts(){
        notifyDataSetChanged();
    }

    public void setUserVehicle(UserVehicle userVehicle){
        if (userVehicle != null){
            mVehicleNum = userVehicle.getVehicleNum();
            mCompany = userVehicle.getCompany();
            mPhone = userVehicle.getPhone();
            mNianjianDate = userVehicle.getNianjianDate();
        }
    }

    public void setIsRefreshingWeizhang(boolean b){
        mIsRefreshingWeizhang = b;
        notifyItemChanged(0);
    }

    public void setJiazhaoRefreshingList(List<String> list){
        mListJiazhaoRefreshing = list;
        notifyItemChanged(1);
    }

    public void setInsurance(String company, String phone){
        mCompany = company;
        mPhone = phone;
    }

    @Override
    public void onADRemoved() {
        notifyDataSetChanged();
    }
}

