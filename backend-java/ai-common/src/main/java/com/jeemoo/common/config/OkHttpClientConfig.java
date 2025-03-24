package com.jeemoo.common.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpClientConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(600000, TimeUnit.MILLISECONDS)
            .readTimeout(600000, TimeUnit.MILLISECONDS)
            .writeTimeout(600000, TimeUnit.MILLISECONDS)
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
            .build();
    }
}
