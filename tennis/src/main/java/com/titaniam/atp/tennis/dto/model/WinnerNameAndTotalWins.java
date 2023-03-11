package com.titaniam.atp.tennis.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinnerNameAndTotalWins {

    private String id;
    private int count;

}
