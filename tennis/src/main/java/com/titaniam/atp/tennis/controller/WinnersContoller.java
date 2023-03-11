package com.titaniam.atp.tennis.controller;

import com.titaniam.atp.tennis.dto.WinnerNameAndTotalWinsDTO;
import com.titaniam.atp.tennis.dto.WinnerNameAndTotalWinsForDurationDTO;
import com.titaniam.atp.tennis.service.WinnersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("winners")
public class WinnersContoller {

    @Autowired
    private WinnersService winnersService;

    @GetMapping("/{year}/{surface}")
    public WinnerNameAndTotalWinsDTO getUniquePlayersByCountry(@PathVariable String year, @PathVariable String surface) {

        return winnersService.getWinnerNamesAndFinalWins(year, surface);
    }

    @GetMapping("/{fromYear}/{toYear}/{surface}")
    public WinnerNameAndTotalWinsForDurationDTO getUniquePlayersByCountryForSpecificDuration(@PathVariable String fromYear, @PathVariable String toYear, @PathVariable String surface) {

        return winnersService.getWinnerNamesAndFinalWinsForSpecificYears(fromYear, toYear, surface);
    }
}
