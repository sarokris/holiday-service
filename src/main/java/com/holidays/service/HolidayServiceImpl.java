package com.holidays.service;

import com.holidays.model.Holiday;
import com.holidays.model.SortOrder;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
@Service
@AllArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final RestClient restClient;

    @Override
    public List<Holiday> retrieveHolidays(int year, String country) {
        return restClient.get()
                .uri("/PublicHolidays/{year}/{country}",year,country)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<Holiday> getHolidays(String countryCode, int limit, SortOrder sortOrder) {
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
