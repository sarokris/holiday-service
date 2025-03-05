package com.holidays.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record Holiday(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                      String localName,
                      String name,
                      String countryCode,
                      boolean fixed,
                      boolean global,
                      List<String> counties,
                      Integer launchYear,
                      List<String> types) {
}
