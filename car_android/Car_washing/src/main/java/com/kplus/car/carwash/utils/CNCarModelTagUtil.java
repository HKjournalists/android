package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.CarModelTag;
import com.kplus.car.carwash.bean.ServiceSupportCarTag;
import com.kplus.car.carwash.module.base.CNBaseCacheDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 2015/9/23.
 * <br/><br/>
 */
public class CNCarModelTagUtil extends CNBaseCacheDataUtil<CNCarModelTagUtil.CarModelTagEntity> {
    private static final String TAG = "CNCarModelTagUtil";

    private static final String FILE_NAME = "carModelTag_V1";

    private static CNCarModelTagUtil ins = null;

    public static CNCarModelTagUtil getIns() {
        if (null == ins) {
            synchronized (CNCarModelTagUtil.class) {
                ins = new CNCarModelTagUtil(FILE_NAME);
            }
        }
        return ins;
    }

    private CNCarModelTagUtil(String fileName) {
        super(fileName);
    }

    public static class CarModelTagEntity implements Serializable {
        public long mVersion;
        public ArrayList<CarModelTag> mCarModelTags;
//        /**
//         * key 用tagId_modelId
//         */
//        public HashMap<String, ArrayList<CarModelTag>> mCarModelTagMap;
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
    public CarModelTagEntity get() {
        return super.get();
    }

    @Override
    public boolean add(Object... param) {
        long version = (long) param[0];
        List<CarModelTag> upCarModel = (List<CarModelTag>) param[1];

        if (null == upCarModel || upCarModel.isEmpty()) {
            return false;
        }

        List<CarModelTag> tempCarModels;
        CarModelTagEntity entity = get();
        if (null == entity) {
            entity = new CarModelTagEntity();
            tempCarModels = upCarModel;
        } else {
            tempCarModels = new ArrayList<>(upCarModel);
            tempCarModels.addAll(entity.mCarModelTags);

            for (int i = 0; i < tempCarModels.size() - 1; i++) {
                for (int j = tempCarModels.size() - 1; j > i; j--) {
                    if (tempCarModels.get(i).getTagId() == tempCarModels.get(j).getTagId()
                            && tempCarModels.get(i).getModelId() == tempCarModels.get(j).getModelId()) {
                        // 去掉重复数据
                        tempCarModels.remove(j);
                    }
                }
            }
        }

//        HashMap<String, ArrayList<CarModelTag>> mCarModelMap = getCarModelTags(tempCarModels);

        entity.mVersion = version;
        entity.mCarModelTags = new ArrayList<>(tempCarModels);
//        entity.mCarModelTagMap = new HashMap<>(mCarModelMap);
        replaceQueues(entity);
        return true;
    }

    @Override
    public boolean del(Object... param) {
        long version = (long) param[0];
        List<CarModelTag> delCarmodelTags = (List<CarModelTag>) param[1];

        if (null == delCarmodelTags || delCarmodelTags.isEmpty()) {
            return false;
        }

        List<CarModelTag> tempCarModels;
        CarModelTagEntity entity = get();
        if (null != entity) {
            tempCarModels = new ArrayList<>(entity.mCarModelTags);
            for (int k = 0; k < delCarmodelTags.size(); k++) {
                CarModelTag delCarmodelTag = delCarmodelTags.get(k);
                for (int i = 0; i < tempCarModels.size(); i++) {
                    CarModelTag tempCarModel = tempCarModels.get(i);
                    if (delCarmodelTag.getTagId() == tempCarModel.getTagId()
                            && delCarmodelTag.getModelId() == tempCarModel.getModelId()) {
                        // 删除该项
                        tempCarModels.remove(i);
                    }
                }
            }

//            HashMap<String, ArrayList<CarModelTag>> mCarModelMap = getCarModelTags(tempCarModels);

            entity.mVersion = version;
            entity.mCarModelTags = new ArrayList<>(tempCarModels);
//            entity.mCarModelTagMap = new HashMap<>(mCarModelMap);
            replaceQueues(entity);
            return true;
        }
        return false;
    }

    private HashMap<String, ArrayList<CarModelTag>> getCarModelTags(List<CarModelTag> carModels) {
        HashMap<String, ArrayList<CarModelTag>> mCarModelMap = new HashMap<>();
        for (int i = 0; i < carModels.size(); i++) {
            CarModelTag carModel = carModels.get(i);

            String key = getKey(carModel.getTagId(), carModel.getModelId());

            // 从map中取出该品牌是否已经有了，如果有了，直接把此车型加入
            ArrayList<CarModelTag> tempCarModels = mCarModelMap.get(key);
            if (null == tempCarModels || tempCarModels.isEmpty()) {
                tempCarModels = new ArrayList<>();
            }
            tempCarModels.add(carModel);
            mCarModelMap.put(key, tempCarModels);
        }
        return mCarModelMap;
    }

    private String getKey(long tagId, long modelId) {
        return String.valueOf(tagId + "_" + modelId);
    }

    public long getVersion() {
        CarModelTagEntity entity = get();
        if (null != entity) {
            return entity.mVersion;
        }
        return 0;
    }

    public List<CarModelTag> getCarModelByTags(List<ServiceSupportCarTag> supportCarTags) {
        if (null == supportCarTags || supportCarTags.isEmpty()) {
            return null;
        }
        CarModelTagEntity entity = get();
        if (null != entity) {
            List<CarModelTag> carModelTags = entity.mCarModelTags;
            List<CarModelTag> tempCarModelTags = new ArrayList<>();
            for (CarModelTag carModelTag : carModelTags) {
                for (ServiceSupportCarTag supportCarTag : supportCarTags) {
                    // 相同的tag
                    if (carModelTag.getTagId() == supportCarTag.getTagId()) {
                        tempCarModelTags.add(carModelTag);
                    }
                }
            }

            for (int i = 0; i < tempCarModelTags.size() - 1; i++) {
                for (int j = tempCarModelTags.size() - 1; j > i; j--) {
                    if (tempCarModelTags.get(i).getModelId() == tempCarModelTags.get(j).getModelId()) {
                        // 去掉重复数据
                        tempCarModelTags.remove(j);
                    }
                }
            }
            return tempCarModelTags;
        }
        return null;
    }
}
