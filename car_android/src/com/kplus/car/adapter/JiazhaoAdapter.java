package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.BaseActivity;
import com.kplus.car.activity.JiazhaoEditActivity;
import com.kplus.car.activity.VehicleDetailActivity;
import com.kplus.car.asynctask.JiazhaoUpdateSpaceTask;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.JiazhaoAgainst;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.service.JiazhaoQueryScoreService;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/5/26.
 */
public class JiazhaoAdapter extends RecyclerView.Adapter implements ClickUtils.NoFastClickListener, JiazhaoDetailViewHolder.DetailClickListener {
    private Context mContext;
    private KplusApplication mApplication;
    private List<Jiazhao> mListJiazhao;
    private List<String> mListRefreshing;
    private List<JiazhaoAgainst> mListJiazhaoAgainst;
    private PopupWindow mPopupWindow;
    private int mCurPosition = 0;
    private boolean mbCheckIndex = false;
    private boolean mbCheckNew = false;

    public JiazhaoAdapter(Context context, boolean checkIndex){
        mContext = context;
        mbCheckIndex = checkIndex;
        mApplication = (KplusApplication) ((Activity)context).getApplication();
        mListJiazhao = mApplication.dbCache.getJiazhaos();
        mListJiazhaoAgainst = mApplication.dbCache.getJiazhaoAgainst();
    }

    public void setCheckNew(boolean checkNew){
        mbCheckNew = checkNew;
    }

    public void setJiazhaoList(List<Jiazhao> list){
        mListJiazhao = list;
    }

    public void setRefreshingList(List<String> list) {
        mListRefreshing = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_jiazhao_detail, parent, false);
        return new JiazhaoDetailViewHolder(v, mContext, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Jiazhao jiazhao = mListJiazhao.get(position);
        JiazhaoDetailViewHolder vh = (JiazhaoDetailViewHolder) holder;
        if (position == mListJiazhao.size() - 1){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.bottomMargin = BaseActivity.dip2px(mContext, 48);
            vh.itemView.setLayoutParams(params);
        }
        else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.bottomMargin = 0;
            vh.itemView.setLayoutParams(params);
        }
        if (vh.mViewCount != 0){
            vh.mLayout.removeViews(vh.mLayout.getChildCount() - vh.mViewCount, vh.mViewCount);
            vh.mViewCount = 0;
        }
        if (jiazhao.getXm() != null && !"".equals(jiazhao.getXm()))
            vh.mName.setText(jiazhao.getXm() + "的驾驶证");
        else {
            String jszh = jiazhao.getJszh();
            vh.mName.setText("证号：" + jszh.substring(0, 4) + "..." + jszh.substring(jszh.length() - 4));
        }
        if (!StringUtils.isEmpty(jiazhao.getZjcx())){
            vh.mType.setText("(" + jiazhao.getZjcx() + ")");
        }
        else {
            vh.mType.setText("");
        }
        if (jiazhao.getSpace() == 0) {
            vh.mShowIndex.setVisibility(View.VISIBLE);
            if (mbCheckIndex) {
                mbCheckIndex = false;
                onCheckIconClick((JiazhaoDetailViewHolder) holder, position);
            }
        }
        else
            vh.mShowIndex.setVisibility(View.GONE);
        if (mListRefreshing != null && mListRefreshing.contains(jiazhao.getId())){
            vh.mProgressLayout.setVisibility(View.GONE);
            vh.mRefreshingLayout.setVisibility(View.VISIBLE);
        }
        else {
            vh.mRefreshingLayout.setVisibility(View.GONE);
            vh.mProgressLayout.setVisibility(View.VISIBLE);
            vh.mScore.setText(String.valueOf(jiazhao.getLjjf()));
            if (jiazhao.getLjjf() > 12){
                vh.mProgress.setProgressColor(mContext.getResources().getColor(R.color.daze_darkred1));
                vh.mProgress.setProgressColor2(mContext.getResources().getColor(R.color.daze_darkred2));
            }
            else {
                vh.mProgress.setProgressColor(mContext.getResources().getColor(R.color.daze_orangered9));
                vh.mProgress.setProgressColor2(mContext.getResources().getColor(R.color.daze_orangered4));
            }
            if (jiazhao.getLjjf() > 0)
                vh.mProgress.setProgress((jiazhao.getLjjf() - 1) % 12 + 1);
            else
                vh.mProgress.setProgress(0);
        }
        if ("0".equals(jiazhao.getIsHidden())){
            vh.mRemindLabel.setVisibility(View.GONE);
            vh.mRemindBtn.setVisibility(View.GONE);
            vh.mUpdateTime.setVisibility(View.VISIBLE);
            final Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(jiazhao.getDate()));
                int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
                vh.mUpdateTime.setText("于" + jiazhao.getDate().replace("-", "/") + "更新（剩余" + gap + "天）");
                if (gap <= 7){
                    vh.mUpdateTime.setTextColor(mContext.getResources().getColor(R.color.daze_darkred2));
                    if (mListJiazhaoAgainst != null){
                        for (JiazhaoAgainst against : mListJiazhaoAgainst){
                            TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_jiazhao_against, vh.mLayout, false);
                            final String vehicleNum = against.getVehicleNum();
                            tv.setText("驾照分即将更新，你的车" + vehicleNum + "还有" + against.getTotal() + "条违章未处理");
                            vh.mLayout.addView(tv);
                            vh.mViewCount++;
                            ClickUtils.setNoFastClickListener(tv, new ClickUtils.NoFastClickListener() {
                                @Override
                                public void onNoFastClick(View v) {
                                    Intent intent = new Intent(mContext, VehicleDetailActivity.class);
                                    intent.putExtra("vehicleNumber", vehicleNum);
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                    }
                }
                else {
                    vh.mUpdateTime.setTextColor(mContext.getResources().getColor(R.color.daze_darkgrey9));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            vh.mRemindLabel.setVisibility(View.VISIBLE);
            vh.mRemindBtn.setVisibility(View.VISIBLE);
            vh.mUpdateTime.setVisibility(View.GONE);
        }
        if (mbCheckNew && position == mListJiazhao.size() - 1) {
            mbCheckNew = false;
            onCheckIconClick((JiazhaoDetailViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mListJiazhao != null ? mListJiazhao.size() : 0;
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.showIndex:
                EventAnalysisUtil.onEvent(mContext, "click_homeDisplay", "点击显示在首页", null);
                mPopupWindow.dismiss();
                showIndex(mCurPosition);
                break;
            case R.id.edit:
                mPopupWindow.dismiss();
                Intent intent = new Intent(mContext, JiazhaoEditActivity.class);
                intent.putExtra("jiazhao", mListJiazhao.get(mCurPosition));
                ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
                break;
        }
    }

    private void showPopup(View anchor){
        View layout = LayoutInflater.from(mContext).inflate(R.layout.popup_jiazhao_more, null, false);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.showIndex), this);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.edit), this);
        mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(anchor);
    }

