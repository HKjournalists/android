package com.kplus.car.comm;
public class LogUtil {
    
    @SuppressWarnings("unchecked")
    public static String makeLogTag(Class cls) {
        return "single_" + cls.getSimpleName();
    }

}
