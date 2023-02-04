package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Speaker;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateSpeakerRepositoryImpl implements SpeakerRepository {

    public List<Speaker> findAll() {
        List<Speaker> speakers = new ArrayList<>();
        Speaker speaker = new Speaker();
        speaker.setLastName("Kumar");
        speaker.setFirstName("Krishna");
        speakers.add(speaker);
        return speakers;
    }
}
