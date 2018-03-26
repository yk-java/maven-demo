package com.glens.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/2/26 15:17
 */
public class DateTimeUtils {
    public static final int BYWEEK = 1;
    public static final int BYMONTH = 2;
    public static final int BYSEASON = 3;
    public static final int BYSEMIYEAR = 4;

    public DateTimeUtils() {
    }

    public static Date getDateNow() {
        return Calendar.getInstance().getTime();
    }

    public static String getStringDateNow() {
        Date currentTime = new Date();
        return DateFormat.getDateTimeInstance().format(currentTime);
    }

    public static String getStringDateNowShort() {
        Date currentTime = new Date();
        return DateFormat.getDateInstance().format(currentTime);
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    public static String dateToStrLong(Date dateDate) {
        if (dateDate == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(dateDate);
        }
    }

    public static String dateToStr(Date dateDate) {
        if (dateDate == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(dateDate);
        }
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    public static Date before(Date dtDate, long lDays) {
        long lCurrentDate = 0L;
        lCurrentDate = dtDate.getTime() - lDays * 24L * 60L * 60L * 1000L;
        return new Date(lCurrentDate);
    }

    public static Date after(Date dtDate, long lDays) {
        long lCurrentDate = 0L;
        lCurrentDate = dtDate.getTime() + lDays * 24L * 60L * 60L * 1000L;
        return new Date(lCurrentDate);
    }

    public static String[] before(Integer year, Integer month, Integer IMonths) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        c.add(Calendar.MONTH, -IMonths);
        return new String[]{String.valueOf(c.get(Calendar.YEAR)), String.valueOf(c.get(Calendar.MONTH) + 1)};
    }

    public static String[] after(Integer year, Integer month, Integer IMonths) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        c.add(Calendar.MONTH, IMonths);
        return new String[]{String.valueOf(c.get(Calendar.YEAR)), String.valueOf(c.get(Calendar.MONTH) + 1)};
    }

    public static int getDaysOfMonth(int nYear, int nMonth) {
        int nDay = Integer.parseInt(((String) getDaysPerMonth().get(String.valueOf(nMonth))));
        if (nMonth == 2 && nYear % 4 == 0 && nYear % 100 > 0) {
            nDay = 29;
        }

        return nDay;
    }

