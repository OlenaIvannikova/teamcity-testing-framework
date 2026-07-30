package com.example.teamcity.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildStatus {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    ERROR("ERROR"),
    UNKNOWN("UNKNOWN");

    private final String value;
}
