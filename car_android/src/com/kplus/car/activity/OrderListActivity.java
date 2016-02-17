package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.carwash.module.fragments.CNOrderListFragment;
import com.kplus.car.fragment.FormOrderListFragment;
import com.kplus.car.fragment.ServiceOrderListFragment;
import com.kplus.car.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListActivity extends BaseActivity implements View.OnClickListener {
    private View leftView;
    private ImageView ivLeft;
    private TextView tvTitle;
    private View rightView;
    private ImageView ivRight;
    private GridView gvOrderType;
    private ViewPager vpOrders;
    private LayoutInflater layoutInflater;
    private List<OrderTypeItem> orderTypeItems;
    private OrderTypeAdapter orderTypeAdapter;
    private OrderFragmentAdapter orderFragmentAdapter;
    private int nCurrentOrderType = 0;
    private int orderStatus;
    private FormOrderListFragment formOrderListFragment;
    private ServiceOrderListFragment serviceOrderListFragment;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.daze_order_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
        leftView = findViewById(R.id.leftView);
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("我的订单");
        rightView = findViewById(R.id.rightView);
        rightView.setVisibility(View.VISIBLE);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);
        gvOrderType = (GridView) findViewById(R.id.gvOrderType);
        vpOrders = (ViewPager) findViewById(R.id.vpOrders);
    }

    @Override
    protected void loadData() {
        orderStatus = getIntent().getIntExtra("ordeStatus", 0);
        setOrderStatus(orderStatus);
        layoutInflater = LayoutInflater.from(this);
        orderTypeItems = new ArrayList<OrderTypeItem>(2);
        orderTypeItems.add(new OrderTypeItem(0, "在线缴费"));
        orderTypeItems.add(new OrderTypeItem(1, "汽车服务"));

        orderTypeAdapter = new OrderTypeAdapter(orderTypeItems);
        gvOrderType.setAdapter(orderTypeAdapter);
        formOrderListFragment = new FormOrderListFragment();
        serviceOrderListFragment = new ServiceOrderListFragment();
        orderFragmentAdapter = new OrderFragmentAdapter(getSupportFragmentManager());
        vpOrders.setAdapter(orderFragmentAdapter);
    }

    @Override
    protected void setListener() {
        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        gvOrderType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vpOrders.setCurrentItem(position, true);
            }
        });

        vpOrders.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                nCurrentOrderType = position;
                orderTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        int position = getIntent().getIntExtra("position", 0);
        vpOrders.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                finish();
                break;
            default:
                break;
        }
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    class OrderTypeItem {
        public int nOrderType;
        public String strName;

        public OrderTypeItem(int nOrderType, String strName) {
            this.nOrderType = nOrderType;
            this.strName = strName;
        }
    }

    class OrderTypeAdapter extends BaseAdapter {
        private List<OrderTypeItem> mList = null;

        public OrderTypeAdapter(List<OrderTypeItem> data) {
            mList = new ArrayList<>();
            append(data);
        }

        public void clear() {
            if (null != mList) {
                mList.clear();
                notifyDataSetChanged();
            }
        }

        public void append(List<OrderTypeItem> items) {
            if (null != items) {
                mList.addAll(items);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            if (mList == null)
                return 0;
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            if (mList == null || mList.isEmpty())
                return null;
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, Object> holder = null;
            if (convertView == null) {
                holder = new HashMap<String, Object>();
                convertView = layoutInflater.inflate(R.layout.daze_gridview_item, parent, false);
                TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
                holder.put("tvText", tvText);
                convertView.setTag(holder);
            } else
                holder = (HashMap<String, Object>) convertView.getTag();
            TextView tvText = (TextView) holder.get("tvText");
            OrderTypeItem orderTypeItem = mList.get(position);
            if (!StringUtils.isEmpty(orderTypeItem.strName))
                tvText.setText(orderTypeItem.strName);
            if (nCurrentOrderType == position) {
                tvText.setBackgroundColor(Color.rgb(232, 232, 232));
                tvText.setTextColor(Color.rgb(76, 76, 76));
            } else {
                tvText.setBackgroundColor(Color.rgb(248, 248, 248));
                tvText.setTextColor(Color.rgb(151, 151, 151));
            }
            return convertView;
        }
    }

    class OrderFragmentAdapter extends FragmentPagerAdapter {
        OrderFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return formOrderListFragment;
            } else if (position == 1) {
                return serviceOrderListFragment;
            }
            return null;
        }
    }
}