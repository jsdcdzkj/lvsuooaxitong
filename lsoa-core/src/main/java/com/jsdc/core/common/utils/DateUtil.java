package com.jsdc.core.common.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2020/7/16.
 */
public class DateUtil extends DateUtils {
    /**
     * 默认日期格式
     */
    public static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static String parsePatterns[] = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS"};
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @param dateStr 转成date
     * @return
     */
    public static Date parseDate(String dateStr) {
        Date d = null;
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(dateStr, parsePatterns);
        } catch (Exception e) {
            d = new Date();
        }
        return d;
    }

    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
        String sDate = f.format(date);
        return sDate;
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 把所有日期字符串 统一输出 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static String formatDate(String dateStr) {
        Date d = null;
        try {
            d = org.apache.commons.lang3.time.DateUtils.parseDate(dateStr, parsePatterns);
            return sdf.format(d);
        } catch (Exception e) {
            d = new Date();
            return sdf.format(d);
        }
    }

    public static String format(Date d) {
        return sdf.format(d);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }


    /**
     * 获取前几天数据信息
     *
     * @return
     */
    public static String yesterDay(Integer amount) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, amount);
        return df.format(calendar.getTime());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String currentDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return df.format(date.getTime());
    }


    /**
     * 当天开始时间
     *
     * @return
     */
    public static String startTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return df.format(date.getTime()) + "00:00:00";
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    public static String endTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return df.format(date.getTime()) + "23:59:59";
    }

}
