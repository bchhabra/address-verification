package com.payworks.request;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AbstractRestRequest implements RestRequest{
    private final String url;
    private final Map<String, Object> headers = new HashMap<>();
    private final Map<String, String> parameters = new LinkedHashMap<>();

    //TODO, provision constructing object using url and body.
    public AbstractRestRequest(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public Map<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public String getRequestBody() {
        return "";
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    public AbstractRestRequest addHeader(String name, Object value) {
        headers.put(name, value);
        return this;
    }

    public AbstractRestRequest addParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }
}
