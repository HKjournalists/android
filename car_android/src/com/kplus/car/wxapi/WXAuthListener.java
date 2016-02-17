package com.kplus.car.wxapi;

import com.tencent.mm.sdk.modelbase.BaseResp;

public interface WXAuthListener {
	public void onAuthSuccess(BaseResp response);
	public void onAuthCancel(BaseResp response);
	public void onAuthFail(BaseResp response);
}
