package com.sky.xposed.rimet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String DATETIME_ONLY_DIGIT = "yyyyMMddHHmmss";
    public static final String DATE_ONLY_DIGIT = "yyyyMMdd";


    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parse(String dtStr, String pattern) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dtStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date addDay(Date date, int delta) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.DAY_OF_MONTH, delta);
        return cd.getTime();
    }

    /**
     * 判断某个事件是否在指定的开始时间和结束时间范围内.
     * @param dtNow
     * @param startTime HHmm
     * @param endTime HHmm
     * @return
     */
    public static boolean isInTime(Date dtNow, String startTime, String endTime) {
        String dayStr = DateUtils.format(dtNow, DateUtils.DATE_ONLY_DIGIT);
        Date dtStart = DateUtils.parse(dayStr+startTime+"00", DateUtils.DATETIME_ONLY_DIGIT);
        Date dtEnd = DateUtils.parse(dayStr+endTime+"00", DateUtils.DATETIME_ONLY_DIGIT);
        if(dtEnd.before(dtStart)){
            if(dtNow.after(dtStart)){
                dtEnd = DateUtils.addDay(dtEnd, 1);
            }else if(dtNow.before(dtStart)){
                dtStart = DateUtils.parse(dayStr+"000000", DateUtils.DATETIME_ONLY_DIGIT);
            }

        }
        boolean bInTime = dtNow.after(dtStart) && dtNow.before(dtEnd);


        return bInTime;
    }
}
