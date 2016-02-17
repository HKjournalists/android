package com.kplus.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.model.BaoyangBrand;

import java.util.List;

/**
 * Created by Administrator on 2015/8/5.
 */
public class BaoyangBrandAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<BaoyangBrand> list;

    private class ViewHolder {
        TextView title;
        TextView name;
    }

    public BaoyangBrandAdapter(Context context, List<BaoyangBrand> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public BaoyangBrand getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_baoyang_brand, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.model_select_item_title);
            holder.name = (TextView) convertView.findViewById(R.id.model_select_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).getName());
        String currentStr = getItem(position).getInitial();
        String previewStr = (position - 1) >= 0 ? getItem(position - 1).getInitial() : " ";
        if (!previewStr.equals(currentStr)) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(currentStr);
        } else {
            holder.title.setVisibility(View.GONE);
        }
        return convertView;
    }
}
