package ru.yandex.practicum.analyzer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public abstract class BaseConsumer<T extends SpecificRecordBase> {

    private KafkaConsumer<String, T> consumer;
    private final KafkaConfig.ConsumerConfig consumerConfig;

    protected BaseConsumer(KafkaConfig.ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    @PostConstruct
    private void init() {
        log.debug("init BaseConsumer with {}", getDeserializerName());
        Properties properties = consumerConfig.getProperties();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, getDeserializerName());
        consumer = new KafkaConsumer<>(properties);
    }

    protected abstract String getDeserializerName();

    public void subscribe(List<String> topics) {
        consumer.subscribe(topics);
    }

    public ConsumerRecords<String, T> poll(Duration duration) {
        return consumer.poll(duration);
    }

    public void commitAsync() {
        consumer.commitAsync();
    }

    public void close() {
        consumer.wakeup();
        consumer.close();
    }
}
