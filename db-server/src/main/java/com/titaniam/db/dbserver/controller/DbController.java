package com.titaniam.db.dbserver.controller;

import java.util.concurrent.ExecutionException;

import com.titaniam.db.dbserver.dto.DbDTO;
import com.titaniam.db.dbserver.service.DbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("db")
@Slf4j
public class DbController {

    @Autowired
    private DbService dbService;

    @PostMapping("/data")
    public String createServerMessage(@RequestBody DbDTO dbDTO) throws Exception {
        log.info("controller input " + dbDTO);
        return dbService.createHost(dbDTO);
    }

    @GetMapping("/data/{id}")
    public DbDTO getSeverMessage(@PathVariable String id) throws Exception {
        log.info("controller read " + id);
        return dbService.getHost(id);
    }

    @DeleteMapping("/data/{id}")
    public int deleteServerMessage(@PathVariable String id) {
        log.info("controller delete " + id);
        return dbService.deleteHost(id);
    }
}