    @Override
    public void onMoreIconClick(JiazhaoDetailViewHolder vh, int position) {
        mCurPosition = position;
        showPopup(vh.mMore);
    }

    @Override
    public void onRemindIconClick(JiazhaoDetailViewHolder vh, int position) {
        Intent intent = new Intent(mContext, JiazhaoEditActivity.class);
        intent.putExtra("jiazhao", mListJiazhao.get(position));
        intent.putExtra("remind", true);
        ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
    }

    @Override
    public void onCheckIconClick(JiazhaoDetailViewHolder vh, final int position) {
        EventAnalysisUtil.onEvent(mContext, "refresh_endorse", "驾照分查询页面刷新驾照分", null);
        final Jiazhao jiazhao = mListJiazhao.get(position);
        Intent it = new Intent("com.kplus.car.jiazhao.start");
        it.putExtra("id", jiazhao.getId());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
        it = new Intent(mContext, JiazhaoQueryScoreService.class);
        it.putExtra("jiazhao", jiazhao);
        mContext.startService(it);
    }

    private void showIndex(final int position){
        new JiazhaoUpdateSpaceTask(mApplication){
            @Override
            protected void onPostExecute(GetResultResponse response) {
                if (response != null && response.getCode() == 0){
                    for (int i = 0; i < mListJiazhao.size(); i++){
                        Jiazhao jiazhao = mListJiazhao.get(i);
                        if (i == position)
                            jiazhao.setSpace(0);
                        else
                            jiazhao.setSpace(-1);
                        mListJiazhao.set(i, jiazhao);
                    }
                    mApplication.dbCache.saveJiazhaos(mListJiazhao);
                    ((Activity)mContext).setResult(Constants.RESULT_TYPE_CHANGED);
                    notifyDataSetChanged();
                }
                else {
                    ToastUtil.makeShortToast(mContext, "设置为首页失败");
                }
            }
        }.execute(mListJiazhao.get(position).getId());
    }
}
