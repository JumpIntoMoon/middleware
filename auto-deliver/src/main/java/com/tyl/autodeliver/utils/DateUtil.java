package com.tyl.autodeliver.utils;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    public static final String DATE_FMT = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(7);
        return dayOfWeek;
    }


    public static boolean isInMiddle(Date currDate, Date startdate, Date enddate) {
        boolean result = false;
        if ((null == currDate) || (null == startdate) || (null == enddate)) {
            return result;
        }
        long currentTimeVal = currDate.getTime();
        result = (currentTimeVal >= startdate.getTime()) && (currentTimeVal < enddate.getTime());
        return result;
    }


    public static String getWeekDayString(Date date) {
        String weekString = "";
        String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(7);
        weekString = dayNames[(dayOfWeek - 1)];
        return weekString;
    }


    public static String getDayString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String tdate = sdf.format(date);
        tdate = tdate + " " + getWeekDayString(date);
        return tdate;
    }

    public static String amOrPm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        int hour = Integer.valueOf(sdf.format(date)).intValue();
        if (hour <= 12) {
            return "上午";
        }
        return "下午";
    }


    public static Date getDateTimeByStr(String date) {
        Date date1 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (date != null)
                date1 = sdf.parse(date);
        } catch (Exception e) {
            System.out.println("将字符串转换成yyyy-MM-dd HH:mm:ss日期出错");
            e.printStackTrace();
        }
        return date1;
    }

    public static Date getDateTimeByString(String date) {
        Date date1 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            if (date != null)
                date1 = sdf.parse(date);
        } catch (Exception e) {
            System.out.println("将字符串转换成yyyy-MM-dd HH:mm:ss日期出错");
            e.printStackTrace();
        }
        return date1;
    }

    public static Date getDateByStr(String date) {
        Date date1 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null)
                date1 = sdf.parse(date);
        } catch (Exception e) {
            System.out.println("将字符串转换成yyyy-MM-dd日期出错");
            e.printStackTrace();
        }
        return date1;
    }

    public static String getDateStrByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getDateStrByMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }

    public static String getDateStrByDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }


    public static String getDateString(Date date, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }


    public static String getDateYesterday() {
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        dBefore = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String defaultStartDate = sdf.format(dBefore);
        return defaultStartDate;
    }


    public static String getDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }


    public static String getDateStrYMDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static Date addHMS(Date date, int hour, int minute, int second) {
        return addHour(addMinute(addSecond(date, second), minute), hour);
    }

    public static Date addMinute(Date date, int num) {
        /* 229 */
        return addDateTime(date, 5, num);
    }

    public static Date addDateTime(Date date, int type, int num) {
        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        switch (type) {
            case 1:
                cal.set(1, cal.get(1) + num);
                break;
            case 2:
                cal.set(2, cal.get(2) + num);
                break;
            case 3:
                cal.set(5, cal.get(5) + num);
                break;
            case 4:
                cal.set(10, cal.get(10) + num);
                break;
            case 5:
                cal.set(12, cal.get(12) + num);
                break;
            case 6:
                cal.set(13, cal.get(13) + num);
        }


        return cal.getTime();
    }

    public static Date addSecond(Date date, int num) {
        /* 268 */
        return addDateTime(date, 6, num);
    }

    public static Date addHour(Date date, int num) {
        /* 272 */
        return addDateTime(date, 4, num);
    }


    public static String getDateStrForSql(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static String getTimeStrForSql(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(timestamp);
    }


    public static String getTimeDateStrForSql(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }

    public static String getDateStr2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String getDateStr3(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getWeekFirstDay() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        cal.setFirstDayOfWeek(2);
        cal.set(7, 2);
        return df.format(cal.getTime());
    }

    public static String getWeekLastDay() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        cal.setFirstDayOfWeek(2);
        cal.set(7, 2);
        cal.add(7, 6);
        return df.format(cal.getTime());
    }


    public static List getSomeDate(String startDateStr, String endDateStr)
            throws ParseException {
        List list = new ArrayList();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = df.parse(startDateStr);
        startCalendar.setTime(startDate);
        Date endDate = df.parse(endDateStr);
        endCalendar.setTime(endDate);
        for (; ; ) {
            startCalendar.add(5, 1);
            if (startCalendar.getTimeInMillis() >= endCalendar.getTimeInMillis()) break;
            list.add(df.format(startCalendar.getTime()));
        }


        return list;
    }


    public static List<String> getMonthBetween(String minDate, String maxDate)
            throws ParseException {
        ArrayList<String> result = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(1), min.get(2), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(1), max.get(2), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(2, 1);
        }
        return result;
    }

    public static String getMonthFirstDay(String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateStrToDate(date));
        c.set(5, 1);
        Date firstDate = c.getTime();
        return formatDate("yyyy-MM-dd", firstDate);
    }

    public static String getMonthLastDay(String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateStrToDate(date));
        c.set(5, 1);
        c.add(2, 1);
        c.add(5, -1);
        Date lastDate = c.getTime();
        return formatDate("yyyy-MM-dd", lastDate);
    }

    public static Date dateStrToDate(String date) {
        Date temp = null;
        try {
            temp = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String formatDate(Date date) {
        /* 426 */
        return formatDate("yyyy-MM-dd", date);
    }

    public static String formatDate(String fmt, Date date) {
        if ((isNull(fmt)) || (isNull(date))) {
            return null;
        }
        String temp = new SimpleDateFormat(fmt).format(date);

        return temp;
    }

    public static boolean isNull(Object value) {
        if (value == null)
            return true;
        if ((value instanceof String)) {
            if (((String) value).trim().replaceAll("\\s", "").equals("")) {
                return true;
            }
        } else if ((value instanceof Collection)) {
            if (((Collection) value).isEmpty()) {
                return true;
            }
        } else if (value.getClass().isArray()) {
            if (Array.getLength(value) == 0) {
                return true;
            }
        } else if ((value instanceof Map)) {
            if (((Map) value).isEmpty()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static Date parseDate(String patten, String date) {
        try {
            return new SimpleDateFormat(patten).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else
                hs.append(stmp);
        }
        return hs.toString();
    }

    public static void main(String[] args) {
        Random localRandom = new Random();
        String str3 = "" + System.currentTimeMillis() + localRandom.nextInt(1000000);
        String param = "appver=19_areading_3.1.10&cgi=&devid=359627010330725&qn-rid=" + str3 + "&secret=" + "qn123456";
        String rlt = Base64.getEncoder().encodeToString(param.getBytes());
        System.out.println(rlt);


        MessageDigest md5 = null;
        try {
            param = "appver=19_areading_3.1.10&devid=359627010330725";
            md5 = MessageDigest.getInstance("MD5");
            rlt = byte2hex(md5.digest(param.getBytes()));
            System.out.println(rlt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
