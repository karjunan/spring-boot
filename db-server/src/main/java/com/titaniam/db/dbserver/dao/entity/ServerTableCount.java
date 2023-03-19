package com.titaniam.db.dbserver.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerTableCount {

    @Id
    private int id;
    private String table_name;
    private int count;
}
