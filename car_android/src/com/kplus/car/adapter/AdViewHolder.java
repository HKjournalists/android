package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.Advert;
import com.kplus.car.util.AdvertUtil;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.widget.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/3/15.
 */
public class AdViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    public SharedPreferences sp;
    private ArrayList<Advert> adverts;
    private KplusApplication mApp;
    private DisplayImageOptions optionsPhoto;
    private View rlAD;
    private ViewPager vpAD;
    private CirclePageIndicator cpiAD;
    private ImageView ivCloseAd;
    private List<ImageView> advertImages;
    private AadvertPagerAdapter advertAdapter;
    private String type;
    private int nScreenWidth;
    private float fScale;
    private ADRemoveListener adRemoveListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                int nNext = (vpAD.getCurrentItem() + 1)%vpAD.getChildCount();
                cpiAD.setCurrentItem(nNext);
                vpAD.setCurrentItem(nNext);
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessageDelayed(message,5000);
            }
        }
    };

    public AdViewHolder(View itemView, Context context, final String type, ADRemoveListener adRemoveListener) {
        super(itemView);
        mContext = context;
        nScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        this.type = type;
        if(type.equals(KplusConstants.ADVERT_VEHICLE_BODY))
            fScale = ((float)320)/56;
        else if(type.equals(KplusConstants.ADVERT_VEHICLE_DETAIL_HEAD))
            fScale = 10;
        else if(type.equals(KplusConstants.ADVERT_USER_BODY))
            fScale = ((float)320)/56;
        else if(type.equals(KplusConstants.ADVERT_HOME))
            fScale = ((float)320)/56;
        this.adRemoveListener = adRemoveListener;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        optionsPhoto = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        rlAD = itemView.findViewById(R.id.rlAD);
        vpAD = (ViewPager) itemView.findViewById(R.id.vpAD);
        cpiAD = (CirclePageIndicator) itemView.findViewById(R.id.cpiAD);
        cpiAD.setVisibility(View.GONE);
        ivCloseAd = (ImageView) itemView.findViewById(R.id.ivCloseAd);
        ivCloseAd.setVisibility(View.GONE);
        ClickUtils.setNoFastClickListener(ivCloseAd, new ClickUtils.NoFastClickListener(){
            @Override
            public void onNoFastClick(View v) {
                if(type.equals(KplusConstants.ADVERT_VEHICLE_BODY))
                    mApp.setVehicleBodyAdvert(null);
                else if(type.equals(KplusConstants.ADVERT_VEHICLE_DETAIL_HEAD))
                    mApp.setVehicleDetailHeadAdvert(null);
                else if(type.equals(KplusConstants.ADVERT_USER_BODY))
                    mApp.setUserBodyAdvert(null);
                else if(type.equals(KplusConstants.ADVERT_HOME))
                    mApp.setHomeAdvert(null);
                AdViewHolder.this.adRemoveListener.onADRemoved();
            }
        });
    }

    public void bind(ArrayList<Advert> adverts){
        this.adverts = adverts;
        if (adverts != null && !adverts.isEmpty()) {
            updateAdverts();
        }
    }

    private void updateAdverts(){
        Collections.sort(adverts, new AdvertComparator());
        try {
            RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams)vpAD.getLayoutParams();
            rllp.width = nScreenWidth;
            rllp.height = (int)(nScreenWidth/fScale);
            vpAD.setLayoutParams(rllp);
            if(advertImages == null)
                advertImages = new ArrayList<ImageView>();
            else
                advertImages.clear();
            for (int i = 0; i < adverts.size(); i++) {
                ImageView imageItem = new ImageView(mContext);
                imageItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                imageItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Advert advert = adverts.get(i);
                mApp.imageLoader.displayImage(advert.getImgUrl(), imageItem, optionsPhoto);
                imageItem.setTag(advert);
                ClickUtils.setNoFastClickListener(imageItem, new ClickUtils.NoFastClickListener() {
                    public void onNoFastClick(View v) {
                        EventAnalysisUtil.onEvent(mContext, "click_banner_myCarMiddle", "我的车中部广告位被点击", null);
                        Advert advertInfo = (Advert) v.getTag();
                        AdvertUtil au = new AdvertUtil(mContext, advertInfo, type);
                        au.OnAdvertClick();
                    }
                });
                advertImages.add(imageItem);
            }
            advertAdapter = new AadvertPagerAdapter(advertImages);
            vpAD.setAdapter(advertAdapter);
            cpiAD.setViewPager(vpAD);
            if(adverts.size() == 1){
                cpiAD.setVisibility(View.GONE);
                if(adverts.get(0) != null && adverts.get(0).getCanClose() != null && adverts.get(0).getCanClose()){
                    ivCloseAd.setVisibility(View.VISIBLE);
                }
                else{
                    ivCloseAd.setVisibility(View.GONE);
                }
            }
            else if(adverts.size() > 1){
                cpiAD.setVisibility(View.VISIBLE);
                ivCloseAd.setVisibility(View.GONE);
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessageDelayed(message, 5000);
            }
            cpiAD.setViewPager(vpAD);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class AdvertComparator implements Comparator<Advert>{
        @Override
        public int compare(Advert lhs, Advert rhs) {
            int result = 0;
            try{
                result = (lhs.getSeq().intValue() - rhs.getSeq().intValue());
            }catch (Exception e){
                e.printStackTrace();
                result = 0;
            }
            return result;
        }
    }

    private class AadvertPagerAdapter extends PagerAdapter {

        private List<ImageView> views;

        public AadvertPagerAdapter(List<ImageView> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public interface ADRemoveListener{
        public void onADRemoved();
    }
}
