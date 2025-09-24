package ru.yandex.practicum.aggregator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final AggregatorKafkaClient kafkaClient;

    @Value("${kafka.topics.sensor}")
    private String topic;

    private final Aggregator baseHandler;

    @PostConstruct
    public void init() {
        log.debug("topic: '{}'", topic);
        log.debug("consumer: '{}'", this.kafkaClient.getConsumer());
        this.kafkaClient.getConsumer().subscribe(List.of(topic));
    }

    public void stop() {
        kafkaClient.stop();
    }

    @Scheduled(fixedDelay = 500)
    public void poll() {
        // log.debug("{}.poll()", AggregationStarter.class.getSimpleName());
        ConsumerRecords<String, SensorEventAvro> records = kafkaClient.getConsumer().poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, SensorEventAvro> record : records) {
            handle(record);
        }
    }

    public void handle(ConsumerRecord<String, SensorEventAvro> record) {
        baseHandler.handle(record);
    }
}
