package com.holidays.service;

import com.holidays.service.model.Holiday;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HolidayRetrieverTest {

    @Autowired
    private HolidayRetriever holidayService;

    @Test
    public void testRetrieveHolidays(){
        List<Holiday> nlHolidayList = holidayService.retrieveHolidays(2025, "NL");
        Assertions.assertNotNull(nlHolidayList);
    }
}
