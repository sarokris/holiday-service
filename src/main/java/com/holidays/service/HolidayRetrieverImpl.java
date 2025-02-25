package com.holidays.service;

import com.holidays.service.model.Holiday;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class HolidayRetrieverImpl implements HolidayRetriever{
    @Override
    public List<Holiday> retrieveHolidays(int year, String country) {
        return null;
    }

    @Override
    public List<Holiday> getLastThreeHolidays(String countryCode) {
        return null;
    }

    @Override
    public Map<String, Integer> getWorkingDayHolidays(int year, List<String> countryCodes) {
        return null;
    }

    @Override
    public List<Holiday> getCommonHolidays(int year, String firstCountryCode, String secondCountryCode) {
        return null;
    }
}
