package com.titaniam.db.dbserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DbServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(DbServerApplication.class, args);
    }


}
