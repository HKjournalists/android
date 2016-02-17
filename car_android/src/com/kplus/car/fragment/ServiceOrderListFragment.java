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
import com.kplus.car.activity.OrderListActivity;
import com.kplus.car.activity.VehicleServiceActivity;
import com.kplus.car.model.FWOrder;
import com.kplus.car.model.json.FWOrderJson;
import com.kplus.car.model.response.FWOrderResponse;
import com.kplus.car.model.response.request.FWOrderRequest;
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

public class ServiceOrderListFragment extends Fragment implements BroadcastReceiverUtil.BroadcastListener {
	private TextView tvEmpty;
	private PullToRefreshListView refreshListView;
	private LayoutInflater inflater;
	private int orderStatus;
	private DecimalFormat df = new DecimalFormat("#.##");
	private SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfnew = new SimpleDateFormat("MM-dd");
    private List<FWOrder> mainList = new ArrayList<FWOrder>();
    private OrderListAdapterQichebao adapter = new OrderListAdapterQichebao(mainList);
	private int nPage = 1;
    private int nTotal;
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
                new GetServiceOrdersTask().execute();
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

	class GetServiceOrdersTask extends AsyncTask<Void, Void, FWOrderResponse>{
		@Override
		protected FWOrderResponse doInBackground(Void... params) {
			try{
				FWOrderRequest request = new FWOrderRequest();
				nPage= mainList.size()/KplusConstants.PAGE_SIZE + 1;
				request.setParams(mApplication.getId(), nPage, KplusConstants.PAGE_SIZE, orderStatus);
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}			
		}
		
		@Override
		protected void onPostExecute(FWOrderResponse result) {
			try{
				if(refreshListView.isRefreshing())
					refreshListView.onRefreshComplete();
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						FWOrderJson data = result.getData();
						if(data != null){
                            nTotal = data.getTotal();
							List<FWOrder> listTemp = result.getData().getList();
							List<FWOrder> mainTemp = new ArrayList<FWOrder>();
							List<FWOrder> oldTemp = new ArrayList<FWOrder>();
							for(FWOrder order : listTemp){
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
			super.onPostExecute(result);
		}
	}

	private void showEmptyView(){
		if(mainList == null || mainList.size() == 0)
			tvEmpty.setVisibility(View.VISIBLE);
		else
			tvEmpty.setVisibility(View.GONE);
	}
	
	private boolean containsOrder(List<FWOrder> list, FWOrder order){
		boolean result = false;
		long id = order.getOrderId();
		for(FWOrder so : list){
			if(so.getOrderId().longValue() == id){
				result = true;
				break;
			}
		}		
		return result;
	}
	
	class OrderListAdapterQichebao extends BaseAdapter{
		private List<FWOrder> list;
		
		public OrderListAdapterQichebao(List<FWOrder> _list){
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
				holder = new HashMap<String, Object>();
				convertView = inflater.inflate(R.layout.daze_qichebao_order_list_item, parent, false);
				TextView status = (TextView) convertView.findViewById(R.id.order_listItem_status);
				TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				TextView tvService = (TextView) convertView.findViewById(R.id.tvService);
				TextView tvFacilitator = (TextView) convertView.findViewById(R.id.tvFacilitator);
                View llLoadMore = convertView.findViewById(R.id.llLoadMore);
                TextView tvLoadmore = (TextView) convertView.findViewById(R.id.tvLoadmore);
                View rootView = convertView.findViewById(R.id.rootView);
				holder.put("status", status);
				holder.put("tvTime", tvTime);
				holder.put("tvService", tvService);
				holder.put("tvFacilitator", tvFacilitator);
                holder.put("llLoadMore", llLoadMore);
                holder.put("tvLoadmore", tvLoadmore);
                holder.put("rootView", rootView);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			TextView status = (TextView) holder.get("status");
			TextView tvTime = (TextView) holder.get("tvTime");
			TextView tvService = (TextView) holder.get("tvService");
			TextView tvFacilitator = (TextView) holder.get("tvFacilitator");
            View llLoadMore = (View) holder.get("llLoadMore");
            TextView tvLoadmore = (TextView) holder.get("tvLoadmore");
            View rootView = (View) holder.get("rootView");
            llLoadMore.setVisibility(View.GONE);
			FWOrder fwOrder = list.get(position);
			if(fwOrder.getStatus() == 2){
				if(mApplication.dbCache.containOrderId(fwOrder.getOrderId()).equals("true")){
					status.setText("支付确认中");
				}
				else{
					status.setText(KplusConstants.orderStatus(fwOrder.getStatus()));
				}
			}
			else
				status.setText(KplusConstants.c2cOrderStatus(fwOrder.getStatus()));
			switch(fwOrder.getStatus()){
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

			tvService.setText(fwOrder.getServiceName());
			tvFacilitator.setText(fwOrder.getProviderName() != null ? fwOrder.getProviderName() : "");
			String timeTemp = "";
			try{
				Date date = sdfold.parse(fwOrder.getOrderTime());
				timeTemp = sdfnew.format(date);
			}catch(Exception e){
				e.printStackTrace();
			}
			tvTime.setText(timeTemp);
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
                        new GetServiceOrdersTask().execute();
                }
            });
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> item = new HashMap<String, String>();
                    item.put("serviceType", "1");
					FWOrder order = list.get(position);
                    long orderOd = order.getOrderId();
                    Intent intent = new Intent(mApplication, VehicleServiceActivity.class);
                    int orderType = order.getOrderType();
                    if(orderType == 5){
                        intent.putExtra("appId", "10000012");
                        intent.putExtra("startPage", "order-detail.html?id=" + orderOd + "&uid=" + mApplication.getId() + "&pid=" + mApplication.getpId());
                    }
                    else{
                        intent.putExtra("appId", "10000009");
                        intent.putExtra("startPage", "detail.html?orderId=" + orderOd);
                    }
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, 0);
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
