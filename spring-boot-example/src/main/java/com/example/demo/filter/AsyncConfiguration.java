package com.example.demo.filter;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Value("${thread.pool.executor.queue.capacity:#{100}}")
    private int threadQueueCapacity;

    @Value("${thread.pool.max.pool.size:#{50}}")
    private int threadMaxPoolSize;

    @Value("${thread.pool.core.pool.size:#{20}}")
    private int threadCorePoolSize;

//    @Bean
//    public Executor taskExecutor() {
//        System.out.println("Creating Async Task Executor");
//        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(threadCorePoolSize);
//        executor.setMaxPoolSize(threadMaxPoolSize);
//        executor.setQueueCapacity(threadQueueCapacity);
//        executor.setThreadNamePrefix("AsyncThread-");
//        executor.initialize();
//        return new LazyTraceExecutor(beanFactory, executor);
//    }

    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(threadCorePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(threadMaxPoolSize);
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
