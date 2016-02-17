package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.bean.CarModelTag;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.bean.Coupon;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.OrderPayment;
import com.kplus.car.carwash.bean.ServiceSupportCarTag;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.IPopupItemClickListener;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.adapter.CNBrandContentAdapter;
import com.kplus.car.carwash.module.adapter.CNCarColorAdapter;
import com.kplus.car.carwash.module.adapter.CNCarModelAdapter;
import com.kplus.car.carwash.module.adapter.CNCityAdapter;
import com.kplus.car.carwash.module.adapter.CNLocationAdapter;
import com.kplus.car.carwash.module.adapter.CNPayListAdapter;
import com.kplus.car.carwash.module.adapter.CNVoucherAdapter;
import com.kplus.car.carwash.utils.CNCarModelTagUtil;
import com.kplus.car.carwash.utils.CNCarModelUtil;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNResourcesUtil;
import com.kplus.car.carwash.utils.CNSupportCarTagUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.widget.CNProviceView;
import com.kplus.car.carwash.widget.indexscroller.IndexableListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description 弹框Activity不继承ParentActivity
 * <br/><br/>Created by FU ZHIXUE on 2015/8/4.
 * <br/><br/>
 */
public class CNDialogActivity extends Activity {
    private static final String TAG = "CNDialogActivity";

    private Context mContext = null;
    protected Point mPoint;

    private CNCityAdapter mCityAdapter = null;
    private CNCarColorAdapter mColorAdapter = null;
    private CNBrandContentAdapter mBrandAdapter = null;
    private CNCarModelAdapter mCarModelAdapter = null;
    private CNLocationAdapter mLocationAdapter = null;
    private CNVoucherAdapter mVoucherAdapter = null;

    private int mDialogType = Const.NEGATIVE;

    public interface IShowProgressCallback {
        void showProgress();
    }

    public interface ICarBrandCallback {
        void onSelectedCarBrand(CarBrand brand, CarModel carModel);
    }

    public interface IProviceItemCallback {
        void onProviceItem(String provice);
    }

    private static IShowProgressCallback mShowProgressCallback = null;
    private static IPopupItemClickListener mPopupItemClickListener = null;
    private static ICarBrandCallback mCarBrandCallback = null;
    private static IProviceItemCallback mProviceItemCallback = null;

    public static void setShowProgressCallback(IShowProgressCallback showProgressCallback) {
        mShowProgressCallback = showProgressCallback;
    }

    public static void setPopupItemClickListener(IPopupItemClickListener popupItemClickListener) {
        mPopupItemClickListener = popupItemClickListener;
    }

    public static void setCarBrandCallback(ICarBrandCallback carBrandCallback) {
        mCarBrandCallback = carBrandCallback;
    }

    public static void setProviceItemCallback(IProviceItemCallback proviceItemCallback) {
        mProviceItemCallback = proviceItemCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mPoint = CNPixelsUtil.getDeviceSize(mContext);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mDialogType = bundle.getInt(CNCarWashingLogic.KEY_DIALOG_VALUE, Const.NEGATIVE);
        }

        initDialog();
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onDestroy() {
        mShowProgressCallback = null;
        mPopupItemClickListener = null;
        mCarBrandCallback = null;
        if (null != mCityAdapter) {
            mCityAdapter.clear();
            mCityAdapter = null;
        }
        if (null != mColorAdapter) {
            mColorAdapter.clear();
            mColorAdapter = null;
        }
        if (null != mBrandAdapter) {
            mBrandAdapter.clear();
            mBrandAdapter = null;
        }
        if (null != mCarModelAdapter) {
            mCarModelAdapter.clear();
            mCarModelAdapter = null;
        }
        if (null != mLocationAdapter) {
            mLocationAdapter.clear();
            mLocationAdapter = null;
        }
        if (null != mVoucherAdapter) {
            mVoucherAdapter.clear();
            mVoucherAdapter = null;
        }
        System.gc();

        super.onDestroy();
    }

