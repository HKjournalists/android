package com.kplus.car.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.R;
import com.kplus.car.model.FWProviderInfo;
import com.kplus.car.model.ProviderComment;
import com.kplus.car.model.response.GetProviderCommentResponse;
import com.kplus.car.model.response.request.GetProviderCommentRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.RatingBar;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshListView;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EvaluationActivity extends BaseActivity implements OnClickListener {
	private ImageView ivGoBack;
	private RatingBar rbComprehensiveScore;
	private TextView tvEvaluationNumber;
	private PullToRefreshListView prlEvaluation;
	private ListView lvEvaluation;
	private View loadingLayout;
	private View emptyView;
	private LayoutInflater layoutInflater;
	private List<ProviderComment> evalautionList;
	private EvaluationAdapter adapter;
	private TextView mProviderName;
	private int pageNum;
	private int nTotal;
	private FWProviderInfo mProviderInfo;

	private static final int PAGE_SIZE = 10;

	@Override
	protected void initView() {
		setContentView(R.layout.daze_evaluation_layout);
		ivGoBack = (ImageView) findViewById(R.id.ivGoBack);
		rbComprehensiveScore = (RatingBar) findViewById(R.id.rbComprehensiveScore);
		tvEvaluationNumber = (TextView) findViewById(R.id.tvEvaluationNumber);
		prlEvaluation = (PullToRefreshListView) findViewById(R.id.prlEvaluation);
		prlEvaluation.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lvEvaluation = prlEvaluation.getRefreshableView();
		loadingLayout = findViewById(R.id.page_loading);
		mProviderName = (TextView) findViewById(R.id.provider_name);
	}

	@Override
	protected void loadData() {
		mProviderInfo = (FWProviderInfo) getIntent().getSerializableExtra("providerInfo");
		layoutInflater = LayoutInflater.from(this);
		evalautionList = new ArrayList<>();
		String name = mProviderInfo.getName();
		if (StringUtils.isEmpty(name))
			name = mProviderInfo.getPhone();
		mProviderName.setText(name);
		int count = mProviderInfo.getTotalServiceCount() != null ? mProviderInfo.getTotalServiceCount().intValue() : 0;
		tvEvaluationNumber.setText(String.valueOf(count));
		rbComprehensiveScore.setRating(5, mProviderInfo.getScore());
		adapter = new EvaluationAdapter();
		prlEvaluation.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		ivGoBack.setOnClickListener(this);
		prlEvaluation.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (evalautionList != null && !evalautionList.isEmpty())
					evalautionList.clear();
				if (emptyView != null && lvEvaluation.indexOfChild(emptyView) != -1)
					lvEvaluation.removeFooterView(emptyView);
				adapter.notifyDataSetChanged();
				getEvaluations();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		prlEvaluation.setRefreshing(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivGoBack:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void getEvaluations(){
		new AsyncTask<Void, Void, GetProviderCommentResponse>(){
			@Override
			protected GetProviderCommentResponse doInBackground(Void... params) {
				GetProviderCommentRequest request = new GetProviderCommentRequest();
				pageNum = evalautionList.size() / PAGE_SIZE + 1;
				request.setParams(mProviderInfo.getProviderId(), pageNum, PAGE_SIZE);
				try {
					return mApplication.client.execute(request);
				}catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(GetProviderCommentResponse response) {
				if (response != null && response.getCode() != null && response.getCode() == 0){
					if(response.getData().getTotal() != null)
						nTotal = response.getData().getTotal();
					List<ProviderComment> listTemp = response.getData().getList();
					if(listTemp != null && !listTemp.isEmpty()){
						if(evalautionList.isEmpty())
							evalautionList.addAll(listTemp);
						else{
							for(ProviderComment eva : listTemp){
								evalautionList.add(eva);
							}
						}
					}
					else{
						if(evalautionList.isEmpty()){
							if(emptyView == null){
								emptyView = layoutInflater.inflate(R.layout.daze_listview_empty_item, null);
								((TextView)emptyView.findViewById(R.id.tvEmpty)).setText("亲，您还没有评论记录");
							}
							if(lvEvaluation.indexOfChild(emptyView) == -1)
								lvEvaluation.addFooterView(emptyView);
						}
					}
					adapter.notifyDataSetChanged();
				}
				loadingLayout.setVisibility(View.GONE);
				if(prlEvaluation.isRefreshing())
					prlEvaluation.onRefreshComplete();
			}
		}.execute();
	}
	
	class EvaluationAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(evalautionList == null)
				return 0;
			return evalautionList.size();
		}

		@Override
		public Object getItem(int position) {
			if(evalautionList == null)
				return null;
			return evalautionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder = null;
			if(convertView == null){
				holder = new HashMap<String, Object>();
				convertView = layoutInflater.inflate(R.layout.daze_evaluation_item, parent, false);
				TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.put("tvName", tvName);
				RatingBar rbEvaluation = (RatingBar) convertView.findViewById(R.id.rbEvaluation);
				holder.put("rbEvaluation", rbEvaluation);
				TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				holder.put("tvTime", tvTime);
				TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
				holder.put("tvContent", tvContent);
				View llLoadmore = convertView.findViewById(R.id.llLoadmore);
				holder.put("llLoadmore", llLoadmore);
				TextView tvLoadmore = (TextView) convertView.findViewById(R.id.tvLoadmore);
				holder.put("tvLoadmore", tvLoadmore);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			TextView tvName = (TextView) holder.get("tvName");
			RatingBar rbEvaluation = (RatingBar) holder.get("rbEvaluation");
			TextView tvTime = (TextView) holder.get("tvTime");
			TextView tvContent = (TextView) holder.get("tvContent");
			View llLoadmore = (View) holder.get("llLoadmore");
			llLoadmore.setVisibility(View.GONE);
			TextView tvLoadmore = (TextView) holder.get("tvLoadmore");
			ProviderComment evaluation = evalautionList.get(position);
			tvName.setText(evaluation.getUserName() == null ? "" : evaluation.getUserName());
			rbEvaluation.setRating(5, evaluation.getScore() != null ? evaluation.getScore() : 0);
			tvTime.setText(evaluation.getCreateDatetime() == null ? "" : evaluation.getCreateDatetime().substring(0, evaluation.getCreateDatetime().indexOf(" ")));
			tvContent.setText(evaluation.getContent() == null ? "" : evaluation.getContent());
			if(position == evalautionList.size() -1){
				llLoadmore.setVisibility(View.VISIBLE);
				if(nTotal != 0 && evalautionList.size() == nTotal){
					tvLoadmore.setText("没有更多记录了");
					tvLoadmore.setTextColor(getResources().getColor(R.color.daze_grey));
				}
				else{
					tvLoadmore.setText("点击加载更多");
					tvLoadmore.setTextColor(getResources().getColor(R.color.daze_orangered5));
					tvLoadmore.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View v) {
							loadingLayout.setVisibility(View.VISIBLE);
							getEvaluations();
						}
					});
				}
			}
			return convertView;
		}
		
	}

}
