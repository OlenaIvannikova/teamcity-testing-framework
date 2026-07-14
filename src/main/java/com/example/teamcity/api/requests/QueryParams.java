package com.example.teamcity.api.requests;

import java.util.HashMap;
import java.util.Map;

public class QueryParams {
    private final Map<String, Object> params = new HashMap<>();

    private QueryParams() {
    }

    public static QueryParams create() {
        return new QueryParams();
    }

    public QueryParams locator(String locator) {
        params.put("locator", locator);
        return this;
    }

    public QueryParams fields(String fields) {
        params.put("fields", fields);
        return this;
    }

    public QueryParams buildId(String buildId) {
        params.put("buildId", buildId);
        return this;
    }

    public QueryParams add(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public Map<String, Object> build() {
        return params;
    }
}
