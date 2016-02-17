package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.ServiceSupportCarTag;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description：服务支持的车型tag
 * <br/><br/>Created by Fausgoal on 2015/9/23.
 * <br/><br/>
 */
public class CNSupportCarTagUtil extends CNBaseCacheDataUtil<HashMap<Long, CNSupportCarTagUtil.SupportCarTagEntity>> {
    private static final String TAG = "CNSupportCarTagUtil";

    private static final String FILE_NAME = "supportCarTag_V1";

    private static CNSupportCarTagUtil ins = null;

    public static CNSupportCarTagUtil getIns() {
        if (null == ins) {
            synchronized (CNSupportCarTagUtil.class) {
                ins = new CNSupportCarTagUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNSupportCarTagUtil(String fileName) {
        super(fileName);
    }

    public static class SupportCarTagEntity implements Serializable {
        public long mVersion;
        public long mCityId;
        public ArrayList<ServiceSupportCarTag> mSupportCarTags;
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
    public HashMap<Long, SupportCarTagEntity> get() {
        HashMap<Long, SupportCarTagEntity> map = super.get();
        if (null == map) {
            map = new HashMap<>();
            replaceQueues(map);
        }
        return map;
    }

    @Override
    public boolean add(Object... param) {
        long cityId = (long) param[0];
        long version = (long) param[1];
        List<ServiceSupportCarTag> supportCarTags = (List<ServiceSupportCarTag>) param[2];

        if (null == supportCarTags || supportCarTags.isEmpty()) {
            return false;
        }

        SupportCarTagEntity entity = get().get(cityId);
        List<ServiceSupportCarTag> serviceArrayList = new ArrayList<>(supportCarTags);
        if (null != entity) {
            // 如果本地已经有了，把两次的放到一起，然后去掉重复
            serviceArrayList.addAll(entity.mSupportCarTags);
        } else {
            entity = new SupportCarTagEntity();
        }

        for (int i = 0; i < serviceArrayList.size() - 1; i++) {
            for (int j = serviceArrayList.size() - 1; j > i; j--) {
                if (serviceArrayList.get(i).getTagId() == serviceArrayList.get(j).getTagId()
                        && serviceArrayList.get(i).getServiceId() == serviceArrayList.get(j).getServiceId()) {
                    // 去掉重复数据
                    serviceArrayList.remove(j);
                }
            }
        }

        entity.mCityId = cityId;
        entity.mVersion = version;
        entity.mSupportCarTags = new ArrayList<>(serviceArrayList);
        get().put(cityId, entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        long cityId = (long) param[0];
        long version = (long) param[1];
        List<ServiceSupportCarTag> delSupportCarTags = (List<ServiceSupportCarTag>) param[2];

        if (null == delSupportCarTags || delSupportCarTags.isEmpty()) {
            return false;
        }

        SupportCarTagEntity entity = get().get(cityId);
        if (null != entity) {
            ArrayList<ServiceSupportCarTag> supportCarTags = new ArrayList<>(entity.mSupportCarTags);
            for (int k = 0; k < delSupportCarTags.size(); k++) {
                ServiceSupportCarTag delTag = delSupportCarTags.get(k);
                for (int i = 0; i < supportCarTags.size(); i++) {
                    ServiceSupportCarTag tag = supportCarTags.get(i);
                    if (tag.getTagId() == delTag.getTagId() && tag.getServiceId() == delTag.getServiceId()) {
                        supportCarTags.remove(i);
                    }
                }
            }

            entity.mCityId = cityId;
            entity.mVersion = version;
            entity.mSupportCarTags = new ArrayList<>(supportCarTags);
            get().put(cityId, entity);
            return true;
        }
        return false;
    }

    public long getVersion(long cityId) {
        SupportCarTagEntity entity = get().get(cityId);
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<ServiceSupportCarTag> getTagId(long cityId, long serviceId) {
        SupportCarTagEntity entity = get().get(cityId);
        if (null != entity) {
            List<ServiceSupportCarTag> tempsupportCarTag = new ArrayList<>();
            ArrayList<ServiceSupportCarTag> supportCarTags = entity.mSupportCarTags;
            for (ServiceSupportCarTag supportCarTag : supportCarTags) {
                if (supportCarTag.getServiceId() == serviceId) {
                    tempsupportCarTag.add(supportCarTag);
                }
            }
            return tempsupportCarTag;
        }
        return null;
    }
}
