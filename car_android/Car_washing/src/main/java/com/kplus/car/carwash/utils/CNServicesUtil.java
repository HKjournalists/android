package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Description：缓存服务项目工具类
 * <br/><br/>Created by Fu on 2015/5/29.
 * <br/><br/>
 */
public class CNServicesUtil extends CNBaseCacheDataUtil<HashMap<Long, CNServicesUtil.ServicesEntity>> {
    private static final String TAG = "CNServicesUtil";

    private static final String FILE_NAME = "servicesItem_V1";

    private static CNServicesUtil ins = null;

    public static CNServicesUtil getIns() {
        if (null == ins) {
            synchronized (CNServicesUtil.class) {
                ins = new CNServicesUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNServicesUtil(String fileName) {
        super(fileName);
    }

    public static class ServicesEntity implements Serializable {
        public long mCityId;
        public long mVersion;
        public ArrayList<OnSiteService> mServices;
    }

    private void sort(List<OnSiteService> services) {
        // 按权重排序
        Collections.sort(services, new Comparator<OnSiteService>() {
            @Override
            public int compare(OnSiteService o1, OnSiteService o2) {
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
        long cityId = (long) param[0];
        long version = (long) param[1];
        List<OnSiteService> services = (List<OnSiteService>) param[2];

        if (null == services || services.isEmpty()) {
            return false;
        }

        ServicesEntity entity = get().get(cityId);
        List<OnSiteService> serviceArrayList = new ArrayList<>(services);
        if (null != entity) {
            // 如果本地已经有了，把两次的放到一起，然后去掉重复
            serviceArrayList.addAll(entity.mServices);
        } else {
            entity = new ServicesEntity();
        }

        for (int i = 0; i < serviceArrayList.size() - 1; i++) {
            for (int j = serviceArrayList.size() - 1; j > i; j--) {
                if (serviceArrayList.get(i).getId() == serviceArrayList.get(j).getId()) {
                    // 去掉重复数据
                    serviceArrayList.remove(j);
                }
            }
        }

        // 重新按权重排序
        sort(serviceArrayList);

        entity.mCityId = cityId;
        entity.mVersion = version;
        entity.mServices = new ArrayList<>(serviceArrayList);
        get().put(cityId, entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        long cityId = (long) param[0];
        long version = (long) param[1];
        List<Long> delServicerIds = (List<Long>) param[2];

        if (null == delServicerIds || delServicerIds.isEmpty()) {
            return false;
        }

        ServicesEntity entity = get().get(cityId);
        if (null != entity) {
            boolean isDel = false;
            ArrayList<OnSiteService> services = new ArrayList<>(entity.mServices);
            for (int k = 0; k < delServicerIds.size(); k++) {
                long delId = delServicerIds.get(k);
                for (int i = 0; i < services.size(); i++) {
                    OnSiteService service = services.get(i);
                    if (service.getId() == delId) {
                        isDel = true;
                        services.remove(i);
                        Log.trace(TAG, "删除id-->" + delId);
                    }
                }
            }

            if (isDel) {
                sort(services);
            }

            entity.mCityId = cityId;
            entity.mVersion = version;
            entity.mServices = new ArrayList<>(services);
            get().put(cityId, entity);
            return true;
        }
        return false;
    }

    @Override
    public HashMap<Long, ServicesEntity> get() {
        HashMap<Long, ServicesEntity> map = super.get();
        if (null == map) {
            map = new HashMap<>();
            replaceQueues(map);
        }
        return map;
    }

    public ArrayList<OnSiteService> getServices(long cityId) {
        ServicesEntity entity = get().get(cityId);
        if (null != entity && null != entity.mServices) {
            ArrayList<OnSiteService> services = new ArrayList<>(entity.mServices);
            for (OnSiteService service : services) {
                service.setIsChecked(false);
            }
            return services;
        }
        return null;
    }

    /**
     * 获取选择的服务项目
     *
     * @param cityId
     * @param id
     * @return
     */
    public OnSiteService getServiceById(long cityId, long id) {
        ServicesEntity entity = get().get(cityId);
        if (null != entity && null != entity.mServices) {
            OnSiteService usedService = null;
            for (OnSiteService service : entity.mServices) {
                if (service.getId() == id) {
                    service.setIsChecked(false);
                    usedService = service;
                    break;
                }
            }
            return usedService;
        }
        return null;
    }

    public long getVersion(long cityId) {
        ServicesEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }
}
