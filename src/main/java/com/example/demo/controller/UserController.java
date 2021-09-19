package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<User> getUser() {
        User user = new User();
        user.setName("Krishna");
        user.setAge("37");

        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @PostMapping
    public String createUser() {

        return "post user";
    }

    @PutMapping
    public String updateUser() {

        return "update";
    }

    @DeleteMapping
    public String deleteUser() {

        return "delete";
    }


}
