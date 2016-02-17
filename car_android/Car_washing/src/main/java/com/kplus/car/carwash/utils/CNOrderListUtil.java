package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.bean.ServiceOrder;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description：缓存订单列表工具类
 * <br/><br/>Created by Fu on 2015/5/29.
 * <br/><br/>
 */
public class CNOrderListUtil {
    private static final String TAG = "CNOrderListUtil";

    private static final String FILE_NAME = "orderList";
    private File mFile = null;
    private ArrayList<ServiceOrder> mOrderListQueues = null;

    private static CNOrderListUtil ins = null;

    public static CNOrderListUtil getIns() {
        if (null == ins) {
            synchronized (CNOrderListUtil.class) {
                ins = new CNOrderListUtil();
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

            Log.trace(TAG, "订单列表缓存路径：" + fileName);

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

    private ArrayList<ServiceOrder> getQueues() {
        if (null == mOrderListQueues) {
            if (null != getFile()) {
                mOrderListQueues = (ArrayList<ServiceOrder>) FileUtils.readObject(getFile());
            }
        }
        return mOrderListQueues;
    }

    public static void saveOrderLists() {
        if (null != getIns().mOrderListQueues
                && getIns().mOrderListQueues.size() > Const.NONE
                && null != getIns().getFile()) {
            FileUtils.saveObject(getIns().mOrderListQueues, getIns().getFile());
        }
    }

    public void addOrderLists(List<ServiceOrder> serviceOrders) {
        if (null != serviceOrders && serviceOrders.size() > Const.NONE) {
            if (null != mOrderListQueues) {
                mOrderListQueues = null;
            }
            mOrderListQueues = new ArrayList<>(serviceOrders);
        }
    }

    public List<ServiceOrder> getOrderLists() {
        return getQueues();
    }

    public void setOrderStatus(long orderId, int statu) {
        List<ServiceOrder> serviceCacheOrders = getQueues();
        if (null != serviceCacheOrders) {
            for (int i = 0; i < serviceCacheOrders.size(); i++) {
                ServiceOrder temp = serviceCacheOrders.get(i);
                if (temp.getId() == orderId) {
                    temp.setStatus(statu);
                    break;
                }
            }
        }
    }
}
