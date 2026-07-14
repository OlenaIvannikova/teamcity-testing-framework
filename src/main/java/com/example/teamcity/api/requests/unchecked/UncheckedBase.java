package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.PathParams;
import com.example.teamcity.api.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class UncheckedBase extends Request implements CrudInterface {

    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response create(BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl());
    }

    public Response create(BaseModel model, PathParams pathParams) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl(pathParams.build()));
    }

    public Response create(byte[] body) {
        return RestAssured
                .given()
                .spec(spec)
                .body(body)
                .post(endpoint.getUrl());
    }

    public Response create(Object body) {
        return RestAssured
                .given()
                .spec(spec)
                .body(body)
                .post(endpoint.getUrl());
    }

    @Override
    public Response read(String id) {
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    public Response readByLocator(String locator) {
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + "/" + locator);
    }

    @Override
    public Response read(Map<String, Object> queryParams) {
        return RestAssured
                .given()
                .spec(spec)
                .queryParams(queryParams)
                .get(endpoint.getUrl());
    }

    @Override
    public Response update(String id, BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .put(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/id:" + id);
    }
}
