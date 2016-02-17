package com.kplus.car.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.kplus.car.Request;
import com.kplus.car.Response;
import com.kplus.car.parser.Converter;
import com.kplus.car.parser.JsonConverter;
import com.kplus.car.parser.ModelObj;

public class FileCache {
	private static final String TAG = LogUtil.makeLogTag(FileCache.class);
	private static String IMG_PATH = "img";
	private static String JSON_PATH = "json";
	private File cacheDir;
	private File imgDir;
	private File jsonDir;
	private String imgExt = ".car";//图片后缀，为了不被图片软件识别出来
	
	
	
	public FileCache(Context context){
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			cacheDir = context.getCacheDir();
		}else{
			cacheDir = new File(Environment.getExternalStorageDirectory(),"car");
		}
		
		if(!cacheDir.exists()){
			cacheDir.mkdir();
		}
		
		imgDir = new File(cacheDir, IMG_PATH);
		jsonDir = new File(cacheDir,JSON_PATH);
		if(!imgDir.exists()){
			imgDir.mkdir();
		}
		if(!jsonDir.exists()){
			jsonDir.mkdir();
		}
		
	}
	
	protected void clearImgCache(){
		File file = imgDir;
		if(file.isDirectory()){
			File[] imgfiles = file.listFiles();
			for(File imgfile : imgfiles){
				imgfile.delete();
			}
		}
	}
	
	protected void clearFileCache(){
		File file = jsonDir;
		if(file.isDirectory()){
			File[] jsonfiles = file.listFiles();
			for(File jsonfile : jsonfiles){
				jsonfile.delete();
			}
		}
	}
	
	/*****************************************************************************************
	 * ×××××××××××××××××××××××××××××××××××××××××图片缓存
	 */
	/*****
	 * 判断是否存在缓存
	 * @param imageName
	 * @return
	 */
	public boolean isCached(String name){
	    return new File(imgDir, name+imgExt).exists();
	}
	/****
	 * 缓存图片
	 * @param res
	 */
	public void cache(String url){
		String   fileName   = "";  
		
		// 获取url中图片的文件名与后缀  
		if(url!=null&&url.length()!=0){   
			fileName  = url.substring(url.lastIndexOf("/")+1);  
		}  
		// 图片在手机本地的存放路径,注意：fileName为空的情况  
		File file = new File(imgDir,fileName+imgExt);// 保存文件  
		try {  
			// 可以在这里通过文件名来判断，是否本地有此图片  
			FileOutputStream   fos=new   FileOutputStream( file );  
			
			InputStream is = new URL(url).openStream(); 
			BufferedInputStream bin = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			
			int   data = -1;   
			while((data = bin.read(buffer)) != -1){   
				fos.write(buffer, 0, data);   
			}   
			fos.close();  
			is.close();  
		} catch (Exception e) {  
			file.delete();
			Log.e(TAG, e.toString() + "图片下载及保存时出现异常！");  
		}
		
	}
	/****
	 * 获得图片
	 * @param <T>
	 * @param request
	 * @return
	 */
	public Drawable getCache(String name){
		return Drawable.createFromPath(new File(imgDir, name+imgExt).toString());
	}
	/****
	 * 获得图片
	 * @param <T>
	 * @param request
	 * @return
	 */
	public File getCacheFile(String name){
		return new File(imgDir, name+imgExt);
	}
	/*************************************************************************************************
	 * **************************************************缓存TOP接口数据
	 */
	public boolean isCached(Request<?> request){
		if(isAllowed(request)){
			String fileName = request.getResponseClass().getName() + ".json";
			File file = new File(jsonDir, fileName);
			if(file.exists()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	/****
	 * 缓存数据
	 * @param res
	 * @throws  
	 */
	public void cache(Response response)  {
		String fileName = response.getClass().getName() + ".json";
		try {
			File file = new File(jsonDir, fileName);
			saveFile(file, response.getBody());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/****
	 * 获得缓存数据
	 * @param <T>
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws ApiException 
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends ModelObj> T getCache(Request request) throws Exception  {
		String fileName = request.getResponseClass().getName() + ".json";
		File file = new File(jsonDir, fileName);
		T root;
		String body;
		body = getBody(file);
		Log.e(TAG, body);
		Converter converter = new JsonConverter();
		
		root = (T) converter.toResponse(body,  request.getResponseClass());
		return root;

	}
	
	private void saveFile(File file,String body) throws IOException{
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(body.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	private String getBody(File file) throws IOException{
		FileInputStream inputstream = new FileInputStream(file);
		StringBuffer buffer = new StringBuffer();
		String line; // 用来保存每行读取的内容
		BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputstream));
		line = bufferreader.readLine(); // 读取第一行
		while (line != null) { // 如果 line 为空说明读完了
		buffer.append(line); // 将读到的内容添加到 buffer 中
		// buffer.append("\n"); // 添加换行符
		line = bufferreader.readLine(); // 读取下一行
		}
		// 将读到 buffer 中的内容写出来
		inputstream.close();
		return buffer.toString();
	}
	//设置时候缓存
	public boolean isAllowed(Request<?> request){
		return false;
	}
	public File getImgDir() {
		return imgDir;
	}
	 
}
