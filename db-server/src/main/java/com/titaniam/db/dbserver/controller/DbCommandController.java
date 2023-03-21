package com.titaniam.db.dbserver.controller;

import com.titaniam.db.dbserver.command.dto.DbCommandDTO;
import com.titaniam.db.dbserver.command.service.DBCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cmd/")
@Slf4j
public class DbCommandController {


    @Autowired
    private DBCommandService dbCommandService;

    @PostMapping("/data")
    public String createServerMessage(@RequestBody DbCommandDTO dbDTO) throws Exception {
        log.info("controller input " + dbDTO);
        return dbCommandService.createHost(dbDTO);
    }

    @DeleteMapping("/data/{id}")
    public int deleteServerMessage(@PathVariable String id) {
        log.info("controller delete " + id);
        return dbCommandService.deleteHost(id);
    }

}
