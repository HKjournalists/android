package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.OrderLog;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：缓存订单日志工具类
 * <br/><br/>Created by Fu on 2015/5/29.
 * <br/><br/>
 */
public class CNOrderLogUtil {
    private static final String TAG = "CNOrderLogUtil";

    private static final String FILE_NAME = "orderLogs";
    private File mFile = null;
    private HashMap<Long, OrderLogsEntity> mOrderLogsQueues = null;

    private static CNOrderLogUtil ins = null;

    public static CNOrderLogUtil getIns() {
        if (null == ins) {
            synchronized (CNOrderLogUtil.class) {
                ins = new CNOrderLogUtil();
            }
        }
        return ins;
    }

    private static class OrderLogsEntity implements Serializable {
        public long mOrderId;
        public ArrayList<OrderLog> mOrderLogs;
    }

    private String getPath() {
        long uid = AppBridgeUtils.getIns().getUid();
        if (uid > Const.NONE) {
            String name = MD5.md5(FILE_NAME + "_" + uid);
            String folder = FileUtils.getContextCacheFileDir();
            folder = folder + Const.CACHE_FILE_FOLRDER;
            FileUtils.makeFolders(folder);
            String fileName = folder + name + ".dat";

            Log.trace(TAG, "订单日志缓存路径：" + fileName);

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

    private synchronized Map<Long, OrderLogsEntity> getQueues() {
        if (null == mOrderLogsQueues) {
            if (null != getFile()) {
                mOrderLogsQueues = (HashMap<Long, OrderLogsEntity>) FileUtils.readObject(getFile());
            }
            if (null == mOrderLogsQueues) {
                mOrderLogsQueues = new HashMap<>();
            }
        }
        return mOrderLogsQueues;
    }

    public static void saveOrderlogs() {
        if (null != getIns().mOrderLogsQueues
                && null != getIns().getFile()) {
            FileUtils.saveObject(getIns().mOrderLogsQueues, getIns().getFile());
        }
    }

    public synchronized void addOrderLogs(long orderId, List<OrderLog> orderLogs) {
        OrderLogsEntity entity = getQueues().get(orderId);
        if (null == entity) {
            entity = new OrderLogsEntity();
        }

        entity.mOrderId = orderId;
        entity.mOrderLogs = new ArrayList<>(orderLogs);
        getQueues().put(orderId, entity);
    }

    public ArrayList<OrderLog> getOrderLogs(long orderId) {
        OrderLogsEntity entity = getQueues().get(orderId);
        if (null != entity) {
            return entity.mOrderLogs;
        }
        return null;
    }
}
