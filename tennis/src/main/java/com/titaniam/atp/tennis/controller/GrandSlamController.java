package com.titaniam.atp.tennis.controller;

import com.titaniam.atp.tennis.dto.GrandSlamDTO;
import com.titaniam.atp.tennis.dto.WinnerNameAndTotalWinsForDurationDTO;
import com.titaniam.atp.tennis.service.CountryService;
import com.titaniam.atp.tennis.service.GrandSlamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("grandSlam")
public class GrandSlamController {

    @Autowired
    private GrandSlamService gransSlamService;

    @GetMapping("/{fromYear}/{toYear}")
    public GrandSlamDTO getUniquePlayersByCountryForSpecificDuration(@PathVariable String fromYear, @PathVariable String toYear) {

        return gransSlamService.getWinnerNamesAndFinalWinsForSpecificYears(fromYear,toYear);
    }
}
