package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.StringMatcher;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Description：车辆品牌适配器
 * <br/><br/>Created by Fu on 2015/5/22.
 * <br/><br/>
 */
public class CNBrandContentAdapter extends CNBaseListAdapter<CarBrand> implements SectionIndexer {
    private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final DisplayImageOptions mOptions;

    public CNBrandContentAdapter(Context context, List<CarBrand> list, OnListItemClickListener listener) {
        super(context, list, listener);

        mOptions = CNImageUtils.getImageOptions();
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        CarBrandViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_car_brand_item, parent, false);
            holder = new CarBrandViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (CarBrandViewHolder) view.getTag();
        }

        holder.setValue(position, getItem(position));
        return view;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        // If there is no item for current section, previous section will be selected
        // 如果当前部分没有item，则之前的部分将被选择
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    // For numeric section 数字
                    for (int k = 0; k <= 9; k++) {
                        // 字符串第一个字符与1~9之间的数字进行匹配
                        String py = getItem(j).getPy();
                        if (StringMatcher.match(py, String.valueOf(k)))
                            return j;
                    }
                } else {
                    String py = getItem(j).getPy();
                    if (StringMatcher.match(py, String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private class CarBrandViewHolder extends CNBaseAdapterViewHolder<CarBrand> implements CNViewClickUtil.NoFastClickListener {

        private RelativeLayout rlCarColorItem = null;
        private ImageView ivBrandIcon = null;
        private TextView tvItemName = null;
        private ImageView ivRightArrow = null;

        private LinearLayout llIndexTitleItem = null;
        private TextView tvIndexTitleItem = null;

        public CarBrandViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            rlCarColorItem = findView(R.id.rlCarColorItem);
            ivBrandIcon = findView(R.id.ivBrandIcon);
            tvItemName = findView(R.id.tvItemName);
            ivRightArrow = findView(R.id.ivRightArrow);

            llIndexTitleItem = findView(R.id.llIndexTitleItem);
            tvIndexTitleItem = findView(R.id.tvIndexTitleItem);

            CNViewClickUtil.setNoFastClickListener(rlCarColorItem, this);
        }

        @Override
        public void setValue(int position, CarBrand brand) {
            mPosition = position;

            String catalog = brand.getPy();
            if (mPosition == Const.NONE) {
                llIndexTitleItem.setVisibility(View.VISIBLE);
                tvIndexTitleItem.setVisibility(View.VISIBLE);
                tvIndexTitleItem.setText(catalog);
            } else {
                CarBrand tempBrand = getItem(mPosition - Const.ONE);
                if (null != tempBrand && CNStringUtil.isNotEmpty(catalog)) {
                    String lastCatalog = tempBrand.getPy();
                    if (CNStringUtil.isNotEmpty(lastCatalog) && lastCatalog.equals(catalog)) {
                        llIndexTitleItem.setVisibility(View.GONE);
                        tvIndexTitleItem.setVisibility(View.GONE);
                    } else {
                        llIndexTitleItem.setVisibility(View.VISIBLE);
                        tvIndexTitleItem.setVisibility(View.VISIBLE);
                        tvIndexTitleItem.setText(catalog);
                    }
                } else {
                    llIndexTitleItem.setVisibility(View.GONE);
                    tvIndexTitleItem.setVisibility(View.GONE);
                }
            }

            ivRightArrow.setVisibility(View.VISIBLE);
            tvItemName.setText(brand.getName());

            String url = brand.getLogo();
            ImageLoader.getInstance().displayImage(url, ivBrandIcon, mOptions);
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
