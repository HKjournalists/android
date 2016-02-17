package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetAgainstRecordListJson;
import com.kplus.car.parser.ApiField;

public class GetAgainstRecordListResponse extends Response {

	@ApiField("data")
	private GetAgainstRecordListJson data;

	public GetAgainstRecordListJson getData() {
		if (data == null)
			data = new GetAgainstRecordListJson();
		return data;
	}

	public void setData(GetAgainstRecordListJson data) {
		this.data = data;
	}

}
