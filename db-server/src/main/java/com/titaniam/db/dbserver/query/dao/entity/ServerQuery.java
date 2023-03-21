package com.titaniam.db.dbserver.query.dao.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerQuery implements Serializable {
    private String id;
    private String host;
    private String message;
    private String date;
    private String time;
}
