package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：常用车辆适配器
 * <br/><br/>Created by Fu on 2015/5/11.
 * <br/><br/>
 */
public class CNCommonCarsAdapter extends RecyclerView.Adapter<CNCommonCarsAdapter.CarsViewHolder> {

    private List<Car> mDatas = null;
    private LayoutInflater mInflater = null;
    private OnListItemClickListener mClickListener = null;

    public CNCommonCarsAdapter(Context context, List<Car> datas, OnListItemClickListener listener) {
        mDatas = (null == datas ? new ArrayList<Car>() : datas);
        mInflater = LayoutInflater.from(context);
        mClickListener = listener;
    }

    public void append(Car car) {
        if (null != car) {
            mDatas.add(car);
            notifyDataSetChanged();
        }
    }

    public void append(List<Car> cars) {
        if (null != cars) {
            mDatas.addAll(cars);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (null != mDatas) {
            mDatas.clear();
        }
    }

    public Car getItem(int position) {
        if (null != mDatas && mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public CarsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cn_timeline_cars_item, parent, false);
        return new CarsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarsViewHolder holder, int position) {
        holder.setValue(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return (null == mDatas ? Const.NONE : mDatas.size());
    }

    class CarsViewHolder extends RecyclerView.ViewHolder implements CNViewClickUtil.NoFastClickListener {
        private int mPosition = Const.NEGATIVE;

        private LinearLayout llTimeCarsInfo = null;
        private TextView tvCarsLicence = null;
        private TextView tvCarsModel = null;
        private TextView tvCarsColor = null;
        private View viewBottom = null;

        public CarsViewHolder(View view) {
            super(view);

            llTimeCarsInfo = findView(view, R.id.llTimeCarsInfo);
            tvCarsLicence = findView(view, R.id.tvCarsLicence);
            tvCarsModel = findView(view, R.id.tvCarsModel);
            tvCarsColor = findView(view, R.id.tvCarsColor);
            viewBottom = findView(view, R.id.viewBottom);

            CNViewClickUtil.setNoFastClickListener(llTimeCarsInfo, this);
        }

        public void setValue(int position, Car car) {
            mPosition = position;

            tvCarsLicence.setText(car.getLicense());
//            tvCarsModel.setText(car.getBrand().getName() + " " + car.getModel().getName());
//            CarBrand brand = car.getBrand();
            CarModel model = car.getModel();
            if (null != model) {
                tvCarsModel.setText(model.getName());
            }

            CarColor color = car.getColor();
            if (null != color) {
                tvCarsColor.setText(color.getName());
            }

            switch (mPosition) {
                case Const.NONE:
                    llTimeCarsInfo.setAlpha(1.0f);
                    break;
                case Const.ONE:
                    llTimeCarsInfo.setAlpha(0.85f);
                    break;
                case Const.TWO:
                    llTimeCarsInfo.setAlpha(0.7f);
                    break;
                case 3:
                    llTimeCarsInfo.setAlpha(0.55f);
                    break;
                case 4:
                    llTimeCarsInfo.setAlpha(0.4f);
                    break;
                default:
                    llTimeCarsInfo.setAlpha(0.35f);
                    break;
            }

            if (mPosition == getItemCount() - Const.ONE) {
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }
        }

        protected <T extends View> T findView(View parent, int id) {
            if (null == parent) {
                throw new NullPointerException("parent is not null!");
            }
            return (T) parent.findViewById(id);
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
