package com.kplus.car.widget;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kplus.car.activity.BaseActivity;

public class HashMapAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Entry<String, Object>> dataList = new LinkedList<Map.Entry<String, Object>>();

	private int resource;
	private int key;
	private int value;

	public HashMapAdapter(BaseActivity context, Map<String, Object> map,
			int resource, int key, int value) {
		this.mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.key = key;
		this.value = value;
		init(map);
	}

	private void init(Map<String, Object> map) {
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			dataList.add(iterator.next());
		}
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Entry<String, Object> getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup vg) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(resource, null);
			holder.key = (TextView) convertView.findViewById(key);
			holder.value = (TextView) convertView.findViewById(value);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Entry<String, Object> entry = getItem(position);
		holder.key.setText(entry.getKey());
		holder.value.setText(entry.getValue().toString());

		return convertView;
	}

	private class Holder {
		TextView key;
		TextView value;
	}
}
