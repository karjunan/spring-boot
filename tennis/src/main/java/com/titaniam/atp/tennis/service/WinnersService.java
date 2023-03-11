package com.titaniam.atp.tennis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.titaniam.atp.tennis.dto.CountryPlayersDTO;
import com.titaniam.atp.tennis.dto.WinnerNameAndTotalWinsDTO;
import com.titaniam.atp.tennis.dto.model.CountryPlayerModel;
import com.titaniam.atp.tennis.dto.model.WinnerNameAndTotalWins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WinnersService {


    @Autowired
    private MongoTemplate mongoTemplate;

    public WinnerNameAndTotalWinsDTO getWinnerNamesAndFinalWins(String year, String surface) {
        Criteria surfaceCriteria = new Criteria("surface").is(surface);
        Criteria roundCriteria = new Criteria("round").is("F");
        List<Criteria> list = new ArrayList<>();
        list.add(surfaceCriteria);
        list.add(roundCriteria);

        MatchOperation matchStage = Aggregation.match(new Criteria("year").is(year).andOperator(list));
        GroupOperation groupWinnerNames = Aggregation.group("winner_name", "round")
                .count().as("count");

        Aggregation winnerAggregation
                = Aggregation.newAggregation(matchStage, groupWinnerNames);


        AggregationResults<CountryPlayerModel> output
                = mongoTemplate.aggregate(winnerAggregation, "tennis", CountryPlayerModel.class);

        log.info("Total document for the ouput => " + output.getMappedResults().size());


        return setDataToDTO(output);
    }

    public WinnerNameAndTotalWinsDTO setDataToDTO(AggregationResults<CountryPlayerModel> output) {

        for(CountryPlayerModel model: output.getMappedResults()) {

        }

        return null;
    }

}
