package com.holidays.controller;

import com.holidays.service.HolidayService;
import com.holidays.model.Holiday;
import com.holidays.model.SortOrder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("holidays")
@AllArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping("/last/{countryCode}")
    public List<Holiday> getLastHolidays(
            @PathVariable String countryCode,
            @RequestParam(defaultValue = "DESC") SortOrder sortOrder,
            @RequestParam(defaultValue = "3") int limit) {

        return holidayService.getHolidays(countryCode, limit,sortOrder);
    }
}
