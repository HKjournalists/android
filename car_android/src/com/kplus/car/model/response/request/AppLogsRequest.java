package com.kplus.car.model.response.request;

import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetResultResponse;

/**
 * Created by Administrator on 2015/9/21.
 */
public class AppLogsRequest extends BaseRequest<GetResultResponse> {

    @Override
    public String getServerUrl() {
        return KplusConstants.ANALYZE_URL;
    }

    @Override
    public String getApiMethodName() {
        return "/app_logs";
    }

    @Override
    public Class<GetResultResponse> getResponseClass() {
        return GetResultResponse.class;
    }

    public void setParams(String header, String body){
        addParams("header", header);
        addParams("body", body);
    }
}
