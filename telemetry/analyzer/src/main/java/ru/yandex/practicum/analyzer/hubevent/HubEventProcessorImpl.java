package ru.yandex.practicum.analyzer.hubevent;

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
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HubEventProcessorImpl implements HubEventProcessor {

    private final KafkaConfig.ConsumerConfig consumerConfig;
    private final HubEventConsumer hubEventConsumer;
    private final AnalyzerService service;

    @Autowired
    public HubEventProcessorImpl(KafkaConfig kafkaConfig,
                                 HubEventConsumer hubEventConsumer,
                                 AnalyzerService service) {
        this.consumerConfig = kafkaConfig.getHubEventsConsumer();
        this.hubEventConsumer = hubEventConsumer;
        this.service = service;
    }

    @Override
    public void run() {
        System.out.println("HubEventProcessorImpl.run");
        try {
            hubEventConsumer.subscribe(List.of(consumerConfig.getTopic()));
            long pollTimeoutMs = consumerConfig.pollTimeoutMs;
            while (true) {

                ConsumerRecords<String, HubEventAvro> records =
                        hubEventConsumer.poll(Duration.ofMillis(pollTimeoutMs));
                if (!records.isEmpty()) {
                    Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                    int processed = 0;

                    for (ConsumerRecord<String, HubEventAvro> record : records) {
                        log.debug("Received hubEvent record {}", record);

                        service.handle(record.value());
                        processed++;

                        // updateOffsets(record, offsets);
//                        if (processed % 10 == 0)
//                            commitOffsets(offsets, processed);
                    }
                    // snapShotConsumerConfig.getProducer().flush();
                    // commitOffsets(offsets, processed);
                    hubEventConsumer.commitAsync();
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
            hubEventConsumer.close();
        }
    }
}
