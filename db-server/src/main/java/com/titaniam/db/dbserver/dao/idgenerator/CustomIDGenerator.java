package com.titaniam.db.dbserver.dao.idgenerator;

import java.util.UUID;

import com.titaniam.db.dbserver.dao.entity.Server;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomIDGenerator implements IdentifierGenerator {


    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try {
            Server server = (Server) o;
            StringBuffer buffer = new StringBuffer();
            buffer.append(server.getHost());
            buffer.append("-");
            buffer.append(server.getDate());
            buffer.append("-");
            buffer.append(UUID.randomUUID());
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
