/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.kplus.car.widget.pulltorefresh.library;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase.Mode;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase.State;

public class LastUpdatedPullEventListener<V extends View> implements
		PullToRefreshBase.OnPullEventListener<V> {

	private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

	private String mVehicleNum;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - Context
	 */
	public LastUpdatedPullEventListener(String vehicleNum) {
		mVehicleNum = vehicleNum;
	}

	@Override
	public final void onPullEvent(PullToRefreshBase<V> refreshView,
			State event, Mode direction) {
		if (event.getIntValue() == State.PULL_TO_REFRESH.getIntValue()) {
			// 显示最后更新时间
			long reftime = 0;
			try {
				String _reftime = KplusApplication.getInstance().dbCache
						.getValue(KplusConstants.DB_KEY_AGAINST_QUERY_TIME
								+ mVehicleNum);
				reftime = _reftime == null ? 0 : Long.parseLong(_reftime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (reftime == 0) {
				reftime = System.currentTimeMillis();
			}
			long curTime = System.currentTimeMillis();
			long time = (curTime - reftime) / 1000;
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
					genRefTime(time, reftime));
		}
	}

	private String genRefTime(long time, long reftime) {
		if (time < 60) {
			return "刚刚更新";
		} else if (time < 60 * 60) {
			return time / 60 + "分钟前更新";
		} else if (time < 60 * 60 * 24) {
			return time / 3600 + "小时前更新";
		} else if (time < 60 * 60 * 24 * 30) {
			return time / 86400 + "天前更新";
		}
		return "更新于" + sdf.format(new Date(reftime));
	}
}
