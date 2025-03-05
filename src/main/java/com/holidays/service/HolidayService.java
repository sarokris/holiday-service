package com.holidays.service;

import com.holidays.model.Holiday;
import com.holidays.model.SortOrder;

import java.util.List;
import java.util.Map;

public interface HolidayService {
   List<Holiday> retrieveHolidays(int year, String country);

   List<Holiday> getHolidays(String countryCode, int limit, SortOrder sortOrder);

   Map<String,Integer> getWorkingDayHolidays(int year,List<String> countryCodes);

   List<Holiday> getCommonHolidays(int year,String firstCountryCode,String secondCountryCode);
}
