package com.synchrony.synchronyswat.api.services;


import com.synchrony.synchronyswat.api.enums.HttpMethod;
import com.synchrony.synchronyswat.api.enums.ValidatorOperation;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.path.xml.NodeChildrenImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ApiImpl implements IApi {
    private RequestSpecification reqSpec;
    private HttpMethod method;
    private String url;
    private Response resp;


    @Override
    @Step("Initialize api")
    public void init(String basURI, String endpoint, int port, HttpMethod method) {
        reqSpec = RestAssured.given()
                .config(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .dontReuseHttpClientInstance()
                        .setParam("http.connection.timeout", 30000)
                        .setParam("http.socket.timeout", 30000)
                        .setParam("http.connection-manager.timeout", 30000)))
                .baseUri(basURI)
                .port(port)
                .log().all();

        this.url = endpoint;
        this.method = method;
    }

    @Override
    public void init(String basURI, String endpoint, HttpMethod method) {
        this.init(basURI, endpoint, -1, method);
    }

    @Override
    public void init(String basURI, String endpoint, HttpMethod method, String bearerToken, Boolean setRelaxedHTTPSValidation) {
        this.init(basURI, endpoint, -1, method);
        this.setOAuthToken(bearerToken);
        if (setRelaxedHTTPSValidation) {
            this.setRelaxedHTTPSValidation();
        }
    }

    @Override
    public void setHeader(String head, String val) {
        reqSpec.header(head, val);
    }

    @Override
    public void setOAuthToken(String tokenValue) {
        reqSpec = reqSpec.auth().oauth2(tokenValue);
    }


    @Override
    public void setQueryParam(String key, String val) {
        reqSpec.queryParam(key, val);
    }

    @Override
    public void setPathParam(String key, String val) {
        reqSpec.pathParam(key, val);
    }

    @Override
    public void setFormParam(String key, String val) {
        reqSpec.formParam(key, val);
    }

    @Override
    public void setBody(String body) {
        reqSpec.body(body);
    }

    @Override
    public void setBody(File bodyAsFile) throws FileNotFoundException {
        if (bodyAsFile.exists()) {
            reqSpec.body(bodyAsFile);
        } else {
            throw new FileNotFoundException("File not found: " + bodyAsFile.getName());
        }
    }

    @Override
    public void setBody(Object bodyAsObject) {
        reqSpec.body(bodyAsObject);
    }

    @Override
    @Step("Call api")
    public void callService() {
        switch (method) {
            case GET:
                resp = reqSpec.get(url);
                break;
            case POST:
                resp = reqSpec.post(url);
                break;
            case PUT:
                resp = reqSpec.put(url);
                break;
            case DELETE:
                resp = reqSpec.delete(url);
                break;
            default:
                throw new IllegalArgumentException("Http method type not valid");
        }
    }

    @Override
    public String extractString(String path) {
        return resp.jsonPath().getString(path);
    }

    @Override
    @Step("Assert status code")
    public void assertIt(int code) {
        resp.then().statusCode(code);
    }

    @Override
    @Step("Assert data")
    public void assertIt(String key, Object val, ValidatorOperation operation) throws AssertionError {
        switch (operation) {
            case EQUALS:
                resp.then().assertThat().body(key, equalTo(val));
                break;
            case KEY_PRESENTS:
                resp.then().assertThat().body(key, hasKey(key));
                break;
            case NOT_EQUALS:
                resp.then().assertThat().body(key, not(equalTo(val)));
                break;
            case EMPTY:
                resp.then().assertThat().body(key, empty());
                break;
            case NOT_EMPTY:
                resp.then().assertThat().body(key, not(emptyArray()));
                break;
            case NOT_NULL:
                resp.then().assertThat().body(key, notNullValue());
                break;
            case HAS_STRING:
                resp.then().assertThat().body(key, containsString((String) val));
                break;
            case SIZE:
                resp.then().assertThat().body(key, hasSize((int) val));
                break;
            case HAS_ITEM:
                resp.then().assertThat().body(key, hasItem(val));
                break;
            case GREATER_THAN:
                resp.then().assertThat().body(key, greaterThan((int) val));
                break;
            case GREATER_THAN_OR_EQUALTO:
                resp.then().assertThat().body(key, greaterThanOrEqualTo((int) val));
                break;
            case LESS_THAN:
                resp.then().assertThat().body(key, lessThan((int) val));
                break;
            case LESS_THAN_OR_EQUALTO:
                resp.then().assertThat().body(key, lessThanOrEqualTo((int) val));
                break;
            case HAS_XPATH:
                resp.then().assertThat().body(hasXPath(key, containsString(val.toString())));
                break;
            default:
                throw new IllegalArgumentException(String.format("%s assert operation not available", operation));
        }
    }

    @Override
    public void assertItJsonSchema(File file) throws IOException {
        resp.then().log().all().body(matchesJsonSchema(file));
    }

    @Override
    public void setRelaxedHTTPSValidation() {
        reqSpec.relaxedHTTPSValidation();
    }

    @Override
    public String getResponseBody() {
        return resp.then().log().all().extract().body().asString();
    }

    public NodeChildrenImpl getXmlNodeFromResponse(String xpath){
        return resp.then().extract().path(xpath);
    }
}
