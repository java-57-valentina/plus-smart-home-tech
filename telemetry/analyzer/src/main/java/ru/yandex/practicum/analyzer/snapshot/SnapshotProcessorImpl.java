package ru.yandex.practicum.analyzer.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.AnalyzerService;
import ru.yandex.practicum.analyzer.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SnapshotProcessorImpl implements SnapshotProcessor {

    private final KafkaConfig.ConsumerConfig consumerConfig;
    private final SnapshotConsumer snapshotConsumer;
    private final AnalyzerService service;

    @Autowired
    public SnapshotProcessorImpl(KafkaConfig kafkaConfig,
                                 SnapshotConsumer snapshotConsumer,
                                 AnalyzerService service) {
        this.consumerConfig = kafkaConfig.getSnapshotsConsumer();
        this.snapshotConsumer = snapshotConsumer;
        this.service = service;
    }

    @Override
    public void run() {
        System.out.println("SnapshotProcessorImpl.run");
        try {
            snapshotConsumer.subscribe(List.of(consumerConfig.getTopic()));
            long pollTimeoutMs = consumerConfig.pollTimeoutMs;
            while (true) {

                ConsumerRecords<String, SensorsSnapshotAvro> records =
                        snapshotConsumer.poll(Duration.ofMillis(pollTimeoutMs));
                if (!records.isEmpty()) {
                    Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                    int processed = 0;

                    for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                        log.debug("Received snapshot record {}", record);

                        service.handle(record.value());
                        processed++;

                        // updateOffsets(record, offsets);
//                        if (processed % 10 == 0)
//                            commitOffsets(offsets, processed);
                    }
                    // snapShotConsumerConfig.getProducer().flush();
                    // commitOffsets(offsets, processed);
                    snapshotConsumer.commitAsync();
                }
            }
        }
        catch (WakeupException e) {
            log.error(e.getMessage());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        finally {
            log.info("Consumer was closed");
            // snapShotConsumerConfig.getProducer().flush();
            // snapShotConsumerConfig.getProducer().close();
            snapshotConsumer.close();
        }
    }
}
