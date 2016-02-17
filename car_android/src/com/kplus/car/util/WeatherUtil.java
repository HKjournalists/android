package com.kplus.car.util;

import android.content.Context;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.model.Weather;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class WeatherUtil {

    public static int getWeatherIcon(String phenomenon, boolean isGrayIcon) {
        if (StringUtils.isEmpty(phenomenon))
            return 0;

        if (phenomenon.contains("晴")) {
            return isGrayIcon ? R.drawable.sunny : R.drawable.weather_sunny_detail;
        } else if (phenomenon.contains("多云")) {
            return isGrayIcon ? R.drawable.cloudy : R.drawable.weather_cloudy_detail;
        } else if (phenomenon.contains("阴")) {
            return isGrayIcon ? R.drawable.overcast : R.drawable.weather_overcast_detail;
        } else if (phenomenon.contains("雨")) {
            if (phenomenon.equals("阵雨")) {
                return isGrayIcon ? R.drawable.shower : R.drawable.weather_shower_detail;
            } else if (phenomenon.contains("雷阵雨")) {
                return isGrayIcon ? R.drawable.thundershower : R.drawable.weather_thundershower_detail;
            } else if (phenomenon.contains("小")) {
                return isGrayIcon ? R.drawable.light_rain : R.drawable.weather_light_rain_detail;
            } else if (phenomenon.contains("中")) {
                return isGrayIcon ? R.drawable.moderate_rain : R.drawable.weather_moderate_rain_detail;
            } else if (phenomenon.equals("大雨") || phenomenon.equals("大到暴雨")) {
                return isGrayIcon ? R.drawable.heavy_rain : R.drawable.weather_heavy_rain_detail;
            } else if (phenomenon.equals("暴雨")) {
                return isGrayIcon ? R.drawable.storm : R.drawable.weather_storm_detail;
            }
        } else if (phenomenon.contains("雪")) {
            if (phenomenon.contains("小")) {
                return isGrayIcon ? R.drawable.light_snow : R.drawable.weather_light_snow_detail;
            } else if (phenomenon.contains("中")) {
                return isGrayIcon ? R.drawable.moderate_snow : R.drawable.weather_moderate_snow_detail;
            } else if (phenomenon.equals("大雪") || phenomenon.equals("大到暴雪")) {
                return isGrayIcon ? R.drawable.heavy_snow : R.drawable.weather_heavy_snow_detail;
            } else if (phenomenon.equals("暴雪")) {
                return isGrayIcon ? R.drawable.snowstorm : R.drawable.weather_snowstorm_detail;
            } else if (phenomenon.equals("雨夹雪")) {
                return isGrayIcon ? R.drawable.sleet : R.drawable.weather_sleet_detail;
            }
        } else if (phenomenon.equals("雾") || phenomenon.equals("霾")) {
            return isGrayIcon ? R.drawable.foggy : R.drawable.weather_foggy_detail;
        } else if (phenomenon.contains("沙") || phenomenon.equals("浮尘")) {
            return isGrayIcon ? R.drawable.duststorm : R.drawable.weather_duststorm_detail;
        } else if (phenomenon.contains("台风")) {
            return isGrayIcon ? R.drawable.typhoon : R.drawable.weather_typhoon_detail;
        }
        return 0;
    }

    public static String getWashText(long wash) {
        String strWash;
        if (wash == 1 || wash == 2) {
            strWash = "洗个车吧";
        } else {
            strWash = "预约洗车";
        }
        return strWash;
    }

    public static String getHomeWashText(long wash) {
        String strWash;
        if (wash == 1) {
            strWash = "适宜洗车";
        } else {
            strWash = "预约洗车";
        }
        return strWash;
    }

    public static String getWashIndexText(long wash) {
        String strWash = "";
        if (wash == 1) {
            strWash = "适宜洗车";
        } else if (wash == 2) {
            strWash = "较宜洗车";
        } else if (wash == 3) {
            strWash = "不宜洗车";
        }
        return strWash;
    }

    public static int getWashIndexTextColor(long wash) {
        int resId = 0;
        if (wash == 1) {
            // 适宜
            resId = R.color.daze_orangered18;
        } else if (wash == 2) {
            // 较适宜
            resId = R.color.daze_orangered4;
        } else if (wash == 3) {
            // 不适宜
            resId = R.color.daze_black2;
        }
        Context context = KplusApplication.getInstance().getApplicationContext();
        resId = context.getResources().getColor(resId);
        return resId;
    }

    /**
     * 上面的渐变背景
     *
     * @param wash
     * @return
     */
    public static int getBgResIdOfToday(long wash) {
        int resId = 0;
        if (wash == 1) {
            // 适宜洗车的背景
            resId = R.drawable.weather_bg_suitable_washing;
        } else if (wash == 2) {
            // 较适宜
            resId = R.drawable.weather_bg_jiao_suitable_washing;
        } else if (wash == 3) {
            // 不适宜洗车的背景
            resId = R.drawable.weather_bg_no_suitable_washing;
        }
        return resId;
    }

    /**
     * 洗车按钮字体颜色
     *
     * @param wash
     * @return
     */
    public static int getTextColorOfToday(long wash) {
        int resId = 0;
        if (wash == 1) {
            // 适宜洗车的字体颜色
            resId = R.color.daze_orangered5;
        } else if (wash == 2) {
            // 较适宜的字体颜色
            resId = R.color.daze_orangered4;
        } else if (wash == 3) {
            // 不适宜洗车的字体颜色
            resId = R.color.daze_black2;
        }
        return resId;
    }

    public static void sortWeatherForDay(List<Weather> weathers) {
        if (null == weathers) {
            return;
        }
        // 把车型按字母排序
        Collections.sort(weathers, new Comparator<Weather>() {
            @Override
            public int compare(Weather o1, Weather o2) {
                if (null == o1.getType() || null == o2.getType()) {
                    return -1;
                }

                int o1T = o1.getType();
                int o2T = o2.getType();

                if (o1T < o2T) {
                    return -1;
                } else if (o1T == o2T) {
                    return 0;
                } else if (o1T > o2T) {
                    return 1;
                }
                return -1;
            }
        });
    }
}
