package com.kplus.car.carwash.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Fu on 2015/5/19.
 */
public class ServiceOrder extends BaseInfo {
    private long id;
    private long formId;
    private String formOrderNo;
    private long uid;
    private long pid;
    private long userId;

    private Contact contact;
    private List<OnSiteService> services;
    private Car car;
    private Position carPosition;
    private ServingTime servingTime;
    private List<Coupon> usedCoupons;
    private BigDecimal price;
    private BigDecimal couponPrice;
    private BigDecimal reducePrice;

    private int status;
    private Timestamp createTime;
    private Timestamp updateTime;

    private Integer balancePay;
    private Float cashPay;
    private Date payTime;

    private Payment payment;

    private City city;
    private int servingStatus;
    private Staff worker;
    private Timestamp actualBeginTime;
    private Timestamp actualEndTime;

    private Review review;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getFormOrderNo() {
        return formOrderNo;
    }

    public void setFormOrderNo(String formOrderNo) {
        this.formOrderNo = formOrderNo;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<OnSiteService> getServices() {
        return services;
    }

    public void setServices(List<OnSiteService> services) {
        this.services = services;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Position getCarPosition() {
        return carPosition;
    }

    public void setCarPosition(Position carPosition) {
        this.carPosition = carPosition;
    }

    public ServingTime getServingTime() {
        return servingTime;
    }

    public void setServingTime(ServingTime servingTime) {
        this.servingTime = servingTime;
    }

    public List<Coupon> getUsedCoupons() {
        return usedCoupons;
    }

    public void setUsedCoupons(List<Coupon> usedCoupons) {
        this.usedCoupons = usedCoupons;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getReducePrice() {
        return reducePrice;
    }

    public void setReducePrice(BigDecimal reducePrice) {
        this.reducePrice = reducePrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getBalancePay() {
        return balancePay;
    }

    public void setBalancePay(Integer balancePay) {
        this.balancePay = balancePay;
    }

    public Float getCashPay() {
        return cashPay;
    }

    public void setCashPay(Float cashPay) {
        this.cashPay = cashPay;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getServingStatus() {
        return servingStatus;
    }

    public void setServingStatus(int servingStatus) {
        this.servingStatus = servingStatus;
    }

    public Staff getWorker() {
        return worker;
    }

    public void setWorker(Staff worker) {
        this.worker = worker;
    }

    public Timestamp getActualBeginTime() {
        return actualBeginTime;
    }

    public void setActualBeginTime(Timestamp actualBeginTime) {
        this.actualBeginTime = actualBeginTime;
    }

    public Timestamp getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Timestamp actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
