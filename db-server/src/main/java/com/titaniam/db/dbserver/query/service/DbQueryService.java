package com.titaniam.db.dbserver.query.service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.titaniam.db.dbserver.command.dao.entity.ServerTableCount;
import com.titaniam.db.dbserver.query.dao.entity.ServerQuery;
import com.titaniam.db.dbserver.command.dto.DbCommandDTO;
import com.titaniam.db.dbserver.query.dto.DBQueryDTO;
import com.titaniam.db.dbserver.util.DbUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DbQueryService {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    public DBQueryDTO getHost(String id) throws Exception {
        String tableName = DbUtil.splitAndGetTableName(id);
        String query = "select * from " + tableName + " where id = ?";
        Object[] args = {id};
        int[] argTypes = {Types.VARCHAR};

        try {
            return jdbcTemplate.queryForObject(query, args, argTypes, (rs, num) -> {
                ServerQuery p = new ServerQuery(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5));
                DBQueryDTO dbDTO = new DBQueryDTO();
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



    public List<DBQueryDTO> getServerByHostAndDate(String host, String date) throws Exception {
        ServerQuery server = new ServerQuery();
        server.setHost(host);
        server.setDate(date);
        String tableName = buildTableName(server);
        int noOfTables = getTableCount(tableName);
        List<CompletableFuture<List<DBQueryDTO>>> list = new ArrayList<>();
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i <= noOfTables; i++) {
            String tableNameWithIndex = tableName + "_" + i;
            CompletableFuture<List<DBQueryDTO>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    String query = "select * from " + tableNameWithIndex + " where host = ? and date = ?";
                    Object[] args = {host, date};
                    int[] argTypes = {Types.VARCHAR, Types.VARCHAR};
                    try {
                        return jdbcTemplate.query(query, args, argTypes, (rs, num) -> {
                            ServerQuery p = new ServerQuery(rs.getString(1), rs.getString(2),
                                    rs.getString(3), rs.getString(4), rs.getString(5));
                            DBQueryDTO dbDTO = new DBQueryDTO();
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
        CompletableFuture<List<List<DBQueryDTO>>> result = sequence(list);
        List<List<DBQueryDTO>> resultList = result.get(1000, TimeUnit.MILLISECONDS);
        return resultList.stream().flatMap(v -> v.stream()).collect(Collectors.toList());
    }

    public int getTableCount(String table_name) throws Exception {
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

    public int getDataCount(String table_name) throws Exception {
        return jdbcTemplate.queryForObject("select count(*) from " + table_name, Integer.class);
    }

    static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> com) {
        return CompletableFuture.allOf(com.toArray(new CompletableFuture[com.size()]))
                .thenApply(v -> com.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }





    private String buildTableName(ServerQuery server) {
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
