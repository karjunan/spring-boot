package com.titaniam.atp.tennis.service;

import java.util.ArrayList;
import java.util.List;

import com.titaniam.atp.tennis.dto.WinnerNameAndTotalWinsDTO;
import com.titaniam.atp.tennis.dto.WinnerNameAndTotalWinsForDurationDTO;
import com.titaniam.atp.tennis.dto.WinnerNamesAndWinsDTO;
import com.titaniam.atp.tennis.dto.model.WinnerNameAndTotalWinsModel;
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

        AggregationResults<WinnerNameAndTotalWinsModel> output = buildAggregation(year, null, surface);

        WinnerNameAndTotalWinsDTO winnerNameAndTotalWinsDTO = new WinnerNameAndTotalWinsDTO();
        winnerNameAndTotalWinsDTO.setYear(year);
        winnerNameAndTotalWinsDTO.setSurface(surface);
        List<WinnerNamesAndWinsDTO> list = setDataToDTO(output);
        winnerNameAndTotalWinsDTO.setWinners(list);
        return winnerNameAndTotalWinsDTO;
    }

    public WinnerNameAndTotalWinsForDurationDTO getWinnerNamesAndFinalWinsForSpecificYears(String fromYear, String toYear, String surface) {

        AggregationResults<WinnerNameAndTotalWinsModel> output = buildAggregation(fromYear, toYear, surface);
        WinnerNameAndTotalWinsForDurationDTO winnerNameAndTotalWinsDTO = new WinnerNameAndTotalWinsForDurationDTO();
        winnerNameAndTotalWinsDTO.setFromYear(fromYear);
        winnerNameAndTotalWinsDTO.setToYear(toYear);
        winnerNameAndTotalWinsDTO.setSurface(surface);
        List<WinnerNamesAndWinsDTO> list = setDataToDTO(output);
        winnerNameAndTotalWinsDTO.setWinners(list);
        return winnerNameAndTotalWinsDTO;
    }

    private AggregationResults<WinnerNameAndTotalWinsModel> buildAggregation(String fromYear, String toYear, String surface) {
        Criteria surfaceCriteria = new Criteria("surface").is(surface);
        Criteria roundCriteria = new Criteria("round").is("F");
        List<Criteria> list = new ArrayList<>();
        list.add(surfaceCriteria);
        list.add(roundCriteria);
        MatchOperation matchStage = null;
        if (toYear == null) {
            matchStage = Aggregation.match(new Criteria("year").is(fromYear).andOperator(list));
        } else {
            matchStage = Aggregation.match(new Criteria("year").gte(fromYear).lte(toYear).andOperator(list));
        }


        GroupOperation groupWinnerNames = Aggregation.group("winner_name", "round")
                .count().as("count");

        Aggregation winnerAggregation
                = Aggregation.newAggregation(matchStage, groupWinnerNames);


        AggregationResults<WinnerNameAndTotalWinsModel> output
                = mongoTemplate.aggregate(winnerAggregation, "tennis", WinnerNameAndTotalWinsModel.class);

        log.info("Total document for the ouput => " + output.getMappedResults().size());
        return output;
    }

    private List<WinnerNamesAndWinsDTO> setDataToDTO(AggregationResults<WinnerNameAndTotalWinsModel> output) {

        List<WinnerNamesAndWinsDTO> list = new ArrayList<>();
        for (WinnerNameAndTotalWinsModel model : output.getMappedResults()) {
            WinnerNamesAndWinsDTO winnerNamesAndWinsDTO = new WinnerNamesAndWinsDTO();
            winnerNamesAndWinsDTO.setName(model.getId().getWinner_name());
            winnerNamesAndWinsDTO.setCount(model.getCount());
            list.add(winnerNamesAndWinsDTO);
        }

        return list;
    }

}
