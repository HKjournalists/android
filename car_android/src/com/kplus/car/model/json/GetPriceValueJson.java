package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetPriceValueJson extends BaseModelObj {

	@ApiField("price")
	private Float price;//预计服务费，-1代表不能下单
	@ApiField("isFirstOrder")
	private Boolean isFirstOrder;//该用户是否首单
	@ApiField("origPrice")
	private Float origPrice;//原服务费
	@ApiField("reduced")
	private Boolean reduced;//是否有减免
	@ApiField("reducePrice")
	private Float reducePrice;//减免金额

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Boolean getIsFirstOrder()
	{
		return isFirstOrder;
	}

	public void setIsFirstOrder(Boolean isFirstOrder)
	{
		this.isFirstOrder = isFirstOrder;
	}

	public Float getOrigPrice() {
		return origPrice;
	}

	public void setOrigPrice(Float origPrice) {
		this.origPrice = origPrice;
	}

	public Boolean getReduced() {
		return reduced;
	}

	public void setReduced(Boolean reduced) {
		this.reduced = reduced;
	}

	public Float getReducePrice() {
		return reducePrice;
	}

	public void setReducePrice(Float reducePrice) {
		this.reducePrice = reducePrice;
	}

}
