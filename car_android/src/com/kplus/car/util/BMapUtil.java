package com.kplus.car.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class BMapUtil {
    	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	
	public static Bitmap toRoundBitmap(Bitmap bitmap) {//切圆形图片
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
        } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
        }
         
        Bitmap output = Bitmap.createBitmap(width,
                        height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
         
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
         
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
	}
	
	public static Bitmap toRoundBitmap(Bitmap bitmap, int radius) {//切特定半径圆形图片
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int max = width;
        if(width < height)
        	max = height;
        if(max > 100){
        	width = width*100/max;
        	height = height*100/max;
        	bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
        } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
        }
         
        Bitmap output = Bitmap.createBitmap(width,
                        height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
         
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
         
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
	}
	
	public static Bitmap getBitmapFromUri(Context context, Uri uri){
		Bitmap bm = null;
		try{
			InputStream inputStream;
        	if(uri.getScheme().startsWith("http") || uri.getScheme().startsWith("https")){
        		inputStream = new URL(uri.toString()).openStream();
        	}
        	else{
        		inputStream = context.getContentResolver().openInputStream(uri);
        	}
        	BitmapFactory.Options bfopt = new BitmapFactory.Options();
        	bfopt.inJustDecodeBounds = true;
        	BitmapFactory.decodeStream(inputStream, null, bfopt);
        	bfopt.inSampleSize = calculateInSampleSize(bfopt, 400);
        	bfopt.inJustDecodeBounds = false;
        	inputStream.close();
        	inputStream = null;
        	if(uri.getScheme().startsWith("http") || uri.getScheme().startsWith("https")){
        		inputStream = new URL(uri.toString()).openStream();
        	}
        	else{
        		inputStream = context.getContentResolver().openInputStream(uri);
        	}
        	bm = BitmapFactory.decodeStream(inputStream, null, bfopt);
		}
		catch(Exception e){
			ToastUtil.TextToast(context, "获取图片失败", Toast.LENGTH_SHORT, Gravity.CENTER);
			e.printStackTrace();
		}
		return bm;
	}
	
	public static Bitmap getBitmapFromPath(String path){
		Bitmap bm = null;
		try{
			BitmapFactory.Options bfopt = new BitmapFactory.Options();
			bfopt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, bfopt);
			bfopt.inSampleSize = calculateInSampleSize(bfopt, 400);
        	bfopt.inJustDecodeBounds = false;
        	bm = BitmapFactory.decodeFile(path, bfopt);
		}catch(Exception e){
			e.printStackTrace();
		}
		return bm;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}
		return inSampleSize;
	}
	
	public static Bitmap cutBitmapFromUri(Context context, Uri uri, int cutsize){
		Bitmap bm = null;
		try{
			InputStream inputStream;
        	if(uri.getScheme().startsWith("http") || uri.getScheme().startsWith("https")){
        		inputStream = new URL(uri.toString()).openStream();
        	}
        	else{
        		inputStream = context.getContentResolver().openInputStream(uri);
        	}
        	BitmapFactory.Options bfopt = new BitmapFactory.Options();
        	bfopt.inJustDecodeBounds = true;
        	BitmapFactory.decodeStream(inputStream, null, bfopt);
        	bfopt.inSampleSize = calculateInSampleSizeForCut(bfopt, cutsize);
        	bfopt.inJustDecodeBounds = false;
        	inputStream.close();
        	inputStream = null;
        	if(uri.getScheme().startsWith("http") || uri.getScheme().startsWith("https")){
        		inputStream = new URL(uri.toString()).openStream();
        	}
        	else{
        		inputStream = context.getContentResolver().openInputStream(uri);
        	}
        	bm = BitmapFactory.decodeStream(inputStream, null, bfopt);
        	if(bm != null){
        		int resultWidth = bfopt.outWidth;
				int resultHeight= bfopt.outHeight;
				if(resultWidth >= resultHeight){
					bm = Bitmap.createBitmap(bm, (resultWidth - resultHeight)/2, 0, resultHeight, resultHeight);
				}
				else{
					bm = Bitmap.createBitmap(bm, 0, (resultHeight-resultWidth)/2, resultWidth, resultWidth);
				}
        	}
		}
		catch(Exception e){
			ToastUtil.TextToast(context, "获取图片失败", Toast.LENGTH_SHORT, Gravity.CENTER);
			e.printStackTrace();
			return null;
		}
		return bm;
	}
	
	public static int calculateInSampleSizeForCut(BitmapFactory.Options options, int cutsize) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;
		if(width > height){
			if(height > cutsize){
				inSampleSize = Math.round((float) height / (float) cutsize);
			}
		}
		else{
			if(width > cutsize){
				inSampleSize = Math.round((float) width / (float) cutsize);
			}
		}
		return inSampleSize;
	}
	
	public static Bitmap cutBitmapFromPath(String path, int cutSize){
		Bitmap bm = null;
		try{
			BitmapFactory.Options bfopt = new BitmapFactory.Options();
			bfopt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, bfopt);
			bfopt.inSampleSize = calculateInSampleSizeForCut(bfopt, cutSize);
        	bfopt.inJustDecodeBounds = false;
        	bm = BitmapFactory.decodeFile(path, bfopt);
        	if(bm != null){
        		int resultWidth = bfopt.outWidth;
				int resultHeight= bfopt.outHeight;
				if(resultWidth >= resultHeight){
					bm = Bitmap.createBitmap(bm, (resultWidth - resultHeight)/2, 0, resultHeight, resultHeight);
				}
				else{
					bm = Bitmap.createBitmap(bm, 0, (resultHeight-resultWidth)/2, resultWidth, resultWidth);
				}
        	}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return bm;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
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
			}
		} catch (IOException e) {
			e.printStackTrace();
			return degree;
		}
		return degree;
	}
	
	/**
	 * 旋转图片，使图片保持正确的方向。
	 * @param bitmap 原始图片
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}
	
	public static Bitmap getBitmapFromUrl(Context context, String url){
		Bitmap bm = null;
		try{
			InputStream inputStream = new URL(url).openStream();
        	BitmapFactory.Options bfopt = new BitmapFactory.Options();
        	bfopt.inJustDecodeBounds = true;
        	BitmapFactory.decodeStream(inputStream, null, bfopt);
        	bfopt.inSampleSize = calculateInSampleSize2(bfopt, 32);
        	bfopt.inJustDecodeBounds = false;
        	inputStream.close();
        	inputStream = null;
        	inputStream = new URL(url).openStream();
        	bm = BitmapFactory.decodeStream(inputStream, null, bfopt);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return bm;
	}
	
	public static int calculateInSampleSize2(BitmapFactory.Options options, int reqSize) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;
		if (width*height > reqSize*1024) {
			inSampleSize = Math.round((float)Math.sqrt((double) width*height / (double) (reqSize*1024)));
		}
		return inSampleSize;
	}

    public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap rotateByExifInfo(Bitmap bitmap, String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int rotateDegrees = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            switch (rotateDegrees) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotate(bitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotate(bitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotate(bitmap, 270f);
                default:
                    return bitmap;
            }
        } catch (Exception e) {
            return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        int iWidth = bitmap.getWidth();
        int iHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float fWidth = iWidth;
        float fHeight = iHeight;
        float px = fWidth / 2;
        float py = fHeight / 2;
        matrix.setRotate(degrees, px, py);
        return Bitmap.createBitmap(bitmap, 0, 0, iWidth, iHeight, matrix, true);
    }

    public static void saveBitmap(Bitmap mBitmap, String fileName, int quality)  {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPhotoPathByLocalUri(Context context, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
		if (cursor == null)
			return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

	public static Bitmap getBitmapFromPath(String path, int reqSize){
		Bitmap bm = null;
		try{
			BitmapFactory.Options bfopt = new BitmapFactory.Options();
			bfopt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, bfopt);
			bfopt.inSampleSize = calculateInSampleSize2(bfopt, reqSize);
			bfopt.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(path, bfopt);
		}catch(Exception e){
			e.printStackTrace();
		}
		return bm;
	}

	public static byte[] getContentFromBitmap(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1000 > 500) {
			baos.reset();
			options -= 10;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		byte[] content = baos.toByteArray();
		return content;
	}
}
