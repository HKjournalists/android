package com.kplus.car.carwash.bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Fu on 2015/5/19.
 */
public class OrderLog extends BaseInfo {
    private long id;
    private Admin operator;
    private Staff worker;
    private String action;
    private String content;
    private Timestamp createTime;

    private List<String> images;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Admin getOperator() {
        return operator;
    }

    public void setOperator(Admin operator) {
        this.operator = operator;
    }

    public Staff getWorker() {
        return worker;
    }

    public void setWorker(Staff worker) {
        this.worker = worker;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
