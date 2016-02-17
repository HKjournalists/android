package com.kplus.car.carwash.module.base;

import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.FileUtils;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.MD5;

import java.io.File;

/**
 * Description：文件缓存的基类
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public abstract class CNBaseCacheDataUtil<T> {
    private static final String TAG = "CNBaseCacheDataUtil";

    private final String mFileName;
    private File mFile = null;
    protected T mQueues = null;

    public CNBaseCacheDataUtil(String fileName) {
        mFileName = fileName;
    }

    private String getPath() {
        long uid = AppBridgeUtils.getIns().getUid();
        if (uid > Const.NONE) {
            String name = MD5.md5(mFileName + "_" + uid);
            String folder = FileUtils.getContextCacheFileDir();
            folder = folder + Const.CACHE_FILE_FOLRDER;
            FileUtils.makeFolders(folder);
            String fileName = folder + name + ".dat";

            Log.trace(TAG, "文件名：" + mFileName + "，缓存路径：" + fileName);

            return fileName;
        }
        return null;
    }

    protected File getFile() {
        if (null == mFile) {
            String filePath = getPath();
            if (CNStringUtil.isNotEmpty(filePath)) {
                mFile = new File(filePath);
            }
        }
        return mFile;
    }

    private synchronized T getQueues() {
        if (null == mQueues) {
            if (null != getFile()) {
                mQueues = (T) FileUtils.readObject(getFile());
            }
        }
        return mQueues;
    }

    public void replaceQueues(T queues) {
        mQueues = queues;
    }

    public abstract void saveData();

    public abstract boolean add(Object... param);

    public abstract boolean del(Object... param);

    public T get() {
        return getQueues();
    }
}
