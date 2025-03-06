package com.holidays.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidays.config.RestClientConfig;
import com.holidays.model.Holiday;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(HolidayService.class)
@Import(RestClientConfig.class)
class HolidayServiceTest {

    @Autowired
    HolidayService holidayService;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.base-url}")
    private String baseUrl;

    @BeforeAll
    static void setUp() {

    }

    @Test
    void retrieveHolidays() throws JsonProcessingException {

        List<Holiday> value = List.of(new Holiday(LocalDate.now(),"new year","new year","NL"
                ,false,false,List.of(),2025,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(value), MediaType.APPLICATION_JSON));

        List<Holiday> holidays = holidayService.retrieveHolidays(2025,"NL");
        assertEquals(1, holidays.size());
        assertTrue(holidays.stream().allMatch(h -> "NL".equalsIgnoreCase(h.countryCode())));
    }

    @Test
    void getHolidays() {
    }

    @Test
    void getWorkingDayHolidays() {
    }

    @Test
    void getCommonHolidays() {
    }
}