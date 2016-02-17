package com.kplus.car.widget;

import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.activity.BaseActivity;

public abstract class BasePageListAdapter<I> extends BaseLoadList<I> {

	/****
	 * 标记是否加载完全
	 */
	protected boolean isAll = false;
	
	/****
	 * 
	 * @param context
	 * @param response
	 * @param req
	 * @param v 列表本身
	 * @param layoutId 单个
	 * @param totle 单个总数
	 */
	public BasePageListAdapter(BaseActivity context) {
		super(context);
	}


	protected void loadNextPage()  {
		if(isLoading || isAll){
			return;
		}
		isLoading = true;
		loadingTask  = new AsyncTask<Object, Exception, List<I>>() {
			@Override
			protected List<I> doInBackground(Object... params) {
				try {
					return executeNext();
				} catch (Exception e) {
					publishProgress(e);
					return null;
				}
			}
			@Override
			protected void onProgressUpdate(Exception... values) {
				super.onProgressUpdate(values);
				onErro( values[0]);
			}
			@Override
			protected void onPostExecute(List<I> o) {
				loadCallBack(o);
			}
		}.execute();
	}
	protected void loadFirst()  {
		if(isLoading || isAll){
			return;
		}else{
			super.loadFirst();
		}
	}
	

	protected void loadCallBack(List<I> rs) {
		if(rs != null){
			// TODO排重
			linkeList.addAll(rs);
		}
		isLoading = false;
		if(isAll(linkeList) ){
			isAll = true;
			showLoading(false);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int id, View v, ViewGroup vg) {
		v = super.getView(id, v, vg);
		if(id >= linkeList.size() - 2 ){
			loadNextPage();
		}
		return v;
	}
	public LinkedList<I> getList(){
		return linkeList;
	}
	
	
	public abstract List<I> executeNext() throws Exception;
	
	public abstract boolean isAll(List<I> linkeList);
	
	public void onErro(Exception e){
		e.printStackTrace();
	}
}
