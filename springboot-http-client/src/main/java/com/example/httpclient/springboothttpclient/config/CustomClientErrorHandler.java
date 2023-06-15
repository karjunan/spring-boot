package com.example.httpclient.springboothttpclient.config;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomClientErrorHandler implements ResponseErrorHandler {

    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().is4xxClientError();
    }
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        System.out.println("HTTP Status Code: " + clientHttpResponse.getStatusCode().value());
    }
}
