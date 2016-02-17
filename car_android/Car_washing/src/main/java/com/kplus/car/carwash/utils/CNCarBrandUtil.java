package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description：缓存车辆品牌工具类
 * <br/><br/>Created by Fu on 2015/5/28.
 * <br/><br/>
 */
public class CNCarBrandUtil extends CNBaseCacheDataUtil<CNCarBrandUtil.CarBrandEntity> {
    private static final String TAG = "CNCarBrandUtil";

    private static final String FILE_NAME = "carBrand_V1";

    private static CNCarBrandUtil ins = null;

    public static CNCarBrandUtil getIns() {
        if (null == ins) {
            synchronized (CNCarBrandUtil.class) {
                ins = new CNCarBrandUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNCarBrandUtil(String fileName) {
        super(fileName);
    }

    public static class CarBrandEntity implements Serializable {
        public long mVersion;
        public ArrayList<CarBrand> mCarBrands;
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

    /**
     * 把车型按字母排序
     *
     * @param brands 品牌
     */
    private void sort(List<CarBrand> brands) {
        // 把车型按字母排序
        Collections.sort(brands, new Comparator<CarBrand>() {
            @Override
            public int compare(CarBrand o1, CarBrand o2) {
                if (null != o1
                        && null != o2
                        && CNStringUtil.isNotEmpty(o1.getPy())
                        && CNStringUtil.isNotEmpty(o2.getPy())) {
                    if ("#".equals(o2.getPy())) {
                        return -1;
                    } else if ("#".equals(o1.getPy())) {
                        return 1;
                    } else {
                        return o1.getPy().compareTo(o2.getPy());
                    }
                }
                return Const.NEGATIVE;
            }
        });
    }

    @Override
    public boolean add(Object... param) {
        long version = (long) param[0];
        List<CarBrand> upBrands = (List<CarBrand>) param[1];
        if (null == upBrands || upBrands.isEmpty()) {
            return false;
        }

        List<CarBrand> tempBrands;
        CarBrandEntity entity = get();
        if (null == entity) {
            entity = new CarBrandEntity();
            tempBrands = upBrands;
        } else {
            // 增量更新
            tempBrands = new ArrayList<>(upBrands);
            tempBrands.addAll(entity.mCarBrands);

            for (int i = 0; i < tempBrands.size() - 1; i++) {
                for (int j = tempBrands.size() - 1; j > i; j--) {
                    if (tempBrands.get(i).getId() == tempBrands.get(j).getId()) {
                        // 去掉重复数据
                        tempBrands.remove(j);
                    }
                }
            }
        }
        sort(tempBrands);
        entity.mVersion = version;
        entity.mCarBrands = new ArrayList<>(tempBrands);
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

        List<CarBrand> tempBrand;
        CarBrandEntity entity = get();
        if (null != entity) {
            boolean isDel = false;
            // 删除品牌
            tempBrand = new ArrayList<>(entity.mCarBrands);
            for (int k = 0; k < delIds.size(); k++) {
                long delId = delIds.get(k);
                for (int i = 0; i < tempBrand.size(); i++) {
                    CarBrand brand = tempBrand.get(i);
                    if (brand.getId() == delId) {
                        isDel = true;
                        tempBrand.remove(i);
                        Log.trace(TAG, "删除id-->" + delId);
                    }
                }
            }
            if (isDel) {
                sort(tempBrand);
            }
            entity.mVersion = version;
            entity.mCarBrands = new ArrayList<>(tempBrand);
            replaceQueues(entity);
            return true;
        }
        return false;
    }

    public long getVersion() {
        CarBrandEntity entity = get();
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<CarBrand> getCarBrands() {
        CarBrandEntity entity = get();
        if (null != entity) {
            return entity.mCarBrands;
        }
        return null;
    }
}
