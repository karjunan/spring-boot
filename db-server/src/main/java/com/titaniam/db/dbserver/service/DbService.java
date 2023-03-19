package com.titaniam.db.dbserver.service;

import java.sql.Types;
import java.util.UUID;

import com.titaniam.db.dbserver.dao.entity.Server;
import com.titaniam.db.dbserver.dao.entity.ServerTableCount;
import com.titaniam.db.dbserver.dto.DbDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DbService {

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
        String table_name = buildTableName(server);

        int tableCount = getTableCount(table_name);
        if (tableCount == -1) {
            tableCount += 1;
            createNewTable(table_name, Integer.toString(tableCount));
            insertOrUpdateCountIntoTable(table_name, tableCount);
        } else {
            int dataCount = getDataCount(table_name + "_" + tableCount);
            if(dataCount > 3) {
                try {
                    createNewTable(table_name, Integer.toString(tableCount + 1));
                } catch (Exception ex) {
                    log.info("Table already exists");
                }
                log.info("Data count " + dataCount);
                insertOrUpdateCountIntoTable(table_name,  tableCount+1);
                tableCount += 1;
            }
        }
        insertRecordIntoHostTable(table_name+"_"+tableCount, server);
    }

    private void insertRecordIntoHostTable(String table_name, Server server) {
        String query = "insert into " + table_name + " values (?, ? , ? , ?, ? )";
        String uuid = UUID.randomUUID().toString();
        String id = table_name+"_"+uuid;
        jdbcTemplate.update(query, id, server.getHost(), server.getMessage(), server.getDate(),server.getTime());
    }

    private void insertOrUpdateCountIntoTable(String table_name, int count) {
        if (count == 0) {
            String insertQuery = "insert into server_table_name values(?,?,?)";
            jdbcTemplate.update(insertQuery, UUID.randomUUID().toString(), table_name, count);
        } else {
            String updateQuery = "update  server_table_name set count = ? where table_name = ?";
            jdbcTemplate.update(updateQuery, count, table_name);

        }

    }

    private int getDataCount(String table_name) {
        return jdbcTemplate.queryForObject("select count(*) from "+ table_name, Integer.class);
    }

    private void createNewTable(String table_name, String count) {
        String name = table_name + "_" + count;
        System.out.println(name);
        String queryCreateTable = "CREATE TABLE " + name +
                "(id VARCHAR(100) NOT NULL ," +
                "host VARCHAR(100)," +
                "message VARCHAR(100)," +
                "date VARCHAR(20)," +
                "time VARCHAR(50)," +
                "PRIMARY KEY ( id ))";
        jdbcTemplate.execute(queryCreateTable);

    }

    private int getTableCount(String table_name) {
        try {
            String query = "select * from server_table_name where table_name = ?";
             Object[] args = { table_name };
            int[] argTypes = { Types.VARCHAR };
            return jdbcTemplate.queryForObject(query, args, argTypes, (rs, num) -> {
                    ServerTableCount p = new ServerTableCount(rs.getString(1), rs.getString(2),
                            rs.getInt(3));
                    return p.getCount();
            });
        } catch (Exception ex) {
            return -1;
        }
    }

    private String buildTableName(Server server) {
        StringBuffer buffer = new StringBuffer();
        String[] date = server.getDate().split("-");
        buffer.append(server.getHost())
                .append("_")
                .append(date[0])
                .append("_")
                .append(date[1])
                .append("_")
                .append(date[2]);

        return buffer.toString();
    }

}
