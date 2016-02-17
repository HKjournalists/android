package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.Region;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Description：缓存城市服务区域工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class CNRegionUtil extends CNBaseCacheDataUtil<HashMap<Long, CNRegionUtil.RegionEntity>> {
    private static final String TAG = "CNRegionUtil";

    private static final String FILE_NAME = "region_V1";

    private static CNRegionUtil ins = null;

    public static CNRegionUtil getIns() {
        if (null == ins) {
            synchronized (CNRegionUtil.class) {
                ins = new CNRegionUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNRegionUtil(String fileName) {
        super(fileName);
    }

    public static class RegionEntity implements Serializable {
        public long mCityId;
        public long mVersion;
        public ArrayList<Region> mRegions;
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
    public HashMap<Long, RegionEntity> get() {
        HashMap<Long, RegionEntity> map = super.get();
        if (null == map) {
            map = new HashMap<>();
            replaceQueues(map);
        }
        return map;
    }

    private void sort(List<Region> regions) {
        // 排序
        Collections.sort(regions, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
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
        long cityId = (long) param[0];
        long version = (long) param[1];
        List<Region> upRegions = (List<Region>) param[2];
        if (null == upRegions || upRegions.isEmpty()) {
            return false;
        }

        List<Region> tempRegions = new ArrayList<>(upRegions);
        RegionEntity entity = get().get(cityId);
        if (null == entity) {
            entity = new RegionEntity();
        } else {
            tempRegions.addAll(entity.mRegions);
        }

        for (int i = 0; i < tempRegions.size() - 1; i++) {
            for (int j = tempRegions.size() - 1; j > i; j--) {
                if (tempRegions.get(i).getId() == tempRegions.get(j).getId()) {
                    // 去掉重复数据
                    tempRegions.remove(j);
                }
            }
        }

        sort(tempRegions);

        entity.mCityId = cityId;
        entity.mVersion = version;
        entity.mRegions = new ArrayList<>(tempRegions);
        get().put(cityId, entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        long cityId = (long) param[0];
        long version = (long) param[1];
        List<Long> delIds = (List<Long>) param[2];
        if (null == delIds || delIds.isEmpty()) {
            return false;
        }

        List<Region> tempRegions;
        RegionEntity entity = get().get(cityId);
        if (null != entity) {
            boolean isDel = false;
            tempRegions = new ArrayList<>(entity.mRegions);
            for (int k = 0; k < delIds.size(); k++) {
                long delId = delIds.get(k);
                for (int i = 0; i < tempRegions.size(); i++) {
                    Region tempRegion = tempRegions.get(i);
                    if (delId == tempRegion.getId()) {
                        isDel = true;
                        // 删除该项
                        tempRegions.remove(i);
                        Log.trace(TAG, "删除id-->" + delId);
                    }
                }
            }
            if (isDel) {
                sort(tempRegions);
            }
            entity.mCityId = cityId;
            entity.mVersion = version;
            entity.mRegions = new ArrayList<>(tempRegions);
            get().put(cityId, entity);
            return true;
        }
        return false;
    }

    public List<Region> getRegions(long cityId) {
        RegionEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mRegions;
        }
        return null;
    }

    public long getVersion(long cityId) {
        RegionEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }
}
