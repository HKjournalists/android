package com.kplus.car.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapReader implements Reader {
	private Map<?, ?> json;

	public MapReader(Map<?, ?> json) {
		this.json = json;
	}

	public boolean hasReturnField(Object name) {
		return json.containsKey(name);
	}

	public Object getPrimitiveObject(Object name) {
		return json.get(name);
	}

	public Object getObject(Object name, Class<?> type)  {
		Object tmp = json.get(name);
		if (tmp instanceof Map<?, ?> && ModelObj.class.isAssignableFrom(type)) {
			Map<?, ?> map = (Map<?, ?>) tmp;
			try {
				ModelObj temp = (ModelObj) type.newInstance();
				temp.init(map, (Class<ModelObj>) type);
				return temp;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public List<?> getListObjects(Object listName, Object itemName,
			Class<?> subType) {
		List<Object> listObjs = null;
		Object listTmp = json.get(listName);
		if (listTmp == null) {
			listTmp = json.get(itemName);
		}

		if (listTmp instanceof List<?>) {
			listObjs = new ArrayList<Object>();
			List<?> tmpList = (List<?>) listTmp;
			for (Object subTmp : tmpList) {
				if (subTmp instanceof Map<?, ?>) {// object
					Map<?, ?> subMap = (Map<?, ?>) subTmp;

					try {
						ModelObj temp = (ModelObj) subType.newInstance();
						temp.init(subMap, (Class<ModelObj>) subType);
						if (temp != null) {
							listObjs.add(temp);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}

				} else if (subTmp instanceof List<?>) {// array
					// TODO not support yet
				} else {// boolean, long, double, string, null
					listObjs.add(subTmp);
				}
			}
		}
		return listObjs;
	}
}