package com.example.demo.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.example.demo.filter.AuditThreadLocal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    public String getUserService() throws ExecutionException, InterruptedException {
        System.out.println("Get thread local object at Service" + AuditThreadLocal.get());

        System.out.println("Completable future thread : " + Thread.currentThread().getName());

        CompletableFuture<HttpResponse<String>> response = get("https://www.google.com");
        CompletableFuture<HttpResponse<String>> response1 = get("https://www.facebook.com");

        CompletableFuture.allOf(response,response1).join();


        return response.get().statusCode() + " | " + response1.get().statusCode();
    }

    @Async
    private CompletableFuture<HttpResponse<String>> get(String url) throws ExecutionException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        HttpResponse<String> send = null;
        try {
            send = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("IOException" + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception" + e.getMessage());
        }
        System.out.println("Completable future thread get : " + AuditThreadLocal.get());
        return CompletableFuture.completedFuture(send);
    }

}
