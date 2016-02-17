package com.kplus.car.wxapi;

import com.tencent.mm.sdk.modelbase.BaseResp;

public interface WXPayListener {
	public void onPaySuccess(BaseResp response);
	public void onPayCancel(BaseResp response);
	public void onPayFail(BaseResp response);
}
