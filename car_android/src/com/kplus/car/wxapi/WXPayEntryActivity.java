package com.kplus.car.wxapi;

import com.kplus.car.KplusConstants;
import com.kplus.car.activity.BaseActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Fu on 2015/6/8.
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api = null;

    @Override
    protected void initView() {

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
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (mApplication.getWxPayListener() != null) {
            if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        mApplication.getWxPayListener().onPaySuccess(baseResp);
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        mApplication.getWxPayListener().onPayCancel(baseResp);
                        break;
                    default:
                        mApplication.getWxPayListener().onPayFail(baseResp);
                        break;
                }
            }
        }
        finish();
    }
}
