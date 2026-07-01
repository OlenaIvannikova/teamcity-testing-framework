package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.BuildType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Для связки 2х констант: URL эндпоинта и model, в которую мы сериализуем тело запроса и тело ответа
 */
@AllArgsConstructor
@Getter
public enum Endpoint {
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;
}
