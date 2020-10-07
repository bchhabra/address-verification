package com.payworks.handler;

import com.payworks.response.RestResponse;
import com.payworks.request.RestRequest;

public interface RestHandler {
    RestResponse sendRequest(RestRequest request);
}
