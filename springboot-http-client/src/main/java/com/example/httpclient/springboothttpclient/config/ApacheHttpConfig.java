package com.example.httpclient.springboothttpclient.config;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HeaderElements;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.http.message.BasicHeaderIterator;
import org.apache.hc.core5.http.message.MessageSupport;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ApacheHttpConfig {


    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        // set a total amount of connections across all HTTP routes
        poolingConnectionManager.setMaxTotal(100);
        // set a maximum amount of connections for each HTTP route in pool
        poolingConnectionManager.setDefaultMaxPerRoute(100);
        // increase the amounts of connections if the host is localhost
//        HttpHost remote = new HttpHost("http://20.198.0.44/", 80);
//        poolingConnectionManager.setMaxPerRoute(new HttpRoute(remote), 50);
        return poolingConnectionManager;
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (httpResponse, httpContext) -> {
            final Iterator<HeaderElement> it = MessageSupport.iterate(httpResponse, HeaderElements.KEEP_ALIVE);
            while (it.hasNext()) {
                final HeaderElement he = it.next();
                final String param = he.getName();
                final String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return TimeValue.ofSeconds(Long.parseLong(value) * 100000);
                    } catch(final NumberFormatException ignore) {
                    }
                }
            }
            final HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
            final RequestConfig requestConfig = clientContext.getRequestConfig();
//            requestConfig.getConnectionKeepAlive();
//            return TimeValue.ofSeconds(100000);
            return  requestConfig.getConnectionKeepAlive();
        };
    }


    @Bean
    public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager pool) {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 20000)
            public void run() {
                // only if connection pool is initialised
                if (pool != null) {
                    pool.closeIdle(TimeValue.of(100000, TimeUnit.SECONDS));
                    pool.closeExpired();
                    System.out.println(pool.getMaxTotal() + " === " + pool.getTotalStats());
                }
            }
        };
    }

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10, TimeUnit.MINUTES)
                .setDefaultKeepAlive(199, TimeUnit.MINUTES)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }

//    @Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setThreadNamePrefix("idleMonitor");
//        scheduler.setPoolSize(3);
//        return scheduler;
//    }
}
