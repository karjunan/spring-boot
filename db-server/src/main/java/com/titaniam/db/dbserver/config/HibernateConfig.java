package com.titaniam.db.dbserver.config;

import com.titaniam.db.dbserver.dao.event.EventInspector;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    public Session getSession() {
//        return entityManagerFactory.unwrap(SessionFactory.class).withOptions()
//                .statementInspector(new EventInspector())
//                .openSession();
//    }
}
