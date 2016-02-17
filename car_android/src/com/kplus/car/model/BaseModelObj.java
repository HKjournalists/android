package com.kplus.car.model;

import java.io.Serializable;
import java.util.Map;

import com.kplus.car.parser.Converters;
import com.kplus.car.parser.MapReader;
import com.kplus.car.parser.ModelObj;

public class BaseModelObj implements Serializable,ModelObj{
	private static final long serialVersionUID = 1L;
	public void init(Map<?,?> root,Class<ModelObj> clazz ) throws Exception{
		Converters.convert(clazz, new MapReader(root),this);
	}
}
