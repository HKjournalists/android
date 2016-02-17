package com.kplus.car.comm;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.kplus.car.util.StringUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

/****
 * 下载网络图片的类 集成异步下载、图片缓存等功能
 * 
 * @author wang.yi
 * 
 */
public class AsyncImageLoader {
	// private static final String TAG = "AsyncImageLoader";

	private Map<String, SoftReference<Drawable>> imageCache;
	private FileCache fileCache;
	protected HashMap<String, LoadTask> tasks = new HashMap<String, AsyncImageLoader.LoadTask>();

	public AsyncImageLoader(Map<String, SoftReference<Drawable>> imageCache,
			FileCache fileCache) {
		this.imageCache = imageCache;
		this.fileCache = fileCache;
	}

	public void clearImageCache() {
		if (imageCache != null && !imageCache.isEmpty()) {
			imageCache.clear();
			fileCache.clearImgCache();
		}
	}

	public Drawable loadDrawable(final Context context, final String imageUrl,
			Drawable def, final ImageCallback imageCallback) {
		if (StringUtils.isEmpty(imageUrl)) {
			return def;
		}
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		if (imageCache.containsKey(fileName)) {
			SoftReference<Drawable> softReference = imageCache.get(fileName);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				// Log.e("img", "内存中找到缓存"+fileName);
				return drawable;
			} else {
				// Log.e("img", "缓存被抛弃"+fileName);
			}
		}
		// Log.e("img", "图片在文件缓存中的状态："+fileCache.isCached(fileName));
		if (fileCache.isCached(fileName)) {
			Drawable drawable = fileCache.getCache(fileName);
			if (drawable != null) {
				imageCache.put(fileName, new SoftReference<Drawable>(drawable));
				// Log.e("img", "文件中找到缓存并放入内存"+fileName);
				return drawable;
			}
		}

		// 用线程池来做下载图片的任务
		addTask(imageUrl, imageCallback);
		return def;
	}

	public Drawable loadDrawable1(final Context context, final String imageUrl,
			Drawable def) {
		if (StringUtils.isEmpty(imageUrl)) {
			return def;
		}
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		// Log.e("img", "图片在文件缓存中的状态："+fileCache.isCached(fileName));
		if (fileCache.isCached(fileName)) {
			Drawable drawable = fileCache.getCache(fileName);
			if (drawable != null) {
				return drawable;
			}
		}
		return def;
	}

	public Drawable loadDrawable2(final Context context, final String imageUrl,
			Drawable def, final ImageCallback imageCallback) {
		if (StringUtils.isEmpty(imageUrl)) {
			return def;
		}
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		// Log.e("img", "图片在文件缓存中的状态："+fileCache.isCached(fileName));
		if (fileCache.isCached(fileName)) {
			Drawable drawable = fileCache.getCache(fileName);
			if (drawable != null) {
				return drawable;
			}
		}

		// 用线程池来做下载图片的任务
		addTask(imageUrl, imageCallback);
		return def;
	}

	public Drawable loadImageFromUrl(String imageUrl) {
		Drawable drawable = null;

		String fileName = "";
		// 获取url中图片的文件名与后缀
		fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		if (fileName == null || fileName.length() == 0) {
			return drawable;
		}
		if (!fileCache.isCached(fileName)) {
			// Log.e("img", "开始下载"+imageUrl);
			fileCache.cache(imageUrl);
		}
		// Log.e("img", "下载完成"+imageUrl);
		drawable = fileCache.getCache(fileName);
		if (drawable == null) {
			// Log.e("img", imageUrl+"下载完成重新获取？");
			fileCache.cache(imageUrl);
			drawable = fileCache.getCache(fileName);
		}
		return drawable;
	}

	public void addTask(String url, ImageCallback call) {
		synchronized (tasks) {
			LoadTask task = tasks.get(url);
			if (task == null) {
				task = new LoadTask(url);
				task.calls.add(call);
				tasks.put(url, task);
				task.execute();
			} else {
				task.calls.add(call);
			}
		}
	}

	private class LoadTask extends AsyncTask<Object, Drawable, Drawable> {
		public LinkedList<ImageCallback> calls = new LinkedList<ImageCallback>();
		private String url;

		public LoadTask(String url) {
			this.url = url;
		}

		@Override
		protected Drawable doInBackground(Object... params) {
			// Log.e("img", "启动加载线程");
			Drawable drawable = loadImageFromUrl(url);
			synchronized (tasks) {
				tasks.remove(url);
			}
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable o) {
			if (o != null) {
				for (ImageCallback call : calls) {
					call.imageLoaded(o, url);
				}
			}
		}

	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	public FileCache getFileCache() {
		return fileCache;
	}

}
