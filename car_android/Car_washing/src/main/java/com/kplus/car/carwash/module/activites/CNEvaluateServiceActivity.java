package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.Review;
import com.kplus.car.carwash.bean.ServiceOrder;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.OrderStatus;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNNumberUtils;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestHelper;
import com.kplus.car.carwash.widget.CNNavigationBar;
import com.kplus.car.carwash.widget.CNStarGradeLinearlayout;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * 评价界面
 * Created by Fu on 2015/5/12.
 */
public class CNEvaluateServiceActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener {
    private static final String TAG = "CNEvaluateServiceActivity";

    private View mView = null;
    private TextView tvStarsGrade = null;
    private EditText etEvaluateContent = null;

    /**
     * 选择星星等级
     */
    private int mRating = Const.NONE;

    private ServiceOrder mServiceOrder = null;
    private ArrayList<String> mServicePicUrls = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mServiceOrder = (ServiceOrder) bundle.get(CNCarWashingLogic.KEY_ORDER_DETAILS);
            mServicePicUrls = bundle.getStringArrayList(CNCarWashingLogic.KEY_SHARE_IMAGES);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_evaluate_service_layout, null);
        CNStarGradeLinearlayout sgStarView = findView(mView, R.id.sgStarView);
        tvStarsGrade = findView(mView, R.id.tvStarsGrade);
        etEvaluateContent = findView(mView, R.id.etEvaluateContent);
        Button btnSubmitEvaluate = findView(mView, R.id.btnSubmitEvaluate);

        // 初始化star
        sgStarView.setStarOnResId(R.drawable.cn_icon_star_on);
        sgStarView.setStarOffResId(R.drawable.cn_icon_star_off);
        sgStarView.setIsClicStar(true);
        int wh = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_starWH);
        sgStarView.initStar(wh, wh);
        sgStarView.setRating(5);
        sgStarView.setStarRatingChangeListener(new CNStarGradeLinearlayout.IStarRatingChangeListener() {
            @Override
            public void onStarRatingChanged(int rating) {
                // 每次点击星星时，改变分数
                mRating = CNNumberUtils.floatToInt(rating);
                tvStarsGrade.setText(String.valueOf(mRating));
            }
        });
        // 设置默认的
        mRating = CNNumberUtils.floatToInt(sgStarView.getRating());
        tvStarsGrade.setText(String.valueOf(mRating));

        CNViewClickUtil.setNoFastClickListener(btnSubmitEvaluate, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 评价服务
        navBar.setNavTitle(getStringResources(R.string.cn_service_evalute_title));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
//        // 刷新按钮
//        navBar.setRightBtn("", R.drawable.btn_refresh_selector, Const.NONE);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            CNViewManager.getIns().pop(this);
        } else if (viewId == R.id.tvNavRight) {
            CNCommonManager.makeText(mContext, "刷新中");
        }
        return true;
    }

    @Override
    public void onNoFastClick(View v) {
        int vId = v.getId();
        if (vId == R.id.btnSubmitEvaluate) {
            if (mRating == Const.NONE) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_please_selected_rating));
            } else if (CNStringUtil.isEmpty(etEvaluateContent.getText().toString().trim())) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_please_entry_content));
            } else {
                submitReview();
            }
        }
    }

    private void submitReview() {
        if (null != mServiceOrder) {
            Log.trace(TAG, "状态：" + mServiceOrder.getStatus());
            final String content = etEvaluateContent.getText().toString().trim();

            HttpRequestHelper.submitReview(mContext, mServiceOrder.getId(), mRating, content, new ApiHandler(mContext) {
                @Override
                public void onSuccess(BaseInfo baseInfo) {
                    super.onSuccess(baseInfo);
                    if (null != baseInfo) {
                        if (CNStringUtil.isEmpty(baseInfo.getMsg())) {
                            CNCommonManager.makeText(mContext, getStringResources(R.string.cn_evaluate_success));
                            Intent data = new Intent();
                            // 设置状态
                            mServiceOrder.setStatus(OrderStatus.REVIEWED.value());
                            // 评论内容
                            long uid = AppBridgeUtils.getIns().getUid();

                            Review review = new Review();
                            review.setUid(uid);
                            review.setContent(content);
                            review.setRank(mRating);
                            review.setUserName(mServiceOrder.getContact().getName());
                            review.setCreateTime(new Timestamp(System.currentTimeMillis()));
                            // 设置评论
                            mServiceOrder.setReview(review);

                            data.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS, mServiceOrder);
                            setResult(Activity.RESULT_OK, data);

                            // 打开分享界面
                            Intent shareData = new Intent(mContext, CNShareActivity.class);
                            shareData.putExtra(CNCarWashingLogic.KEY_SHARE_TYPE, CNCarWashingLogic.SHARE_TYPE_ORDERLOG);
                            shareData.putExtra(CNCarWashingLogic.KEY_SHARE_IMAGES, mServicePicUrls);
                            startActivityForResult(shareData, CNCarWashingLogic.TYPE_SHARE_IMAGES_CODE);
                        } else {
                            CNCommonManager.makeText(mContext, baseInfo.getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(BaseInfo baseInfo) {
                    super.onFailure(baseInfo);
                    if (null != baseInfo) {
                        Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                        CNCommonManager.makeText(mContext, baseInfo.getMsg());
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CNCarWashingLogic.TYPE_SHARE_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                CNViewManager.getIns().pop(CNEvaluateServiceActivity.this);
            }
        }
    }
}
