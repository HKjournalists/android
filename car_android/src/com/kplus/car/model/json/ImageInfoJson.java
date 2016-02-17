package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.ImageInfo;
import com.kplus.car.parser.ApiListField;

public class ImageInfoJson extends BaseModelObj
{
	@ApiListField("flash")
	private List<ImageInfo> flash;
	@ApiListField("flash1")
	private List<ImageInfo> flash1;
	@ApiListField("head")
	private List<ImageInfo> head;
	@ApiListField("body")
	private List<ImageInfo> body;
	@ApiListField("vehicleHead")
	private List<ImageInfo> vehicleHead;
	
	public List<ImageInfo> getFlash() {
		return flash;
	}
	public void setFlash(List<ImageInfo> flash) {
		this.flash = flash;
	}
	public List<ImageInfo> getFlash1() {
		return flash1;
	}
	public void setFlash1(List<ImageInfo> flash1) {
		this.flash1 = flash1;
	}
	public List<ImageInfo> getHead() {
		return head;
	}
	public void setHead(List<ImageInfo> head) {
		this.head = head;
	}
	public List<ImageInfo> getBody() {
		return body;
	}
	public void setBody(List<ImageInfo> body) {
		this.body = body;
	}
	public List<ImageInfo> getVehicleHead() {
		return vehicleHead;
	}
	public void setVehicleHead(List<ImageInfo> vehicleHead) {
		this.vehicleHead = vehicleHead;
	}
}
