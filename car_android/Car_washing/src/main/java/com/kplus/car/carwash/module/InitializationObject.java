package com.kplus.car.carwash.module;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Description：全局初始化管理
 * <br/><br/>Created by Fu on 2015/5/15.
 * <br/><br/>
 */
public final class InitializationObject {
    private static Context mContext;

    private InitializationObject() {
    }

    public static void initialization(Context context) {
        mContext = context;
        initImageLoader();
    }

    /**
     * 初始化 imageLoader
     */
    private static void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(3 * 1024 * 1024))
                .memoryCacheSize(3 * 1024 * 1024)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
