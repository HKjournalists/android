package com.chengniu.client.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.util.StringUtils;

public class Xml2JsonUtil {

	public static String replaceMessage(String message) {
		if (StringUtils.hasText(message)) {
			message = message.replace("forceBeginDate", "jqxStartDate");
			message = message.replace("bizBeginDate", "syxStartDate");
			message = message.replace("vehicleTaxPremium", "ccsPrice");
			message = message.replace("bizTotalPremium", "syxPrice");
			message = message.replace("forcePremium", "jqxPrice");
			message = message.replace("standardPremium", "marketPrice");
			message = message.replace("totalPremium", "totalPrice");
			message = message.replace("vehicleFrameNo", "frameNum");
			message = message.replace("engineNo", "motorNum");
			message = message.replace("specialCarDate", "guohudate");
			message = message.replace("vehicleId", "vehicleId");
			message = message.replace("specialCarFlag", "guohu");
			message = message.replace("ownerName", "idCardName");
			message = message.replace("ownerIdNo", "idCardNum");
			message = message.replace("engineNo", "motorNum");
			message = message.replace("firstRegisterDate", "registerDate");
			message = message.replace("cov_200", "csx");
			message = message.replace("cov_701", "sjzrx");
			message = message.replace("cov_702", "ckx");
			message = message.replace("forceFlag", "jqx");
			message = message.replace("cov_600", "szx");
			if(message.indexOf("vehicleModelName")<0)
				message = message.replace("CH_BrandName", "vehicleModelName");
			message = message.replace("cov_321", "zdzxc");
			message = message.replace("cov_500", "dqx");
			message = message.replace("cov_291", "ssx");
			message = message.replace("cov_210", "hfx");
			message = message.replace("cov_231", "blx");
			message = message.replace("cov_310", "zrx");
			message = message.replace("cov_921", "dqxBjmp");
			message = message.replace("cov_922", "hfxBjmp");
			message = message.replace("cov_912", "szxBjmp");
			message = message.replace("cov_928", "sjzrxBjmp");
			message = message.replace("cov_929", "ckxBjmp");
			message = message.replace("cov_911", "csxBjmp");
			message = message.replace("Premium", "Price");
			message = message.replace("\"premium\"", "\"price\"");
			message = message.replace("forcePrice", "jqxPrice");
		}
		return message;
	}

	/**
	 * 转换一个xml格式的字符串到json格式
	 * 
	 * @param xml
	 *            xml格式的字符串
	 * @return 成功返回json 格式的字符串;失败反回null
	 */
	public static String xml2JSON(String xml) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			InputStream is = new ByteArrayInputStream(replaceMessage(xml)
					.getBytes("utf-8"));
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(is);
			Element root = doc.getRootElement();
			map.put(root.getName(), iterateElement(root));
			return CommonUtil.gson().toJson(map);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 转换一个xml格式的字符串到json格式
	 * 
	 * @param xml
	 *            java.io.File实例是一个有效的xml文件
	 * @return 成功反回json 格式的字符串;失败反回null
	 */
	public static Map<String, Map<String, String>> xml2JSONByMap(String xml) {
		Map<String, Map<String, String>> resmap = new HashMap<String, Map<String, String>>();
		try {
			// System.err.println(xml);
			InputStream is = new ByteArrayInputStream(replaceMessage(xml)
					.getBytes("utf-8"));
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(is);
			Element root = doc.getRootElement();
			Map<String, List<Map<String, String>>> map = itmap(root);
			Iterator<Entry<String, List<Map<String, String>>>> it = map
					.entrySet().iterator();
			while (it.hasNext()) {
				try {
					Entry<String, List<Map<String, String>>> entry = it.next();
					resmap.put(entry.getKey(), entry.getValue().get(0));
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
		return resmap;
	}

	/**
	 * 一个迭代方法
	 * 
	 * @param element
	 *            : org.jdom.Element
	 * @return java.util.Map 实例
	 */
	private static Map<String, List<Map<String, String>>> itmap(Element element) {
		@SuppressWarnings("unchecked")
		List<Object> jiedian = element.getChildren();
		Element et = null;
		Map<String, List<Map<String, String>>> obj = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> list = null;
		for (int i = 0; i < jiedian.size(); i++) {
			list = new LinkedList<Map<String, String>>();
			et = (Element) jiedian.get(i);
			if (et.getTextTrim().equals("")) {
				if (et.getChildren().size() == 0)
					continue;
				if (obj.containsKey(et.getName())) {
					list = obj.get(et.getName());
				}
				obj.putAll(itmap(et));
			} else {
				if (obj.containsKey(et.getName())) {
					list = obj.get(et.getName());
				}
				String name = et.getAttributeValue("name");
				String value = et.getTextTrim();
				Map<String, String> m = new HashMap<String, String>();
				m.put("name", name);
				m.put("value", value);
				list.add(m);
				obj.put(et.getName(), list);
			}
		}
		List<Map<String, String>> cha = obj.get("Definition");
		Map<String, String> oms = new HashMap<String, String>();
		if (cha != null && !cha.isEmpty())
			for (Map<String, String> om : cha) {
				try {
					String name = om.get("name").toString();
					String value = om.get("value").toString();
					oms.put(name, value);
				} catch (Exception e) {
				}
			}
		list = new LinkedList<Map<String, String>>();
		list.add(oms);
		obj.put(oms.get("key"), list);
		return obj;
	}

	/**
	 * 一个迭代方法
	 * 
	 * @param element
	 *            : org.jdom.Element
	 * @return java.util.Map 实例
	 */
	private static Map<String, List<Object>> iterateElement(Element element) {
		@SuppressWarnings("unchecked")
		List<Object> jiedian = element.getChildren();
		Element et = null;
		Map<String, List<Object>> obj = new HashMap<String, List<Object>>();
		List<Object> list = null;
		for (int i = 0; i < jiedian.size(); i++) {
			list = new LinkedList<Object>();
			et = (Element) jiedian.get(i);
			if (et.getTextTrim().equals("")) {
				if (et.getChildren().size() == 0)
					continue;
				if (obj.containsKey(et.getName())) {
					list = obj.get(et.getName());
				}
				list.add(iterateElement(et));
				obj.put(et.getName(), list);
			} else {
				if (obj.containsKey(et.getName())) {
					list = obj.get(et.getName());
				}
				String name = et.getAttributeValue("name");
				String value = et.getTextTrim();
				Map<String, String> m = new HashMap<String, String>();
				m.put("name", name);
				m.put("value", value);
				list.add(m);
				obj.put(et.getName(), list);
			}
		}
		return obj;
	}

}