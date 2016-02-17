package com.kplus.car.carwash.common;

import android.app.Activity;
import android.content.SharedPreferences;

import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.utils.Base64;
import com.kplus.car.carwash.utils.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * 简易写入preference操作，存放相关字段和默认值
 * Created by Fu on 2015/5/8.
 */
public class CNCommonVariables {
    private static final String TAG = "CNCommonVariables";

    public static class Keys {
        public static final String ORDER_LIST_REFRESH_TIME = "orderListRefreshTime";
    }

    private static final String PREF_FILE = "com.kplus.car.carwash_preferences";

    private static CNCommonVariables ins = null;
    private SharedPreferences mSharedPreferences = null;

    public static CNCommonVariables getIns() {
        if (null == ins) {
            synchronized (CNCommonVariables.class) {
                ins = new CNCommonVariables();
            }
        }
        return ins;
    }

    private CNCommonVariables() {
        reset(getSharedPreferences());
    }

    private SharedPreferences getSharedPreferences() {
        return CNCarWashApp.getIns().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
    }

    private void reset(SharedPreferences sharedPreferences) {
        if (null == sharedPreferences) {
            Log.trace(TAG, "sharedPreferences is null");
            return;
        }
        mSharedPreferences = sharedPreferences;
    }

    /**
     * String key, int defValue
     *
     * @param key   key
     * @param value value
     */
    private void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Retrieve a String value from the preferences
     *
     * @param key      key
     * @param defValue defValue
     * @return value
     */
    private String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }


    public void saveObject(String key, Object object) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(bos);
            // 将对象写入字节流
            oos.writeObject(object);

            // 将字节流编码成base64的字符窜
            byte[] bytes = bos.toByteArray();
            String value = Base64.encode(bytes);
            putString(key, value);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();
                if (oos != null)
                    oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object readObject(String key, Object o) {
        Object object = null;

        ByteArrayInputStream bis = null;
        ObjectInputStream ois;
        try {
            String value = getString(key, "");
            byte[] bytes = Base64.decode(value);

            if (bytes.length > 0) {
                //封装到字节流
                bis = new ByteArrayInputStream(bytes);
                //再次封装
                ois = new ObjectInputStream(bis);
                //读取对象
                object = ois.readObject();
            } else {
                object = o;
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public Object readObject(String key) {
        return readObject(key, null);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
