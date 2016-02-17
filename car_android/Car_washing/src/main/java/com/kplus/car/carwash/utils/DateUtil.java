package com.kplus.car.carwash.utils;


import android.content.Context;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.ServingTime;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static final long HOURS = 60 * 60 * 1000;
    public static final long DAY = 24 * 60 * 60 * 1000;
    public static final long FIVE_MINUTES = 5 * 60 * 1000;
    public static final String YMD_FORMAT = "yyyy年MM月dd日";
    public static final String YMDHM_FORMAT = "yy/MM/dd HH:mm";
    public static final String MDHM_FORMAT = "MM/dd HH:mm";
    public static final String DAY_FORMAT = "MM月dd日";
    public static final String DAY_FORMAT_SHORT = "M月d日";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DAY_TIME_FORMAT = "MM月dd日 HH:mm";
    public static final String YYYY_MM_DD_HHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_P_MM_P_DD_HHMMSS_FORMAT = "yyyy.MM.dd HH:mm:ss";

    public static boolean isToday(long timestamp) {
        long t = new Date().getTime() - timestamp;
        if (t > 0 && t < DAY)
            return true;
        else
            return false;
    }

    public static boolean isTomorrow(long timestamp) {
        long t = new Date().getTime() - timestamp;
        if (t < 0 && t < -DAY)
            return true;
        else
            return false;
    }

    public static String format(String format, long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(timestamp);
//		return DateFormat.format(format, timestamp).toString();
    }

    public static StringBuffer getDetailTime(Context context, long startTime, long endTime, long serviceTime, boolean isDetail) {
        int day = (int) ((startTime - serviceTime) / DAY);
        StringBuffer timeBuffer = new StringBuffer();
        if (isDetail) {
            timeBuffer.append(DateUtil.format(DAY_FORMAT, startTime));
        } else {
            if (day > 2 || day < -2)
                timeBuffer.append(DateUtil.format(DAY_FORMAT, startTime));
            else if (day == 2)
                timeBuffer.append(context.getString(R.string.after_tomorrow));
            else if (day == 1)
                timeBuffer.append(context.getString(R.string.tomorrow));
            else if (day == 0)
                timeBuffer.append(context.getString(R.string.cn_today));
            else if (day == -1)
                timeBuffer.append(context.getString(R.string.yesterday));
            else if (day == -2)
                timeBuffer.append(context.getString(R.string.before_yesterday));
        }

        timeBuffer.append(" ");
        timeBuffer.append(DateUtil.format(TIME_FORMAT, startTime));
        if (endTime > 0) {
            timeBuffer.append("-");
            timeBuffer.append(DateUtil.format(TIME_FORMAT, endTime));
        }
        return timeBuffer;
    }

    public static String getListTimeTab(Context context, long time, long serviceTime) {
        int day = (int) ((time - serviceTime) / DAY);
        int hours = new Time(time).getHours();
        String timeString = null;
        if (day > 2)
            timeString = context.getString(R.string.two_day_after);
        else if (day == 2)
            timeString = context.getString(R.string.after_tomorrow);
        else if (day == 1)
            timeString = context.getString(R.string.tomorrow);
        else if (day == 0)
            timeString = hours + ":00-" + (hours + 1) + ":00";
        else if (day == -1)
            timeString = context.getString(R.string.yesterday);
        else if (day == -2)
            timeString = context.getString(R.string.before_yesterday);
        else if (day < -2)
            timeString = context.getString(R.string.two_day_before);
        return timeString;
    }

    // 添加大小月月份并将其转换为list,方便之后的判断
    private static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private static String[] months_little = {"4", "6", "9", "11"};
    private static List<String> list_big = Arrays.asList(months_big);
    private static List<String> list_little = Arrays.asList(months_little);

    /**
     * 获得今天日期
     *
     * @return
     */
    public static long getTodayData() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * 获得明天日期
     *
     * @return
     */
    public static long getTomorrowData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 31;
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 1;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else {
            day++;
        }
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    /**
     * 获得后天日期
     *
     * @return
     */
    public static long getAfterTomorrowData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 2;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 2;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else {
            day = day + 2;
        }
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    /**
     * 获得两天后日期
     *
     * @return
     */
    public static long getTwoDayAfterData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 3;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 3;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 3;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else {
            day = day + 3;
        }
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public static String minsToString(int mins) {
        int hours = mins / 60;
        mins = mins - hours * 60;
        return String.format("%02d:%02d", hours, mins);
    }

    /**
     * 获取服务时间
     *
     * @param servingTime
     * @param hasDay
     * @return
     */
    public static String getServingTime(ServingTime servingTime, boolean hasDay) {
        String date = DateUtil.format(DateUtil.YYYY_MM_DD, servingTime.getDay().getTime());
        String begTime = DateUtil.minsToString(servingTime.getBeginInMins());
        String endTime = DateUtil.minsToString(servingTime.getEndInMins());

        String time = begTime + "--" + endTime;
        if (begTime.equals("00:00") && endTime.equals("23:59")) {
            time = "全天";
        }
        if (hasDay) {
            time = date + " " + time;
        }
        return time;
    }
}
