package com.titaniam.db.dbserver.util;

import org.springframework.stereotype.Component;

@Component
public class DbUtil {

    public static String splitAndGetTableName(String id) {
        String[] splitString = id.split("_");
        String server_name = splitString[0];
        String date = splitString[1] + "_" + splitString[2] + "_" + splitString[3];
        String position = splitString[4];
        String tableName = server_name + "_" + date + "_" + position;
        return tableName;
    }
}
