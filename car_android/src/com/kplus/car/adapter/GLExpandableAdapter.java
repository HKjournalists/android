package com.kplus.car.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.model.CarService;
import com.kplus.car.model.CarServiceGroup;
import com.kplus.car.model.ServiceImg;
import com.kplus.car.model.ServiceImgList;
import com.kplus.car.model.ServiceImgPoint;
import com.kplus.car.util.CarServicesUtil;
import com.kplus.car.util.ServicesActionUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：汽车服务中下面其他服务的适配器
 * <br/><br/>Created by Fausgoal on 2015/9/2.
 * <br/><br/>
 */
public class GLExpandableAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final DisplayImageOptions mOptions;
    private final KplusApplication mApp;

    private List<CarServiceGroup> mGroups = null;
    private List<List<CarService>> mChildServices = null;
    private List<ServiceImgList> mServiceImgs = null;

    public GLExpandableAdapter(Context context, List<CarServiceGroup> groups, List<List<CarService>> childServices) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mApp = KplusApplication.getInstance();
        mGroups = (null == groups) ? new ArrayList<CarServiceGroup>() : groups;
        mChildServices = (null == childServices) ? new ArrayList<List<CarService>>() : childServices;

        // ImageLoader参数
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(0)
                .showImageForEmptyUri(0)
                .showImageOnFail(0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void clear() {
        if (null != mGroups) {
            mGroups.clear();
        }
        if (null != mChildServices) {
            mChildServices.clear();
        }
        notifyDataSetChanged();
    }

    public void appendGroups(List<CarServiceGroup> groups) {
        if (null != groups) {
            mGroups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    public void appendChild(List<List<CarService>> childServices) {
        if (null != childServices) {
            mChildServices.addAll(childServices);
            notifyDataSetChanged();
        }
    }

    public void setCarServiceImgs(List<ServiceImgList> groupList){
        if (groupList != null){
            mServiceImgs = groupList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ServiceImgList list = getGroupImgs(groupPosition);
        if (list != null)
            return list.getImgList().size();
        else
            return (null == mChildServices) ? 0 : mChildServices.get(groupPosition).size();
    }

    @Override
    public CarServiceGroup getGroup(int groupPosition) {
        return (null == mGroups) ? null : mGroups.get(groupPosition);
    }

    @Override
    public CarService getChild(int groupPosition, int childPosition) {
        return (null == mChildServices) ? null : mChildServices.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        GroupViewholder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.item_car_service_group_layout, parent, false);
            holder = new GroupViewholder(view);
            view.setTag(holder);
        } else {
            holder = (GroupViewholder) view.getTag();
        }
        holder.setValue(groupPosition, getGroup(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ChildViewholder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.view_other_service_item, parent, false);
            holder = new ChildViewholder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewholder) view.getTag();
        }
        holder.setValue(groupPosition, childPosition, getChild(groupPosition, childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewholder {
        private final View mView;
        private int mGroupPosition = 0;

        private final LinearLayout llServiceGroupTitle;
        private final TextView tvGroupTitle;
        private final ImageView ivMoreArror;

        public GroupViewholder(View view) {
            mView = view;
            llServiceGroupTitle = findView(R.id.llServiceGroupTitle);
            tvGroupTitle = findView(R.id.tvGroupTitle);
            ivMoreArror = findView(R.id.ivMoreArror);
        }

        public void setValue(int groupPosition, CarServiceGroup group) {
            mGroupPosition = groupPosition;

            // 是否显示分组名称
            boolean showName = (null == group.getShowName() ? false : group.getShowName());
            // 首页展示数 NOTICE 0 表示不限
            int indexServiceCount = (null == group.getIndexServiceCount() ? 0 : group.getIndexServiceCount());
            // 包含服务数
            int serviceCount = (null == group.getServiceCount() ? 0 : group.getServiceCount());

            if (!showName) {
                // 不显示分组名称时，要全部加载数据
                llServiceGroupTitle.setVisibility(View.GONE);
            } else {
                llServiceGroupTitle.setVisibility(View.VISIBLE);
                tvGroupTitle.setText(group.getName());

                // 0 表示不限，全部显示
                if (indexServiceCount == 0 || serviceCount <= indexServiceCount) {
                    ivMoreArror.setVisibility(View.GONE);
                } else {
                    // 显示时，可以点击整行
                    ivMoreArror.setVisibility(View.VISIBLE);
                }
            }
        }

        protected <T extends View> T findView(int id) {
            if (null == mView) {
                throw new IllegalArgumentException("view is not null!");
            }
            return (T) mView.findViewById(id);
        }
    }

    private class ChildViewholder {
        private final View mView;
        private int mGroupPosition;
        private int mChildPosition;


        private final ImageView ivServiceIcon;
        private final TextView tvServiceName;
        private final ImageView ivIconFlag;
        private final ImageView ivIconFavorableTag;
        private final FrameLayout flFavorableTag;
        private final TextView tvFavorableTag;
        private final TextView tvServiceDesc;
        private final View view_center_line;
        private final View serviceItem;
        private final ImageView ivServiceImg;

        public ChildViewholder(View view) {
            mView = view;

            ivServiceIcon = findView(R.id.ivServiceIcon);
            tvServiceName = findView(R.id.tvServiceName);
            ivIconFlag = findView(R.id.ivIconFlag);
            flFavorableTag = findView(R.id.flFavorableTag);
            ivIconFavorableTag = findView(R.id.ivIconFavorableTag);
            tvFavorableTag = findView(R.id.tvFavorableTag);

            tvServiceDesc = findView(R.id.tvServiceDesc);
            view_center_line = findView(R.id.view_center_line);
            serviceItem = findView(R.id.service_item);
            ivServiceImg = findView(R.id.service_img);
        }

        public void setValue(int groupPosition, int childPosition, final CarService carService) {
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;

            ServiceImgList list = getGroupImgs(groupPosition);
            if (list != null){
                serviceItem.setVisibility(View.GONE);
                ivServiceImg.setVisibility(View.VISIBLE);
                final ServiceImg serviceImg = list.getImgList().get(childPosition);
                mApp.imageLoader.displayImage(serviceImg.getImg_url(), ivServiceImg, mOptions);
                final float ratio = mApp.getnScreenWidth() / serviceImg.getWidth();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivServiceImg.getLayoutParams();
                params.height = (int) (serviceImg.getHeight() * ratio);
                ivServiceImg.setLayoutParams(params);
                ivServiceImg.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP && serviceImg.getPoints() != null){
                            int x = (int) event.getX();
                            int y = (int) event.getY();
                            for (ServiceImgPoint point : serviceImg.getPoints()){
                                int l = (int) (point.getX() * ratio);
                                int r = (int) ((point.getX() + point.getWidth()) * ratio);
                                int t = (int) (point.getY() * ratio);
                                int b = (int) ((point.getY() + point.getHeight()) * ratio);
                                if (x >= l && x <= r && y >= t && y <= b){
                                    CarService carService1 = new CarService();
                                    carService1.setName(point.getName());
                                    carService1.setMotionType(point.getMotion_type());
                                    carService1.setMotionValue(point.getMotion_value());
                                    carService1.setTransitionUrl(point.getTransition_url());
                                    CarServicesUtil.onClickCarServiceItem(mContext, carService1);
                                    break;
                                }
                            }
                        }
                        return true;
                    }
                });
            }
            else {
                serviceItem.setVisibility(View.VISIBLE);
                ivServiceImg.setVisibility(View.GONE);
                String url = carService.getListIcon();
                mApp.imageLoader.displayImage(url, ivServiceIcon, mOptions);

                tvServiceName.setText(carService.getName());
                tvServiceDesc.setText(carService.getInfo());

                // 设置推荐图标
                int resId = CarServicesUtil.getServiceFlag((null == carService.getFlag() ? 0 : carService.getFlag()));
                if (resId == 0) {
                    ivIconFlag.setVisibility(View.GONE);
                } else {
                    ivIconFlag.setVisibility(View.VISIBLE);
                    ivIconFlag.setImageResource(resId);
                }

                // 设置显示优惠标签
                String favorableTag = carService.getFavorableTag();
                if (TextUtils.isEmpty(favorableTag)) {
                    ivIconFavorableTag.setVisibility(View.GONE);
                    flFavorableTag.setVisibility(View.GONE);
                } else {
                    boolean hasHttp = CNStringUtil.hasHttpUrl(favorableTag);
                    if (hasHttp) {
                        ivIconFavorableTag.setVisibility(View.VISIBLE);
                        flFavorableTag.setVisibility(View.GONE);
                        mApp.imageLoader.displayImage(favorableTag, ivIconFavorableTag, mOptions);
                    } else {
                        ivIconFavorableTag.setVisibility(View.GONE);
                        flFavorableTag.setVisibility(View.VISIBLE);
                        tvFavorableTag.setText(favorableTag);
                    }
                }

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_center_line.getLayoutParams();
                if (mChildPosition == getChildrenCount(mGroupPosition) - 1) {
                    params.leftMargin = 0;
                    view_center_line.setLayoutParams(params);
                } else {
                    params.leftMargin = CNPixelsUtil.dip2px(mContext, 78);
                    view_center_line.setLayoutParams(params);
                }
            }
        }

        protected <T extends View> T findView(int id) {
            if (null == mView) {
                throw new IllegalArgumentException("view is not null!");
            }
            return (T) mView.findViewById(id);
        }
    }

    private ServiceImgList getGroupImgs(int groupPosition){
        CarServiceGroup group = getGroup(groupPosition);
        if (group != null && mServiceImgs != null){
            for (ServiceImgList list : mServiceImgs){
                if (list.getImgList() != null && list.getImgList().size() > 0){
                    long id1 = list.getImgList().get(0).getService_group_id() != null ? list.getImgList().get(0).getService_group_id() : 0;
                    long id2 = group.getId() != null ? group.getId() : 0;
                    if (id1 == id2){
                        return list;
                    }
                }
            }
        }
        return null;
    }
}
