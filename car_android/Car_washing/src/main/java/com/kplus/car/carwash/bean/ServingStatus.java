package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/17.
 */
public class ServingStatus extends BaseInfo {
    private long id;

    private ServingTime time;
    private int status;

    private Integer max;
    private int cache;
    private int bookedCounter;
    private int canceledCounter;
    private int assignedCounter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ServingTime getTime() {
        return time;
    }

    public void setTime(ServingTime time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public int getCache() {
        return cache;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }

    public int getBookedCounter() {
        return bookedCounter;
    }

    public void setBookedCounter(int bookedCounter) {
        this.bookedCounter = bookedCounter;
    }

    public int getCanceledCounter() {
        return canceledCounter;
    }

    public void setCanceledCounter(int canceledCounter) {
        this.canceledCounter = canceledCounter;
    }

    public int getAssignedCounter() {
        return assignedCounter;
    }

    public void setAssignedCounter(int assignedCounter) {
        this.assignedCounter = assignedCounter;
    }
}
