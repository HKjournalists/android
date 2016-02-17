package com.kplus.car.widget;

import com.kplus.car.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimeSetAdapter extends com.kplus.car.widget.antistatic.spinnerwheel.adapters.NumericWheelAdapter {
	private int nMax;
	public TimeSetAdapter(Context context, int minValue, int maxValue, String format) {
		super(context, minValue, maxValue, format);
		setItemResource(R.layout.daze_listview_item5);
		setItemTextResource(R.id.tvTextView);
		nMax = maxValue;
	}

	
	@Override
    public View getItem(int index, View convertView, ViewGroup parent){
		View view = super.getItem(index, convertView, parent);
		if(view != null){
			TextView tvTextView = (TextView) view.findViewById(R.id.tvTextView);
			tvTextView.setText(getItemText(index));
			if(nMax == 23){
				tvTextView.setGravity(Gravity.RIGHT);
			}
			else if(nMax == 59){
				tvTextView.setGravity(Gravity.LEFT);
			}
		}
        return view;
	}
}
