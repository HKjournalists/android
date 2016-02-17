package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class ImageInfo extends BaseModelObj
{
	@ApiField("imgUrl")
	private String imgUrl;
	@ApiField("actionType")
	private String actionType;
	@ApiField("actionValue")
	private String actionValue;
	@ApiField("orderId")
	private Integer orderId;
	@ApiField("valid")
	private Boolean valid;
	
	private String imagePath;
	
	public String getImgUrl()
	{
		return imgUrl;
	}
	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}
	public String getActionType()
	{
		return actionType;
	}
	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}
	public String getActionValue()
	{
		return actionValue;
	}
	public void setActionValue(String actionValue)
	{
		this.actionValue = actionValue;
	}
	public Integer getOrderId()
	{
		return orderId;
	}
	public void setOrderId(Integer orderId)
	{
		this.orderId = orderId;
	}
	public String getImagePath()
	{
		return imagePath;
	}
	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
}
