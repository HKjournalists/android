package com.kplus.car.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON格式转换器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class JsonConverter implements Converter {

	@SuppressWarnings("unchecked")
	public <T extends ModelObj> T toResponse(String rsp, Class<T> clazz) {
		JSONReader reader = new JSONValidatingReader(new ExceptionErrorListener());
		Object rootObj = reader.read(rsp);
		T root = null;
		try {
			root =  (T) clazz.newInstance();
			if(rootObj instanceof List<?>){
				Map<Object,Object> map = new HashMap<Object, Object>();
				map.put("default", rootObj);
				root.init(map,(Class<ModelObj>) clazz);
			}else{
				root.init((Map<Object,Object>)rootObj,(Class<ModelObj>) clazz);
			}
			return root;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
