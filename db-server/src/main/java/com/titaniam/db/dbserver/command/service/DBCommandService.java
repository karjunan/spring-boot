package com.titaniam.db.dbserver.command.service;

import java.sql.Types;
import java.util.UUID;

import com.titaniam.db.dbserver.command.dao.entity.Server;
import com.titaniam.db.dbserver.command.dto.DbCommandDTO;
import com.titaniam.db.dbserver.query.service.DbQueryService;
import com.titaniam.db.dbserver.util.DbUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DBCommandService {

    @Value("${tableSize}")
    private String tableSize;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbQueryService dbQueryService;

    @Transactional
    public String createHost(DbCommandDTO dbDTO) {
        log.info("service input " + dbDTO);
        Server server = new Server();
        server.setHost(dbDTO.getHost());
        server.setDate(dbDTO.getDate());
        server.setMessage(dbDTO.getMessage());
        server.setTime(dbDTO.getTime());
        String table_name = buildTableName(server);


        int tableCount = dbQueryService.getTableCount(table_name);
        if (tableCount == -1) {
            tableCount += 1;
            createNewTable(table_name, tableCount);
            insertOrUpdateCountIntoTable(table_name, tableCount);
        } else {
            int dataCount = dbQueryService.getDataCount(table_name + "_" + tableCount);
            try {
                if (dataCount > Integer.parseInt(tableSize)) {
                    createNewTable(table_name, tableCount + 1);
                    insertOrUpdateCountIntoTable(table_name, tableCount + 1);
                    tableCount += 1;
                }
                log.info("Data count " + dataCount);
            } catch (NumberFormatException ex) {
                log.info("Issue while parsing the number");
                return null;
            } catch (Exception ex) {
                log.info("Table already exists");
            }
        }
        return insertRecordIntoHostTable(table_name + "_" + tableCount, server);
    }

    public int deleteHost(String id) {
        String tableName = DbUtil.splitAndGetTableName(id);
        String query = "delete from " + tableName + " where id = ?";
        Object[] args = {id};
        int[] argTypes = {Types.VARCHAR};
        int rows = jdbcTemplate.update(query, args, argTypes);
        return rows;
    }




    private void createNewTable(String table_name, int count) {
        String name = table_name + "_" + count;
        String queryCreateTable = "CREATE TABLE " + name +
                "(id VARCHAR(100) NOT NULL ," +
                "host VARCHAR(100)," +
                "message VARCHAR(100)," +
                "date VARCHAR(20)," +
                "time VARCHAR(50)," +
                "PRIMARY KEY ( id ))";
        jdbcTemplate.execute(queryCreateTable);

    }

    private String insertRecordIntoHostTable(String table_name, Server server) {
        String query = "insert into " + table_name + " values (?, ? , ? , ?, ? )";
        String uuid = UUID.randomUUID().toString();
        String id = table_name + "_" + uuid;
        jdbcTemplate.update(query, id, server.getHost(), server.getMessage(), server.getDate(), server.getTime());
        return id;
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
