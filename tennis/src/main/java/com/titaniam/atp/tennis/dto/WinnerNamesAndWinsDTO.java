package com.titaniam.atp.tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinnerNamesAndWinsDTO {

    private String name;
    private int count;
}
