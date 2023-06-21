package com.example.db.dbdemo.controller;

import com.example.db.dbdemo.servlce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController()
@RequestMapping
public class UserController {

    @Autowired
    UserService service;

    @GetMapping
    public String getUser() throws ExecutionException, InterruptedException {
        return service.getUser();
    }
}
