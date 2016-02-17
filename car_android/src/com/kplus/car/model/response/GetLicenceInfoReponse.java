package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.LicenceInfo;
import com.kplus.car.parser.ApiField;

public class GetLicenceInfoReponse extends Response {
	@ApiField("data")
	private LicenceInfo data;

	public LicenceInfo getData() {
		if(data == null)
			data = new LicenceInfo();
		return data;
	}

	public void setData(LicenceInfo data) {
		this.data = data;
	}
	
}
