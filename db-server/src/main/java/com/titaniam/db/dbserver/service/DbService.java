package com.titaniam.db.dbserver.service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.titaniam.db.dbserver.dao.entity.Server;
import com.titaniam.db.dbserver.dao.entity.ServerTableCount;
import com.titaniam.db.dbserver.dto.DbDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DbService {

    @Value("${tableSize}")
    private String tableSize;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public String createHost(DbDTO dbDTO) {
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
            createNewTable(table_name, tableCount);
            insertOrUpdateCountIntoTable(table_name, tableCount);
        } else {
            int dataCount = getDataCount(table_name + "_" + tableCount);
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


    public DbDTO getHost(String id) throws Exception {
        String tableName = splitAndGetTableName(id);
        String query = "select * from " + tableName + " where id = ?";
        Object[] args = {id};
        int[] argTypes = {Types.VARCHAR};

        try {
            return jdbcTemplate.queryForObject(query, args, argTypes, (rs, num) -> {
                Server p = new Server(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5));
                DbDTO dbDTO = new DbDTO();
                dbDTO.setHost(p.getHost());
                dbDTO.setDate(p.getDate());
                dbDTO.setMessage(p.getMessage());
                dbDTO.setTime(p.getTime());
                return dbDTO;
            });
        } catch (Exception ex) {
            log.info("Exception while getting the object  " + ex);
            throw new Exception("No element found");
        }
    }

    public int deleteHost(String id) {
        String tableName = splitAndGetTableName(id);
        String query = "delete from " + tableName + " where id = ?";
        Object[] args = {id};
        int[] argTypes = {Types.VARCHAR};
        int rows = jdbcTemplate.update(query, args, argTypes);
        return rows;
    }

    public List<DbDTO> getServerByHostAndDate(String host, String date) throws ExecutionException, InterruptedException, TimeoutException {
        Server server = new Server();
        server.setHost(host);
        server.setDate(date);
        String tableName = buildTableName(server);
        int noOfTables = getTableCount(tableName);
        List<CompletableFuture<List<DbDTO>>> list = new ArrayList<>();
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i <= noOfTables; i++) {
            String tableNameWithIndex = tableName + "_" + i;
            CompletableFuture<List<DbDTO>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    String query = "select * from " + tableNameWithIndex + " where host = ? and date = ?";
                    Object[] args = {host, date};
                    int[] argTypes = {Types.VARCHAR, Types.VARCHAR};
                    try {
                        List<DbDTO> ls = jdbcTemplate.query(query, args, argTypes, (rs, num) -> {
                            Server p = new Server(rs.getString(1), rs.getString(2),
                                    rs.getString(3), rs.getString(4), rs.getString(5));
                            DbDTO dbDTO = new DbDTO();
                            dbDTO.setHost(p.getHost());
                            dbDTO.setDate(p.getDate());
                            dbDTO.setMessage(p.getMessage());
                            dbDTO.setTime(p.getTime());
                            return dbDTO;
                        });
                    } catch (Exception ex) {
                        log.info("Exception while getting the object  " + ex);
                        throw new Exception("No element found");
                    }
                } catch (Exception e) {
                    log.info("Exception while getting data " + e);
                }
                return null;
            }, service);
            list.add(future);
        }

//        CompletableFuture<Void> resultantCf = CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));
        CompletableFuture<Void> resultantCf = CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));

//        CompletableFuture<List<DbDTO>> allCompletableFuture = resultantCf.thenApply(future -> {
//            return list.stream()
//                    .map(completableFuture -> completableFuture.join())
//                    .collect(Collectors.toList());
//        });

//                .thenApply(t -> list.stream().map(CompletableFuture::join).collect(Collectors.toList()));

//        CompletableFuture<List<DbDTO>> allFutureResults = sequence(list);

        List<DbDTO> resultList = allFutureResults.get(100000, TimeUnit.MILLISECONDS);

        return resultList;
    }

    static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> com) {
        return CompletableFuture.allOf(com.toArray(new CompletableFuture[com.size()]))
                .thenApply(v -> com.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }


    private String splitAndGetTableName(String id) {
        String[] splitString = id.split("_");
        String server_name = splitString[0];
        String date = splitString[1] + "_" + splitString[2] + "_" + splitString[3];
        String position = splitString[4];
        String tableName = server_name + "_" + date + "_" + position;
        return tableName;
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

    private int getDataCount(String table_name) {
        return jdbcTemplate.queryForObject("select count(*) from " + table_name, Integer.class);
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

    private int getTableCount(String table_name) {
        try {
            String query = "select * from server_table_name where table_name = ?";
            Object[] args = {table_name};
            int[] argTypes = {Types.VARCHAR};
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
