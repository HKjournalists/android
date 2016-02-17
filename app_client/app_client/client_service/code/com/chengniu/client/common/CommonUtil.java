package com.chengniu.client.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommonUtil {
	private static final Logger log = LogManager.getLogger(CommonUtil.class);

	/**
	 * gson格式转换，日期采用yyyy-MM-dd HH:mm:ss格式转换
	 * 
	 * @return
	 */
	public static Gson gson() {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.disableHtmlEscaping().create();
		return gson;
	}

	/**
	 * 潜复制，对不同对象的相同属性进行赋值，被改变的是desObject
	 * 
	 * @param source
	 * @param desObject
	 *            目标对象
	 */
	public static <T> T simpleValueCopy(Object source, T desObject) {
		if (source == null || desObject == null)
			return null;
		Method[] sourceMethods = source.getClass().getMethods();
		Method[] desMethods = desObject.getClass().getMethods();
		for (Method m : sourceMethods) {
			String methodName = m.getName();
			if (methodName.startsWith("get")) {
				String methodTmp = methodName.replaceFirst("get", "set");
				for (Method desMethod : desMethods) {
					try {
						if (methodTmp.equals(desMethod.getName())) {
							Object result = m.invoke(source);
							if (result != null)
								desMethod.invoke(desObject, result);
						}
					} catch (Exception e) {
					}
				}
			} else if (methodName.startsWith("is")) {
				String methodTmp = methodName.replaceFirst("is", "set");
				for (Method desMethod : desMethods) {
					try {
						if (methodTmp.equals(desMethod.getName())) {
							Object result = m.invoke(source);
							if (result != null)
								desMethod.invoke(desObject, result);
						}
					} catch (Exception e) {
					}
				}
			}
		}
		return desObject;
	}

	/**
	 * 赋值阳光报价
	 * 
	 * @param <T>
	 * @param source
	 * @param desObject
	 *            目标对象
	 */
	public static <T> T mapCopyToObject(Map<String, Map<String, String>> map,
			Class<T> desClass) {
		try {
			if (map == null || desClass == null)
				return null;
			Iterator<Entry<String, Map<String, String>>> it = map.entrySet()
					.iterator();
			Map<String, String> resultMap = new HashMap<String, String>();
			BigDecimal dxc = new BigDecimal(100);
			while (it.hasNext()) {
				Entry<String, Map<String, String>> entry = it.next();
				try {
					String key = "value";
					if (entry.getKey().toString().endsWith("Price"))
						key = "price";
					if (map.get(entry.getKey().toString()) != null) {
						Map<String, String> value = map.get(entry.getKey()
								.toString());
						if ("price".equals(key)) {
							resultMap.put(entry.getKey().toString(),
									new BigDecimal(value.get(key)).divide(dxc)
											.toString());
						} else if ("value".equals(key)) {
							resultMap.put(entry.getKey().toString(),
									value.get(key));
						}
					}
				} catch (Exception e) {
				}
			}
			// 赋值
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
					.disableHtmlEscaping().create();
			T desObject = gson.fromJson(gson.toJson(resultMap), desClass);
			Field[] fields = desClass.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					String methodTmp = field.getName();
					String value = resultMap.get(methodTmp);
					if (!StringUtils.hasText(value)) {
						if (methodTmp.endsWith("Price")) {
							String tmp = methodTmp.substring(0,
									methodTmp.lastIndexOf("Price"));
							try {
								if (map.get(tmp) != null) {
									value = map.get(tmp).get("price");
									if (StringUtils.hasText(value)) {
										field.set(desObject, new BigDecimal(
												value).divide(dxc));
									}
								}
							} catch (Exception e) {
							}
						}
					}
					if (methodTmp.endsWith("Bjmp")) {
						value = map.get(methodTmp).get("value");
						if ("1".equals(value)) {
							field.set(desObject, true);
						}
					}
				} catch (Exception e) {
				}
			}
			return desObject;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 潜复制，对不同类的相同名称属性进行赋值，被改变的是desClass
	 * 
	 * @param source
	 * @param desClass
	 *            目标对象
	 */
	public static <T> T simpleValueCopy(Object source, Class<T> desClass) {
		if (source == null || desClass == null)
			return null;
		T desObject = null;
		try {
			Class.forName(desClass.getName());
			desObject = desClass.newInstance();
			simpleValueCopy(source, desObject);
		} catch (Exception e) {
			log.debug("潜复制失败", e);
		}
		return desObject;
	}

	/**
	 * 相加
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal add(BigDecimal add, BigDecimal des) {
		if (add == null)
			add = new BigDecimal("0");
		if (des == null)
			des = new BigDecimal("0");
		return toRoundHalf(add.add(des), 3);
	}

	/**
	 * 相减
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
		if (b1 == null)
			b1 = new BigDecimal("0");
		if (b2 == null)
			b2 = new BigDecimal("0");
		return toRoundHalf(b1.subtract(b2), 3);
	}

	public static void main(String[] args) {
		System.err.println(add(new BigDecimal(200.4), new BigDecimal(200.3)));
	}

	/**
	 * 给定精度，四舍五入
	 */
	public static BigDecimal toRoundHalf(BigDecimal b, int s) {
		if (b != null) {
			return b.setScale(s, BigDecimal.ROUND_HALF_UP);
		}
		return null;
	}

	/**
	 * 给定精度向上舍入
	 * 
	 * @param b
	 * @param s
	 * @return
	 */
	public static BigDecimal toRoundUp(BigDecimal b, int s) {
		if (b != null) {
			return b.setScale(s, BigDecimal.ROUND_UP);
		}
		return null;
	}
}