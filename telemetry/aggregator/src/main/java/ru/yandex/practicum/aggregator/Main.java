package ru.yandex.practicum.aggregator;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication
@ConfigurationProperties
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Main.class, args);
        AggregationStarter aggregationStarter = configurableApplicationContext.getBean(AggregationStarter.class);
        aggregationStarter.start();
    }
}
