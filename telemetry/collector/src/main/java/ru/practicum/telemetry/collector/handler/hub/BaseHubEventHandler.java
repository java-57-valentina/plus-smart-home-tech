package ru.practicum.telemetry.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    @Value("${kafka.topics.hub}")
    private String topic;

    private final KafkaEventProducer producer;

    @Override
    public void handleEvent(HubEvent event) {
        T payload = toAvro(event);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(topic, eventAvro);
        log.debug("Sending event to kafka: {}", producerRecord);
        producer.send(producerRecord);
    }

    protected abstract T toAvro(HubEvent event);

    public void validateEventType(HubEvent event, Class<? extends HubEvent> expectedClass) {
        if (!expectedClass.isInstance(event)) {
            String message = "Expected " + expectedClass.getSimpleName()
                    + " but got " + event.getClass().getSimpleName() + ". Event ID: " + event.getHubId();
            throw new IllegalArgumentException(message);
        }
    }
}
