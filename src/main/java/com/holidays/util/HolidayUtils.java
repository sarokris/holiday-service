package com.holidays.util;

import com.holidays.model.Holiday;
import lombok.experimental.UtilityClass;

import java.time.DayOfWeek;
import java.time.LocalDate;

@UtilityClass
public class HolidayUtils {
    public static boolean isWeekend(Holiday holiday){
        DayOfWeek dayOfWeek = holiday.date().getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static boolean isWeekday(Holiday holiday){
        return !isWeekend(holiday);
    }

    public static boolean isCurrentOrPast(LocalDate date) {
        return date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now());
    }


}
