package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.JiazhaoEditActivity;
import com.kplus.car.activity.JiazhaoListActivity;
import com.kplus.car.activity.VehicleDetailActivity;
import com.kplus.car.asynctask.JiazhaoSaveTask;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.JiazhaoAgainst;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/5/22.
 */
public class JiazhaoViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private List<Jiazhao> mListJiazhao;
    private Context mContext;
    private KplusApplication mApp;
    private LinearLayout mLayout;
    private PopupWindow mRulePopup;
    private View mAddLayout;
    private View mJiazhaoLayout;
    private View mRuleLayout;
    private View mRemindLayout;
    private TextView mUpdateTime;
    private TextView mName;
    private TextView mDesc;
    private TextView mScore;
    private View mFen;
    private View mRefreshing;
    private ImageView mRefreshBtn;
    private List<JiazhaoAgainst> mListJiazhaoAgainst;
    private List<String> mRefreshingList;
    private int mViewCount = 0;

    public JiazhaoViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mLayout = (LinearLayout) itemView.findViewById(R.id.layout);
        mAddLayout = itemView.findViewById(R.id.add_layout);
        mJiazhaoLayout = itemView.findViewById(R.id.jiazhao_layout);
        mRuleLayout = itemView.findViewById(R.id.rule_layout);
        mRemindLayout = itemView.findViewById(R.id.remind_layout);
        mUpdateTime = (TextView) itemView.findViewById(R.id.update_time);
        mName = (TextView) itemView.findViewById(R.id.name);
        mDesc = (TextView) itemView.findViewById(R.id.desc);
        mScore = (TextView) itemView.findViewById(R.id.score);
        mScore.setTypeface(mApp.mDin);
        mFen = itemView.findViewById(R.id.fen);
        mRefreshing = itemView.findViewById(R.id.refreshing);
        mRefreshBtn = (ImageView) itemView.findViewById(R.id.refresh_jiazhao);
        ClickUtils.setNoFastClickListener(mLayout, this);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.rule), this);
        ClickUtils.setNoFastClickListener(mRefreshBtn, this);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.remind_btn), this);
    }

    public void bind(List<String> refreshingList){
        mRefreshingList = refreshingList;
        mListJiazhao = mApp.dbCache.getJiazhaos();
        mListJiazhaoAgainst = mApp.dbCache.getJiazhaoAgainst();
        if (mListJiazhao != null){
            for (Jiazhao jiazhao : mListJiazhao){
                if (!"0".equals(jiazhao.getIsHidden()))
                    continue;
                Calendar oldCal = Calendar.getInstance();
                try {
                    oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(jiazhao.getDate()));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                if (oldCal.before(cal)){
                    cal.setTime(oldCal.getTime());
                    cal.add(Calendar.YEAR, 1);
                    jiazhao.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                    int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                    try {
                        cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jiazhao.getRemindDate()));
                        cal.add(Calendar.DATE, gap);
                        jiazhao.setRemindDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mApp.dbCache.updateJiazhao(jiazhao);
                    RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_JIAZHAOFEN, jiazhao.getId());
                    if (mApp.getId() != 0)
                        new JiazhaoSaveTask(mApp).execute(jiazhao);
                }
            }
        }
        updateUI();
    }

    private void updateUI(){
        if (mViewCount != 0){
            mLayout.removeViews(mLayout.getChildCount() - mViewCount, mViewCount);
            mViewCount = 0;
        }
        if (mListJiazhao == null){
            mAddLayout.setVisibility(View.VISIBLE);
            mJiazhaoLayout.setVisibility(View.GONE);
            mRuleLayout.setVisibility(View.VISIBLE);
            mRemindLayout.setVisibility(View.GONE);
            mDesc.setVisibility(View.GONE);
        }
        else {
            mAddLayout.setVisibility(View.GONE);
            mJiazhaoLayout.setVisibility(View.VISIBLE);
            mDesc.setVisibility(View.VISIBLE);
            mDesc.setText("共" + mListJiazhao.size() + "本");
            boolean bJiazhaoUpdate = false;
            for (Jiazhao jiazhao : mListJiazhao){
                if (jiazhao.getSpace() == 0){
                    if (jiazhao.getXm() != null && !"".equals(jiazhao.getXm())){
                        mName.setText(jiazhao.getXm() + "的驾驶证");
                    }
                    else {
                        String jszh = jiazhao.getJszh();
                        mName.setText("尾号" + jszh.substring(jszh.length() - 4) + "的驾驶证");
                    }
                    if ("0".equals(jiazhao.getIsHidden())){
                        mRuleLayout.setVisibility(View.GONE);
                        mRemindLayout.setVisibility(View.VISIBLE);
                        Calendar calendar = Calendar.getInstance();
                        try {
                            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(jiazhao.getDate()));
                            int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
                            mUpdateTime.setText(jiazhao.getDate().replace("-", "/") + "（剩余" + gap + "天）");
                            if (gap <= 7){
                                bJiazhaoUpdate = true;
                                mUpdateTime.setTextColor(mContext.getResources().getColor(R.color.daze_darkred2));
                            }
                            else {
                                mUpdateTime.setTextColor(mContext.getResources().getColor(R.color.daze_darkgrey9));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        mRuleLayout.setVisibility(View.VISIBLE);
                        mRemindLayout.setVisibility(View.GONE);
                    }
                    if (mRefreshingList != null && mRefreshingList.contains(jiazhao.getId())){
                        mRefreshing.setVisibility(View.VISIBLE);
                        mScore.setVisibility(View.INVISIBLE);
                        mFen.setVisibility(View.GONE);
                        RotateAnimation anim = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        anim.setInterpolator(new LinearInterpolator());
                        anim.setDuration(1000);
                        anim.setRepeatCount(Animation.INFINITE);
                        mRefreshBtn.startAnimation(anim);
                    }
                    else {
                        mRefreshing.setVisibility(View.GONE);
                        mScore.setVisibility(View.VISIBLE);
                        mFen.setVisibility(View.VISIBLE);
                        mScore.setText(String.valueOf(jiazhao.getLjjf()));
                        mRefreshBtn.clearAnimation();
                    }
                }
                else if ("0".equals(jiazhao.getIsHidden())){
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(jiazhao.getDate()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
                    if (gap <= 7){
                        bJiazhaoUpdate = true;
                        View updateItem = LayoutInflater.from(mContext).inflate(R.layout.item_jiazhao_update, mLayout, false);
                        TextView needUpdate = (TextView) updateItem.findViewById(R.id.need_update);
                        if (jiazhao.getXm() != null && !"".equals(jiazhao.getXm()))
                            needUpdate.setText(jiazhao.getXm() + "的驾照分即将更新（剩余" + gap + "天）");
                        else {
                            String jszh = jiazhao.getJszh();
                            needUpdate.setText("尾号" + jszh.substring(jszh.length() - 4) + "的驾照分即将更新（剩余" + gap + "天）");
                        }
                        mLayout.addView(updateItem);
                        mViewCount++;
                    }
                }
            }
            if (bJiazhaoUpdate && mListJiazhaoAgainst != null){
                for (JiazhaoAgainst against : mListJiazhaoAgainst){
                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_jiazhao_against, mLayout, false);
                    final String vehicleNum = against.getVehicleNum();
                    tv.setText("驾照分即将更新，你的车" + vehicleNum + "还有" + against.getTotal() + "条违章未处理");
                    mLayout.addView(tv);
                    mViewCount++;
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
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.refresh_jiazhao:
                EventAnalysisUtil.onEvent(mContext, "refresh_endorse_Home", "我的车页面刷新驾照分", null);
                if (mRefreshing.getVisibility() == View.VISIBLE)
                    return;
                if(mApp.isUserLogin(true, "驾照分查询需要绑定手机号")) {
                    Intent intent = new Intent(mContext, JiazhaoListActivity.class);
                    intent.putExtra("check", true);
                    intent.putStringArrayListExtra("refreshingList", (ArrayList<String>) mRefreshingList);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
                }
                break;
            case R.id.layout:
                if(mApp.isUserLogin(true, "驾照分查询需要绑定手机号")) {
                    Intent intent = new Intent(mContext, JiazhaoListActivity.class);
                    if (mListJiazhao == null) {
                        EventAnalysisUtil.onEvent(mContext, "addEndorse_home", "首页点添加驾驶证，立即查询", null);
                        intent.putExtra("add", true);
                    }
                    intent.putStringArrayListExtra("refreshingList", (ArrayList<String>) mRefreshingList);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
                }
                break;
            case R.id.rule:
                showRulePopup();
                break;
            case R.id.close_rule:
                mRulePopup.dismiss();
                break;
            case R.id.remind_btn:
                if(mApp.isUserLogin(true, "驾照分查询需要绑定手机号")) {
                    if (mListJiazhao != null){
                        for (Jiazhao jiazhao : mListJiazhao){
                            if (jiazhao.getSpace() == 0){
                                Intent intent = new Intent(mContext, JiazhaoEditActivity.class);
                                intent.putExtra("jiazhao", jiazhao);
                                intent.putExtra("remind", true);
                                ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
                            }
                        }
                    }
                    else {
                        onNoFastClick(mLayout);
                    }
                }
                break;
        }
    }

    private void showRulePopup(){
        View layout = LayoutInflater.from(mContext).inflate(R.layout.popup_rule, null, false);
        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText("驾照分更新规则");
        TextView content = (TextView) layout.findViewById(R.id.content);
        content.setText(mContext.getString(R.string.jiazhao_rule));
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.close_rule), this);
        mRulePopup = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRulePopup.showAtLocation(mLayout.getRootView(), Gravity.LEFT | Gravity.TOP, 0, 0);
    }
}
