package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.AddCustomRemindActivity;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.activity.BaoyangEditActivity;
import com.kplus.car.activity.ChedaiEditActivity;
import com.kplus.car.activity.ChexianEditActivity;
import com.kplus.car.activity.NianjianEditActivity;
import com.kplus.car.activity.RestrictEditActivity;
import com.kplus.car.asynctask.RestrictSaveTask;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/3/13 0013.
 */
public class RemindAdapter extends RecyclerView.Adapter implements ClickUtils.NoFastClickListener, RemindViewHolder.RightIconClickListener, RestrictViewHolder.RestrictIconClickListener {
    private Context mContext;
    private KplusApplication mApp;
    private List<RemindInfo> mListRemindInfo;
    private DataChangeListener mListener;
    private UserVehicle mUserVehicle;
    private RemindRestrict mRemindRestrict;
    private final static int VIEW_TYPE_HEADER = 1;
    private final static int VIEW_TYPE_FOOTER = 2;
    private final static int VIEW_TYPE_NORMAL = 3;
    private final static int VIEW_TYPE_RESTRICT = 4;
    private int mPosition = 0;

    public interface DataChangeListener{
        void onDataChanged();
    }

    public RemindAdapter(Context context, List<RemindInfo> list, DataChangeListener l, UserVehicle userVehicle, RemindRestrict remindRestrict){
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mListRemindInfo = list;
        mListener = l;
        mUserVehicle = userVehicle;
        mRemindRestrict = remindRestrict;
    }

    public void setRemindInfo(List<RemindInfo> list) {
        mListRemindInfo = list;
    }

