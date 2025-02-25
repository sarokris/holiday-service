package com.holidays.service;

import com.holidays.service.model.Holiday;

import java.util.List;
import java.util.Map;

public interface HolidayRetriever {
   List<Holiday> retrieveHolidays(int year, String country);

   List<Holiday> getLastThreeHolidays(String countryCode);

   Map<String,Integer> getWorkingDayHolidays(int year,List<String> countryCodes);

   List<Holiday> getCommonHolidays(int year,String firstCountryCode,String secondCountryCode);
}
