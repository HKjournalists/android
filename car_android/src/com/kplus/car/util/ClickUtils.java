package com.kplus.car.util;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

public class ClickUtils {

	public interface NoFastClickListener{
		public void onNoFastClick(View v);
	}
	
	public static void setNoFastClickListener(View v, final NoFastClickListener l){
		if(v == null || l == null)
			return;
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isFastClick())
					return;
				l.onNoFastClick(v);
			}
		});
	}
	
	private static long lastClickTime;
    private static boolean isFastClick() {
        long time = SystemClock.uptimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
