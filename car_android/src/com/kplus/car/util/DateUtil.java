package com.kplus.car.util;

import android.content.Context;

import com.kplus.car.R;
import com.kplus.car.activity.CertificateActivity;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	private int year;
	private int month;
	private int day;
	private boolean leap;
	final static String chineseNumber[] = { "一", "二", "三", "四", "五", "六", "七",
			"八", "九", "十", "十一", "十二" };
	final static String Big_Or_Small[] = { "大", "小", "大", "小", "大", "小", "大",
			"大", "小", "大", "小", "大" };
	private String[] LunarHolDayName = { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
			"清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露",
			"秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
			" yyyy年MM月dd日 ");
	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	// ====== 传回农历 y年的总天数
	final private static int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	// ====== 传回农历 y年闰月的天数
	final private static int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	// ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	final private static int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	// ====== 传回农历 y年m月的总天数
	final private static int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	// ====== 传回农历 y年的生肖
	final public String animalsYear() {
		final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇",
				"马", "羊", "猴", "鸡", "狗", "猪" };
		return Animals[(year - 4) % 12];
	}

	// ====== 传入 月日的offset 传回干支, 0=甲子
	final private static String cyclicalm(int num) {
		final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚",
				"辛", "壬", "癸" };
		final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午",
				"未", "申", "酉", "戌", "亥" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	// ====== 传入 offset 传回干支, 0=甲子
	final public String cyclical() {
		int num = year - 1900 + 36;
		return (cyclicalm(num));
	}

	/** */
	/**
	 * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
	 * dayCyl5:与1900年1月31日相差的天数,再加40 ?
	 * 
	 * @param cal
	 * @return
	 */
	public DateUtil(Calendar cal) {
		// cal.add(cal.get(Calendar.DAY_OF_MONTH),1);
		@SuppressWarnings(" unused ")
		int yearCyl, monCyl, dayCyl;
		int leapMonth = 0;
		Date baseDate = null;
		try {
			baseDate = chineseDateFormat.parse(" 1900年1月31日 ");
		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
		}

		// 求出和1900年1月31日相差的天数
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		dayCyl = offset + 40;
		monCyl = 14;

		// 用offset减去每农历年的天数
		// 计算当天是农历第几天
		// i最终结果是农历的年份
		// offset是当年的第几天
		int iYear, daysOfYear = 0;
		for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
			monCyl += 12;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
			monCyl -= 12;
		}
		// 农历年份
		year = iYear;

		yearCyl = iYear - 1864;
		leapMonth = leapMonth(iYear); // 闰哪个月,1-12
		leap = false;

		// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
		int iMonth, daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// 闰月
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else
				daysOfMonth = monthDays(year, iMonth);

			offset -= daysOfMonth;
			// 解除闰月
			if (leap && iMonth == (leapMonth + 1))
				leap = false;
			if (!leap)
				monCyl++;
		}
		// offset为0时，并且刚才计算的月份是闰月，要校正
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
				--monCyl;
			}
		}
		// offset小于0时，也要校正
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
			--monCyl;
		}
		month = iMonth;
		day = offset + 1;
	}

	public static String getChinaDayString(int day) {
		String chineseTen[] = { "初", "十", "廿", "卅" };
		int n = day % 10 == 0 ? 9 : day % 10 - 1;
		if (day > 30)
			return "";
		if (day == 10)
			return "初十";
		else
			return chineseTen[day / 10] + chineseNumber[n];
	}

	public String toString() {
		return /* cyclical() + "年" + */(leap ? "闰" : "") + get_month() + "月"
				+ getChinaDayString(day);
	}

	public String numeric_md() {// 返回阿拉伯数字的阴历日期
		String temp_day;
		String temp_mon;
		temp_mon = month < 10 ? "0" + month : "" + month;
		temp_day = day < 10 ? "0" + day : "" + day;

		return temp_mon + temp_day;
	}

	public String get_month() {// 返回阴历的月份
		return month == 1 ? "正" : chineseNumber[month - 1];
	}

	public String get_date() {// 返回阴历的天
		return getChinaDayString(day);
	}

	public String get_Big_Or_Small() {// 返回的月份的大或小
		return Big_Or_Small[month - 1];
	}
	
	public static long getDaysFromNow(Calendar cld){//计算两个日期间的天数
		long result = 0;
		Calendar nowCalendar = Calendar.getInstance();
		String strFrom = "" + nowCalendar.get(Calendar.YEAR) + "-" 
							+ (nowCalendar.get(Calendar.MONTH)+1) + "-" 
							+ nowCalendar.get(Calendar.DAY_OF_MONTH);
		String strTo = "" + cld.get(Calendar.YEAR) + "-" + (cld.get(Calendar.MONTH)+1) + "-" + cld.get(Calendar.DAY_OF_MONTH);
		try
		{
			long lFrom = df.parse(strFrom).getTime();
			long lTo = df.parse(strTo).getTime();
			result = (lTo-lFrom)/(1000 * 60 * 60 * 24);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 功能描述：获取当前的网络时间
	 *
	 */
	public static long getTimeFromWeb()
	{
		long retTime = 0;
		try
		{
			URL urlTime = new URL("http://www.baidu.com");// 取得资源对象
			URLConnection uc = urlTime.openConnection();// 生成连接对象
			uc.setConnectTimeout(5000);
			uc.setReadTimeout(5000);
			uc.connect(); // 发出连接
			retTime = uc.getDate(); // 取得网站日期时间
		}
		catch (Exception e)
		{
			retTime = 0;
			e.printStackTrace();
		}
		return retTime;
	}
	
	public static long getUnixTimestamp(String time, String format){
		long result = 0;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(time);
			result = date.getTime();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getShowTimeOnInterval(long time){
		String result = null;
		Date dateTemp = new Date(time);
//		Calendar calendarNow = Calendar.getInstance();
//		Calendar calendarTemp = Calendar.getInstance();
//		calendarTemp.setTimeInMillis(time);
//		if(calendarTemp.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR)){
//			if(calendarTemp.get(Calendar.WEEK_OF_YEAR) == calendarNow.get(Calendar.WEEK_OF_YEAR)){
//				if(calendarTemp.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR)){
//					result = new SimpleDateFormat("HH:mm").format(dateTemp);
//				}
//				else
//					result = "星期" + getCnWeek(calendarTemp.get(Calendar.DAY_OF_WEEK) - 1);
//			}
//			else{
//				result = new SimpleDateFormat("yyyy-MM-dd").format(dateTemp);
//			}
//		}
//		else{
//			result = new SimpleDateFormat("yyyy-MM-dd").format(dateTemp);
//		}
		result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTemp);
		return result;
	}
	
	public static String getShowTimeOnInterval(String time, String format){
		String result = null;
		Date dateTemp = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			dateTemp = sdf.parse(time);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		long timeTemp = getUnixTimestamp(time, format);
		if(timeTemp == 0)
			return null;
		Calendar calendarNow = Calendar.getInstance();
		Calendar calendarTemp = Calendar.getInstance();
		calendarTemp.setTimeInMillis(timeTemp);
		if(calendarTemp.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR)){
			if(calendarTemp.get(Calendar.WEEK_OF_YEAR) == calendarNow.get(Calendar.WEEK_OF_YEAR)){
				if(calendarTemp.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR)){
					result = new SimpleDateFormat("HH:mm").format(dateTemp);
				}
				else
					result = "星期" + getCnWeek(calendarTemp.get(Calendar.DAY_OF_WEEK) - 1);
			}
			else{
				result = new SimpleDateFormat("yyyy-MM-dd").format(dateTemp);
			}
		}
		else{
			result = new SimpleDateFormat("yyyy-MM-dd").format(dateTemp);
		}
		return result;
	}
	
	public static int getYearFromTime(long time){
		int year = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	public static int getMonthFromTime(long time){
		int month = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		month = (calendar.get(Calendar.MONTH) + 1);
		return month;
	}
	
	public static int getDayFromTime(long time){
		int day = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	public static int getHourFromTime(long time){
		int hour = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	
	public static int getMinuteFromTime(long time){
		int minute = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		minute = calendar.get(Calendar.MINUTE);
		return minute;
	}
	
	public static int getSecondFromTime(long time){
		int second = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		second = calendar.get(Calendar.SECOND);
		return second;
	}
	
	public static String getCnWeek(int week) {
		switch (week) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
		default:
			return "日";
		}
	}

    public static int getGapCount(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return 0;
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Calendar getCalender(String date, String format){
        Calendar cal = Calendar.getInstance();
        if (date != null){
            try {
                cal.setTime(new SimpleDateFormat(format).parse(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cal;
    }

	private static String getVehicleLastNum(String vehicleNum){
		if (StringUtils.isEmpty(vehicleNum))
			return "";
		String lastNum = "";
		for (int i = vehicleNum.length() - 1; i >= 0; i--) {
			char c = vehicleNum.charAt(i);
			if (c >= '0' && c <= '9') {
				lastNum = String.valueOf(c);
				break;
			}
		}
		return lastNum;
	}

	public static Calendar getNextRestrictRemindDate(String restrictNums, String vehicleNum, String remindDateType, String remindDate){
		Calendar calendar = Calendar.getInstance();
		if (StringUtils.isEmpty(restrictNums) || StringUtils.isEmpty(vehicleNum) || StringUtils.isEmpty(remindDateType) || StringUtils.isEmpty(remindDate))
			return calendar;
		Date date;
		try {
			date = new SimpleDateFormat("HH:mm").parse(remindDate);
			calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
			calendar.set(Calendar.MINUTE, date.getMinutes());
		}catch (ParseException e){
			e.printStackTrace();
		}
		Calendar curCal = Calendar.getInstance();
		curCal.add(Calendar.SECOND, 1);
		while (calendar.before(curCal))
			calendar.add(Calendar.DATE, 1);
		if ("1".equals(remindDateType))
			calendar.add(Calendar.DATE, 1);
		String lastNum = getVehicleLastNum(vehicleNum);
		int count = 0;
		while (!isRestrict(calendar, restrictNums, lastNum)){
			calendar.add(Calendar.DATE, 1);
			if (count++ > 365)
				break;
		}
		if ("1".equals(remindDateType))
			calendar.add(Calendar.DATE, -1);
		return calendar;
	}

	public static String getRestrictDesc(Context context, String restrictNums, String vehicleNum){
		String lastNum = getVehicleLastNum(vehicleNum);
		if (StringUtils.isEmpty(restrictNums) || StringUtils.isEmpty(lastNum))
			return "";
		String result = null;
		if (restrictNums.contains(";")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String[] timeNums = restrictNums.split(";");
			for (String timeNum : timeNums) {
				String time = timeNum.split("#")[0];
				String num = timeNum.split("#")[1];
				Date date;
				try {
					date = sdf.parse(time);
					if (Calendar.getInstance().getTime().getTime() - date.getTime() < 86400000) {
						result = num;
						break;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			result = restrictNums;
		}
		if (result != null) {
			String desc = "";
			String[] nums = result.split(",");
			List<Integer> restrictList = new ArrayList<>();
			for (int i = 0; i < nums.length; i++){
				if (!StringUtils.isEmpty(nums[i]) && nums[i].contains(lastNum))
					restrictList.add(i);
			}
			switch (nums.length) {
				case 2:
					if (restrictList.get(0) == 0)
						desc = "单日限行";
					else
						desc = "双日限行";
					break;
				case 5:
					String[] weekArr = context.getResources().getStringArray(R.array.week);
					desc = "每" + weekArr[restrictList.get(0)] + "限行";
					break;
				case 7:
					weekArr = context.getResources().getStringArray(R.array.week);
					desc = "每";
					for (int i = 0; i < restrictList.size(); i++){
						desc += weekArr[restrictList.get(i)];
						if (i != restrictList.size() - 1)
							desc += "、";
					}
					desc += "限行";
					break;
				case 8:
					desc = "日期个位数为" + nums[restrictList.get(0)].replace("|", "、") + "的日子限行";
					break;
			}
			return "您的车" + vehicleNum + desc;
		}
		return "";
	}

	private static boolean isRestrict(Calendar calendar, String restrictNums, String lastNum){
		if (calendar == null || StringUtils.isEmpty(restrictNums) || StringUtils.isEmpty(lastNum))
			return false;
		String result = null;
		if (restrictNums.contains(";")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String[] timeNums = restrictNums.split(";");
			for (String timeNum : timeNums) {
				String time = timeNum.split("#")[0];
				String num = timeNum.split("#")[1];
				Date date;
				try {
					date = sdf.parse(time);
					if (calendar.getTime().getTime() - date.getTime() < 86400000) {
						result = num;
						break;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			result = restrictNums;
		}
		if (result != null) {
			String[] nums = result.split(",");
			String num = null;
			switch (nums.length) {
				case 2:
					num = nums[(calendar.get(Calendar.DATE) + 1) % 2];
					break;
				case 5:
					Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
						num = null;
					else
						num = nums[dayOfWeek - 2];
					break;
				case 7:
					num = nums[(calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7];
					break;
				case 8:
					dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
						num = null;
					else
						num = nums[(calendar.get(Calendar.DATE) + 4) % 5];
					break;
			}
			if (!StringUtils.isEmpty(num) && num.contains(lastNum))
				return true;
		}
		return false;
	}

	public static String getRemindLabel(String[] labelArr, int gap){
		String label;
		switch (gap){
			case 15:
				label = "提前15天";
				break;
			case 30:
				label = "提前30天";
				break;
			case 60:
				label = "提前60天";
				break;
			default:
				label = labelArr[gap];
				break;
		}
		return label;
	}
}
