package com.kplus.car.carwash.bean;

import java.io.Serializable;

public class BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String result;
    private int errorCode;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("errorCode=").append(errorCode);
        buffer.append(",result=").append(result);
        buffer.append(",msg=").append(msg);
        return buffer.toString();
    }
}
