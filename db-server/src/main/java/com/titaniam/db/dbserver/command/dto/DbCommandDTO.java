package com.titaniam.db.dbserver.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class DbCommandDTO {

    private String host;
    private String message;
    private String date;
    private String time;
}
