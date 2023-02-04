package com.example.repository;

import java.util.List;

import com.example.model.Speaker;

public interface SpeakerRepository {
    List<Speaker> findAll();
}
