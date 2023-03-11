package com.titaniam.atp.tennis.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tennis")
@ToString
public class TennisDAO {

    @Id
    private String tourney_id;

    private String tourney_name;
    private String surface;
    private String draw_size;
    private String tourney_level;
    private String tourney_date;
    private String match_num;
    private String winner_id;
    private String winner_seed;
    private String winner_entry;
    private String winner_name;
    private String winner_hand;
    private String winner_ht;
    private String winner_ioc;
    private String winner_age;
    private String loser_id;
    private String loser_seed;
    private String loser_entry;
    private String loser_name;
    private String loser_hand;
    private String loser_ht;
    private String loser_ioc;
    private String loser_age;
    private String score;
    private String best_of;
    private String round;
    private String year;
}
