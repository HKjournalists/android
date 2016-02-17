package com.kplus.car.carwash.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Fu on 2015/5/17.
 */
public class FetchUsableCouponsResp extends BaseInfo {
    private List<Coupon> coupons;
    /**
     * 所有代金券的总金额
     */
    private BigDecimal couponPrice;
    /**
     * 服务总费用
     */
    private BigDecimal serviceAmount;

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }
}