    public void setRemindRestrict(RemindRestrict restrict){
        mRemindRestrict = restrict;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_HEADER:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.add_remind_header, parent, false);
                return new CommonViewHolder(v);
            case VIEW_TYPE_FOOTER:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.add_remind_footer, parent, false);
                ClickUtils.setNoFastClickListener(v.findViewById(R.id.add_custom_remind), this);
                return new CommonViewHolder(v);
            case VIEW_TYPE_RESTRICT:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_restrict, parent, false);
                return new RestrictViewHolder(v, this);
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_remind, parent, false);
                return new RemindViewHolder(v, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_NORMAL){
            RemindInfo info = mListRemindInfo.get(position - 1);
            RemindViewHolder vh = (RemindViewHolder) holder;
            vh.mTitle.setText(info.getTitle());
            vh.mDesc.setText(info.getDesc());
            switch (info.getType()){
                case Constants.REQUEST_TYPE_JIAZHAOFEN:
                    vh.mLeftIcon.setImageResource(R.drawable.jiazhaofen);
                    break;
                case Constants.REQUEST_TYPE_CHEXIAN:
                    vh.mLeftIcon.setImageResource(R.drawable.chexian);
                    break;
                case Constants.REQUEST_TYPE_BAOYANG:
                    vh.mLeftIcon.setImageResource(R.drawable.baoyang);
                    break;
                case Constants.REQUEST_TYPE_NIANJIAN:
                    vh.mLeftIcon.setImageResource(R.drawable.nianjian);
                    break;
                case Constants.REQUEST_TYPE_CHEDAI:
                    vh.mLeftIcon.setImageResource(R.drawable.chedai);
                    break;
                case Constants.REQUEST_TYPE_CUSTOM:
                    vh.mLeftIcon.setImageResource(R.drawable.notes);
                    break;
            }
            vh.mOnOffIcon.setSelected(!info.isHidden());
            if (info.getType() == Constants.REQUEST_TYPE_CUSTOM){
                vh.mDeleteIcon.setVisibility(View.VISIBLE);
            }
            else{
                vh.mDeleteIcon.setVisibility(View.GONE);
            }
        }
        else if (viewType == VIEW_TYPE_RESTRICT){
            RestrictViewHolder vh = (RestrictViewHolder) holder;
            vh.mDesc.setText(DateUtil.getRestrictDesc(mContext, mUserVehicle.getRestrictNums(), mUserVehicle.getVehicleNum()));
            if ("0".equals(mRemindRestrict.getIsHidden())){
                vh.mOnOffIcon.setSelected(true);
                vh.mDivider.setVisibility(View.VISIBLE);
                vh.mRemindLayout.setVisibility(View.VISIBLE);
                String remindDateType = "1".equals(mRemindRestrict.getRemindDateType()) ? "前一天" : "当天";
                vh.mRemindDate.setText(remindDateType + " " + mRemindRestrict.getRemindDate());
            }
            else {
                vh.mOnOffIcon.setSelected(false);
                vh.mDivider.setVisibility(View.GONE);
                vh.mRemindLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mRemindRestrict != null)
            return mListRemindInfo != null ? mListRemindInfo.size() + 3 : 3;
        else
            return mListRemindInfo != null ? mListRemindInfo.size() + 2 : 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return VIEW_TYPE_HEADER;
        }
        else if (position == getItemCount() - 1){
            return VIEW_TYPE_FOOTER;
        }
        else if (position == getItemCount() - 2 && mRemindRestrict != null){
            return VIEW_TYPE_RESTRICT;
        }
        else{
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.add_custom_remind:
                EventAnalysisUtil.onEvent(mContext, "click_AddCustomNotify", "点添加自定义提醒", null);
                Intent it = new Intent(mContext, AddCustomRemindActivity.class);
                it.putExtra("vehicleNum", mListRemindInfo.get(0).getVehicleNum());
                ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CUSTOM);
                break;
        }
    }

    public void deleteRemind(){
        RemindInfo info = mListRemindInfo.get(mPosition - 1);
        mApp.dbCache.deleteRemindCustom(info.getVehicleNum(), info.getTitle());
        mListRemindInfo.remove(mPosition - 1);
        notifyItemRemoved(mPosition);
        if (mListener != null)
            mListener.onDataChanged();
    }

    @Override
    public void onDeleteIconClick(RemindViewHolder vh, int position) {
        mPosition = position;
        Intent alertIntent = new Intent(mContext, AlertDialogActivity.class);
        alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
        alertIntent.putExtra("title", "删除提醒");
        alertIntent.putExtra("message", "确定要删除吗？");
        ((Activity)mContext).startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
    }

    @Override
    public void onOnOffIconClick(RemindViewHolder vh, int position) {
        RemindInfo info = mListRemindInfo.get(position - 1);
        if (vh.mOnOffIcon.isSelected()){
            vh.mOnOffIcon.setSelected(false);
            info.setHidden(true);
            mListRemindInfo.set(position - 1, info);
            if (mListener != null)
                mListener.onDataChanged();
        }
        else{
            switch (info.getType()){
                case Constants.REQUEST_TYPE_CHEXIAN:
                    Intent it = new Intent(mContext, ChexianEditActivity.class);
                    it.putExtra("position", position);
                    it.putExtra("vehicleNum", info.getVehicleNum());
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CHEXIAN);
                    break;
                case Constants.REQUEST_TYPE_NIANJIAN:
                    RemindNianjian remindNianjian = mApp.dbCache.getRemindNianjian(info.getVehicleNum());
                    it = new Intent(mContext, NianjianEditActivity.class);
                    it.putExtra("position", position);
                    it.putExtra("vehicleNum", info.getVehicleNum());
                    it.putExtra("RemindNianjian", remindNianjian);
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_NIANJIAN);
                    break;
                case Constants.REQUEST_TYPE_BAOYANG:
                    RemindBaoyang remindBaoyang = mApp.dbCache.getRemindBaoyang(info.getVehicleNum());
                    it = new Intent(mContext, BaoyangEditActivity.class);
                    it.putExtra("position", position);
                    it.putExtra("vehicleNum", info.getVehicleNum());
                    it.putExtra("RemindBaoyang", remindBaoyang);
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_BAOYANG);
                    break;
                case Constants.REQUEST_TYPE_CHEDAI:
                    RemindChedai remindChedai = mApp.dbCache.getRemindChedai(info.getVehicleNum());
                    it = new Intent(mContext, ChedaiEditActivity.class);
                    it.putExtra("position", position);
                    it.putExtra("vehicleNum", info.getVehicleNum());
                    it.putExtra("RemindChedai", remindChedai);
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CHEDAI);
                    break;
                case Constants.REQUEST_TYPE_CUSTOM:
                    RemindCustom remindCustom = mApp.dbCache.getRemindCustom(info.getVehicleNum(), info.getTitle());
                    it = new Intent(mContext, AddCustomRemindActivity.class);
                    it.putExtra("position", position);
                    it.putExtra("vehicleNum", info.getVehicleNum());
                    it.putExtra("RemindCustom", remindCustom);
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CUSTOM);
                    break;
            }
        }
    }

    @Override
    public void onOnOffIconClick(RestrictViewHolder vh) {
        if (vh.mOnOffIcon.isSelected()){
            vh.mOnOffIcon.setSelected(false);
            vh.mDivider.setVisibility(View.GONE);
            vh.mRemindLayout.setVisibility(View.GONE);
            mRemindRestrict.setIsHidden("1");
            mApp.dbCache.updateRemindRestrict(mRemindRestrict);
            RemindManager.getInstance(mContext).cancel(mUserVehicle.getVehicleNum(), Constants.REQUEST_TYPE_RESTRICT);
            ToastUtil.makeShortToast(mContext, "限行提醒已关闭");
            new RestrictSaveTask(mApp).execute(mRemindRestrict);
        }
        else {
            Intent it = new Intent(mContext, RestrictEditActivity.class);
            it.putExtra("RemindRestrict", mRemindRestrict);
            it.putExtra("UserVehicle", mUserVehicle);
            ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_RESTRICT);
        }
    }

    @Override
    public void onRemindLayoutClick(RestrictViewHolder vh) {
        Intent it = new Intent(mContext, RestrictEditActivity.class);
        it.putExtra("RemindRestrict", mRemindRestrict);
        it.putExtra("UserVehicle", mUserVehicle);
        it.putExtra("edit", true);
        ((Activity) mContext).startActivityForResult(it, Constants.REQUEST_TYPE_RESTRICT);
    }
}
