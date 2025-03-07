package com.holidays.controller;

import com.holidays.model.LocalHoliday;
import com.holidays.model.SimpleHoliday;
import com.holidays.service.HolidayService;
import com.holidays.model.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("holidays")
@AllArgsConstructor
@Tag(name="Holiday")
public class HolidayController {

    private final HolidayService holidayService;

    @Operation(
            summary = "This API returns the last celebrated holidays based on the limit"
    )
    @GetMapping("/last/{countryCode}")
    public List<SimpleHoliday> getLastHolidays(
            @PathVariable String countryCode,
            @RequestParam(defaultValue = "ASC") SortOrder sortOrder,
            @RequestParam(defaultValue = "3") int limit) {

        return holidayService.getHolidays(countryCode, limit,sortOrder);
    }

    @Operation(
            summary = "This API returns a number of public holidays not falling on weekends"
    )
    @GetMapping("/public/weekdays/{year}")
    public Map<String,Long> getWeekHolidayCount(@PathVariable Integer year,@RequestParam List<String> countryCodes){
        return holidayService.getWorkingDayHolidays(year,countryCodes);
    }
    @Operation(
            summary = "This API returns the deduplicated list of dates celebrated in both countries with local names"
    )
    @GetMapping("/common/{year}")
    public List<LocalHoliday> getCommonHolidays(@PathVariable Integer year, @RequestParam String firstCountryCode, @RequestParam String secondCountryCode){
        return holidayService.getCommonHolidays(year,firstCountryCode,secondCountryCode);
    }
}
