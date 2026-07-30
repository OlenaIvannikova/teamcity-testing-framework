package com.example.teamcity.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildState {
    FINISHED("finished"),
    QUEUED("queued"),
    RUNNING("running");

    private final String value;
}
