package com.jude.rollviewpagerdome.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtil {

    /**
     * 判断当前星期
     */
    public static String getCurrentWeedDay() {
        String dayOfWeek = "星期一";
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "星期日";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
        }
        Log.i("TimeUtil", "dayOfWeek=" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        return dayOfWeek;
    }

    public static boolean isTheSameDay(long after, long befor) {
        if (after < 0 || befor < 0)
            return false;
        Calendar calendar1 = Calendar.getInstance(Locale.CHINESE);
        Calendar calendar2 = Calendar.getInstance(Locale.CHINESE);
        calendar1.setTimeInMillis(after);
        calendar2.setTimeInMillis(befor);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
            return true;

        }
        return false;
    }
}
