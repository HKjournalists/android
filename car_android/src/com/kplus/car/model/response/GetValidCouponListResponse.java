package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.CouponListJson;
import com.kplus.car.parser.ApiField;

public class GetValidCouponListResponse extends Response{
	@ApiField("data")
	private CouponListJson data;

	public CouponListJson getData() {
		if(data == null)
			data = new CouponListJson();
		return data;
	}

	public void setData(CouponListJson data) {
		this.data = data;
	}
}
