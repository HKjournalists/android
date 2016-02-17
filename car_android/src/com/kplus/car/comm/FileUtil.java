package com.kplus.car.comm;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;

public class FileUtil
{
	public static void copyfile(File fromFile, File toFile, Boolean rewrite)
	{
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return ;
		}
		if (!fromFile.canRead()) {
			return ;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		try {
			java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); //将内容写到新文件当中
			}
			fosfrom.close();
			fosto.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String getParentDirectory()
	{	 	   
		String filePath = "";
		String  ExternalStorageState=android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(ExternalStorageState)) //有SD卡
			{
				filePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
			}
		else
		   {  //找可写的内置SD卡
			   File filetmp=new File("/mnt");
			   File[] subFile = filetmp.listFiles();
			   for(int i = 0; i < subFile.length; i++) 
				{
				   if(!subFile[i].isDirectory())
					   continue;					 
					 String dirname=subFile[i].getName();
					 if(dirname==null || dirname.length()<=0)
						 continue;					  
					 if(dirname.indexOf("sdcard")<0)
						 continue;				 
					String path=subFile[i].getAbsolutePath();
					File file1=new File(subFile[i].getAbsolutePath()+"/DCIM/Camera");
					boolean canWrite=file1.exists();
					if (!file1.exists()) 
						canWrite=file1.mkdirs(); 					 
					 if(canWrite)
					   {  filePath = subFile[i].getAbsolutePath() + "/DCIM/Camera/";
						      break;
					   }
				}
				
				// 没有sdcard
			    if(filePath.length()<=0)
				  filePath = Environment.getDataDirectory().getPath() + "/data/com.kplus.car/files/";
		   }
		
		File file = new File(filePath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		return filePath;
	}
	
	public static String getContainerParentDirectory()
	{	 	   
		String filePath = Environment.getDataDirectory().getPath() + "/data/com.kplus.car/files/";		
		File file = new File(filePath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		return filePath;
	}
	
	public static String getAppRootPath(){
		String filePath = "";
		String  ExternalStorageState=android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(ExternalStorageState)){
			filePath = Environment.getExternalStorageDirectory().getPath() + "/car/"; //有SD卡
		}
		else{
			File filetmp=new File("/mnt");
			File[] subFile = filetmp.listFiles();
			for(int i = 0; i < subFile.length; i++) {
				if(!subFile[i].isDirectory())
					continue;
				String dirname=subFile[i].getName();
				if(dirname==null || dirname.length()<=0)
					continue;
				if(dirname.indexOf("sdcard")<0)
					continue;
				String path=subFile[i].getAbsolutePath();
				File file1=new File(subFile[i].getAbsolutePath()+"/car/");
				boolean canWrite=file1.exists();
				if (!file1.exists())
					canWrite=file1.mkdirs();
				if(canWrite){
					filePath = subFile[i].getAbsolutePath() + "/car/";
					break;
				}
			}
			if(filePath.length()<=0)
				filePath = Environment.getDataDirectory().getPath() + "/data/com.kplus.car/files/";
		}
		File file = new File(filePath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		
		return filePath;
	}
}
