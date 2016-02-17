package com.chengniu.client.action;

import java.util.HashMap;
import java.util.Map;

public class IndexAction extends SuperAction {
	private static final long serialVersionUID = -2027055836778011409L;

	@Override
	public String execute() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);
		return super.ajax(resultMap);
	}
}