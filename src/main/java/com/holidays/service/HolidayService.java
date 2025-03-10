package com.holidays.service;

import com.holidays.model.Holiday;
import com.holidays.model.LocalHoliday;
import com.holidays.model.SimpleHoliday;
import com.holidays.model.SortOrder;

import java.util.List;
import java.util.Map;

public interface HolidayService {
   List<Holiday> retrieveHolidays(int year, String country);

   List<SimpleHoliday> getHolidays(String countryCode, int limit, SortOrder sortOrder);

   Map<String,Long> getWorkingDayHolidays(int year,List<String> countryCodes);

   List<LocalHoliday> getCommonHolidays(int year, String firstCountryCode, String secondCountryCode);
}
