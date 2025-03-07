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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(HolidayService.class)
@Import(RestClientConfig.class)
class HolidayServiceTest {

    public static final String US = "US";
    public static final String NL = "NL";
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

        List<Holiday> value = List.of(new Holiday(LocalDate.now(),"new year","new year", NL
                ,false,false,List.of(),2025,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(value), MediaType.APPLICATION_JSON));

        List<Holiday> holidays = holidayService.retrieveHolidays(2025, NL);
        assertEquals(1, holidays.size());
        assertTrue(holidays.stream().allMatch(h -> NL.equalsIgnoreCase(h.countryCode())));
    }

    @Test
    void testGetHolidays() throws JsonProcessingException{
        int limit = 4;
        List<Holiday> holidays25 = List.of(new Holiday(LocalDate.of(2025,1,1),"Nieuwjaarsdag","new year", NL
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 4,18),"Goede Vrijdag","new year", NL
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 4,20),"Eerste Paasdag","new year", NL
                ,false,false,List.of(),2025,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(holidays25), MediaType.APPLICATION_JSON));

        List<Holiday> holidays24 = List.of(new Holiday(LocalDate.of(2024,1,1),"Nieuwjaarsdag","new year", NL
                ,false,false,List.of(),2024,List.of()),new Holiday(LocalDate.of(2024,3,29),"Goede Vrijdag","new year", NL
                ,false,false,List.of(),2024,List.of()),new Holiday(LocalDate.of(2024,3,31),"Eerste Paasdag","new year", NL
                ,false,false,List.of(),2024,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2024/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(holidays24), MediaType.APPLICATION_JSON));

        List<SimpleHoliday> lastCelebratedHoliday = holidayService.getHolidays(NL, limit,SortOrder.ASC);
        assertNotNull(lastCelebratedHoliday);
        assertEquals(limit,lastCelebratedHoliday.size());
        assertTrue(lastCelebratedHoliday.stream().map(SimpleHoliday::date).anyMatch(lDate -> lDate.getYear() == 2024));
        assertTrue(lastCelebratedHoliday.stream().map(SimpleHoliday::date).anyMatch(lDate -> lDate.getYear() == 2025));
    }

    @Test
    void testGetWorkingDayHolidays() throws JsonProcessingException {
        List<Holiday> nlHolidays = List.of(new Holiday(LocalDate.of(2025,1,1),"Nieuwjaarsdag","new year", NL
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025,4,20),"Eerste Paasdag","Easter Sunday", NL
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025,6,8),"Eerste Pinksterdag","Pentecost", NL
                ,false,false,List.of(),2025,List.of()));

        List<Holiday> usHolidays = List.of(new Holiday(LocalDate.of(2025,1,1),"New Year","new year", US
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 1,20),"Martin Luther King, Jr. Day","Martin Luther King, Jr. Day", US
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 2,17),"Washington's Birthday","Washington's Birthday", US
                ,false,false,List.of(),2025,List.of()));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(nlHolidays), MediaType.APPLICATION_JSON));
        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/US"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(usHolidays), MediaType.APPLICATION_JSON));

        Map<String, Long> weekHolidays = holidayService.getWorkingDayHolidays(2025,List.of(NL, US));
        assertNotNull(weekHolidays);
        assertEquals(1,weekHolidays.get(NL));
        assertEquals(3,weekHolidays.get(US));

    }

    @Test
    void getCommonHolidays() throws JsonProcessingException {
        List<Holiday> nlHolidays = List.of(new Holiday(LocalDate.of(2025,1,1),"Nieuwjaarsdag","new year", NL
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025,4,18),"Goede Vrijdag","Good Friday", NL
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025,12,25),"Eerste Kerstdag","Christmas Day", NL
                ,false,false,List.of(),2025,List.of()));

        List<Holiday> usHolidays = List.of(new Holiday(LocalDate.of(2025,1,1),"New Year","new year", US
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 4,18),"Good Friday","Good Friday", US
                ,false,false,List.of(),2025,List.of()),new Holiday(LocalDate.of(2025, 12,25),"Christmas Day","Christmas Day", US
                ,false,false,List.of(),2025,List.of()));

        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/US"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(usHolidays), MediaType.APPLICATION_JSON));

        this.mockServer
                .expect(requestTo(baseUrl+"/PublicHolidays/2025/NL"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(nlHolidays), MediaType.APPLICATION_JSON));


        holidayService.getCommonHolidays(2025, US, NL);


    }
}