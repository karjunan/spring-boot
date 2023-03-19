package com.titaniam.db.dbserver.dao.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@Entity
public class Server implements Serializable {

//    @Id
//    @GenericGenerator(name = "custom_id", strategy = "com.titaniam.db.dbserver.dao.idgenerator.CustomIDGenerator")
//    @GeneratedValue(generator = "custom_id")
    private String id;
    private String host;
    private String message;
    private String date;
    private String time;
}
