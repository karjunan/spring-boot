package com.titaniam.atp.tennis.controller;

import java.util.List;

import com.titaniam.atp.tennis.dto.CountryPlayersDTO;
import com.titaniam.atp.tennis.service.TennisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("country")
public class TennisController {

    @Autowired
    private TennisService tennisService;

    @GetMapping("/{year}")
    public List<CountryPlayersDTO> getUniquePlayersByCountry(@PathVariable String year) {

        return tennisService.getUniquePlayersByCountry(year);
    }
}
