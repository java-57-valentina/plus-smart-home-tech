package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface AnalyzerService {

    void handle(SensorsSnapshotAvro record);

    void handle(HubEventAvro value);
}
