package com.titaniam.db.dbserver.service;

import com.titaniam.db.dbserver.dao.entity.Server;
import com.titaniam.db.dbserver.dao.repository.ServerRepository;
import com.titaniam.db.dbserver.dto.DbDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DbService {

//    @Autowired
//    private ServerRepository serverRepository;
//
//    @Autowired
//    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void createHost(DbDTO dbDTO) {
        log.info("service input " + dbDTO);
        Server server = new Server();
        server.setHost(dbDTO.getHost());
        server.setDate(dbDTO.getDate());
        server.setMessage(dbDTO.getMessage());
        server.setTime(dbDTO.getTime());
        jdbcTemplate.execute("drop table server");
        jdbcTemplate.execute("drop table server_table_count");
//        log.info("user repository" + serverRepository.save(server))
//        entityManager.persist(server);
//        try {
//            long count = serverRepository.count();
//            log.info("size is => " + count);
//        } catch (Exception ex) {
//            Session session =
//            session.createQuery("CREATE TABLE movies(title VARCHAR(50) NOT NULL,genre VARCHAR(30) NOT NULL,director VARCHAR(60) NOT NULL,release_year INT NOT NULL,PRIMARY KEY(title));", Server.class);
//            session.close();
//        }
//        log.info("size is => " + serverRepository.count());
    }

}
