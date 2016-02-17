package com.kplus.car.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import com.kplus.car.Constants;
import com.kplus.car.Response;
import com.kplus.car.util.StringUtils;

/**
 * 转换工具类。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class Converters {

	public static boolean isCheckJsonType = false; // 是否对JSON返回的数据类型进行校验，默认不校验。给内部测试JSON返回时用的开关。规则：返回的"基本"类型只有String,Long,Boolean,Date,采取严格校验方式，如果类型不匹配，报错

	public static SimpleDateFormat format;
	static {
		format = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone(Constants.DATE_TIMEZONE));
	}

	private static final Set<String> baseFields = new HashSet<String>();
	private static final Set<String> myBaseFields = new HashSet<String>();

	static {
		baseFields.add("errorCode");
		baseFields.add("msg");
		baseFields.add("subCode");
		baseFields.add("subMsg");
		baseFields.add("body");
		baseFields.add("params");
		baseFields.add("success");
		baseFields.add("topForbiddenFields");
	}
	static {
		myBaseFields.add("code");
		myBaseFields.add("message");
	}

	private Converters() {
	}

	/**
	 * 使用指定 的读取器去转换字符串为对象。
	 * 
	 * @param <T>
	 *            领域泛型
	 * @param clazz
	 *            领域类型
	 * @param reader
	 *            读取器
	 * @param res
	 *            已经实例好的对象
	 * @return 领域对象
	 * @throws ApiException
	 */
	public static void convert(Class<ModelObj> clazz, Reader reader,
			ModelObj rsp) {

		try {
			if (rsp == null) {
				rsp = clazz.newInstance();
			}

			Field[] fs = getResponseFile(clazz);

			for (Field f : fs) {

				Method method = getWriteMethod(f);
				if (method == null) {
					continue;
				}

				String itemName = f.getName();
				String listName = null;

				Field field = f;

				ApiField jsonField = field.getAnnotation(ApiField.class);
				if (jsonField != null && !"".equals(jsonField.value())) {
					itemName = jsonField.value();
				}
				ApiListField jsonListField = field
						.getAnnotation(ApiListField.class);
				if (jsonListField != null && !"".equals(jsonListField.value())) {
					listName = jsonListField.value();
				}

				if (!reader.hasReturnField(itemName)) {
					if (listName == null || !reader.hasReturnField(listName)) {
						continue; // ignore non-return field
					}
				}

				Class<?> typeClass = field.getType();
				// 目前
				if (String.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof String) {
						method.invoke(rsp, value.toString());
					} else {
						if (value != null) {
							method.invoke(rsp, value.toString());
						} else {
							method.invoke(rsp, "");
						}
					}
				} else if (Long.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof Long) {
						method.invoke(rsp, (Long) value);
					} else {
						if (StringUtils.isNumeric(value)) {
							method.invoke(rsp, Long.valueOf(value.toString()));
						}
					}
				} else if (Integer.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof Integer) {
						method.invoke(rsp, (Integer) value);
					} else {

						if (StringUtils.isNumeric(value)) {
							method.invoke(rsp,
									Integer.valueOf(value.toString()));
						}
					}
				} else if (Boolean.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof Boolean) {
						method.invoke(rsp, (Boolean) value);
					} else {

						if (value != null) {
							method.invoke(rsp,
									Boolean.valueOf(value.toString()));
						}
					}
				} else if (Double.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof Double) {
						method.invoke(rsp, (Double) value);
					} else {
						method.invoke(rsp, Double.valueOf(value.toString()));
					}
				} else if (Float.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof Float) {
						method.invoke(rsp, (Float) value);
					} else {
						method.invoke(rsp, Float.valueOf(value.toString()));
					}
				} else if (Number.class.isAssignableFrom(typeClass)) {
					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof Number) {
						method.invoke(rsp, (Number) value);
					} else {

					}
				} else if (Date.class.isAssignableFrom(typeClass)) {

					Object value = reader.getPrimitiveObject(itemName);
					if (value instanceof String) {
						method.invoke(rsp, format.parse(value.toString()));
					}
				} else if (List.class.isAssignableFrom(typeClass)) {
					Type fieldType = field.getGenericType();
					if (fieldType instanceof ParameterizedType) {
						ParameterizedType paramType = (ParameterizedType) fieldType;
						Type[] genericTypes = paramType
								.getActualTypeArguments();
						if (genericTypes != null && genericTypes.length > 0) {
							if (genericTypes[0] instanceof Class<?>) {
								Class<?> subType = (Class<?>) genericTypes[0];
								List<?> listObjs = reader.getListObjects(
										listName, itemName, subType);
								if (listObjs != null) {
									method.invoke(rsp, listObjs);
								}
							}
						}
					}
				} else {
					Object obj = reader.getObject(itemName, typeClass);
					if (obj != null) {
						method.invoke(rsp, obj);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Method getWriteMethod(Field f) {
		if (f == null)
			return null;
		else {
			String methodEnd = "set"
					+ f.getName().substring(0, 1).toUpperCase()
					+ f.getName().substring(1);
			try {
				return f.getDeclaringClass().getDeclaredMethod(methodEnd,
						new Class[] { f.getType() });
			} catch (SecurityException e) {
				return null;
			} catch (NoSuchMethodException e) {
				return null;
			}
		}
	}

	private static Field[] getResponseFile(Class<?> clazz) {
		Field[] root = clazz.getDeclaredFields();
		if (!Response.class.isAssignableFrom(clazz)) {
			return root;
		} else if (Response.class.isAssignableFrom(clazz)) {
			Field[] e = Response.class.getDeclaredFields();
			Field[] temp = new Field[e.length + root.length];
			for (int i = 0; i < e.length; i++) {
				temp[i] = e[i];
			}
			for (int i = 0; i < root.length; i++) {
				temp[e.length + i] = root[i];
			}
			root = temp;
		}
		return root;
	}
}
