package com.kplus.car.fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.OrderActivity;
import com.kplus.car.activity.OrderListActivity;
import com.kplus.car.model.Order;
import com.kplus.car.model.json.GetOrderListJson;
import com.kplus.car.model.response.GetOrderListResponse;
import com.kplus.car.model.response.request.GetOrderListRequest;
import com.kplus.car.util.BroadcastReceiverUtil;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshListView;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase.Mode;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FormOrderListFragment extends Fragment implements BroadcastReceiverUtil.BroadcastListener {
	private TextView tvEmpty;
	private PullToRefreshListView refreshListView;
	private LayoutInflater inflater;
	private int orderStatus;
	private DecimalFormat df = new DecimalFormat("#.##");
	private SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfnew = new SimpleDateFormat("MM-dd");
	private List<Order> mainList = new ArrayList<Order>();
	private OrderListAdapter adapter = new OrderListAdapter(mainList);
	private int nPage = 1;
    private int nTotal = 0;
	private KplusApplication mApplication;
	private BroadcastReceiver paySuccessReceiver;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.daze_fragment_order_list, container, false);
		refreshListView = (PullToRefreshListView) view.findViewById(R.id.order_list_body);
		refreshListView.setMode(Mode.PULL_FROM_START);
		tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
		refreshListView.getLoadingLayoutProxy().setRefreshingLabel("加载中...");
        refreshListView.setAdapter(adapter);
		setListeners();
		paySuccessReceiver = BroadcastReceiverUtil.createBroadcastReceiver(this);
		return view;
	}
	
	private void setListeners(){
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mainList.clear();
				adapter.notifyDataSetChanged();
				tvEmpty.setVisibility(View.GONE);
				getOrderList();
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		orderStatus = ((OrderListActivity)getActivity()).getOrderStatus();
		mApplication = (KplusApplication) getActivity().getApplication();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		BroadcastReceiverUtil.registerReceiver(getActivity(), BroadcastReceiverUtil.ACTION_PAY_SUCCESS, paySuccessReceiver);
        if(!refreshListView.isRefreshing()) {
			refreshListView.setRefreshing(false);
        }
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		BroadcastReceiverUtil.unRegisterReceiver(getActivity(), paySuccessReceiver);
	}

	private void getOrderList(){
		new AsyncTask<Void, Void, GetOrderListResponse>(){
			@Override
			protected GetOrderListResponse doInBackground(Void... params) {
				try{
					GetOrderListRequest request = new GetOrderListRequest();
					nPage = mainList.size()/KplusConstants.PAGE_SIZE + 1;
					request.setParams(mApplication.getpId(), nPage, orderStatus);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(GetOrderListResponse result) {
				try{
					if(refreshListView.isRefreshing())
						refreshListView.onRefreshComplete();
					if(result != null && result.getCode() != null && result.getCode() == 0){
						GetOrderListJson data = result.getData();
						if(data != null){
                            nTotal = data.getTotal();
							List<Order> listTemp = result.getData().getList();
							if(listTemp != null && !listTemp.isEmpty()){
								List<Order> mainTemp = new ArrayList<Order>();
								for(Order order : listTemp){
									if(!containsOrder(mainList, order)){
										mainTemp.add(order);
									}
								}
								if(!mainTemp.isEmpty()){
									mainList.addAll(mainTemp);
								}
								adapter.notifyDataSetChanged();
							}
						}
						
					}
					showEmptyView();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}.execute();
	}
	
	private void showEmptyView(){
		if(mainList == null || mainList.size() == 0)
			tvEmpty.setVisibility(View.VISIBLE);
		else
			tvEmpty.setVisibility(View.GONE);
	}
	
	private boolean containsOrder(List<Order> list, Order order){
		boolean result = false;
		long id = order.getId();
		for(Order so : list){
			if(so.getId().longValue() == id){
				result = true;
				break;
			}
		}		
		return result;
	}
	
	class OrderListAdapter extends BaseAdapter{
		private List<Order> list;
		
		public OrderListAdapter(List<Order> _list){
			list = _list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			if(list == null)
				return null;
			else if(list.isEmpty())
				return null;
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.daze_order_list_item, parent, false);
				holder = new HashMap<String, Object>();
				TextView status = (TextView) convertView.findViewById(R.id.order_listItem_status);
				TextView veihcleNum = (TextView) convertView.findViewById(R.id.order_listItem_vehicleNum);
				TextView content = (TextView) convertView.findViewById(R.id.order_listItem_content);
				TextView time = (TextView) convertView.findViewById(R.id.order_listItem_time);
				TextView price = (TextView) convertView.findViewById(R.id.order_listItem_price);
                View llLoadMore = convertView.findViewById(R.id.llLoadMore);
                TextView tvLoadmore = (TextView) convertView.findViewById(R.id.tvLoadmore);
                View rootView = convertView.findViewById(R.id.rootView);
				holder.put("status", status);
				holder.put("veihcleNum", veihcleNum);
				holder.put("content", content);
				holder.put("time", time);
				holder.put("price", price);
                holder.put("llLoadMore", llLoadMore);
                holder.put("tvLoadmore", tvLoadmore);
                holder.put("rootView", rootView);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			TextView status = (TextView) holder.get("status");
			TextView veihcleNum = (TextView) holder.get("veihcleNum");
			TextView content = (TextView) holder.get("content");
			TextView time = (TextView) holder.get("time");
			TextView price = (TextView) holder.get("price");
            View llLoadMore = (View) holder.get("llLoadMore");
            TextView tvLoadmore = (TextView) holder.get("tvLoadmore");
            View rootView = (View) holder.get("rootView");
            llLoadMore.setVisibility(View.GONE);
			Order order = list.get(position);
			if(order.getStatus() == 2){
				if(mApplication.dbCache.containOrderId(order.getId()).equals("true")){
					status.setText("支付确认中");
				}
				else{
					status.setText(KplusConstants.orderStatus(order.getStatus()));
				}
			}
			else
				status.setText(KplusConstants.orderStatus(order.getStatus()));
			switch(order.getStatus()){
				case 2:
				case 3:
				case 4:
				case 6:
				case 7:
					status.setTextColor(getResources().getColor(R.color.daze_orangered5));
					break;
				case 5:
				case 10:
				case 13:
					status.setTextColor(getResources().getColor(R.color.daze_orangered4));
					break;
				case 11:
				case 20:
				case 14:
				case -1:
					status.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
					break;
				case 12:
					status.setTextColor(getResources().getColor(R.color.daze_load_normal_end));
					break;
			}
			veihcleNum.setText(order.getVehicleNum());
			content.setText(order.getContent());
			String timeTemp = "";
			try{
				Date date = sdfold.parse(order.getOrderTime());
				timeTemp = sdfnew.format(date);
			}catch(Exception e){
				e.printStackTrace();
			}
			time.setText(timeTemp);
			if(order.getPrice() > 1)
				price.setText("¥" + order.getPrice().intValue());
			else
				price.setText("¥" + df.format(order.getPrice()));
            if(position == list.size() - 1){
                llLoadMore.setVisibility(View.VISIBLE);
                if(nTotal != 0 && mainList.size() == nTotal){
                    tvLoadmore.setText("没有更多记录了");
                    tvLoadmore.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
                }
                else{
                    tvLoadmore.setText("点击加载更多");
                    tvLoadmore.setTextColor(getResources().getColor(R.color.daze_orangered5));
                }
            }
            else
                llLoadMore.setVisibility(View.GONE);
            tvLoadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(nTotal != 0 && mainList.size() == nTotal))
                        getOrderList();
                }
            });
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> item = new HashMap<String, String>();
                    item.put("serviceType", "0");
					if(list != null && !list.isEmpty()) {
                        if(position >=0 && position < list.size()) {
                            Order order = list.get(position);
                            if(order != null && order.getId() != null) {
                                Intent intent = new Intent(mApplication, OrderActivity.class);
                                intent.putExtra("orderId", order.getId());
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
			return convertView;
		}
		
	}

	@Override
	public void onReceiverBroadcast(Intent data) {
		if(!refreshListView.isRefreshing())
			refreshListView.setRefreshing(false);
	}
}
