package com.example.demo.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.example.demo.filter.AuditThreadLocal;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String getUser() throws ExecutionException, InterruptedException {
        System.out.println("Get thread local object at controller" + AuditThreadLocal.get());
        return userService.getUserService();
    }
}
