package com.titaniam.db.dbserver.service;

import com.titaniam.db.dbserver.dao.UserRepository;
import com.titaniam.db.dbserver.dto.DbDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DbService {


    @Autowired
    UserRepository userRespository;

    public void createHost(DbDTO dbDTO) {
        log.info("service input " + dbDTO);
        log.info("user repository" + userRespository.findAll());
    }

}
