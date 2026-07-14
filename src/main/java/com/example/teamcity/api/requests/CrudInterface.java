package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;

import java.util.Map;

public interface CrudInterface {
    Object create(BaseModel model);

    Object read(String id);

    Object update(String id, BaseModel model);

    Object delete(String id);

    // GET /resource/name:Project1
    Object readByLocator(String locator);

    // GET /resource?locator=name:Project1
    Object read(Map<String, Object> queryParams);
}
