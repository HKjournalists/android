package com.kplus.car.carwash.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.kplus.car.carwash.common.Const;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Description：图片处理工具类
 * <br/><br/>Created by Fu on 2015/5/16.
 * <br/><br/>
 */
public class CNImageUtils {
    private static final String TAG = "CNImageUtils";

    /**
     * 默认压缩比例
     */
    private final static int IMG_COMPRESS_RATIO = 88;

    private CNImageUtils() {
    }

    /**
     * 使用ImageLoader时获取的参数
     *
     * @return DisplayImageOptions
     */
    public static DisplayImageOptions getImageOptions() {
        return getImageOptions(Const.NONE, Const.NONE, Const.NONE);
    }


    /**
     * 使用ImageLoader时获取的参数
     *
     * @param loadingResId  加载中的提示图片，没有为0
     * @param emptyuriResId EmptyUri的提示图片，没有为0
     * @param failResId     加载失败的提示图片，没有为0
     * @return DisplayImageOptions
     */
    public static DisplayImageOptions getImageOptions(int loadingResId, int emptyuriResId, int failResId) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingResId)
                .showImageForEmptyUri(emptyuriResId)
                .showImageOnFail(failResId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    /**
     * 使用ImageLoader时获取的参数
     *
     * @param loadingResId  加载中的提示图片，没有为0
     * @param emptyuriResId EmptyUri的提示图片，没有为0
     * @param failResId     加载失败的提示图片，没有为0
     * @return DisplayImageOptions
     */
    public static DisplayImageOptions getImageOptions(int loadingResId, int emptyuriResId, int failResId, BitmapDisplayer displayer) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingResId)
                .showImageForEmptyUri(emptyuriResId)
                .showImageOnFail(failResId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(displayer)
                .build();
    }

    public static byte[] readData(String path) {
        if (TextUtils.isEmpty(path))
            return null;

        File file = new File(path);
        if (!file.exists())
            return null;

        try {
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            byte[] b = bos.toByteArray();
            bos.flush();
            bos.close();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap bytes2Bitmap(byte[] b) {
        if (null != b && b.length != 0) {
            Bitmap bitmap = null;

            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 2;
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, opt);
            } catch (OutOfMemoryError e) {
                Log.trace(TAG, "bytes2Bitmap-->内存溢出了！" + e);
            } catch (Exception e) {
                Log.trace(TAG, "bytes2Bitmap-->出异常了！" + e);
            }
            return bitmap;
        }
        return null;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 获取bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {
        if (!FileUtils.isFileExist(path))
            return null;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(path));
        } catch (OutOfMemoryError e) {
            Log.trace(TAG, "getBitmap:内存溢出了！");
            e.printStackTrace();
        } catch (Exception e) {
            Log.trace(TAG, "getBitmap:出现异常!");
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 压缩bitmap
     *
     * @param srcPath 原始图片路径
     * @param dstPath 压缩图片路径
     * @param width   目标图片宽度
     * @param height  目标图片高度
     * @param format  目标图片规格
     */
    public static boolean compressBitmap(String srcPath, String dstPath, int width, int height, Bitmap.CompressFormat format) {
        return compressBitmap(srcPath, dstPath, width, height, format, IMG_COMPRESS_RATIO);
    }

    /**
     * 压缩bitmap
     *
     * @param srcPath 原始图片路径
     * @param dstPath 压缩图片路径
     * @param width   目标图片宽度
     * @param height  目标图片高度
     * @param format  目标图片规格
     * @param quality 压缩质量
     */
    public static boolean compressBitmap(String srcPath, String dstPath, int width, int height, Bitmap.CompressFormat format, int quality) {
        boolean isSuccess = true;
        Bitmap bitmap = getScaledBitmap(srcPath, width, height);

        FileOutputStream fos = null;
        try {
            if (null != bitmap) {
                FileUtils.deleteFile(dstPath);
                FileUtils.createNewFile(dstPath);

                File imagefile = new File(dstPath);

                fos = new FileOutputStream(imagefile);
                isSuccess = bitmap.compress(format, quality, fos);
                fos.flush();
            } else {
                isSuccess = false;
            }
        } catch (FileNotFoundException e) {
            Log.trace(TAG, "compressBitmap failed!! FileNotFoundException");
            e.printStackTrace();
            isSuccess = false;
        } catch (IOException e) {
            Log.trace(TAG, "compressBitmap failed!! IOException");
            e.printStackTrace();
            isSuccess = false;
        } catch (Exception e) {
            Log.trace(TAG, "compressBitmap failed!!");
            e.printStackTrace();
            isSuccess = false;
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.trace(TAG, "compressBitmap close failed!!");
                    e.printStackTrace();
                }
            }

        }
        return isSuccess;
    }

    /**
     * 按给定宽高获得编辑图片
     *
     * @param path   图片路径
     * @param width  图片新宽度
     * @param height 图片新高度
     * @return 编辑后的图片
     */
    public static Bitmap getScaledBitmap(String path, int width, int height) {
        //默认尝试两次
        Bitmap src = antiOMDecodeFile(path, 2);
        if (null == src) {
            Log.trace(TAG, "get bitmap outmemory end block1!!");
            return null;
        }

        boolean sameSize = src.getWidth() == width && src.getHeight() == height;

        int degree = getImageInterfaceDegree(path);
        Bitmap modifBitmap;
        if (degree == Const.NONE) {
            modifBitmap = src;
        } else {
            if (degree == 90 || degree == 270) {
                width = width ^ height;
                height = width ^ height;
                width = width ^ height;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            modifBitmap = Bitmap.createBitmap(
                    src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

        }
        return sameSize ? modifBitmap : Bitmap.createScaledBitmap(modifBitmap, width, height, true);
    }

    public static Bitmap getScaledBitmap(Bitmap src, int width, int height) {
        if (null == src)
            return null;

        boolean sameSize = src.getWidth() == width && src.getHeight() == height;
        return sameSize ? src : Bitmap.createScaledBitmap(src, width, height, true);
    }

    /**
     * 获得图片方向转化角度
     *
     * @param imgPath 图片文件完整路径
     * @return 如果返回0则代表不需要转动
     */
    public static int getImageInterfaceDegree(String imgPath) {
        int degree = Const.NONE;
        ExifInterface exif;

        try {
            exif = new ExifInterface(imgPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            Log.trace(TAG, "getImageInterfaceDegree failed!");
            ex.printStackTrace();
        }
        return degree;
    }

    /**
     * 防爆读取图片 ^ ^
     *
     * @param path  图片路径
     * @param retry 重试次数，如果内存溢出的话
     * @return 读取出的图片，在重试次数内仍然失败会返回null
     */
    public static Bitmap antiOMDecodeFile(String path, int retry) {
        if (retry <= Const.NONE) {
            return null;
        }

        Bitmap src = null;
        try {
            src = BitmapFactory.decodeFile(path);
        } catch (OutOfMemoryError ome) {
            ImageLoader.getInstance().clearMemoryCache();
            return antiOMDecodeFile(path, retry - 1);
        } catch (Exception e) {
            Log.trace(TAG, "antiOMDecodeFile exception!");
            e.printStackTrace();
        }
        return src;
    }

    /**
     * 合并图片
     *
     * @param context
     * @param bitmaps
     * @return
     */
    public static Bitmap mergeBitmap(Context context, ArrayList<Bitmap> bitmaps) {
        if (null == bitmaps || bitmaps.size() <= 0) {
            return null;
        }
        if (bitmaps.size() == 1) {
            return bitmaps.get(0);
        }

        Point point = CNPixelsUtil.getDeviceSize(context);
        float padding = 6;
        int itemW = (int) (point.x / 2 - padding * 2);

        int imgHeight = (((bitmaps.size() - 1) / 2 + 1) * point.x / 2);
        Bitmap newBitmap = Bitmap.createBitmap(point.x, imgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        // 设置画布的颜色
        canvas.drawARGB(0, 255, 255, 255);
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        Log.trace(TAG, "itemW-->" + itemW);
        Log.trace(TAG, "imgHeight-->" + imgHeight);

        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            // 先计算切一图片中的部分
            bitmap = getScaledBitmap(bitmap);
            // 再按宽高按等比压缩
            bitmap = getScaledBitmap(bitmap, itemW, itemW);
            float left = padding + (i % 2) * itemW + (i % 2) * padding * 2;
            float top = padding + (i / 2) * itemW + (i / 2) * padding * 2;

            Log.trace(TAG, "left " + i + "-->" + left);
            Log.trace(TAG, "top " + i + "-->" + top);

            canvas.drawBitmap(bitmap, left, top, paint);
        }
        return newBitmap;
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }

        int imgW = bitmap.getWidth();
        int imgH = bitmap.getHeight();

        int x, y, w, h;
        if (imgW > imgH) {
            x = (int) ((imgW - imgH) * 0.5f);
            y = 0;
            w = h = imgH;
        } else if (imgW < imgH) {
            x = 0;
            y = (int) ((imgH - imgW) * 0.5f);
            w = h = imgW;
        } else {
            // 相等的返回原图
            return bitmap;
        }
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }

    public static Point getImgWidthAndHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        int width = options.outWidth;
        int height = options.outHeight;

        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }

    public static int getResizedDimension(int maxPrimary, int maxSecondary,
                                          int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling
        // ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    /**
     * url是否为网络地址，还是本地地址
     *
     * @param url url
     * @return http://url
     */
    public static boolean isHttpImgUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;

        int index = url.indexOf(":");
        if (index != -1) {
            // 有冒号，可能是http开始的
            String strStart = url.substring(0, index);
            if ("HTTP".equals(strStart.toUpperCase())
                    || "HTTPS".equals(strStart.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
