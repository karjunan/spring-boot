package com.example.httpclient.springboothttpclient.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // log the http request
        System.out.println("URI: {}" + request.getURI());
        System.out.println("HTTP Method: {}" +  request.getMethod());
        System.out.println("HTTP Headers: {}" + request.getHeaders());
        return execution.execute(request, body);
    }
}
