package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.Refund;
import com.kplus.car.model.RefundInfo;
import com.kplus.car.model.UserInfo;
import com.kplus.car.parser.ApiField;

public class GetRefundInfoJson extends BaseModelObj{
	@ApiField("refund")
	private Refund refund;

	public Refund getRefund() {
		return refund;
	}

	public void setRefund(Refund refund) {
		this.refund = refund;
	}

	
}
