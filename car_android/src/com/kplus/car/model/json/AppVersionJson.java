package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class AppVersionJson extends BaseModelObj
{
	@ApiField("hasNew")
	private Boolean hasNew;//是否有新版本
	@ApiField("versionName")
	private String versionName;//新版本名称
	@ApiField("desc")
	private String desc;//新版本描述
	@ApiField("appFileType")
	private Integer appFileType;//安装包类型，1=完整 2=增量
	@ApiField("test")
	private Integer test;//是否测试包，1=是 0=否
	@ApiField("downloadUrl")
	private String downloadUrl;
	
	public Boolean getHasNew()
	{
		return hasNew;
	}
	public void setHasNew(Boolean hasNew)
	{
		this.hasNew = hasNew;
	}
	public String getVersionName()
	{
		return versionName;
	}
	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public Integer getAppFileType()
	{
		return appFileType;
	}
	public void setAppFileType(Integer appFileType)
	{
		this.appFileType = appFileType;
	}
	public Integer getTest()
	{
		return test;
	}
	public void setTest(Integer test)
	{
		this.test = test;
	}
	public String getDownloadUrl()
	{
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}
}
