package com.kplus.car.widget;

import java.util.HashMap;
import java.util.List;

import com.kplus.car.R;
import com.kplus.car.comm.DazeUserAccount;
import com.kplus.car.util.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<DazeUserAccount> list;
	
	public AccountAdapter(Context _context, List<DazeUserAccount> _list){
		context = _context;
		inflater = LayoutInflater.from(_context);
		list = _list;
	}

	@Override
	public int getCount() {
		if(list == null || list.isEmpty())
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		if(list == null || list.isEmpty())
			return null;
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		HashMap<String, Object> holder = null;
		if(arg1 == null){
			holder = new HashMap<String, Object>();
			arg1 = inflater.inflate(R.layout.daze_listview_item1, arg2, false);
			holder.put("tvAccountNickname", (TextView)arg1.findViewById(R.id.tvAccountNickname));
			arg1.setTag(holder);
		}
		else
			holder = (HashMap<String, Object>) arg1.getTag();
		TextView tvAccountNickname = (TextView) holder.get("tvAccountNickname");
		DazeUserAccount dua = list.get(arg0);
		if(!StringUtils.isEmpty(dua.getPhoneNumber())){
			tvAccountNickname.setText(dua.getPhoneNumber());
		}
		else{
			tvAccountNickname.setText(dua.getNickName());
		}
		return arg1;
	}

}
