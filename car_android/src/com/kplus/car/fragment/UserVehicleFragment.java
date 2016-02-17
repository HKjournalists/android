package com.kplus.car.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.BaseActivity;
import com.kplus.car.activity.HomeNewActivity;
import com.kplus.car.activity.VehicleInfoActivity;
import com.kplus.car.adapter.SpaceItemDecoration;
import com.kplus.car.adapter.VehicleInfoAdapter;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.PullRefreshRecyclerView;
import com.kplus.car.widget.RefreshLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserVehicleFragment extends Fragment implements ClickUtils.NoFastClickListener, RefreshLayout.OnRefreshListener {
    private static final String USER_VEHICLE = "user_vehicle";
    private static final String VEHICLE_AUTH = "vehicle_auth";
    private UserVehicle mUserVehicle;
    private VehicleAuth mVehicleAuth;
    private RecyclerView mRecyclerView;
    private VehicleInfoAdapter mAdapter;
    private TextView mVehicleModel;
    private ImageView mIvAuth;
    private ImageView mIvXianxing;
    private TextView mVehicleNum;
    private View mMask;
    private int mScrollY = 0;
    private int mTotal = 0;
    private boolean mbRestrict = false;
    private List<String> mListJiazhaoRefreshing;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userVehicle UserVehicle.
     * @return A new instance of fragment UserVehicleFragment.
     */
    public static UserVehicleFragment newInstance(UserVehicle userVehicle, VehicleAuth vehicleAuth) {
        UserVehicleFragment fragment = new UserVehicleFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_VEHICLE, userVehicle);
        args.putSerializable(VEHICLE_AUTH, vehicleAuth);
        fragment.setArguments(args);
        return fragment;
    }

    public UserVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserVehicle = (UserVehicle) getArguments().getSerializable(USER_VEHICLE);
            mVehicleAuth = (VehicleAuth) getArguments().getSerializable(VEHICLE_AUTH);
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(insuranceReceiver, new IntentFilter("com.kplus.car.set_insurance"));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(insuranceReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_vehicle, container, false);
        mMask = layout.findViewById(R.id.mask);
        mVehicleModel = (TextView) layout.findViewById(R.id.vehicle_model);
        mIvAuth = (ImageView) layout.findViewById(R.id.vehicle_auth);
        mIvXianxing = (ImageView) layout.findViewById(R.id.vehicle_xianxing);
        mVehicleNum = (TextView) layout.findViewById(R.id.vehicle_num);
        mVehicleNum.setText(mUserVehicle.getVehicleNum());
        mVehicleNum.setTypeface(((KplusApplication)getActivity().getApplication()).mDin);
        ClickUtils.setNoFastClickListener(mVehicleNum, this);
        mTotal = BaseActivity.dip2px(getActivity(), 112);
        mScrollY = 0;
        PullRefreshRecyclerView pullRefreshRecyclerView = (PullRefreshRecyclerView) layout.findViewById(R.id.recycler_view);
        pullRefreshRecyclerView.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) pullRefreshRecyclerView.getRefreshView();
        int space = BaseActivity.dip2px(getActivity(), 3);
        mRecyclerView.setPadding(space, 0, space, space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(space));
        mAdapter = new VehicleInfoAdapter(getActivity(), mUserVehicle, mVehicleAuth, mListJiazhaoRefreshing);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY += dy;
                onScroll();
            }
        });
        updateRestrictNum();
        return layout;
    }

    public void resetScroller(){
        mScrollY = 0;
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
            onScroll();
        }
    }

    public void updateRestrictNum(){
        mbRestrict = false;
        if (mUserVehicle != null && !StringUtils.isEmpty(mUserVehicle.getRestrictNum())){
            String[] arr = mUserVehicle.getRestrictNum().split(",");
            String lastNum = "";
            if(!StringUtils.isEmpty(mUserVehicle.getVehicleNum())) {
                for (int i = mUserVehicle.getVehicleNum().length() - 1; i >= 0; i--) {
                    char c = mUserVehicle.getVehicleNum().charAt(i);
                    if (c >= '0' && c <= '9') {
                        lastNum = String.valueOf(c);
                        break;
                    }
                }
                for (String str : arr) {
                    if (lastNum.equals(str)) {
                        mbRestrict = true;
                        break;
                    }
                }
            }
        }
        if (isAdded())
            updateUI();
    }

    public String getVehicleNum(){
        return mUserVehicle.getVehicleNum();
    }

    private void updateUI(){
        if (mUserVehicle == null || mUserVehicle.getModelName() == null || "".equals(mUserVehicle.getModelName()))
            mVehicleModel.setVisibility(View.GONE);
        else {
            mVehicleModel.setVisibility(View.VISIBLE);
            mVehicleModel.setText(mUserVehicle.getModelName());
        }
        if (mIvAuth != null){
            if (mVehicleAuth != null && mVehicleAuth.getStatus() == 2)
                mIvAuth.setImageResource(R.drawable.ver_activate);
            else
                mIvAuth.setImageResource(R.drawable.ver_deactivate);
        }
        if (mIvXianxing != null){
            if (mbRestrict)
                mIvXianxing.setImageResource(R.drawable.not_restricted);
            else
                mIvXianxing.setImageResource(R.drawable.not_unrestricted);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED)
            return;
        switch (requestCode){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                mAdapter.setJiazhaoRefreshingList(mListJiazhaoRefreshing);
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
            case Constants.REQUEST_TYPE_CHEXIAN:
            case Constants.REQUEST_TYPE_BAOYANG:
            case Constants.REQUEST_TYPE_BAOYANG_RECORD:
            case Constants.REQUEST_TYPE_CHEDAI:
            case Constants.REQUEST_TYPE_CUSTOM:
                int position = data.getIntExtra("position", 0);
                switch (resultCode){
                    case Constants.RESULT_TYPE_CHANGED:
                        if (requestCode == Constants.REQUEST_TYPE_NIANJIAN)
                            mUserVehicle = (UserVehicle) data.getSerializableExtra("vehicle");
                        mAdapter.notifyItemChanged(position);
                        break;
                    case Constants.RESULT_TYPE_REMOVED:
                        mAdapter.closeRemind(position);
                        mAdapter.notifyDataSetChanged();
                        resetScroller();
                        break;
                    case Constants.RESULT_TYPE_RELOAD:
                        mAdapter.reloadRemindInfo();
                        mAdapter.notifyItemChanged(position);
                        break;
                }
                break;
            case Constants.REQUEST_TYPE_VEHICLE:
                mUserVehicle = (UserVehicle) data.getSerializableExtra("vehicle");
                updateUI();
                mAdapter.setUserVehicle(mUserVehicle);
                mAdapter.notifyDataSetChanged();
                break;
            case Constants.REQUEST_TYPE_REMIND:
                mAdapter.reloadRemindInfo();
                mRecyclerView.setAdapter(mAdapter);
                resetScroller();
                break;
            case Constants.REQUEST_TYPE_INSURANCE:
                position = data.getIntExtra("position", 0);
                String company = data.getStringExtra("company");
                String phone = data.getStringExtra("phone");
                mUserVehicle.setCompany(company);
                mUserVehicle.setPhone(phone);
                mAdapter.setInsurance(company, phone);
                mAdapter.notifyItemChanged(position);
                break;
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.vehicle_num:
                EventAnalysisUtil.onEvent(getActivity(), "editCar_fromIndexVehicle", "点击车牌号码进入车辆编辑页", null);
                Intent intent = new Intent();
                intent.setClass(getActivity(), VehicleInfoActivity.class);
                intent.putExtra("vehicle", mUserVehicle);
                intent.putExtra("auth", mVehicleAuth);
                getActivity().startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
                break;
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        resetScroller();
    }

    @Override
    public void onPullDown(int y) {
        HomeNewActivity homeNewActivity = (HomeNewActivity)getActivity();
        if (homeNewActivity != null)
            homeNewActivity.onPullDown(y);
    }

    private void onScroll(){
        ViewCompat.setTranslationY(mVehicleModel, -mScrollY / 2);
        ViewCompat.setTranslationY(mIvAuth, -mScrollY / 2);
        ViewCompat.setTranslationY(mIvXianxing, -mScrollY / 2);
        float percent = mScrollY > mTotal / 2.5f ? 1 : (float) mScrollY / mTotal * 2.5f;
        ViewCompat.setAlpha(mVehicleModel, 1 - percent);
        ViewCompat.setAlpha(mIvAuth, 1 - percent);
        ViewCompat.setAlpha(mIvXianxing, 1 - percent);
        percent = mScrollY > mTotal * 1.6f ? 0.8f : (float) (mScrollY - mTotal) / mTotal * 1.5f;
        if (percent < 0)
            percent = 0;
        ViewCompat.setAlpha(mMask, percent);
        percent = mScrollY > mTotal ? 1 : (float) mScrollY / mTotal;
        ViewCompat.setTranslationY(mVehicleNum, mScrollY > mTotal ? -mTotal / 2 : -mScrollY / 2);
        ViewCompat.setTranslationX(mVehicleNum, percent < 0.35f ? 0 : mTotal * (percent - 0.35f) * 1.3f);
        ViewCompat.setAlpha(mVehicleNum, 0.6f + Math.abs(percent - 0.5f) * 0.8f);
        if (percent > 0.65f)
            percent = 0.65f;
        ViewCompat.setScaleX(mVehicleNum, percent < 0.35f ? 1 : 1 - (percent - 0.35f) / 0.7f);
        ViewCompat.setScaleY(mVehicleNum, percent < 0.35f ? 1 : 1 - (percent - 0.35f) / 0.7f);
        ((HomeNewActivity) getActivity()).onScrolled(mScrollY);
    }

    public void setAdverts(){
        if (mAdapter != null)
            mAdapter.setAdverts();
    }

    public void setIsRefreshingWeizhang(boolean b){
        if (mAdapter != null)
            mAdapter.setIsRefreshingWeizhang(b);
    }

    public void setJiazhaoRefreshingList(List<String> list){
        mListJiazhaoRefreshing = list;
        if (mAdapter != null)
            mAdapter.setJiazhaoRefreshingList(list);
    }

    public void setUserVehicle(UserVehicle uv){
        mUserVehicle = uv;
    }

    private BroadcastReceiver insuranceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UserVehicle userVehicle = (UserVehicle) intent.getSerializableExtra("vehicle");
            if (userVehicle.getVehicleNum().equals(mUserVehicle.getVehicleNum())){
                mUserVehicle = userVehicle;
                mAdapter.setUserVehicle(userVehicle);
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
