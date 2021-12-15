package com.synchrony.synchronyswat.api.services;
import com.synchrony.synchronyswat.api.enums.HttpMethod;
import com.synchrony.synchronyswat.api.enums.ValidatorOperation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IApi {
    void init(String basURI, String endpoint, int port, HttpMethod method);

    void init(String basURI, String endpoint, HttpMethod method);

    void init(String basURI, String endpoint, HttpMethod method, String bearerToken, Boolean setRelaxedHTTPSValidation);

    void setRelaxedHTTPSValidation();

    void setHeader(String head, String val);

    void setQueryParam(String key, String val);

    void setPathParam(String key, String val);

    void setFormParam(String key, String val);

    void setBody(String body);

    void setBody(File bodyAsFile) throws FileNotFoundException;

    void setBody(Object bodyAsObject);

    void callService();

    String extractString(String path);

    void assertIt(int code);

    void assertIt(String key, Object val, ValidatorOperation operation) ;

    void assertItJsonSchema(File file) throws IOException;

    String getResponseBody();

    void setOAuthToken(String tokenValue);
}
