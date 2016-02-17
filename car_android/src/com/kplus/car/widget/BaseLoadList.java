package com.kplus.car.widget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseLoadList<I> extends BaseAdapter {
	/***
	 * 结果集
	 */
	public LinkedList<I> linkeList;
	protected Activity context;

	protected View footerView;
	protected AsyncTask<?, ?, ?> loadingTask;
	/****
	 * 标记是否正在加载
	 */
	protected boolean isLoading = false;

	/****
	 * 
	 * @param context
	 * @param response
	 * @param req
	 * @param v
	 *            列表本身
	 * @param layoutId
	 *            单个
	 * @param totle
	 *            单个总数
	 */
	public BaseLoadList() {
		super();
	}

	public BaseLoadList(Activity context) {
		this.context = context;
		linkeList = new LinkedList<I>();
		initView();
	}

	protected void initView() {
		if (isLoading) {
			return;
		}
		showLoading(true);
		loadFirst();
	}

	protected void loadFirst() {
		isLoading = true;
		loadingTask = new AsyncTask<Object, Exception, List<I>>() {
			@Override
			protected List<I> doInBackground(Object... params) {
				try {
					return executeFirst();
				} catch (Exception e) {
					e.printStackTrace();
					publishProgress(e);
					return null;
				}
			}

			@Override
			protected void onProgressUpdate(Exception... values) {
				super.onProgressUpdate(values);
				onErro(values[0]);
			}

			@Override
			protected void onPostExecute(List<I> o) {
				loadCallBack(o);
			}
		}.execute();
	}

	protected void loadCallBack(List<I> rs) {
		if (rs != null) {
			linkeList.addAll(rs);
		}
		notifyDataSetChanged();
		isLoading = false;
		showLoading(false);
	}

	@Override
	public View getView(int id, View v, ViewGroup vg) {
		Map<String, Object> holder;
		if (v != null && getItemViewType(id) != IGNORE_ITEM_VIEW_TYPE
				&& v.getTag() != null) {
			holder = (HashMap<String, Object>) v.getTag();
			if ((Integer) holder.get("layoutId") != getLayoutId(id)) {
				LayoutInflater in = LayoutInflater.from(context);
				v = in.inflate(getLayoutId(id), vg, false);

				holder = getHolder(v);
				holder.put("layoutId", getLayoutId(id));
				v.setTag(holder);
			}
		} else {
			LayoutInflater in = LayoutInflater.from(context);
			v = in.inflate(getLayoutId(id), vg, false);
			holder = getHolder(v);
			holder.put("layoutId", getLayoutId(id));
			v.setTag(holder);
		}
		initItem(getItem(id), holder);
		return v;
	}

	public void destroy() {
		if (isLoading) {
			loadingTask.cancel(true);
		}
	}

	@Override
	public int getCount() {
		return linkeList.size();
	}

	public boolean isLoading() {
		return isLoading;
	}

	@Override
	public I getItem(int id) {
		return linkeList.get(id);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	public abstract void initItem(I it, Map<String, Object> holder);

	public abstract Map<String, Object> getHolder(View v);

	public abstract List<I> executeFirst() throws Exception;

	public abstract int getLayoutId(int index);

	public abstract void showLoading(boolean show);

	public interface ViewHandlr<I> {
		public int getLayoutId();

		public Map<String, Object> getHolder(View v);

		public void initItem(I it, Map<String, Object> holder);
	}

	public void onErro(Exception e) {
		e.printStackTrace();
	}

}
