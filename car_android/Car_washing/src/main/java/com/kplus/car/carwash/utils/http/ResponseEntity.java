package com.kplus.car.carwash.utils.http;

import com.kplus.car.carwash.common.Const;

import java.io.Serializable;

public class ResponseEntity implements Serializable {

    private static final long serialVersionUID = 4691805279783501287L;

    public static final int REQUEST_OK = 200;

    public int mStatusCode = Const.NEGATIVE;
    public String mUrl;
    public String mResult;
    public String mFuncKey;

    @Override
    public String toString() {
        return "ResponseEntity [status=" + mStatusCode + ", url=" + mUrl + ", result=" + mResult + " funcKey=" + mFuncKey + "]";
    }
}
