package com.holidays.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidays.config.RestClientConfig;
import com.holidays.model.Holiday;
import com.holidays.model.SimpleHoliday;
import com.holidays.model.SortOrder;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testRetrieveHolidays() throws JsonProcessingException {

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
    void getHolidays() throws JsonProcessingException{
        int limit = 4;
        List<Holiday> holidays25 = List.of(new Holiday(LocalDate.of(2025,1,1),"Nieuwjaarsdag","new year","NL"
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 4,18),"Goede Vrijdag","new year","NL"
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 4,20),"Eerste Paasdag","new year","NL"
                ,false,false,List.of(),2025,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(holidays25), MediaType.APPLICATION_JSON));

        List<Holiday> holidays24 = List.of(new Holiday(LocalDate.of(2024,1,1),"Nieuwjaarsdag","new year","NL"
                ,false,false,List.of(),2024,List.of()),new Holiday(LocalDate.of(2024,3,29),"Goede Vrijdag","new year","NL"
                ,false,false,List.of(),2024,List.of()),new Holiday(LocalDate.of(2024,3,31),"Eerste Paasdag","new year","NL"
                ,false,false,List.of(),2024,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2024/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(holidays24), MediaType.APPLICATION_JSON));

        List<SimpleHoliday> lastCelebratedHoliday = holidayService.getHolidays("NL", limit,SortOrder.ASC);
        assertNotNull(lastCelebratedHoliday);
        assertEquals(limit,lastCelebratedHoliday.size());
        assertTrue(lastCelebratedHoliday.stream().map(SimpleHoliday::date).anyMatch(lDate -> lDate.getYear() == 2024));
    }

    @Test
    void getWorkingDayHolidays() {
    }

    @Test
    void getCommonHolidays() {
    }
}