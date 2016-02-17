package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OrderPayment;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：支付类型列表适配器
 * <br/><br/>Created by Fu on 2015/5/14.
 * <br/><br/>
 */
public class CNPayListAdapter extends RecyclerView.Adapter<CNPayListAdapter.PayViewHolder> {

    private Context mContext = null;
    private List<OrderPayment> mDatas = null;
    private LayoutInflater mInflater = null;
    private OnListItemClickListener mClickListener = null;

    private BigDecimal mPrice = null;

    private Map<Integer, RadioButton> mRadioState = new HashMap<>();
    private Map<Integer, CheckBox> mCheckBoxState = new HashMap<>();

    public CNPayListAdapter(Context context, List<OrderPayment> datas, OnListItemClickListener listener) {
        mContext = context;
        mDatas = (null == datas ? new ArrayList<OrderPayment>() : datas);
        mInflater = LayoutInflater.from(mContext);
        mClickListener = listener;
    }

    public void setPrice(BigDecimal price) {
        if (null != price) {
            mPrice = price;
        } else {
            mPrice = new BigDecimal(Const.NONE);
        }
    }

    public void append(OrderPayment data) {
        if (null != data) {
            mDatas.add(data);
            notifyDataSetChanged();
        }
    }

    public void append(List<OrderPayment> datas) {
        if (null != datas && datas.size() > Const.NONE) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (null != mDatas) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public PayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cn_pay_list_item, parent, false);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PayViewHolder holder, int position) {
        holder.setValue(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return (null == mDatas ? Const.NONE : mDatas.size());
    }

    public OrderPayment getItem(int position) {
        if (null != mDatas) {
            return mDatas.get(position);
        }
        return null;
    }

    class PayViewHolder extends RecyclerView.ViewHolder implements CNViewClickUtil.NoFastClickListener, CompoundButton.OnCheckedChangeListener {
        private int mPosition = Const.NEGATIVE;

        private RelativeLayout rlPayItem = null;
        private CheckBox cbChecked = null;
        private RadioButton rbChecked = null;
        private ImageView ivPayIcon = null;
        private TextView tvAccountType = null;
        private TextView tvBalance = null;
        private TextView tvPayTypeDesc = null;

        public PayViewHolder(View view) {
            super(view);

            rlPayItem = findView(view, R.id.rlPayItem);
            cbChecked = findView(view, R.id.cbChecked);
            rbChecked = findView(view, R.id.rbChecked);
            ivPayIcon = findView(view, R.id.ivPayIcon);
            tvAccountType = findView(view, R.id.tvAccountType);
            tvBalance = findView(view, R.id.tvBalance);
            tvPayTypeDesc = findView(view, R.id.tvPayTypeDesc);

            CNViewClickUtil.setNoFastClickListener(rlPayItem, this);
        }

        public void setValue(int position, OrderPayment payment) {
            mPosition = position;

            cbChecked.setVisibility(View.GONE);
            rbChecked.setVisibility(View.GONE);
            tvBalance.setVisibility(View.GONE);
            tvPayTypeDesc.setVisibility(View.GONE);

            rlPayItem.setTag(position);
            // 如果禁用把点击事件也拿掉
            if (!payment.isEnabled()) {
                rlPayItem.setOnClickListener(null);
            } else {
                CNViewClickUtil.setNoFastClickListener(rlPayItem, this);
            }
            rlPayItem.setEnabled(payment.isEnabled());

            ivPayIcon.setImageResource(payment.getPayIcon());
            tvAccountType.setText(payment.getPayName());

            String value;
            switch (payment.getPayType()) {
                case Const.BALANCE_PAY: // 显示账户余额
                    cbChecked.setOnCheckedChangeListener(null);
                    cbChecked.setTag(position);
                    cbChecked.setChecked(payment.isCheck());
                    cbChecked.setEnabled(payment.isEnabled());
                    mCheckBoxState.put(mPosition, cbChecked);
                    cbChecked.setOnCheckedChangeListener(mOnCheckBoxChecked);

                    cbChecked.setVisibility(View.VISIBLE);
                    tvBalance.setVisibility(View.VISIBLE);

                    // 账户余额
                    BigDecimal userBalance = AppBridgeUtils.getIns().getUserBalance();
                    // 保留两位小数
                    double balance = userBalance.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    value = mContext.getResources().getString(R.string.cn_order_price);
                    value = String.format(value, balance);
                    tvBalance.setText(value);
                    break;
                default:
                    rbChecked.setVisibility(View.VISIBLE);
                    tvBalance.setVisibility(View.GONE);

                    rbChecked.setOnCheckedChangeListener(null);
                    rbChecked.setChecked(payment.isCheck());
                    rbChecked.setEnabled(payment.isEnabled());
                    rbChecked.setTag(position);
                    mRadioState.put(mPosition, rbChecked);
                    rbChecked.setOnCheckedChangeListener(this);

                    if (CNStringUtil.isNotEmpty(payment.getPayDesc())) {
                        tvPayTypeDesc.setVisibility(View.VISIBLE);
                        tvPayTypeDesc.setText(payment.getPayDesc());
                    } else {
                        tvPayTypeDesc.setVisibility(View.GONE);
                    }
                    break;
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
            if (v.getId() == R.id.rlPayItem) {
                int pos = (int) rlPayItem.getTag();
                // 第一个是余额支付
                if (pos == Const.NONE) {
                    CheckBox checkBox = mCheckBoxState.get(pos);
                    boolean isChecked = checkBox.isChecked();
                    setCheckBoxChecked(pos, !isChecked);
                } else {
                    RadioButton checkRadio = mRadioState.get(pos);
                    if (!checkRadio.isChecked()) {
                        setRadioButtonChecked(pos, !checkRadio.isChecked());
                    }
                }
            } else {
                if (null != mClickListener) {
                    mClickListener.onClickItem(mPosition, v);
                }
            }
        }

        private void setCheckBoxChecked(int pos, boolean isChecked) {
            // 点击checkbox的item，如果是选中的，变不选中，如果不选中，点时变为选中
            OrderPayment payment = getItem(pos);
            CheckBox checkBox = mCheckBoxState.get(pos);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(isChecked);
            payment.setIsCheck(isChecked);
            checkBox.setOnCheckedChangeListener(mOnCheckBoxChecked);

            if (isChecked) {
                // 如果是选择余额支付先判断余额是否足够，如果足够把其他支付方式去掉不选择
                boolean isEnough = CNCarWashingLogic.isBalanceEnough(mPrice);
                if (isEnough) {
                    setRadioButtonChecked(Const.NEGATIVE, false);
                }
            }
        }

        private CompoundButton.OnCheckedChangeListener mOnCheckBoxChecked = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null != buttonView.getTag()) {
                    int pos = (int) buttonView.getTag();
                    setCheckBoxChecked(pos, isChecked);
                }
            }
        };

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int pos = (int) buttonView.getTag();
            setRadioButtonChecked(pos, isChecked);
        }

        /**
         * 设置单选项选择
         */
        private void setRadioButtonChecked(int pos, boolean isChecked) {
            // 如果选择其他支付方式时，余额足够就把余额去掉
            if (isChecked) {
                boolean isEnough = CNCarWashingLogic.isBalanceEnough(mPrice);
                if (isEnough) {
                    setCheckBoxChecked(Const.NONE, false);
                }
            }
            for (Map.Entry<Integer, RadioButton> entry : mRadioState.entrySet()) {
                int key = entry.getKey();
                RadioButton radioButton = entry.getValue();
                radioButton.setOnCheckedChangeListener(null);
                /**
                 * 点击的与这个相同，如果isChecked为true，表示这个未选择，
                 * 要设置为选择，如果为false表示之前就选择这个，又点击了这个，不改为不选择
                 * 其他都设置为不选择，改变时把监听去掉，不然会重复调用
                 */
                if (key == pos) {
                    if (isChecked) {
                        radioButton.setChecked(true);
                        getItem(key).setIsCheck(true);
                    }
                } else {
                    radioButton.setChecked(false);
                    getItem(key).setIsCheck(false);
                }
                radioButton.setOnCheckedChangeListener(this);
            }
        }
    }

}
