package com.example.httpclient.springboothttpclient.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class ExternalClient implements Runnable{

    @Autowired
    private RestTemplate restTemplate;
//    @Async


    @Override
    public void run() {
         restTemplate.getForEntity("http://20.198.0.44/" ,String.class);
    }

//    public CompletableFuture<ResponseEntity<String>> getUser() {
//
//        ResponseEntity<String> entity  = restTemplate.getForEntity("http://20.198.0.44/" ,String.class);
//
//
//        return CompletableFuture.completedFuture(entity);
//    }
}
