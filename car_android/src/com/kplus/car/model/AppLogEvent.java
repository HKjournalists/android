package com.kplus.car.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/21.
 */
public class AppLogEvent {
    private String session_id;
    private String label;
    private double ts;
    private String tag;
    private String date;
    private String time;
    private HashMap<String, String> params;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getTs() {
        return ts;
    }

    public void setTs(double ts) {
        this.ts = ts;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
