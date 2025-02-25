package com.holidays.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class HolidayRetrieverTest {

    @MockBean
    private HolidayRetriever holidayService;

    @Test
    public void testRetrieveHolidays(){
        Assertions.assertNotNull(holidayService.retrieveHolidays(2025,"NL"));
    }
}
