package com.kplus.car.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kplus.car.R;
import com.kplus.car.model.Coupon;
import com.kplus.car.util.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CashSelectAdapter extends com.kplus.car.widget.antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter{
	private List<Coupon> list;
	private SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
	private SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd");

	public CashSelectAdapter(Context context) {
		super(context, R.layout.daze_cash_select_item, NO_RESOURCE);
		setItemTextResource(R.id.tvCashInfo);
	}

	@Override
	public int getItemsCount() {
		if(list == null)
			return 0;
		return list.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		if(list == null)
			return null;
		Coupon coupon = list.get(index);
		if(coupon.getAmount() == null)
			return coupon.getName();
		return coupon.getName() + "¥" + coupon.getAmount().intValue();
	}
	
	@Override
    public View getItem(int index, View convertView, ViewGroup parent) {
		View view = super.getItem(index, convertView, parent);
		if(view != null){
			TextView tvCashDate = (TextView) view.findViewById(R.id.tvCashDate);
			Coupon coupon = list.get(index);
			String timeTemp = coupon.getEndTime();
			if(StringUtils.isEmpty(timeTemp)){
				tvCashDate.setVisibility(View.GONE);
			}
			else{
				tvCashDate.setVisibility(View.VISIBLE);
				try{
					Date date = sdfold.parse(timeTemp);
					timeTemp = sdfnew.format(date);
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
					tvCashDate.setText(timeTemp + "到期");
				}
			}
		}
        return view;
    }

	public List<Coupon> getList() {
		return list;
	}

	public void setList(List<Coupon> list) {
		this.list = list;
	}

}
