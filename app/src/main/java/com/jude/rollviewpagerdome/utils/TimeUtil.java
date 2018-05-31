package com.jude.rollviewpagerdome.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtil {

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
