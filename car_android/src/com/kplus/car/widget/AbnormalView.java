package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.util.ClickUtils;

/**
 * Description：异常情况显示的界面
 * <br/><br/>Created by FU ZHIXUE on 2015/8/11.
 * <br/><br/>
 */
public class AbnormalView extends LinearLayout implements ClickUtils.NoFastClickListener {

    /**
     * 定位失败类型
     */
    public static final int ERROR_LOCATION_FAILED = 1;
    /**
     * 请求失败类型
     */
    public static final int ERROR_REQUEST_FAILED = 2;
    /**
     * 不支持城市类型
     */
    public static final int ERROR_NO_SUPPORT_CITY = 3;
    /**
     * 无网络类型
     */
    public static final int ERROR_NO_NETWORK = 4;
    /**
     * 自定义类型
     */
    public static final int ERROR_CUSTOM = 5;
    /**
     * 定位中
     */
    public static final int ERROR_POSITIONING = 6;
    /**
     * 失败类型
     */
    private int mErrorType = -1;

    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private ImageView ivErrorIcon = null;
    private TextView tvErrorTips = null;
    private TextView tvErrorSet = null;
    private TextView tvErrorCheck = null;

    public AbnormalView(Context context) {
        this(context, null);
    }

    public AbnormalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initView();
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.view_abnormal_layout, null, false);

        ivErrorIcon = findView(view, R.id.ivErrorIcon);
        tvErrorTips = findView(view, R.id.tvErrorTips);
        tvErrorSet = findView(view, R.id.tvErrorSet);
        tvErrorCheck = findView(view, R.id.tvErrorCheck);

        ClickUtils.setNoFastClickListener(tvErrorSet, this);
        ClickUtils.setNoFastClickListener(tvErrorCheck, this);

        this.removeAllViews();
        this.addView(view);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    protected String getStringResources(int id) {
        return mContext.getResources().getString(id);
    }

    public void setCustomAbnormalContent(int errorIconId, String errorTips, String errorBtnText, boolean isShowCheckNetwork) {
        mErrorType = ERROR_CUSTOM;
        setErrorText(errorIconId, errorTips, errorBtnText, (isShowCheckNetwork ? View.VISIBLE : View.GONE));
    }

    public void setAbnormalContent(int type) {
        mErrorType = type;
        if (type == -1) {
            return;
        }
        int errorIconId = 0;
        int errorTipsId = 0;
        int errorBtnTextId = 0;
        int visibility = View.GONE;

        switch (type) {
            case ERROR_LOCATION_FAILED:
                errorIconId = R.drawable.icon_location_failed;
                errorTipsId = R.string.error_location_failed_tips;
                errorBtnTextId = R.string.error_location_failed_btn;
                visibility = View.GONE;
                break;
            case ERROR_REQUEST_FAILED:
                errorIconId = R.drawable.icon_request_failed;
                errorTipsId = R.string.error_request_failed_tips;
                errorBtnTextId = R.string.error_request_failed_btn;
                visibility = View.VISIBLE;
                break;
            case ERROR_NO_SUPPORT_CITY:
                errorIconId = R.drawable.icon_no_support;
                errorTipsId = R.string.error_no_support_city_tips;
                errorBtnTextId = R.string.error_no_support_city_btn;
                visibility = View.GONE;
                break;
            case ERROR_NO_NETWORK:
                errorIconId = R.drawable.icon_no_network;
                errorTipsId = R.string.error_no_network_tips;
                errorBtnTextId = R.string.error_no_network_btn;
                visibility = View.GONE;
                break;
            case ERROR_POSITIONING:
                errorIconId = R.drawable.icon_posigioning;
                errorTipsId = R.string.error_positioning_tips;
                errorBtnTextId = R.string.error_positioning_btn;
                visibility = View.GONE;
                break;
        }
        if (errorIconId != 0) {
            setErrorText(errorIconId, getStringResources(errorTipsId), getStringResources(errorBtnTextId), visibility);
        }
    }

    private void setErrorText(int errorIconId, String errorTips, String errorBtnText, int gone) {
        ivErrorIcon.setImageResource(errorIconId);
        tvErrorTips.setText(errorTips);
        tvErrorSet.setText(errorBtnText);
        tvErrorCheck.setVisibility(gone);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.tvErrorSet: // 出现异常情况点击的按钮
                switch (mErrorType) {
                    case ERROR_LOCATION_FAILED: // 选择城市
                        if (null != mAbormalViewCallback) {
                            mAbormalViewCallback.onLocationFailed();
                        }
                        break;
                    case ERROR_REQUEST_FAILED: // 重新请求
                        if (null != mAbormalViewCallback) {
                            mAbormalViewCallback.onRequestFailed();
                        }
                        break;
                    case ERROR_NO_SUPPORT_CITY:
                        if (null != mAbormalViewCallback) {
                            mAbormalViewCallback.onNoSupportCity();
                        }
                        break;
                    case ERROR_NO_NETWORK:
                        if (null != mAbormalViewCallback) {
                            mAbormalViewCallback.onNoNetwork();
                        }
                        break;
                    case ERROR_CUSTOM:
                        if (null != mCustomAbormalViewCallback) {
                            mCustomAbormalViewCallback.onCustomAbormal();
                        }
                        break;
                    case ERROR_POSITIONING: // 定位中
                        if (null != mAbormalViewCallback) {
                            mAbormalViewCallback.onPositioning();
                        }
                        break;
                }
                break;
            case R.id.tvErrorCheck: // 点击检查网络设置
//                mContext.startActivity(new Intent(mContext, NoNetworkActivity.class););
                // 打开系统网络设置
                CNCommonManager.startWirelessSetting(mContext);
                break;
        }
    }

    private IAbormalViewCallback mAbormalViewCallback = null;

    public void setAbormalViewCallback(IAbormalViewCallback callback) {
        mAbormalViewCallback = callback;
    }

    private ICustomAbormalViewCallback mCustomAbormalViewCallback = null;

    public void setCustomAbormalViewCallback(ICustomAbormalViewCallback callback) {
        mCustomAbormalViewCallback = callback;
    }

    public interface IAbormalViewCallback {
        void onLocationFailed();

        void onRequestFailed();

        void onNoSupportCity();

        void onNoNetwork();

        void onPositioning();
    }

    public interface ICustomAbormalViewCallback {
        void onCustomAbormal();
    }
}
