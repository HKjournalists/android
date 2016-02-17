package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.File;
import java.io.Serializable;

/**
 * Description：缓存获取数据的城市工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class CNLocatedCityUtil extends CNBaseCacheDataUtil<CNLocatedCityUtil.LocatedCityEntity> {
    private static final String TAG = "CNLocatedCityUtil";

    private static final String FILE_NAME = "locatedCity_V1";

    private static CNLocatedCityUtil ins = null;

    public static CNLocatedCityUtil getIns() {
        if (null == ins) {
            synchronized (CNCitiesUtil.class) {
                ins = new CNLocatedCityUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNLocatedCityUtil(String fileName) {
        super(fileName);
    }

    public static class LocatedCityEntity implements Serializable {
        public long mRecommendCityId;
        public City mLocatedCity;
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
    public boolean add(Object... param) {
        long recommendCityId = (long) param[0];
        City locatedCity = (City) param[1];
        if (null == locatedCity) {
            return false;
        }
        LocatedCityEntity entity = get();
        if (null == entity) {
            entity = new LocatedCityEntity();
        }
        entity.mRecommendCityId = recommendCityId;
        entity.mLocatedCity = locatedCity;
        replaceQueues(entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        File file = getFile();
        if (null != file) {
            return FileUtils.deleteFile(file.toString());
        }
        return false;
    }

    public City getLocatedCity() {
        LocatedCityEntity entity = get();
        if (null != entity) {
            return entity.mLocatedCity;
        }
        return null;
    }

    public long getRecommendCityId() {
        LocatedCityEntity entity = get();
        if (null != entity) {
            return entity.mRecommendCityId;
        }
        return 0;
    }
}
