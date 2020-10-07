package com.payworks.request;

import java.util.Map;

public interface RestRequest {
    String getUrl();
    //String getMethod(); // TODO  TO BE IMPLEMENTED
    String getContentType();
    Map<String, Object> getHeaders();
    String getRequestBody();
    Map<String, String> getParameters();
}
