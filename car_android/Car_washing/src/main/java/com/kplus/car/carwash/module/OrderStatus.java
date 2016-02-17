package com.kplus.car.carwash.module;

/**
 * Description：订单状态
 * <br/><br/>Created by Fu on 2015/5/19.
 * <br/><br/>
 */
public enum OrderStatus {
    PAYING(21),                     //  本地新加的，支付中，服务端好像没此状态

    BOOKED(0),                      //  初始值,未下单
    UNABLE_PAY(1),                  //  无法支付
    PAY_PENDING(2),                 //  待支付
    PAID(3),                        //  已支付
    AUDIT_PENDING(5),               //  待审核
    AUDIT_FAILED(6),                //  审核失败
    ASSIGNED(7),                    //  已指派（审核通过）
    HANDLING(10),                   //  服务中（处理中）
    HANDLE_FAILED(11),              //  处理失败
    HANDLED(12),                    //  已完成（处理成功）
    REFUND_PENDING(13),             //  待退款
    REFUNDED(14),                   //  已退款
    REVIEWED(16),                   //  已评价
    CLOSED(20),                     //  已关闭
    DELETED(-1),                    //  已删除
    UNKOWN(-2);                     //  未知


    private final int value;

    OrderStatus(int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static OrderStatus valueOf(int v) {
        switch (v) {
            case 21:
                return PAYING;
            case 0:
                return BOOKED;
            case 1:
                return UNABLE_PAY;
            case 2:
                return PAY_PENDING;
            case 3:
                return PAID;
            case 5:
                return AUDIT_PENDING;
            case 6:
                return AUDIT_FAILED;
            case 7:
                return ASSIGNED;
            case 10:
                return HANDLING;
            case 11:
                return HANDLE_FAILED;
            case 12:
                return HANDLED;
            case 13:
                return REFUND_PENDING;
            case 14:
                return REFUNDED;
            case 16:
                return REVIEWED;
            case 20:
                return CLOSED;
            case -1:
                return DELETED;
        }
        return UNKOWN;
    }

    public String readableName() {
        switch (value) {
            case 21:
                return "支付中";
            case 0:
                return "已预定";
            case 1:
                return "无法支付";
            case 2:
                return "待支付";
            case 3:
                return "已支付";
            case 5:
                return "待审核";
            case 6:
                return "审核失败";
            case 7:
                return "已指派";
            case 10:
                return "服务中";
            case 11:
                return "处理失败";
            case 12:
                return "已完成";
            case 13:
                return "待退款";
            case 14:
                return "已退款";
            case 16:
                return "已评价";
            case 20:
                return "已关闭";
            case -1:
                return "已删除";
        }
        return "未知";
    }
}
