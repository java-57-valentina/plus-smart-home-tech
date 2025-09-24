package ru.yandex.practicum.aggregator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface Aggregator {
    void handle(ConsumerRecord<String, SensorEventAvro> record);
}
