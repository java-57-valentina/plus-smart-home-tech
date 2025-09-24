package ru.yandex.practicum.aggregator.processors;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

public interface SensorEventProcessor {

    Class<?> getPayloadClass();

    boolean updateIfChanged(SensorStateAvro state, SensorEventAvro event);
}
