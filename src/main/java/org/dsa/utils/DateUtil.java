package org.dsa.utils;

import java.time.LocalDate;

public class DateUtil {

    private DateUtil(){}

    static public LocalDate dateNow = LocalDate.now();
    static public LocalDate yesterdayDate = LocalDate.now().minusDays(1);
    static public LocalDate tomorrowDate = LocalDate.now().plusDays(1);
    static public LocalDate dateOneYearLater = LocalDate.now().plusYears(1);
    static public LocalDate dateOneYearBefore = LocalDate.now().minusYears(1);
    static public LocalDate sixMonthsLater = LocalDate.now().plusMonths(6);
    static public LocalDate sixMonthsBefore = LocalDate.now().minusMonths(6);
}
