package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.PathParams;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.Map;

@SuppressWarnings("unchecked")
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface {

    private final UncheckedBase uncheckedBase;

    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    public T create(BaseModel model) {
        var createdModel = (T) uncheckedBase
                .create(model)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());

        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    public T create(BaseModel model, PathParams pathParams) {
        var createdModel = (T) uncheckedBase
                .create(model, pathParams)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());

        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public T read(String locator) {
        return (T) uncheckedBase
                .read(locator)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public T read() {
        return (T) uncheckedBase
                .read()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    public String read(Map<String, Object> queryParams) {
        return uncheckedBase
                .read(queryParams)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();
    }

    @Override
    public T update(String locator, BaseModel model) {
        return (T) uncheckedBase
                .update(locator, model)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public Object delete(String locator) {
        return uncheckedBase
                .delete(locator)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}
