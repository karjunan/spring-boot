package com.titaniam.atp.tennis.controller;

import java.util.List;

import com.titaniam.atp.tennis.dto.CountryPlayersDTO;
import com.titaniam.atp.tennis.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/{year}")
    public List<CountryPlayersDTO> getUniquePlayersByCountry(@PathVariable String year) {

        return countryService.getUniquePlayersByCountry(year);
    }
}
