package com.holidays.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SortOrder {
    ASC,DESC;

    @JsonCreator
    public static SortOrder fromString(String value) {
        return value != null ? SortOrder.valueOf(value.toUpperCase()) : DESC;
    }
}
