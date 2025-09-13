package ru.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class KafkaEventProducer {
    private final KafkaProducer<String, SpecificRecordBase> producer;

    public void send(ProducerRecord<String, SpecificRecordBase> recordBase) {
        Future<RecordMetadata> send = producer.send(recordBase);
    }
}
