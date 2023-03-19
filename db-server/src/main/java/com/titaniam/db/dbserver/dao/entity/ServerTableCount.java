package com.titaniam.db.dbserver.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerTableCount {

    private String id;
    private String table_name;
    private int count;
}
