package com.chengniu.client.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SerializableUtils {
	public static final SimpleDateFormat SIMPLEDATEFORMATHHMMSS = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat(
			"yyyyMMdd");
	private static final AtomicInteger COMMONATOMICINTEGER = new AtomicInteger();
	private static String commonSerializable;

	public static String serializable() {
		String serializable = commonSerializable;
		int length = 1;
		if (commonSerializable == null
				|| commonSerializable.length() < 8
				|| !SerializableUtils.SIMPLEDATEFORMAT.format(new Date())
						.equals(commonSerializable.substring(0, 8))) {
			serializable = null;
			commonSerializable = serializable;
		}
		if (serializable == null) {
			StringBuilder ssb = new StringBuilder();
			ssb.append(SerializableUtils.SIMPLEDATEFORMATHHMMSS
					.format(new Date()));
			int ordersIndex = new Random().nextInt(250);
			if (ordersIndex < 100)
				ordersIndex += 250;
			ssb.append(ordersIndex);
			serializable = ssb.toString();
		}
		String o = String.valueOf(COMMONATOMICINTEGER.getAndIncrement());
		int remainLength = length - o.length();
		StringBuilder sb = new StringBuilder();
		sb.append(serializable);
		if (remainLength > 0) {
			for (int r = 0; r < remainLength; r++) {
				sb.append("0");
			}
		}
		sb.append(o);
		return sb.toString();
	}

	public static String allocUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void main(String[] args) {
		while (true)
			System.err.println(serializable());
	}
}
