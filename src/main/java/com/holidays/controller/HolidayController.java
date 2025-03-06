package com.holidays.controller;

import com.holidays.model.SimpleHoliday;
import com.holidays.service.HolidayService;
import com.holidays.model.SortOrder;
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
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping("/last/{countryCode}")
    public List<SimpleHoliday> getLastHolidays(
            @PathVariable String countryCode,
            @RequestParam(defaultValue = "ASC") SortOrder sortOrder,
            @RequestParam(defaultValue = "3") int limit) {

        return holidayService.getHolidays(countryCode, limit,sortOrder);
    }

    @GetMapping("/public/weekdays/{year}")
    public Map<String,Long> getWeekHolidayCount(@PathVariable Integer year,@RequestParam List<String> countryCodes){
        return holidayService.getWorkingDayHolidays(year,countryCodes);
    }
}
