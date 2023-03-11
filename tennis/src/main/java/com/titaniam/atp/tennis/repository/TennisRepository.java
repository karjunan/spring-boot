package com.titaniam.atp.tennis.repository;

import java.util.List;

import com.titaniam.atp.tennis.repository.entity.TennisDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TennisRepository extends MongoRepository<TennisDAO, String> {


}
