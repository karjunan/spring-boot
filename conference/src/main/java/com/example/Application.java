package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.service.SpeakerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int i = 0; i < 2; i++) {
            SpeakerService service = context.getBean("speakerService", SpeakerService.class);
            executorService.submit(() -> {
                System.out.println(service);
                service.findAll();
            });
        }

        executorService.shutdown();
//        service.findAll().forEach(v -> System.out.println(v.getFirstName()));
//
//        System.out.println(service);
//        SpeakerService service1 = context.getBean("speakerService", SpeakerService.class);
//        System.out.println(service1);
    }
}
