package com.holidays.service;

import com.holidays.exception.HolidayException;
import com.holidays.model.Holiday;
import com.holidays.model.SimpleHoliday;
import com.holidays.model.SortOrder;
import com.holidays.util.HolidayUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final RestClient restClient;

    @Override
    public List<Holiday> retrieveHolidays(int year, String country) {
        return restClient.get()
                .uri("/PublicHolidays/{year}/{country}",year,country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new HolidayException(response.getStatusCode().value(), response.getBody().toString());
                })
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<SimpleHoliday> getHolidays(String countryCode, int limit, SortOrder sortOrder) {
        List<SimpleHoliday> resultHolidays;
        resultHolidays = new ArrayList<>(getHolidayList(countryCode,LocalDate.now().getYear(), limit, sortOrder));
        if(resultHolidays.size() < limit){
            //logic to fetch from previous year if limit not reached
            var newLimit = limit - resultHolidays.size();
            int previousYear = LocalDate.now().minusYears(1).getYear();
            resultHolidays.addAll(getHolidayList(countryCode,previousYear,newLimit,SortOrder.DESC));
        }

        return resultHolidays;
    }

    private List<SimpleHoliday> getHolidayList(String countryCode,int year, int limit, SortOrder sortOrder) {
        List<Holiday> holidays = retrieveHolidays(year, countryCode);
        if(CollectionUtils.isEmpty(holidays)){
            throw new HolidayException(HttpStatus.NOT_FOUND.value(),"No Data Found");
        }

        return  holidays.stream()
                .sorted((h1, h2) -> sortOrder == SortOrder.ASC
                        ? h1.date().compareTo(h2.date())
                        : h2.date().compareTo(h1.date())) // Default DESC
                .filter(h -> HolidayUtils.isCurrentOrPast(h.date()))
                .map(this::mapDateWithName)
                .limit(limit)
                .toList();
    }

    private SimpleHoliday mapDateWithName(Holiday holiday) {
        return new SimpleHoliday(holiday.date(), holiday.name());
    }

    @Override
    public Map<String, Long> getWorkingDayHolidays(int year, List<String> countryCodes) {
        var completableFutureList = countryCodes.stream()
                .map(countryCode -> CompletableFuture.supplyAsync(() -> getNonWeekendHolidayCount(year,countryCodes)))
                .toList();
        return completableFutureList.stream()
                .map(CompletableFuture::join)
                .flatMap(map -> map.entrySet().stream())  // Convert to Stream<Entry<String, Long>>
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue())) // Sort by value DESC
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(k1,k2) -> k1, LinkedHashMap::new));
    }

    private Map<String, Long> getNonWeekendHolidayCount(int year, List<String> countryCodes) {
        Map<String, Long> holidayCountMap = new HashMap<>();

        for (String country : countryCodes) {
            List<Holiday> holidays = retrieveHolidays(year, country);

            // Filter holidays that are NOT on weekends
            long count = holidays.stream()
                    .filter(HolidayUtils::isWeekday)
                    .count();

            holidayCountMap.put(country,count);
        }
        return holidayCountMap;
    }

    @Override
    public List<Holiday> getCommonHolidays(int year, String firstCountryCode, String secondCountryCode) {
        return null;
    }
}
