package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description：缓存车辆颜色工具类
 * <br/><br/>Created by Fu on 2015/5/28.
 * <br/><br/>
 */
public class CNCarColorUtil extends CNBaseCacheDataUtil<CNCarColorUtil.CarColorEntity> {
    private static final String TAG = "CNCarColorUtil";

    private static final String FILE_NAME = "carColor_V1";

    private static CNCarColorUtil ins = null;

    public static CNCarColorUtil getIns() {
        if (null == ins) {
            synchronized (CNCarColorUtil.class) {
                ins = new CNCarColorUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNCarColorUtil(String fileName) {
        super(fileName);
    }

    public static class CarColorEntity implements Serializable {
        public long mVersion;
        public ArrayList<CarColor> mCarColors;
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

    private void sort(List<CarColor> carColors) {
        // 按权重排序
        Collections.sort(carColors, new Comparator<CarColor>() {
            @Override
            public int compare(CarColor o1, CarColor o2) {
                if (o1.getWeight() < o2.getWeight()) {
                    return 1;
                } else if (o1.getWeight() == o2.getWeight()) {
                    return 0;
                } else if (o1.getWeight() > o2.getWeight()) {
                    return -1;
                }
                return -1;
            }
        });
    }

    @Override
    public boolean add(Object... param) {
        long version = (long) param[0];
        List<CarColor> upCarColors = (List<CarColor>) param[1];

        if (null == upCarColors || upCarColors.isEmpty()) {
            return false;
        }

        List<CarColor> tempCarColors;
        CarColorEntity entity = get();
        if (null == entity) {
            entity = new CarColorEntity();
            tempCarColors = upCarColors;
        } else {
            tempCarColors = new ArrayList<>(upCarColors);
            tempCarColors.addAll(entity.mCarColors);

            for (int i = 0; i < tempCarColors.size() - 1; i++) {
                for (int j = tempCarColors.size() - 1; j > i; j--) {
                    if (tempCarColors.get(i).getId() == tempCarColors.get(j).getId()) {
                        // 去掉重复数据
                        tempCarColors.remove(j);
                    }
                }
            }
        }
        sort(tempCarColors);

        entity.mVersion = version;
        entity.mCarColors = new ArrayList<>(tempCarColors);
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

        List<CarColor> tempCarColors;
        CarColorEntity entity = get();
        if (null != entity) {
            boolean isDel = false;
            tempCarColors = new ArrayList<>(entity.mCarColors);
            for (int k = 0; k < delIds.size(); k++) {
                long delId = delIds.get(k);
                for (int i = 0; i < tempCarColors.size(); i++) {
                    CarColor tempCarColor = tempCarColors.get(i);
                    if (delId == tempCarColor.getId()) {
                        isDel = true;
                        // 删除该项
                        tempCarColors.remove(i);
                        Log.trace(TAG, "删除id-->" + delId);
                    }
                }
            }
            // 删除后重新排序
            if (isDel) {
                sort(tempCarColors);
            }
            entity.mVersion = version;
            entity.mCarColors = new ArrayList<>(tempCarColors);
            replaceQueues(entity);
            return true;
        }
        return false;
    }

    public long getVersion() {
        CarColorEntity entity = get();
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<CarColor> getCarColors() {
        CarColorEntity entity = get();
        if (null != entity) {
            return entity.mCarColors;
        }
        return null;
    }
}
