package com.example;

import com.example.repository.HibernateSpeakerRepositoryImpl;
import com.example.repository.SpeakerRepository;
import com.example.service.SpeakerService;
import com.example.service.SpeakerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({"com.example"})
@Configuration
public class AppConfig {

//    @Bean(name="speakerRepository")
//    public SpeakerRepository getSpeakerRepository() {
//        return new HibernateSpeakerRepositoryImpl();
//    }

//    @Bean(name="speakerService")
//    @Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
//    public SpeakerService getSpeakerService() {
//        SpeakerServiceImpl speakerService = new SpeakerServiceImpl(getSpeakerRepository());
//        SpeakerServiceImpl speakerService = new SpeakerServiceImpl();
//        return speakerService;
//    }
}
