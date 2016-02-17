package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Description：缓存常用车辆工具类
 * <br/><br/>Created by Fu on 2015/5/28.
 * <br/><br/>
 */
public class CNCommonCarUtil extends CNBaseCacheDataUtil<CNCommonCarUtil.CommonCarEntity> {
    private static final String TAG = "CNCommonCarUtil";

    private static final String FILE_NAME = "commonCars_V1";

    private static CNCommonCarUtil ins = null;

    public static CNCommonCarUtil getIns() {
        if (null == ins) {
            synchronized (CNCommonCarUtil.class) {
                ins = new CNCommonCarUtil(FILE_NAME);
            }
        }
        return ins;
    }

    public static class CommonCarEntity implements Serializable {
        public long mVersion;
        public ArrayList<Car> mCars;
    }

    public CNCommonCarUtil(String fileName) {
        super(fileName);
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

    private void sort(List<Car> cars) {
        // 排序
        Collections.sort(cars, new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                Collator collator = Collator.getInstance(Locale.CHINA);
                int result = collator.compare(o1.getLicense(), o2.getLicense());
                if (result < 0) {
                    return -1;
                } else if (result == 0) {
                    return 0;
                } else if (result > 0) {
                    return 1;
                }
//                if (o1.getId() < o2.getId()) {
//                    return -1;
//                } else if (o1.getId() == o2.getId()) {
//                    return 0;
//                } else if (o1.getId() > o2.getId()) {
//                    return 1;
//                }
                return -1;
            }
        });
    }

    @Override
    public boolean add(Object... param) {
        long version = (long) param[0];
        List<Car> upCars = (List<Car>) param[1];

        if (null == upCars || upCars.isEmpty()) {
            return false;
        }

        List<Car> tempCars = new ArrayList<>(upCars);
        CommonCarEntity entity = get();
        if (null == entity) {
            entity = new CommonCarEntity();
        } else {
            tempCars.addAll(entity.mCars);
        }

        for (int i = 0; i < tempCars.size() - 1; i++) {
            for (int j = tempCars.size() - 1; j > i; j--) {
                if (tempCars.get(i).getLicense().equals(tempCars.get(j).getLicense())) {
                    // 去掉重复数据
                    tempCars.remove(j);
                }
            }
        }

        sort(tempCars);

        entity.mVersion = version;
        entity.mCars = new ArrayList<>(tempCars);
        replaceQueues(entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        long version = (long) param[0];
        List<String> delLicenses = (List<String>) param[1];

        if (null == delLicenses || delLicenses.isEmpty()) {
            return false;
        }

        List<Car> tempCars;
        CommonCarEntity entity = get();
        if (null != entity) {
            boolean isDel = false;
            tempCars = new ArrayList<>(entity.mCars);
            for (int k = 0; k < delLicenses.size(); k++) {
                String delLicense = delLicenses.get(k);
                for (int i = 0; i < tempCars.size(); i++) {
                    Car tempCar = tempCars.get(i);
                    if (delLicense.equals(tempCar.getLicense())) {
                        isDel = true;
                        // 删除该项
                        tempCars.remove(i);
                        Log.trace(TAG, "删除车牌-->" + delLicense);
                    }
                }
            }

            if (isDel) {
                sort(tempCars);
            }
            entity.mVersion = version;
            entity.mCars = new ArrayList<>(tempCars);
            replaceQueues(entity);
            return true;
        }
        return false;
    }

    public long getVersion() {
        CommonCarEntity entity = get();
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<Car> getCars() {
        CommonCarEntity entity = get();
        if (null != entity) {
            return entity.mCars;
        }
        return null;
    }

}
