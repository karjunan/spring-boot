package com.titaniam.db.dbserver.controller;

import java.util.List;

import com.titaniam.db.dbserver.query.dto.DBQueryDTO;
import com.titaniam.db.dbserver.query.service.DbQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
@Slf4j
public class DBQueryController {


    @Autowired
    private DbQueryService dbQueryService;

    @GetMapping("/data/{id}")
    public DBQueryDTO getSeverMessage(@PathVariable String id) throws Exception {
        log.info("controller read " + id);
        return dbQueryService.getHost(id);
    }

    @GetMapping("/data/hosts")
    public List<DBQueryDTO> getServerByHostAndDate(@RequestParam(name = "host") String host, @RequestParam(name = "date") String date) throws Exception {
        log.info("controller getMapping " + host + " , " + date);
        return dbQueryService.getServerByHostAndDate(host, date);
    }
}
