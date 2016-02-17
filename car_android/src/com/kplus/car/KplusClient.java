package com.kplus.car;

import java.io.IOException;
import java.util.Map;

import com.kplus.car.model.response.request.BaseRequest;
import com.kplus.car.parser.ObjectJsonParser;
import com.kplus.car.util.FileItem;
import com.kplus.car.util.WebUtils;

public class KplusClient {

	public <T extends Response> T execute(BaseRequest<T> request)
			throws IOException {
		ObjectJsonParser<T> parser = new ObjectJsonParser<T>(
				request.getResponseClass());
		String json = "";
		json = WebUtils.doGet(
				request.getServerUrl() + request.getApiMethodName(),
				request.getTextParams());
		if(request.getApiMethodName().equals("/against/getRecords.htm")){
			json = json.replaceAll("\\\\", "");
			json = json.replaceAll("\"\\{", "{");
			json = json.replaceAll("\"\\[\\{", "[{");
			json = json.replaceAll("\\}\"", "}");
			json = json.replaceAll("\\}\\]\"", "}]");
		}
		T rs = parser.parse(json);
		rs.setBody(json);
		return rs;
	}

	public <T extends Response> T executePost(BaseRequest<T> request)
			throws IOException {
		ObjectJsonParser<T> parser = new ObjectJsonParser<T>(
				request.getResponseClass());
		String json = "";
		json = WebUtils.doPost(
				request.getServerUrl() + request.getApiMethodName(),
				request.getTextParams(), 15000, 15000);
		T rs = parser.parse(json);
		rs.setBody(json);
		return rs;
	}

	public <T extends Response> T executePostWithParams(BaseRequest<T> request,
			Map<String, FileItem> fileParams) throws IOException {
		ObjectJsonParser<T> parser = new ObjectJsonParser<T>(
				request.getResponseClass());
		String json = "";
		json = WebUtils.doPost(
				request.getServerUrl() + request.getApiMethodName(),
				request.getTextParams(), fileParams, 120000, 120000);
		T rs = parser.parse(json);
		rs.setBody(json);
		return rs;
	}

}
