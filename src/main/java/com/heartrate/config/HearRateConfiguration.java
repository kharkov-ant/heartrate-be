package com.heartrate.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.heartrate.InMemorySettings;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

@Configuration
public class HearRateConfiguration {

    @Value("${influx.url}")
    private String url;

    @Value("${influx.token}")
    private String token;

    @Bean
    public InMemorySettings inMemorySettings() {
        return new InMemorySettings(10, new ArrayList<>());
    }

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }

}
