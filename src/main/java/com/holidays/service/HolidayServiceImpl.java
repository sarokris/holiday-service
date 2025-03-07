package com.holidays.service;

import com.holidays.exception.HolidayException;
import com.holidays.model.Holiday;
import com.holidays.model.LocalHoliday;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final RestClient restClient;

    @Override
    public List<Holiday> retrieveHolidays(int year, String country) {
        List<Holiday> holidays =  restClient.get()
                .uri("/PublicHolidays/{year}/{country}",year,country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new HolidayException(response.getStatusCode().value(), response.getBody().toString());
                })
                .body(new ParameterizedTypeReference<>() {
                });
        if(CollectionUtils.isEmpty(holidays)){
            throw new HolidayException(HttpStatus.NOT_FOUND.value()
                    ,String.format("No Data Found for the countrycode: %s and the year: %d",country,year));
        }
        return holidays;
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
                .map(countryCode -> CompletableFuture.supplyAsync(() -> getNonWeekendHolidayCount(year,countryCode)))
                .toList();
        return completableFutureList.stream()
                .map(CompletableFuture::join)
                .flatMap(map -> map.entrySet().stream())  // Convert to Stream<Entry<String, Long>>
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue())) // Sort by value DESC
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(k1,k2) -> k1, LinkedHashMap::new));
    }

    private Map<String, Long> getNonWeekendHolidayCount(int year, String countryCode) {
        Map<String, Long> holidayCountMap = new HashMap<>();
        List<Holiday> holidays = retrieveHolidays(year, countryCode);

        // Filter holidays that are NOT on weekends
        long count = holidays.stream()
                .filter(HolidayUtils::isWeekday)
                .count();

        holidayCountMap.put(countryCode,count);
        return holidayCountMap;
    }

    @Override
    public List<LocalHoliday> getCommonHolidays(int year, String country1, String country2) {
        CompletableFuture<List<Holiday>> c1HolidayFuture = CompletableFuture.supplyAsync(() -> retrieveHolidays(year, country1));
        CompletableFuture<List<Holiday>> c22HolidayFuture = CompletableFuture.supplyAsync(() -> retrieveHolidays(year, country2));
        return c1HolidayFuture.thenCombine(c22HolidayFuture,(c1Holidays,c2Holidays) -> {

            Map<LocalDate, Map<String,String>> c1HolidaysMap = getHolidays(country1, c1Holidays);

            Map<LocalDate, Map<String,String>> c2HolidaysMap = getHolidays(country2,c2Holidays);

            // Find de-duplicated dates
            Set<LocalDate> commonDates = new HashSet<>(c1HolidaysMap.keySet());
            commonDates.retainAll(c2HolidaysMap.keySet());

            // Build a de-duplicated list of holidays
            return commonDates.stream()
                    .map(date -> new LocalHoliday(date, mergeLocalNames(c1HolidaysMap.get(date),(c2HolidaysMap.get(date)))))
                    .sorted(Comparator.comparing(LocalHoliday::date))
                    .toList();
        }).join();
    }

    private static Map<LocalDate, Map<String, String>> getHolidays(String country1, List<Holiday> c1Holidays) {
        return c1Holidays.stream()
                .collect(Collectors.toMap(Holiday::date,
                        holiday -> getHolidayMap(country1,holiday.localName()),
                        (v1,v2) -> {v1.putAll(v2); return v1;}));
    }

    private static Map<String, String> getHolidayMap(String country1,String localName) {
        Map<String,String> localNameMap = new HashMap<>();
        localNameMap.put(country1, localName);
        return localNameMap;
    }

    private Map<String, String> mergeLocalNames(Map<String,String> c1HolidayMap, Map<String,String> c2HolidayMap) {
        Map<String, String> merged = new HashMap<>(c1HolidayMap);
        merged.putAll(c2HolidayMap);
        return merged;
    }
}
