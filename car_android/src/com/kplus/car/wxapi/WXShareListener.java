package com.kplus.car.wxapi;

import com.tencent.mm.sdk.modelbase.BaseResp;

public interface WXShareListener {
	public void onShareSuccess(BaseResp response);
	public void onShareCancel(BaseResp response);
	public void onShareFail(BaseResp response);
}
