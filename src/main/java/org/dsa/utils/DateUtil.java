package org.dsa.utils;

import java.sql.Date;
import java.time.LocalDate;

public class DateUtil {

    private DateUtil(){}

    static public LocalDate dateNow = LocalDate.now();
    static public LocalDate yesterdayDate = LocalDate.now().minusDays(1);
    static public LocalDate tomorrowDate = LocalDate.now().plusDays(1);
    static public LocalDate nextWeekDate = LocalDate.now().plusWeeks(1);
    static public LocalDate weekBeforeDate = LocalDate.now().plusWeeks(1);
    static public LocalDate thirtyDaysBefore = LocalDate.now().minusMonths(1);
    static public LocalDate thirtyDaysLater = LocalDate.now().plusMonths(1);
    static public LocalDate quarterlyBefore = LocalDate.now().plusMonths(3);
    static public LocalDate quarterlyLater = LocalDate.now().plusMonths(3);
    static public LocalDate halfAYearBefore = LocalDate.now().plusMonths(3);
    static public LocalDate halfAYearLater = LocalDate.now().plusMonths(3);
    static public LocalDate oneYearLater = LocalDate.now().plusYears(1);
    static public LocalDate oneYearBefore = LocalDate.now().minusYears(1);
    static public LocalDate sixMonthsLater = LocalDate.now().plusMonths(6);
    static public LocalDate sixMonthsBefore = LocalDate.now().minusMonths(6);

    public static Date toDate(LocalDate date){return Date.valueOf(date);}
    public static int getDayToday(){return dateNow.getDayOfMonth();}
    public static int getMonthToday(){return dateNow.getMonthValue();}
    public static int getYearToday(){return dateNow.getYear();}
}
