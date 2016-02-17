package com.chengniu.client.common;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	public static DateFormat getFormat(String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format;
	}

	public static Date parse(String source, String pattern) {
		if (source == null) {
			return null;
		}
		Date date = null;
		try {
			date = getFormat(pattern).parse(source);
		} catch (Exception e) {
		}
		return date;
	}

	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		return getFormat(pattern).format(date);
	}

	/**
	 * 返回指定天数位移后的日期
	 */
	public static Date addDay(String date, String pattern, int day) {
		try {
			return offsetDate(parse(date, pattern), Calendar.DATE, day);
		} catch (Exception e) {
		}
		return null;
	}

	private static Calendar convert(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 返回指定年数位移后的日期
	 */
	public static Date yearOffset(Date date, int year) {
		return offsetDate(date, Calendar.YEAR, year);
	}

	/**
	 * 月份添加
	 */
	public static Date addMonth(Date date, int month) {
		return offsetDate(date, Calendar.MONTH, month);
	}

	/**
	 * 返回指定天数位移后的日期
	 */
	public static Date addDay(Date date, int day) {
		return offsetDate(date, Calendar.DATE, day);
	}

	/**
	 * 返回指定日期相应位移后的日期
	 * 
	 * @param date
	 *            参考日期
	 * @param field
	 *            位移单位，见 {@link Calendar}
	 * @param offset
	 *            位移数量，正数表示之后的时间，负数表示之前的时间
	 * @return 位移后的日期
	 */
	private static Date offsetDate(Date date, int field, int offset) {
		Calendar calendar = convert(date);
		calendar.add(field, offset);
		return calendar.getTime();
	}

	/**
	 * 返回当月第一天的日期
	 */
	public static Date firstDay(Date date) {
		Calendar calendar = convert(date);
		calendar.set(Calendar.DATE, 1);
		return calendar.getTime();
	}

	/**
	 * 返回当月最后一天的日期
	 */
	public static Date lastDay(Date date) {
		Calendar calendar = convert(date);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 返回两个日期间的差异天数
	 * 
	 * @param date1
	 *            参照日期
	 * @param date2
	 *            比较日期
	 * @return 参照日期与比较日期之间的天数差异，正数表示参照日期在比较日期之后，0表示两个日期同天，负数表示参照日期在比较日期之前
	 */
	public static int dayDiff(Date date1, Date date2) {
		long diff = date1.getTime() - date2.getTime();
		return (int) (diff / 1000 * 60 * 60 * 24);
	}

	/**
	 * 获取当前时间
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		SimpleDateFormat simple = new SimpleDateFormat(pattern,
				new DateFormatSymbols(Locale.CHINESE));
		return simple.format(Calendar.getInstance().getTime());
	}

	public static void main(String[] args) {
		System.out.println(getCurrentTime("M月d日"));
	}
}