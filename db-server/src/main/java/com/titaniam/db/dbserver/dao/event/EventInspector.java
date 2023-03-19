package com.titaniam.db.dbserver.dao.event;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventInspector implements StatementInspector {


    @Override
    public String inspect(String s) {
        log.info("Sql statement " + s);
        return s;
    }
}
