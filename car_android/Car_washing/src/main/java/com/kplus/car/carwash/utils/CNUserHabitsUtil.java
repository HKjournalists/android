package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.Contact;
import com.kplus.car.carwash.bean.Position;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Description：缓存用户消费习惯工具类
 * <br/><br/>Created by Fu on 2015/5/28.
 * <br/><br/>
 */
public class CNUserHabitsUtil extends CNBaseCacheDataUtil<HashMap<Long, CNUserHabitsUtil.UserHabitsEntity>> {
    private static final String TAG = "CNUserHabitsUtil";

    private static final String FILE_NAME = "userHabits_V1";

    private static CNUserHabitsUtil ins = null;

    public static CNUserHabitsUtil getIns() {
        if (null == ins) {
            synchronized (CNUserHabitsUtil.class) {
                ins = new CNUserHabitsUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNUserHabitsUtil(String fileName) {
        super(fileName);
    }

    public static class UserHabitsEntity implements Serializable {
        public long mCityId;
        public Contact mContact;
        public Car mCar;
        public Position mPosition;
        public long mUsedServiceId;
    }

    public static void save() {
        getIns().saveData();
    }

    @Override
    public void saveData() {
        if (null != getIns().mQueues
                && null != getIns().getFile()) {
            FileUtils.saveObject(getIns().mQueues, getIns().getFile());
        }
    }

    @Override
    public HashMap<Long, UserHabitsEntity> get() {
        HashMap<Long, UserHabitsEntity> map = super.get();
        if (null == map) {
            map = new HashMap<>();
            replaceQueues(map);
        }
        return map;
    }

    @Override
    public boolean add(Object... param) {
        long cityId = (long) param[0];
        Contact contact = (Contact) param[1];
        Car car = (Car) param[2];
        Position position = (Position) param[3];
        long usedServiceId = (long) param[4];

        UserHabitsEntity entity = get().get(cityId);
        if (null == entity) {
            entity = new UserHabitsEntity();
        }

        entity.mCityId = cityId;
        entity.mContact = contact;
        entity.mCar = car;
        entity.mPosition = position;
        entity.mUsedServiceId = usedServiceId;
        get().put(cityId, entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        return false;
    }

    public long getUsedServiceId(long cityId) {
        UserHabitsEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mUsedServiceId;
        }
        return Const.NONE;
    }

    public Contact getContact(long cityId) {
        UserHabitsEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mContact;
        }
        return null;
    }

    public Car getCar(long cityId) {
        UserHabitsEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mCar;
        }
        return null;
    }

    public Position getPosition(long cityId) {
        UserHabitsEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mPosition;
        }
        return null;
    }
}
