package com.example.teamcity.api.requests;

import java.util.ArrayList;
import java.util.List;

public class PathParams {
    private final List<Object> params = new ArrayList<>();

    public static PathParams create() {
        return new PathParams();
    }

    public PathParams buildTypeId(String id) {
        params.add(id);
        return this;
    }

    public PathParams projectId(String id) {
        params.add(id);
        return this;
    }

    public Object[] build() {
        return params.toArray();
    }
}
