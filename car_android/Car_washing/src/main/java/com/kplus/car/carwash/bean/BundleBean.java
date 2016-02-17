package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/28.
 * <br/><br/>
 */
public class BundleBean<T, S> extends BaseInfo {
    private static final long serialVersionUID = 1;

    private long version;
    private List<T> updatedList;
    private List<S> deletedList;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<T> getUpdatedList() {
        return updatedList;
    }

    public void setUpdatedList(List<T> updatedList) {
        this.updatedList = updatedList;
    }

    public List<S> getDeletedList() {
        return deletedList;
    }

    public void setDeletedList(List<S> deletedList) {
        this.deletedList = deletedList;
    }
}
