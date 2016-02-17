package com.kplus.car.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.Advert;
import com.kplus.car.util.AdvertUtil;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.widget.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AdvertFragment extends Fragment {
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
    private ArrayList<Advert> adverts;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    int nNext = (vpAD.getCurrentItem() + 1) % adverts.size();
                    cpiAD.setCurrentItem(nNext);
                    vpAD.setCurrentItem(nNext);
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessageDelayed(message, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.removeMessages(1);
                }
            }
        }
    };

    public static AdvertFragment newInstance(String type) {
        AdvertFragment fragment = new AdvertFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public AdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = (KplusApplication) getActivity().getApplication();
        nScreenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
        if (type.equals(KplusConstants.ADVERT_VEHICLE_BODY)) {
            adverts = mApp.getVehicleBodyAdvert();
            fScale = ((float) 320) / 56;
        } else if (type.equals(KplusConstants.ADVERT_VEHICLE_DETAIL_HEAD)) {
            adverts = mApp.getVehicleDetailHeadAdvert();
            fScale = 10;
        } else if (type.equals(KplusConstants.ADVERT_USER_BODY)) {
            adverts = mApp.getUserBodyAdvert();
            fScale = ((float) 320) / 56;
        } else if (type.equals(KplusConstants.ADVERT_HOME)) {
            adverts = mApp.getHomeAdvert();
//            fScale = ((float) 320) / 56;
        } else if (KplusConstants.ADVERT_NEW_USER.equals(type)) {
            adverts = (ArrayList<Advert>) mApp.getNewUserAdvert();
        } else if (KplusConstants.ADVERT_SERVICE_HEAD_NEW.equals(type)) {
            // 汽车服务广告
            adverts = (ArrayList<Advert>) mApp.getServiceHeadAdvert();
            fScale = ((float) 320) / 100;
        }

        optionsPhoto = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        View layout = inflater.inflate(R.layout.daze_advert_fragment, container, false);
        rlAD = layout.findViewById(R.id.rlAD);
        vpAD = (ViewPager) layout.findViewById(R.id.vpAD);
        cpiAD = (CirclePageIndicator) layout.findViewById(R.id.cpiAD);
        cpiAD.setVisibility(View.GONE);
        ivCloseAd = (ImageView) layout.findViewById(R.id.ivCloseAd);
        ivCloseAd.setVisibility(View.GONE);
        ClickUtils.setNoFastClickListener(ivCloseAd, new ClickUtils.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                try {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(0, R.anim.slide_out_to_top);
                    ft.remove(AdvertFragment.this).commit();
                    switch (type) {
                        case KplusConstants.ADVERT_VEHICLE_BODY:
                            mApp.setVehicleBodyAdvert(null);
                            break;
                        case KplusConstants.ADVERT_VEHICLE_DETAIL_HEAD:
                            mApp.setVehicleDetailHeadAdvert(null);
                            break;
                        case KplusConstants.ADVERT_USER_BODY:
                            mApp.setUserBodyAdvert(null);
                            break;
                        case KplusConstants.ADVERT_HOME:
                            mApp.setHomeAdvert(null);
                            break;
                        case KplusConstants.ADVERT_NEW_USER:
                            mApp.setNewUserAdvert(null);
                            break;
                        case KplusConstants.ADVERT_SERVICE_HEAD_NEW:
                            mApp.setServiceHeadAdvert(null);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        updateAdverts();
        return layout;
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(1);
        super.onDestroy();
    }

    private void updateAdverts() {
        if (adverts == null)
            return;
        Collections.sort(adverts, new AdvertComparator());
        try {
            RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) vpAD.getLayoutParams();
            if (type.equals(KplusConstants.ADVERT_HOME)) {
                rllp.width = dip2px(getActivity(), 240);
                rllp.height = dip2px(getActivity(), 400);
            } else if (KplusConstants.ADVERT_SERVICE_HEAD_NEW.equals(type)) {
                // 汽车服务
                rllp.width = nScreenWidth;
                rllp.height = dip2px(getActivity(), 113); // 113 = 100px * 1.125(9/8)
            } else {
                rllp.width = nScreenWidth;
                rllp.height = (int) (nScreenWidth / fScale);
            }
            vpAD.setLayoutParams(rllp);
            vpAD.setPadding(0, 0, 0, 0);
            if (advertImages == null)
                advertImages = new ArrayList<ImageView>();
            else
                advertImages.clear();
            for (int i = 0; i < adverts.size(); i++) {
                ImageView imageItem = new ImageView(getActivity());
                imageItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                if (KplusConstants.ADVERT_SERVICE_HEAD_NEW.equals(type)) {
                    imageItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                Advert advert = adverts.get(i);
                mApp.imageLoader.displayImage(advert.getImgUrl(), imageItem, optionsPhoto);
                imageItem.setTag(advert);
                ClickUtils.setNoFastClickListener(imageItem, new ClickUtils.NoFastClickListener() {
                    public void onNoFastClick(View v) {
                        Advert advertInfo = (Advert) v.getTag();
                        AdvertUtil au = new AdvertUtil(getActivity(), advertInfo, type);
                        au.OnAdvertClick();
                        if (type.equals(KplusConstants.ADVERT_HOME))
                            getActivity().finish();
                    }
                });
                advertImages.add(imageItem);
            }
            advertAdapter = new AadvertPagerAdapter(advertImages);
            vpAD.setAdapter(advertAdapter);
            cpiAD.setViewPager(vpAD);
            if (adverts.size() == 1) {
                cpiAD.setVisibility(View.GONE);
                if (adverts.get(0) != null && adverts.get(0).getCanClose() != null && adverts.get(0).getCanClose()) {
                    if (type.equals(KplusConstants.ADVERT_HOME) || KplusConstants.ADVERT_NEW_USER.equals(type))
                        ivCloseAd.setVisibility(View.GONE);
                    else
                        ivCloseAd.setVisibility(View.VISIBLE);
                } else {
                    ivCloseAd.setVisibility(View.GONE);
                }
            } else if (adverts.size() > 1) {
                cpiAD.setVisibility(View.VISIBLE);
                ivCloseAd.setVisibility(View.GONE);
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessageDelayed(message, 5000);
            }
            cpiAD.setViewPager(vpAD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dip2px(Context context, float dpValue) {
        float scale = 1;
        if (context != null) {
            try {
                scale = context.getResources().getDisplayMetrics().density;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (int) (dpValue * scale + 0.5f);
    }

    private class AdvertComparator implements Comparator<Advert> {
        @Override
        public int compare(Advert lhs, Advert rhs) {
            if (null == lhs.getSeq() || null == rhs.getSeq()) {
                return -1;
            }
            int seq1 = lhs.getSeq();
            int seq2 = rhs.getSeq();
            if (seq1 > seq2) {
                return -1;
            } else if (seq1 == seq2) {
                return 0;
            } else if (seq1 < seq2) {
                return 1;
            }
            return -1;
//            int result = 0;
//            try {
//                result = (lhs.getSeq().intValue() - rhs.getSeq().intValue());
//            } catch (Exception e) {
//                e.printStackTrace();
//                result = 0;
//            }
//            return result;
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
}
