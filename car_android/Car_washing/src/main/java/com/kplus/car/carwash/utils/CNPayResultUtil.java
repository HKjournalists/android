package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.OrderStatus;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description 用于缓存支付结果处理
 * <br/><br/>Created by FU ZHIXUE on 2015/7/6.
 * <br/><br/>
 */
public class CNPayResultUtil {

    private static final String TAG = "CNPayResultUtil";

    private static final String FILE_NAME = "payResult";
    private File mFile = null;
    private HashMap<Long, PayResultEntity> mPayResultEntityMap = null;

    private static class PayResultEntity implements Serializable {
        public long mOrderId;
        public long mFormOrderId;
        public int mOrderStatu;
    }

    private static CNPayResultUtil ins = null;

    public static CNPayResultUtil getIns() {
        if (null == ins) {
            synchronized (CNPayResultUtil.class) {
                ins = new CNPayResultUtil();
            }
        }
        return ins;
    }

    private String getPath() {
        long uid = AppBridgeUtils.getIns().getUid();
        if (uid > Const.NONE) {
            String name = MD5.md5(FILE_NAME + "_" + uid);
            String folder = FileUtils.getContextCacheFileDir();
            folder = folder + Const.CACHE_FILE_FOLRDER;
            FileUtils.makeFolders(folder);
            String fileName = folder + name + ".dat";

            Log.trace(TAG, "支付结果缓存路径：" + fileName);

            return fileName;
        }
        return null;
    }

    private File getFile() {
        if (null == mFile) {
            String filePath = getPath();
            if (CNStringUtil.isNotEmpty(filePath)) {
                mFile = new File(filePath);
            }
        }
        return mFile;
    }

    private Map<Long, PayResultEntity> getQueues() {
        if (null == mPayResultEntityMap) {
            if (null != getFile()) {
                mPayResultEntityMap = (HashMap<Long, PayResultEntity>) FileUtils.readObject(getFile());
            }

            if (null == mPayResultEntityMap) {
                mPayResultEntityMap = new HashMap<>();
            }
        }
        return mPayResultEntityMap;
    }

    public static void save() {
        if (null != getIns().mPayResultEntityMap
                && getIns().mPayResultEntityMap.size() > Const.NONE
                && null != getIns().getFile()) {
            FileUtils.saveObject(getIns().mPayResultEntityMap, getIns().getFile());
        }
    }

    public void add(long orderId, long formOrderId, int orderStatu) {
        PayResultEntity entity = getQueues().get(orderId);
        if (null != entity && entity.mFormOrderId == formOrderId) {
            entity.mOrderStatu = orderStatu;
        } else {
            entity = new PayResultEntity();
            entity.mOrderId = orderId;
            entity.mFormOrderId = formOrderId;
            entity.mOrderStatu = orderStatu;
        }
        getQueues().put(orderId, entity);
        save();
    }

    public int getOrderStatu(long orderId, long formOrderId) {
        PayResultEntity entity = getQueues().get(orderId);
        if (null != entity && entity.mFormOrderId == formOrderId) {
            return entity.mOrderStatu;
        }
        return OrderStatus.UNKOWN.value();
    }

    public void remove(long orderId) {
        getQueues().remove(orderId);
        save();
    }
}
