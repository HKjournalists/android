package com.kplus.car.carwash.module;

/**
 * Description：日志状态
 * <br/><br/>Created by Fu on 2015/5/20.
 * <br/><br/>
 */
public enum LogAction {
    ORDER, // 用户下单
    CHANGE_SERVING_TIME, // 重新分配时间
    ASSIGN, // 指派
    REASSIGN, // 重新指派
    START, // 开始服务
    FINISH, // 结束服务
    CANCEL, // 取消
    CLOSE, // 关闭
    CHANGE_STATUS,
    REVIEW,
    UNPAID_CANCEL;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
