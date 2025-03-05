package com.holidays;

import com.holidays.model.Holiday;
import com.holidays.service.HolidayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Test
    public void testRetrieveHolidays(){
        List<Holiday> nlHolidayList = holidayService.retrieveHolidays(2025, "NL");

        Assertions.assertNotNull(nlHolidayList);
        Assertions.assertTrue(nlHolidayList.stream().allMatch(x -> "NL".equalsIgnoreCase(x.countryCode())));


    }
}
