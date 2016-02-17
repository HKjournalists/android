package com.kplus.car.parser;

import java.util.Map;

public interface ModelObj {
	public void init(Map<?,?> root,Class<ModelObj> clazz )throws Exception;
}
