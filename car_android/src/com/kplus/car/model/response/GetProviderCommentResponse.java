package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetProviderCommentJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/24.
 */
public class GetProviderCommentResponse extends Response {
    @ApiField("data")
    private GetProviderCommentJson data;

    public GetProviderCommentJson getData() {
        if (data == null)
            data = new GetProviderCommentJson();
        return data;
    }

    public void setData(GetProviderCommentJson data) {
        this.data = data;
    }
}
