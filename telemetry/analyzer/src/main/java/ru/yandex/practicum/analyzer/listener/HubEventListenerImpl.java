package ru.yandex.practicum.analyzer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.AnalyzerService;
import ru.yandex.practicum.analyzer.KafkaConfig;
import ru.yandex.practicum.analyzer.consumer.HubEventConsumer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventListenerImpl implements HubEventListener {

    private final KafkaConfig.ConsumerConfig consumerConfig;
    private final HubEventConsumer hubEventConsumer;
    private final AnalyzerService service;

    private volatile boolean running = false;

    public HubEventListenerImpl(KafkaConfig kafkaConfig,
                                HubEventConsumer hubEventConsumer,
                                AnalyzerService service) {
        this.consumerConfig = kafkaConfig.getHubEventsConsumer();
        this.hubEventConsumer = hubEventConsumer;
        this.service = service;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("JVM is shooting down. Stop consumer");
            stop();
        }));
    }

    @Override
    public void run() {
        log.debug("HubEventListenerImpl.run");
        running = true;

        try {
            hubEventConsumer.subscribe(List.of(consumerConfig.getTopic()));
            long pollTimeoutMs = consumerConfig.pollTimeoutMs;
            while (running) {

                ConsumerRecords<String, HubEventAvro> records =
                        hubEventConsumer.poll(Duration.ofMillis(pollTimeoutMs));
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, HubEventAvro> record : records) {
                        try {
                            log.debug("Received hubEvent record {}", record);
                            service.handle(record.value());
                        } catch (Exception e) {
                            log.error("Failed to process record: topic={}, partition={}, offset={}",
                                    record.topic(), record.partition(), record.offset(), e);
                        }
                    }
                    log.debug("Processed {} hub events", records.count());
                    // ручная обработка оффсетов не требуется
                    hubEventConsumer.commitAsync();
                }
            }
        } catch (WakeupException e) {
            log.info("Hub events consumer wakeup received - graceful shutdown");
        } catch (Exception e) {
            log.error("Unexpected error in Hub events consumer loop", e);
        } finally {
            log.info("Close Hub events consumer");
            hubEventConsumer.close();
        }
    }

    public void stop() {
        running = false;
        hubEventConsumer.wakeup();
    }
}
