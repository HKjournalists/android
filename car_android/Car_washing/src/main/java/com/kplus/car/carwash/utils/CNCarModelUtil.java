package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.bean.CarModelTag;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Description：缓存车辆车型工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/7/30.
 * <br/><br/>
 */
public class CNCarModelUtil extends CNBaseCacheDataUtil<CNCarModelUtil.CarModelEntity> {
    private static final String TAG = "CNCarModelUtil";

    private static final String FILE_NAME = "carModel_V1";

    private static CNCarModelUtil ins = null;

    public static CNCarModelUtil getIns() {
        if (null == ins) {
            synchronized (CNCarModelUtil.class) {
                ins = new CNCarModelUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNCarModelUtil(String fileName) {
        super(fileName);
    }

    public static class CarModelEntity implements Serializable {
        public long mVersion;
        public long mBrandId;
        public ArrayList<CarModel> mCarModels;
        public HashMap<Long, ArrayList<CarModel>> mCarModelsMap;
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

    private void sort(List<CarModel> carModels) {
        // 排序
        Collections.sort(carModels, new Comparator<CarModel>() {
            @Override
            public int compare(CarModel o1, CarModel o2) {
                Collator collator = Collator.getInstance(Locale.CHINA);
                int result = collator.compare(o1.getName(), o2.getName());
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

    /**
     * 将车型根据品牌对应关系
     *
     * @param carModels 车型
     * @return 车型-->品牌
     */
    private HashMap<Long, ArrayList<CarModel>> getCarModels(List<CarModel> carModels) {
        HashMap<Long, ArrayList<CarModel>> mCarModelMap = new HashMap<>();
        for (int i = 0; i < carModels.size(); i++) {
            CarModel carModel = carModels.get(i);
            CarBrand carBrand = carModel.getBrand();

            // 从map中取出该品牌是否已经有了，如果有了，直接把此车型加入
            ArrayList<CarModel> tempCarModels = mCarModelMap.get(carBrand.getId());
            if (null == tempCarModels || tempCarModels.isEmpty()) {
                tempCarModels = new ArrayList<>();
            }
            tempCarModels.add(carModel);
            mCarModelMap.put(carBrand.getId(), tempCarModels);
        }
        return mCarModelMap;
    }

    @Override
    public boolean add(Object... param) {
        long version = (long) param[0];
        List<CarModel> upCarModel = (List<CarModel>) param[1];

        if (null == upCarModel || upCarModel.isEmpty()) {
            return false;
        }

        List<CarModel> tempCarModels;
        CarModelEntity entity = get();
        if (null == entity) {
            entity = new CarModelEntity();
            tempCarModels = upCarModel;
        } else {
            tempCarModels = new ArrayList<>(upCarModel);
            tempCarModels.addAll(entity.mCarModels);

            for (int i = 0; i < tempCarModels.size() - 1; i++) {
                for (int j = tempCarModels.size() - 1; j > i; j--) {
                    if (tempCarModels.get(i).getId() == tempCarModels.get(j).getId()) {
                        // 去掉重复数据
                        tempCarModels.remove(j);
                    }
                }
            }
        }

        sort(tempCarModels);
        HashMap<Long, ArrayList<CarModel>> mCarModelMap = getCarModels(tempCarModels);

        entity.mVersion = version;
        entity.mCarModels = new ArrayList<>(tempCarModels);
        entity.mCarModelsMap = new HashMap<>(mCarModelMap);
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
        List<CarModel> tempCarModels;
        CarModelEntity entity = get();
        if (null != entity) {
            boolean isDel = false;
            tempCarModels = new ArrayList<>(entity.mCarModels);
            for (int k = 0; k < delIds.size(); k++) {
                long delId = delIds.get(k);
                for (int i = 0; i < tempCarModels.size(); i++) {
                    CarModel tempCarModel = tempCarModels.get(i);
                    if (delId == tempCarModel.getId()) {
                        isDel = true;
                        // 删除该项
                        tempCarModels.remove(i);
                        Log.trace(TAG, "删除id-->" + delId);
                    }
                }
            }

            if (isDel) {
                sort(tempCarModels);
            }

            HashMap<Long, ArrayList<CarModel>> mCarModelMap = getCarModels(tempCarModels);

            entity.mVersion = version;
            entity.mCarModels = new ArrayList<>(tempCarModels);
            entity.mCarModelsMap = new HashMap<>(mCarModelMap);
            replaceQueues(entity);
            return true;
        }
        return false;

    }

    public void resetVersion() {
        CarModelEntity entity = get();
        if (null != entity) {
            entity.mVersion = 0;
            replaceQueues(entity);
        }
    }

    public long getVersion() {
        CarModelEntity entity = get();
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<CarModel> getCarModels(long brandId) {
        CarModelEntity entity = get();
        if (null != entity) {
            return entity.mCarModelsMap.get(brandId);
        }
        return null;
    }

    public List<CarModel> getCarModels(long brandId, List<CarModelTag> carModelTags) {
        List<CarModel> carModels = getCarModels(brandId);
        if (null == carModelTags || carModelTags.isEmpty()
                || null == carModels || carModels.isEmpty()) {
            return null;
        }

        List<CarModel> tempCarModel = new ArrayList<>();

        for (int i = 0; i < carModelTags.size(); i++) {
            CarModelTag carModelTag = carModelTags.get(i);
            for (int j = 0; j < carModels.size(); j++) {
                CarModel carModel = carModels.get(j);
                // 比较相同的id
                if (carModelTag.getModelId() == carModel.getId()) {
                    tempCarModel.add(carModel);
                }
            }
        }
        return tempCarModel;
    }
}
