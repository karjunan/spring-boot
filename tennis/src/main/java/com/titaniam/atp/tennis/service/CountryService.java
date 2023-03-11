package com.titaniam.atp.tennis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.titaniam.atp.tennis.dto.CountryPlayersDTO;
import com.titaniam.atp.tennis.dto.model.CountryPlayerModel;
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
public class CountryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<CountryPlayersDTO> getUniquePlayersByCountry(String year) {
        MatchOperation matchStage = Aggregation.match(new Criteria("year").is(year));
        GroupOperation groupDistinctWinner = Aggregation.group("winner_ioc", "winner_id")
                .count().as("count");
        GroupOperation groupWinner = Aggregation.group("winner_ioc")
                .count().as("count");

        GroupOperation groupDistinctLoser = Aggregation.group("loser_ioc", "loser_id")
                .count().as("count");
        GroupOperation groupLoser = Aggregation.group("loser_ioc")
                .count().as("count");

        Aggregation winnerAggregation
                = Aggregation.newAggregation(matchStage, groupDistinctWinner, groupWinner);

        Aggregation loserAggregation
                = Aggregation.newAggregation(matchStage, groupDistinctLoser, groupLoser);


        AggregationResults<CountryPlayerModel> output//60
                = mongoTemplate.aggregate(winnerAggregation, "tennis", CountryPlayerModel.class);
        AggregationResults<CountryPlayerModel> output1//68
                = mongoTemplate.aggregate(loserAggregation, "tennis", CountryPlayerModel.class);


        log.info("Total document for the ouput => " + output.getMappedResults().size());
        log.info("Total document for the ouput => " + output1.getMappedResults().size());

        Map<String, Integer> map = new HashMap<>();
        for (CountryPlayerModel model : output1.getMappedResults()) {
            map.put(model.getId(), Integer.parseInt(model.getCount()));
        }

        for (CountryPlayerModel model : output.getMappedResults()) {
            String id = model.getId();
            int count = Integer.parseInt(model.getCount());
            if (!map.containsKey(id)) {
                map.put(id, count);
            } else {
                map.put(model.getId(), count + map.get(id));
            }
        }

        return setResultToDTO(map);
    }

    private List<CountryPlayersDTO> setResultToDTO(Map<String, Integer> map) {
        List<CountryPlayersDTO> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            CountryPlayersDTO countryPlayersDTO = new CountryPlayersDTO();
            countryPlayersDTO.setCountry(entry.getKey());
            countryPlayersDTO.setCount(entry.getValue());
            list.add(countryPlayersDTO);
        }
        return list;
    }
}
