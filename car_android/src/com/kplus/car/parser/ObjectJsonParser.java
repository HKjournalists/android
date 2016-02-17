package com.kplus.car.parser;


/**
 * 单个JSON对象解释器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectJsonParser<T extends ModelObj>  {

	private Class<T> clazz;

	public ObjectJsonParser(Class<T> clazz) {
		this.clazz =  clazz;
	}

	public T parse(String rsp)  {
		Converter converter = new JsonConverter();
		return converter.toResponse(rsp,  clazz);
	}

	public Class<T> getResponseClass() {
		return  clazz;
	}

}
