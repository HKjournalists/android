package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description：缓存城市列表工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class CNCitiesUtil extends CNBaseCacheDataUtil<CNCitiesUtil.CitiesEntity> {

    private static final String TAG = "CNCitiesUtil";

    private static final String FILE_NAME = "cities_V1";

    private static CNCitiesUtil ins = null;

    public static CNCitiesUtil getIns() {
        if (null == ins) {
            synchronized (CNCitiesUtil.class) {
                ins = new CNCitiesUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNCitiesUtil(String fileName) {
        super(fileName);
    }

    public static class CitiesEntity implements Serializable {
        public long mVersion;
        public ArrayList<City> mCities;
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

    private void sort(List<City> cities) {
        // 排序
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() == o2.getId()) {
                    return 0;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }
                return -1;
            }
        });
    }

    @Override
    public boolean add(Object... param) {
        long version = (long) param[0];
        List<City> cities = (List<City>) param[1];

        if (null == cities || cities.isEmpty()) {
            return false;
        }

        List<City> tempCities = new ArrayList<>(cities);
        CitiesEntity entity = get();
        if (null == entity) {
            entity = new CitiesEntity();
        } else {
            tempCities.addAll(entity.mCities);
        }

        for (int i = 0; i < tempCities.size() - 1; i++) {
            for (int j = tempCities.size() - 1; j > i; j--) {
                if (tempCities.get(i).getId() == tempCities.get(j).getId()) {
                    // 去掉重复数据
                    tempCities.remove(j);
                }
            }
        }

        sort(tempCities);

        entity.mVersion = version;
        entity.mCities = new ArrayList<>(tempCities);
        replaceQueues(entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        long version = (long) param[0];
        List<Long> delIds = (List<Long>) param[1];

        if (null == delIds || delIds.isEmpty()) {
            return false;
        }

        List<City> tempCities;
        CitiesEntity entity = get();
        if (null != entity) {
            boolean isDel = false;
            tempCities = new ArrayList<>(entity.mCities);
            for (int k = 0; k < delIds.size(); k++) {
                long delId = delIds.get(k);
                for (int i = 0; i < tempCities.size(); i++) {
                    City city = tempCities.get(i);
                    if (city.getId() == delId) {
                        isDel = true;
                        tempCities.remove(i);
                        Log.trace(TAG, "删除id-->" + delId);
                    }
                }
            }

            if (isDel) {
                sort(tempCities);
            }
            entity.mVersion = version;
            entity.mCities = new ArrayList<>(tempCities);
            replaceQueues(entity);
            return true;
        }
        return false;
    }

    public long getVersion() {
        CitiesEntity entity = get();
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<City> getCities() {
        CitiesEntity entity = get();
        if (null != entity) {
            return entity.mCities;
        }
        return null;
    }
}
