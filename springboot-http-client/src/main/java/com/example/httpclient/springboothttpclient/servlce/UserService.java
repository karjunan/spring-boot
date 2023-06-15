package com.example.httpclient.springboothttpclient.servlce;

import com.example.httpclient.springboothttpclient.client.ExternalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class UserService {

//    @Autowired
//    private ExternalClient externalClient;

        @Autowired
        private  RestTemplate restTemplate;

//    public String getUser() throws ExecutionException, InterruptedException {
//        Instant start = Instant.now();
//        List<CompletableFuture<ResponseEntity<String>>> allFutures = new ArrayList<>();
//
//        for (int i = 0; i < 1000; i++) {
//            allFutures.add(externalClient.getUser());
//        }
//
//        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
//
//        for (int i = 0; i < 1000; i++) {
//            System.out.println("response: " + allFutures.get(i).get().getStatusCode());
//        }
//        long time = Duration.between(start, Instant.now()).getSeconds();
//        System.out.println("Total time: " + Duration.between(start, Instant.now()).getSeconds());
//
//        return String.valueOf(time);
//    }


    public String getUser() throws ExecutionException, InterruptedException {
        Instant start = Instant.now();
        ExecutorService executor = Executors.newFixedThreadPool(200);
        Thread th = null;
        for(int i = 0; i < 2000; i++) {
            Runnable runnable = () -> {
                ResponseEntity<String> entity  =  restTemplate.getForEntity("http://20.198.0.44/" ,String.class);
                System.out.println("Completed with status ==>> " + entity.getStatusCode());
            };
            executor.execute(runnable);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // empty body
        }
        long time = Duration.between(start, Instant.now()).getSeconds();
        System.out.println("Total time: " + Duration.between(start, Instant.now()).getSeconds());
        return String.valueOf(time);
    }
}
