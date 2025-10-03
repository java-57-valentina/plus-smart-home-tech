package ru.yandex.practicum.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AggregationStarter {

    private final SnapshotService service;
    private final KafkaConfig kafkaConfig;
    private final Consumer<String, SensorEventAvro> sensorEventConsumer;
    private final Producer<String, SpecificRecordBase> producer;

    public AggregationStarter(AggregatorKafkaClient kafkaClient,
                              SnapshotService service,
                              KafkaConfig kafkaConfig) {
        this.service = service;
        this.kafkaConfig = kafkaConfig;
        this.sensorEventConsumer = kafkaClient.getConsumer();
        this.producer = kafkaClient.getProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("JVM is shooting down. Stop consumer");
            stop();
        }));
    }

    public void start() {
        log.info("AggregationStarter::start");
        try {
            sensorEventConsumer.subscribe(List.of(kafkaConfig.getConsumer().getTopic()));
            long pollTimeoutMs = kafkaConfig.getConsumer().pollTimeoutMs;
            while (true) {

                ConsumerRecords<String, SensorEventAvro> records =
                        sensorEventConsumer.poll(Duration.ofMillis(pollTimeoutMs));
                if (!records.isEmpty()) {
                    Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                    int processed = 0;

                    for (ConsumerRecord<String, SensorEventAvro> record : records) {
                        log.debug("Received sensor record {}", record);
                        service.handle(record);
                        processed++;

                        updateOffsets(record, offsets);
                        if (processed % 10 == 0)
                            commitOffsets(offsets, processed);
                    }
                    producer.flush();
                    commitOffsets(offsets, processed);
                }
            }
        } catch (WakeupException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info("Close sensor events consumer");
            producer.flush();
            producer.close();
            sensorEventConsumer.close();
        }
    }

    private static void updateOffsets(ConsumerRecord<String, SensorEventAvro> record, Map<TopicPartition, OffsetAndMetadata> offsets) {
        offsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset(), "")
        );
    }

    private void commitOffsets(Map<TopicPartition, OffsetAndMetadata> offsets, int processedCount) {
        if (offsets.isEmpty())
            return;

        sensorEventConsumer.commitAsync(offsets, (map, exception) -> {
            if (exception != null) {
                log.error("Failed to commit offsets for {} records", processedCount, exception);
            }
        });
    }

    public void stop() {
        log.info("AggregationStarter::stop");
        sensorEventConsumer.wakeup();
        producer.flush();
        producer.close();
    }
}
