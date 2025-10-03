package ru.yandex.practicum.analyzer.consumer;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.KafkaConfig;
import ru.yandex.practicum.deserializer.SensorsSnapshotAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Component
public class SnapshotConsumer extends BaseConsumer<SensorsSnapshotAvro> {

    public SnapshotConsumer(KafkaConfig kafkaConfig) {
        super(kafkaConfig.getSnapshotsConsumer());
    }

    @Override
    protected String getDeserializerName() {
        return SensorsSnapshotAvroDeserializer.class.getCanonicalName();
    }
}