    private void initDialog() {
        switch (mDialogType) {
            case CNCarWashingLogic.DIALOG_PAY_TYPE:         // 支付类型
                setPayDialog();
                break;
            case CNCarWashingLogic.DIALOG_CITIES_TYPE:      // 城市类型
                setCitiesDialog();
                break;
            case CNCarWashingLogic.DIALOG_COLORS_TYPE:      // 颜色类型
                setColorsDialog();
                break;
            case CNCarWashingLogic.DIALOG_BRAND_TYPE:       // 品牌类型
                setBrandDialog();
                break;
            case CNCarWashingLogic.DIALOG_LOCATION_TYPE:    // 位置类型
                setLocationDialog();
                break;
            case CNCarWashingLogic.DIALOG_VOUCHER_TYPE:     // 代金券
                setVoucherDialog();
                break;
            case CNCarWashingLogic.DIALOG_PROVICE_TYPE:     // 省份
                setProviceDialog();
                break;
            default:
                finish();
                break;
        }
    }

    private void setWindowAttributes() {
        int height = (int) (mPoint.y * 0.6f);
        setWindowAttributes(height);
    }

    private void setWindowAttributes(int height) {
        int width = mPoint.x; // 屏幕的宽度

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = height;
        params.width = width;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    /**
     * 省份弹框
     */
    private void setProviceDialog() {
        setContentView(R.layout.cn_car_licence_layout);

        setWindowAttributes(WindowManager.LayoutParams.WRAP_CONTENT);

        Toolbar toolbar = findView(R.id.toolbar);
        TextView tvNavLeft = findView(R.id.tvNavLeft);
        TextView tvNavTitle = findView(R.id.tvNavTitle);
        LinearLayout llNavCenterTitle = findView(R.id.llNavCenterTitle);
        final CNProviceView llProviceView = findView(R.id.llProviceView);

        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavTitle.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        tvNavLeft.setText(CNResourcesUtil.getStringResources(R.string.cn_cancel));
        tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_car_licence_title));
        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 当前的车牌
                String strLicence = getIntent().getStringExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1);

                llProviceView.setCellData(strLicence, new CNProviceView.OnItemClickListener() {
                    @Override
                    public void onCellItemClick(String provice) {
                        if (null != mProviceItemCallback) {
                            mProviceItemCallback.onProviceItem(provice);
                        }
                        finish();
                    }
                });
            }
        }, 100);
    }

    /**
     * 代金券类型
     */
    private void setVoucherDialog() {
        setContentView(R.layout.cn_car_color_list);

        setWindowAttributes();

        Toolbar toolbar = findView(R.id.toolbar);
        TextView tvNavLeft = findView(R.id.tvNavLeft);
        TextView tvNavVoucherTitle = findView(R.id.tvNavTitle);
        LinearLayout llNavCenterTitle = findView(R.id.llNavCenterTitle);
        ListView lvVoucherList = findView(R.id.lvList);
        TextView tvVoucherEmpty = findView(R.id.tvEmpty);

        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavVoucherTitle.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavVoucherTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        tvNavLeft.setText(CNResourcesUtil.getStringResources(R.string.cn_cancel));

        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                finish();
            }
        });

        mVoucherAdapter = new CNVoucherAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                Coupon coupon = mVoucherAdapter.getItem(position);
                if (null != mPopupItemClickListener) {
                    mPopupItemClickListener.onClickPopupItem(CNCarWashingLogic.DIALOG_VOUCHER_TYPE, position, v, coupon);
                }
                finish();
            }
        });
        lvVoucherList.setAdapter(mVoucherAdapter);

        final List<Coupon> coupons = (ArrayList<Coupon>) getIntent().getExtras().get(CNCarWashingLogic.KEY_DIALOG_PARAM1);
        if (null != coupons && !coupons.isEmpty()) {
            tvNavVoucherTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_voucher));
            lvVoucherList.setVisibility(View.VISIBLE);
            tvVoucherEmpty.setVisibility(View.GONE);
        } else {
            tvNavVoucherTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_no_voucher));
            // 如果没有代金券，显示提示文字
            lvVoucherList.setVisibility(View.GONE);
            tvVoucherEmpty.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVoucherAdapter.append(coupons);
            }
        }, 100);
    }

    /**
     * 位置类型
     */
    private void setLocationDialog() {
        setContentView(R.layout.cn_car_color_list);

        setWindowAttributes();

        Toolbar toolbar = findView(R.id.toolbar);
        TextView tvNavLeft = findView(R.id.tvNavLeft);
        TextView tvNavTitle = findView(R.id.tvNavTitle);
        LinearLayout llNavCenterTitle = findView(R.id.llNavCenterTitle);
        TextView tvNavRight = findView(R.id.tvNavRight);

        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavTitle.setVisibility(View.VISIBLE);
        tvNavRight.setVisibility(View.INVISIBLE);

        toolbar.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        tvNavLeft.setText(CNResourcesUtil.getStringResources(R.string.cn_cancel));
        tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_location));
        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                finish();
            }
        });

        ListView lvList = findView(R.id.lvList);
        mLocationAdapter = new CNLocationAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                if (null != mPopupItemClickListener) {
                    BaseInfo baseInfo = mLocationAdapter.getItem(position);
                    mPopupItemClickListener.onClickPopupItem(CNCarWashingLogic.DIALOG_LOCATION_TYPE, position, v, baseInfo);
                }
                finish();
            }
        });
        lvList.setAdapter(mLocationAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<BaseInfo> poiLocationRoads = (ArrayList<BaseInfo>) getIntent().getExtras().get(CNCarWashingLogic.KEY_DIALOG_PARAM1);
                mLocationAdapter.append(poiLocationRoads);
            }
        }, 100);
    }

    /**
     * 车辆品牌
     */
    private void setBrandDialog() {
        setContentView(R.layout.cn_car_brand_layout);

        setWindowAttributes();

        Toolbar toolbar = findView(R.id.toolbar);
        final TextView tvNavLeft = findView(R.id.tvNavLeft);
        final TextView tvNavTitle = findView(R.id.tvNavTitle);
        LinearLayout llNavCenterTitle = findView(R.id.llNavCenterTitle);
        final IndexableListView mListView = findView(R.id.listview);
        final ListView lvList = findView(R.id.lvList);

        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavTitle.setVisibility(View.VISIBLE);

        mListView.setVisibility(View.VISIBLE);
        lvList.setVisibility(View.GONE);

        toolbar.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        final String strCancel = CNResourcesUtil.getStringResources(R.string.cn_cancel);
        tvNavLeft.setText(strCancel);
        tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_car_model_title));
        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                // 不为空是取消
                if (tvNavLeft.getText().equals(strCancel)) {
                    finish();
                } else {
                    // 从车型中返回到品牌
                    mListView.setVisibility(View.VISIBLE);
                    lvList.setVisibility(View.GONE);
                    // 显示取消
                    tvNavLeft.setCompoundDrawables(null, null, null, null);
                    tvNavLeft.setText(strCancel);
                    // 设置标题显示车型
                    tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_car_model_title));
                }
            }
        });

        mBrandAdapter = new CNBrandContentAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                // 点击品牌
                final CarBrand carBrand = mBrandAdapter.getItem(position);
                if (null == carBrand) {
                    return;
                }

                final ArrayList<OnSiteService> onSiteServices = (ArrayList<OnSiteService>) getIntent().getExtras().get(CNCarWashingLogic.KEY_SERVICE_SELECTED);

                CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<List<CarModel>>() {
                    @Override
                    public List<CarModel> run() {
                        List<CarModel> carModels = null;
                        if (null != onSiteServices && !onSiteServices.isEmpty()) {
                            OnSiteService service = onSiteServices.get(Const.NONE);

                            List<ServiceSupportCarTag> supportCarTags = CNSupportCarTagUtil.getIns().getTagId(service.getCityId(), service.getId());
                            List<CarModelTag> carModelTags = CNCarModelTagUtil.getIns().getCarModelByTags(supportCarTags);
                            carModels = CNCarModelUtil.getIns().getCarModels(carBrand.getId(), carModelTags);
                        }
                        return carModels;
                    }
                }, new FutureListener<List<CarModel>>() {
                    @Override
                    public void onFutureDone(Future<List<CarModel>> future) {
                        List<CarModel> carModels = future.get();
                        // 直接从缓存中取
                        if (null != carModels && carModels.size() > Const.NONE) {
                            mListView.setVisibility(View.GONE);
                            lvList.setVisibility(View.VISIBLE);
                            // 显示返回按钮
                            Drawable drawable = CNResourcesUtil.getDrawable(R.drawable.btn_back_white_selector);
                            int width = drawable.getMinimumWidth();
                            int height = drawable.getMinimumHeight();
                            drawable.setBounds(0, 0, width, height);
                            tvNavLeft.setCompoundDrawables(drawable, null, null, null);
                            tvNavLeft.setText("");
                            // 设置标题显示车型
                            tvNavTitle.setText(carBrand.getName());

                            // 显示车型
                            mCarModelAdapter = new CNCarModelAdapter(mContext, carModels, new OnListItemClickListener() {
                                @Override
                                public void onClickItem(int position, View v) {
                                    // 点击车型
                                    if (v.getId() == R.id.rlCarColorItem) {
                                        CarModel carModel = mCarModelAdapter.getItem(position);
                                        if (null != mCarBrandCallback) {
                                            mCarBrandCallback.onSelectedCarBrand(carBrand, carModel);
                                        }
                                        finish();
                                    }
                                }
                            });
                            lvList.setAdapter(mCarModelAdapter);
                        } else {
//                    if (null != mCarBrandCallback) {
//                        mCarBrandCallback.onSelectedCarBrand(carBrand, null);
//                    }
//                    finish();
                            CNCommonManager.makeText(mContext, CNResourcesUtil.getStringResources(R.string.cn_not_support_car_model));
                            // 未取到数据，执行重新请求
//                    CNCarModelUtil.getIns().resetVersion();
//                    CNCarModelUtil.save();
//                    CNInitializeApiDataUtil.getIns().fetchSupportCarModels(mContext);
//                            finish();
                        }
                    }
                });
            }
        });
        mListView.setAdapter(mBrandAdapter);
        // 如果为true 会滑动后自动隐藏
        mListView.setFastScrollEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<CarBrand> brands = (ArrayList<CarBrand>) getIntent().getExtras().get(CNCarWashingLogic.KEY_DIALOG_PARAM1);
                mBrandAdapter.append(brands);
            }
        }, 250);
    }

    /**
     * 车辆颜色
     */
    private void setColorsDialog() {
        setContentView(R.layout.cn_car_color_list);

        setWindowAttributes();

        Toolbar toolbar = findView(R.id.toolbar);
        TextView tvNavLeft = findView(R.id.tvNavLeft);
        TextView tvNavTitle = findView(R.id.tvNavTitle);
        LinearLayout llNavCenterTitle = findView(R.id.llNavCenterTitle);
        ListView lvList = findView(R.id.lvList);

        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavTitle.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        tvNavLeft.setText(CNResourcesUtil.getStringResources(R.string.cn_cancel));
        tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_car_color_title));
        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                finish();
            }
        });

        mColorAdapter = new CNCarColorAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                if (v.getId() == R.id.rlCarColorItem) {
                    if (null != mPopupItemClickListener) {
                        CarColor color = mColorAdapter.getItem(position);
                        mPopupItemClickListener.onClickPopupItem(CNCarWashingLogic.DIALOG_COLORS_TYPE, position, v, color);
                    }
                    finish();
                }
            }
        });
        lvList.setAdapter(mColorAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<CarColor> colors = (ArrayList<CarColor>) getIntent().getExtras().get(CNCarWashingLogic.KEY_DIALOG_PARAM1);
                mColorAdapter.append(colors);
            }
        }, 100);
    }

    /**
     * 城市列表的弹框
     */
    private void setCitiesDialog() {
        setContentView(R.layout.cn_car_color_list);

        setWindowAttributes();

        Toolbar toolbar = findView(R.id.toolbar);
        TextView tvNavLeft = findView(R.id.tvNavLeft);
        TextView tvNavTitle = findView(R.id.tvNavTitle);
        LinearLayout llNavCenterTitle = findView(R.id.llNavCenterTitle);
        ListView lvList = findView(R.id.lvList);

        llNavCenterTitle.setVisibility(View.VISIBLE);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavTitle.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        tvNavLeft.setText(CNResourcesUtil.getStringResources(R.string.cn_cancel));
        tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_city));
        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                finish();
            }
        });

        mCityAdapter = new CNCityAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                if (null != mPopupItemClickListener) {
                    BaseInfo baseInfo = mCityAdapter.getItem(position);
                    mPopupItemClickListener.onClickPopupItem(CNCarWashingLogic.DIALOG_CITIES_TYPE, position, v, baseInfo);
                }
                finish();
            }
        });
        lvList.setAdapter(mCityAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<City> cities = CNCarWashApp.getIns().mCitys;
                mCityAdapter.append(cities);
            }
        }, 100);
    }


    /**
     * 支付的弹框
     */
    private void setPayDialog() {
        setContentView(R.layout.cn_pay_list);

        setWindowAttributes();

        Bundle bundle = getIntent().getExtras();
        // 订单id
        final long fromOrderId = bundle.getLong(CNCarWashingLogic.KEY_DIALOG_PARAM1);
        // 订单价格
        BigDecimal price = (BigDecimal) bundle.get(CNCarWashingLogic.KEY_DIALOG_PARAM2);

        RelativeLayout llPayPopup = findView(R.id.llPayPopup);
        TextView tvNavLeft = findView(R.id.tvNavLeft);
        TextView tvNavTitle = findView(R.id.tvNavTitle);
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavTitle.setVisibility(View.VISIBLE);

        llPayPopup.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color));
        tvNavLeft.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));
        tvNavTitle.setTextColor(CNResourcesUtil.getColorResources(R.color.cn_white));

        tvNavLeft.setText(CNResourcesUtil.getStringResources(R.string.cn_cancel));
        tvNavTitle.setText(CNResourcesUtil.getStringResources(R.string.cn_selected_pay_type));
        final Button btnListPay = findView(R.id.btnListPay);

        RecyclerView rlPayTypeList = findView(R.id.rlPayTypeList);

        CNViewClickUtil.setNoFastClickListener(tvNavLeft, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                finish();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rlPayTypeList.setLayoutManager(llm);

        /**
         * 如果添加或修改一种支付类型要修改三处
         * 1、 {@link #getPayMent(boolean, BigDecimal)} 支付类型数据源中
         * 2、点击支付按钮，得到相应的支付类型
         * 3、{@link #com.kplus.car.AppBridgeAccessManager.pay} 方法中要根据支付类型调用支付
         */
        // 获取支付类型数据源
        final List<OrderPayment> mOrderpayments = CNCarWashingLogic.getPayMent();

        final CNPayListAdapter payListAdapter = new CNPayListAdapter(mContext, null, null);

        rlPayTypeList.setAdapter(payListAdapter);
        payListAdapter.setPrice(price);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                payListAdapter.append(mOrderpayments);
            }
        }, 100);

        // 点击支付
        CNViewClickUtil.setNoFastClickListener(btnListPay, new CNViewClickUtil.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                AppBridgeUtils.getIns().onEvent(mContext, "pagewashCar_payOrder ", "上门洗车订单在线支付");

                int count = payListAdapter.getItemCount();
                boolean isBlancePay = false;
                int payType = Const.NEGATIVE;
                for (int i = Const.NONE; i < count; i++) {
                    OrderPayment payment = payListAdapter.getItem(i);
                    if (i == Const.NONE) {
                        // 第一个是余额
                        isBlancePay = payment.isCheck();
                        if (isBlancePay) {
                            payType = payment.getPayType();
                        }
                    } else {
                        if (payment.isCheck()) {
                            // 选择其他支付也选择了余额支付
                            int tempType = payment.getPayType();
                            if (isBlancePay) {
                                // Notice 如果添加或修改支付类型，这里是第二步
                                switch (tempType) {
                                    case Const.UPOMP_PAY: // 余额和银行卡支付
                                        payType = Const.BALANCE_UPOMP_PAY;
                                        break;
                                    case Const.ALI_PAY: // 余额和支付宝支付
//                                        payType = Const.BALANCE_ALI_PAY;
//                                        break;
                                    case Const.OPENTRADE_PAY:
                                        payType = Const.BALANCE_OPENTRADE_PAY;
                                        break;
                                    case Const.LIANLIAN_PAY: // 余额和连连支付
                                        payType = Const.BALANCE_LIANLIAN_PAY;
                                        break;
                                    case Const.WECHAT_PAY: // 余额和微信支付
                                        payType = Const.BALANCE_WECHAT_PAY;
                                        break;
                                }
                            } else {
                                // 没有选择余额
                                payType = tempType;
                            }
                            // 其他支付方法，只有一个支付，是单选的，如果有选择了的，就可以退出循环了
                            break;
                        }
                    }
                }

                Log.trace(TAG, "选择的支付类型为：" + payType);

                if (payType == Const.NEGATIVE) {
                    String msg = mContext.getResources().getString(R.string.cn_please_selected_payment);
                    CNCommonManager.makeText(mContext, msg);
                    return;
                }
                if (null != mShowProgressCallback) {
                    mShowProgressCallback.showProgress();
                }
                AppBridgeUtils.getIns().onEvent(mContext, "click_pay_washCar ", "选择服务，下一步，立即下单，在线支付");
                // 开始支付
                AppBridgeUtils.getIns().onPay(mContext, fromOrderId, payType);
                finish();
            }
        });
    }
}
