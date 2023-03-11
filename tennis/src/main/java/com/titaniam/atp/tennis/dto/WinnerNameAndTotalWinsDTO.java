package com.titaniam.atp.tennis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinnerNameAndTotalWinsDTO {

    private String year;
    private String surface;
    private List<WinnerNamesAndWinsDTO> winners;

}
