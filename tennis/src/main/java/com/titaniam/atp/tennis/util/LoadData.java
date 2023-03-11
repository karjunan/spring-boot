package com.titaniam.atp.tennis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.titaniam.atp.tennis.repository.TennisRepository;
import com.titaniam.atp.tennis.repository.entity.TennisDAO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
@Slf4j
public class LoadData {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TennisRepository tennisRepository;

    @PostConstruct
    private void postConstruct() throws IOException {

        File folder = ResourceUtils.getFile("classpath:atp_matches");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            List<TennisDAO> list = readFile(file.getAbsolutePath());
            // check if the values were already inserted in the db . if so, move one
            if(!list.isEmpty() && isDataAlreadyInserted(list.get(0))) {
                continue;
            }
            saveData(list);
        }
    }

    private List<TennisDAO> readFile(String path) throws IOException {
        log.info("path " + path);
        List<TennisDAO> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String str;
            int val = 0;
            while ((str = br.readLine()) != null) {
                if (val != 0) {
                    TennisDAO dao = buildData(str);
                    list.add(dao);
                }
                val += 1;
            }
        }
       return list;
    }

    private boolean isDataAlreadyInserted(TennisDAO tennisDAO) {
        try {
            tennisRepository.save(tennisDAO);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private void saveData(List<TennisDAO> tennislist) {
        BulkOperations bulkInsertion = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TennisDAO.class);
        for (int i = 0; i < tennislist.size(); ++i) {
            bulkInsertion.insert(tennislist.get(i));
        }
        try {
            bulkInsertion.execute();
        } catch (Exception ex) {
            log.warn("Elements already saved , please validate the data");
        }

    }

    private TennisDAO buildData(String data) {
        String[] d = data.split(",");
        TennisDAO tennisDAO = new TennisDAO();
        tennisDAO.setTourney_id(d[0]+"-"+d[6]);
        tennisDAO.setTourney_name(d[1]);
        tennisDAO.setSurface(d[2]);
        tennisDAO.setDraw_size(d[3]);
        tennisDAO.setTourney_level(d[4]);
        tennisDAO.setTourney_date(d[5]);
        tennisDAO.setMatch_num(d[6]);
        tennisDAO.setWinner_id(d[7]);
        tennisDAO.setWinner_seed(d[8]);
        tennisDAO.setWinner_entry(d[9]);
        tennisDAO.setWinner_name(d[10]);
        tennisDAO.setWinner_hand(d[11]);
        tennisDAO.setWinner_ht(d[12]);
        tennisDAO.setWinner_ioc(d[13]);
        tennisDAO.setWinner_age(d[14]);
        tennisDAO.setLoser_id(d[15]);
        tennisDAO.setLoser_seed(d[16]);
        tennisDAO.setLoser_entry(d[17]);
        tennisDAO.setLoser_name(d[18]);
        tennisDAO.setLoser_hand(d[19]);
        tennisDAO.setLoser_ht(d[20]);
        tennisDAO.setLoser_ioc(d[21]);
        tennisDAO.setLoser_age(d[22]);
        tennisDAO.setScore(d[23]);
        tennisDAO.setBest_of(d[24]);
        tennisDAO.setRound(d[25]);
        return  tennisDAO;
    }

}
