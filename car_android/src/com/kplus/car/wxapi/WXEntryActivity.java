package com.kplus.car.wxapi;

import com.kplus.car.KplusConstants;
import com.kplus.car.activity.BaseActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	private IWXAPI api;
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadData() {
		try {
			api = WXAPIFactory.createWXAPI(this, KplusConstants.WECHAT_APPID, true);
			api.handleIntent(getIntent(), this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(BaseResp arg0) {
		if(mApplication.getWxShareListener() != null){
			switch (arg0.errCode) {
	        case BaseResp.ErrCode.ERR_OK:  
	            mApplication.getWxShareListener().onShareSuccess(arg0);
	            break;  
	        case BaseResp.ErrCode.ERR_USER_CANCEL:  
	        	mApplication.getWxShareListener().onShareCancel(arg0);
	            break;  
//	        case BaseResp.ErrCode.ERR_AUTH_DENIED:
	        default:
	        	mApplication.getWxShareListener().onShareFail(arg0);
	            break;  
	        }
		}
		else if(mApplication.getWxPayListener() != null){
			if (arg0.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
				switch(arg0.errCode){
				case BaseResp.ErrCode.ERR_OK:  
		            mApplication.getWxPayListener().onPaySuccess(arg0);
		            break;  
		        case BaseResp.ErrCode.ERR_USER_CANCEL:  
		        	mApplication.getWxPayListener().onPayCancel(arg0);
		            break;  
		        default:
		        	mApplication.getWxPayListener().onPayFail(arg0);
		            break; 
				}
			}
		}
		else if(mApplication.getWxAuthListener() != null){
			switch (arg0.errCode) {
	        case BaseResp.ErrCode.ERR_OK:
	            mApplication.getWxAuthListener().onAuthSuccess(arg0);
	            break;  
	        case BaseResp.ErrCode.ERR_USER_CANCEL:  
	        	mApplication.getWxAuthListener().onAuthCancel(arg0);
	            break;  
	        default:
	        	mApplication.getWxAuthListener().onAuthFail(arg0);
	            break;  
	        }
		}
		finish();
	}
	
}
