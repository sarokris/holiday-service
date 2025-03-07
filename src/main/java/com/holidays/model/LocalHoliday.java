package com.holidays.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Map;

public record LocalHoliday(LocalDate date, @JsonProperty("local-names") Map<String,String> countryLocalNameMap) {
}
