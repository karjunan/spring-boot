package com.titaniam.atp.tennis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.titaniam.atp.tennis.dto.GrandSlamDTO;
import com.titaniam.atp.tennis.dto.GrandSlamWinnersDTO;
import com.titaniam.atp.tennis.dto.model.GrandSlamWinnersModel;
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
public class GrandSlamService {


    @Autowired
    private MongoTemplate mongoTemplate;

    public GrandSlamDTO getWinnerNamesAndFinalWinsForSpecificYears(String fromYear, String toYear) {

        GrandSlamDTO output = buildAggregation(fromYear, toYear);

        return output;
    }

    private GrandSlamDTO buildAggregation(String fromYear, String toYear) {

        Criteria roundCriteria = new Criteria("round").is("F");
        Criteria grandSlamCriteria = new Criteria("tourney_name").in("US Open", "Wimbledon", "Australian Open", "Roland Garros");

        List<Criteria> list = new ArrayList<>();
        list.add(roundCriteria);
        list.add(grandSlamCriteria);
        MatchOperation matchStage = Aggregation.match(new Criteria("year").gte(fromYear).lte(toYear).andOperator(list));


        GroupOperation groupWinnerNames = Aggregation.group("tourney_name", "winner_id", "winner_name", "round")
                .count().as("count");

        Aggregation winnerAggregation
                = Aggregation.newAggregation(matchStage, groupWinnerNames);


        AggregationResults<GrandSlamWinnersModel> output
                = mongoTemplate.aggregate(winnerAggregation, "tennis", GrandSlamWinnersModel.class);

        log.info("Total document for the ouput => " + output.getMappedResults().size());

        return setDataToDTO(output, fromYear, toYear);
    }

    private GrandSlamDTO setDataToDTO(AggregationResults<GrandSlamWinnersModel> output, String fromYear, String toYear) {

        List<GrandSlamWinnersDTO> list = new ArrayList<>();
        GrandSlamDTO grandSlamDTO = new GrandSlamDTO();
        grandSlamDTO.setFromYear(fromYear);
        grandSlamDTO.setToYear(toYear);
        Map<String, Set<String>> winnerMap = new HashMap<>();
        for (GrandSlamWinnersModel model : output.getMappedResults()) {
            String key = model.getId().getWinner_id();
            String value = model.getId().getTourney_name();
            if (!winnerMap.containsKey(key)) {
                Set<String> ls = new HashSet<>();
                ls.add(value);
                winnerMap.put(key, ls);
            } else {
                Set<String> ls1 = winnerMap.get(key);
                ls1.add(value);
                winnerMap.put(key, ls1);
                if (ls1.size() >= 4) {
                    GrandSlamWinnersDTO grandSlamWinnersDTO = new GrandSlamWinnersDTO();
                    grandSlamWinnersDTO.setName(model.getId().getWinner_name());
                    list.add(grandSlamWinnersDTO);

                }
            }
        }
        grandSlamDTO.setWinners(list);
        return grandSlamDTO;
    }

}