    public static String getYestoday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return (new SimpleDateFormat("yyyyMMdd")).format(calendar.getTime());
    }

    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 122400000L * day;
        return new Date(date_3_hm);
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, cal.getMinimum(Calendar.DATE));
        return (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        int value = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DATE, value);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
    }

    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0L;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / 86400000L;
        } catch (Exception var7) {
            return "";
        }

        return String.valueOf(day);
    }

    public static long intervalDays(Date dtBeginDate, Date dtEndDate) {
        if (dtBeginDate != null && dtEndDate != null) {
            GregorianCalendar gc1 = new GregorianCalendar();
            gc1.setTime(dtBeginDate);
            GregorianCalendar gc2 = new GregorianCalendar();
            gc2.setTime(dtEndDate);
            gc1.clear(14);
            gc1.clear(13);
            gc1.clear(12);
            gc1.clear(11);
            gc1.clear(10);
            gc2.clear(14);
            gc2.clear(13);
            gc2.clear(12);
            gc2.clear(11);
            gc2.clear(10);
            long lInterval = 0L;
            lInterval = gc2.getTime().getTime() - gc1.getTime().getTime();
            lInterval /= 86400000L;
            return lInterval;
        } else {
            return -1L;
        }
    }

    private static HashMap<String, String> getDaysPerMonth() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("1", "31");
        hm.put("2", "28");
        hm.put("3", "31");
        hm.put("4", "30");
        hm.put("5", "31");
        hm.put("6", "30");
        hm.put("7", "31");
        hm.put("8", "31");
        hm.put("9", "30");
        hm.put("10", "31");
        hm.put("11", "30");
        hm.put("12", "31");
        return hm;
    }

    public static String getNextDay(String nowdate, int delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String mdate = "";
            Date d = strToDate(nowdate);
            long myTime = d.getTime() / 1000L + (long) (delay * 24 * 60 * 60);
            d.setTime(myTime * 1000L);
            mdate = format.format(d);
            return mdate;
        } catch (Exception var7) {
            return "";
        }
    }

    public static Date getNextDate(Date dtDate, int nDay) {
        if (dtDate == null) {
            return null;
        } else {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(dtDate);
            calendar.add(5, nDay);
            return calendar.getTime();
        }
    }

    public static Date getNextMonth(Date dtDate, int nMonth) {
        if (dtDate == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return getDateTime(calendar.get(1), calendar.get(2) + 1 + nMonth, calendar.get(5), 0, 0, 0);
        }
    }

    public static Date getPreviousMonth(Date dtDate, int nMonth) {
        if (dtDate == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return getDateTime(calendar.get(1), calendar.get(2) + 1 - nMonth, calendar.get(5), 0, 0, 0);
        }
    }

    public static Date getDateTime(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }

    public static Date getFirstDateOfMonth(Date dtDate) {
        if (dtDate == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return getDateTime(calendar.get(1), calendar.get(2) + 1, 1, 0, 0, 0);
        }
    }

    public static Date getLastDateOfMonth(Date dtDate) {
        if (dtDate == null) {
            return null;
        } else {
            Date firstday = getFirstDateOfMonth(dtDate);
            Date nextMonth = getNextMonth(firstday, 1);
            return getPreviousDate(nextMonth);
        }
    }

    public static Date getPreviousDate(Date dtDate) {
        if (dtDate == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return getDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE) - 1, 0, 0, 0);
        }
    }

    public static Date getLastDateOfSeason(Date dtDate) {
        if (dtDate == null) {
            return null;
        } else {
            Date dtRes = null;
            int nMonth = getMonth(dtDate);
            if (nMonth <= 2) {
                dtRes = getLastDateOfMonth(getNextMonth(dtDate, 2 - nMonth));
            } else if (nMonth <= 5) {
                dtRes = getLastDateOfMonth(getNextMonth(dtDate, 5 - nMonth));
            } else if (nMonth <= 8) {
                dtRes = getLastDateOfMonth(getNextMonth(dtDate, 8 - nMonth));
            } else {
                dtRes = getLastDateOfMonth(getNextMonth(dtDate, 11 - nMonth));
            }

            return dtRes;
        }
    }

    public static Date getLastDateOfSemiYear(Date dtDate) {
        if (dtDate == null) {
            return null;
        } else {
            Date dtRes = null;
            int nMonth = getMonth(dtDate);
            if (nMonth <= 5) {
                dtRes = getLastDateOfMonth(getNextMonth(dtDate, 5 - nMonth));
            } else {
                dtRes = getLastDateOfMonth(getNextMonth(dtDate, 11 - nMonth));
            }

            return dtRes;
        }
    }

    public static int getYear(Date dtDate) {
        if (dtDate == null) {
            return -1;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return calendar.get(Calendar.YEAR);
        }
    }

    public static int getMonth(Date dtDate) {
        if (dtDate == null) {
            return -1;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return calendar.get(Calendar.MONTH) + 1;
        }
    }

    public static int getDay(Date dtDate) {
        if (dtDate == null) {
            return -1;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            return calendar.get(Calendar.DATE);
        }
    }

    public static int getWeekDay(Date dtDate) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(dtDate);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Date[] splitIntoSections(Date beginDate, Date endDate, int nType) {
        Date[] timeTable;
        ArrayList<Date> lstTimeTable;
        timeTable = null;
        lstTimeTable = new ArrayList<>();
        Date dtTemp = null;
        label73:
        switch (nType) {
            case 1:
                lstTimeTable.add(beginDate);
                int nWeekDay = getWeekDay(beginDate);
                dtTemp = getNextDate(beginDate, 7 - nWeekDay);
                if (dtTemp.getTime() >= endDate.getTime()) {
                    dtTemp = endDate;
                }

                lstTimeTable.add(dtTemp);

                while (true) {
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        break label73;
                    }

                    dtTemp = getNextDate(dtTemp, 1);
                    lstTimeTable.add(dtTemp);
                    nWeekDay = getWeekDay(dtTemp);
                    dtTemp = getNextDate(dtTemp, 7 - nWeekDay);
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        dtTemp = endDate;
                    }

                    lstTimeTable.add(dtTemp);
                }
            case 2:
                lstTimeTable.add(beginDate);
                dtTemp = getLastDateOfMonth(beginDate);
                if (dtTemp.getTime() >= endDate.getTime()) {
                    dtTemp = endDate;
                }

                lstTimeTable.add(dtTemp);

                while (true) {
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        break label73;
                    }

                    dtTemp = getNextDate(dtTemp, 1);
                    lstTimeTable.add(dtTemp);
                    dtTemp = getLastDateOfMonth(dtTemp);
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        dtTemp = endDate;
                    }

                    lstTimeTable.add(dtTemp);
                }
            case 3:
                lstTimeTable.add(beginDate);
                dtTemp = getLastDateOfSeason(beginDate);
                if (dtTemp.getTime() >= endDate.getTime()) {
                    dtTemp = endDate;
                }

                lstTimeTable.add(dtTemp);

                while (true) {
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        break label73;
                    }

                    dtTemp = getNextDate(dtTemp, 1);
                    lstTimeTable.add(dtTemp);
                    dtTemp = getLastDateOfSeason(dtTemp);
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        dtTemp = endDate;
                    }

                    lstTimeTable.add(dtTemp);
                }
            case 4:
                lstTimeTable.add(beginDate);
                dtTemp = getLastDateOfSemiYear(beginDate);
                if (dtTemp.getTime() >= endDate.getTime()) {
                    dtTemp = endDate;
                }

                lstTimeTable.add(dtTemp);

                for (; dtTemp.getTime() < endDate.getTime(); lstTimeTable.add(dtTemp)) {
                    dtTemp = getNextDate(dtTemp, 1);
                    lstTimeTable.add(dtTemp);
                    dtTemp = getLastDateOfSemiYear(dtTemp);
                    if (dtTemp.getTime() >= endDate.getTime()) {
                        dtTemp = endDate;
                    }
                }
        }

        timeTable = new Date[0];
        timeTable = (Date[]) lstTimeTable.toArray(timeTable);
        return timeTable;
    }

    public static Date formatDate(Date date, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.getDefault());
        String strDate = sdf.format(date);
        Date result = null;

        try {
            result = sdf.parse(strDate);
            return result;
        } catch (ParseException var6) {
            return new Date();
        }
    }

    public static String formatDate2Str(Date date, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.getDefault());
        return sdf.format(date);
    }

    public static Map<Integer, Integer> getWeekAmtByMonth(Date date) {
        Map<Integer, Integer> weeks = new HashMap<>();
        Date d0 = getFirstDateOfMonth(date);
        Date d1 = getNextDate(d0, 27);
        Date d2 = getLastDateOfMonth(date);
        long days = intervalDays(d1, d2);
        int week1 = getWeekDay(d1);
        int i;
        if (days == 0L) {
            for (i = 0; i < 7; ++i) {
                weeks.put(week1 + i >= 7 ? week1 + i - 7 : week1 + i, 4);
            }
        }

        if (days == 1L) {
            weeks.put(week1, 5);

            for (i = 1; i < 7; ++i) {
                weeks.put(week1 + i >= 7 ? week1 + i - 7 : week1 + i, 4);
            }
        }

        if (days == 2L) {
            weeks.put(week1, 5);
            weeks.put(week1 + 1 >= 7 ? week1 + 1 - 7 : week1 + 1, 5);

            for (i = 2; i < 7; ++i) {
                weeks.put(week1 + i >= 7 ? week1 + i - 7 : week1 + i, 4);
            }
        }

        if (days == 3L) {
            weeks.put(week1, 5);
            weeks.put(week1 + 1 >= 7 ? week1 + 1 - 7 : week1 + 1, 5);
            weeks.put(week1 + 2 >= 7 ? week1 + 2 - 7 : week1 + 2, 5);

            for (i = 3; i < 7; ++i) {
                weeks.put(week1 + i >= 7 ? week1 + i - 7 : week1 + i, 4);
            }
        }

        return weeks;
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public static String[] getDaysOfYearWeek(int year, int week) {
        String[] weekDays = new String[7];
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(1, year);
        c.set(3, week);
        c.set(7, 1);
        weekDays[0] = df.format(c.getTime());
        c.set(7, 2);
        weekDays[1] = df.format(c.getTime());
        c.set(7, 3);
        weekDays[2] = df.format(c.getTime());
        c.set(7, 4);
        weekDays[3] = df.format(c.getTime());
        c.set(7, 5);
        weekDays[4] = df.format(c.getTime());
        c.set(7, 6);
        weekDays[5] = df.format(c.getTime());
        c.set(7, 7);
        weekDays[6] = df.format(c.getTime());
        return weekDays;
    }

    public static String getWeekDaysOfYearWeek(int year, int week) {
        String weekDays = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(1, year);
        c.set(3, week);
        c.set(7, 1);
        weekDays = df.format(c.getTime()) + ",";
        c.set(7, 2);
        weekDays = weekDays + df.format(c.getTime()) + ",";
        c.set(7, 3);
        weekDays = weekDays + df.format(c.getTime()) + ",";
        c.set(7, 4);
        weekDays = weekDays + df.format(c.getTime()) + ",";
        c.set(7, 5);
        weekDays = weekDays + df.format(c.getTime()) + ",";
        c.set(7, 6);
        weekDays = weekDays + df.format(c.getTime()) + ",";
        c.set(7, 7);
        weekDays = weekDays + df.format(c.getTime());
        return weekDays;
    }

    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (subYear == 0) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
        } else return -1 == subYear && 11 == cal1.get(2) && cal1.get(3) == cal2.get(3);

    }

    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week;
        }

        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    public static String getWeek(String sdate, int num) {
        Date dd = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if (num == 1) {
            c.set(Calendar.DAY_OF_WEEK, 2);
        } else if (num == 2) {
            c.set(Calendar.DAY_OF_WEEK, 3);
        } else if (num == 3) {
            c.set(Calendar.DAY_OF_WEEK, 4);
        } else if (num == 4) {
            c.set(Calendar.DAY_OF_WEEK, 5);
        } else if (num == 5) {
            c.set(Calendar.DAY_OF_WEEK, 6);
        } else if (num == 6) {
            c.set(Calendar.DAY_OF_WEEK, 7);
        } else if (num == 7) {
            c.set(Calendar.DAY_OF_WEEK, 1);
        }

        return (new SimpleDateFormat("yyyy-MM-dd")).format(c.getTime());
    }

    public static String getWeek(String sdate) {
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (new SimpleDateFormat("EEEE")).format(c.getTime());
    }

    public static boolean IsLeapYear(int year) {
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
    }

    public static int GetAllDays(int year) {
        return IsLeapYear(year) ? 366 : 365;
    }

    public static int GetMaxDay(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return IsLeapYear(year) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return -1;
        }
    }

    public static int GetDays(int year, int month, int day) {
        int sum = 0;

        for (int i = 1; i < month; ++i) {
            sum += GetMaxDay(year, i);
        }

        return sum + day;
    }

    public static int CompareDay(int year1, int month1, int day1, int year2, int month2, int day2) {
        return year1 != year2 ? year1 - year2 : (month1 != month2 ? month1 - month2 : day1 - day2);
    }

    public static int compareDate(Date dtOne, Date dtOther) {
        int result = 0;
        if (dtOne != null && dtOther != null) {
            Calendar calOne = Calendar.getInstance();
            calOne.clear();
            calOne.setTime(dtOne);
            calOne.set(Calendar.HOUR_OF_DAY, 0);
            calOne.set(Calendar.MINUTE, 0);
            calOne.set(Calendar.SECOND, 0);
            Calendar calOther = Calendar.getInstance();
            calOther.clear();
            calOther.setTime(dtOther);
            calOther.set(Calendar.HOUR_OF_DAY, 0);
            calOther.set(Calendar.MINUTE, 0);
            calOther.set(Calendar.SECOND, 0);
            return calOne.compareTo(calOther);
        } else {
            return result;
        }
    }
}
