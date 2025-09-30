package ru.yandex.practicum.analyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerServiceImpl implements AnalyzerService {

    @Override
    public void handle(SensorsSnapshotAvro record) {
        log.debug("handle sensor event: {}", record);
    }

    @Override
    public void handle(HubEventAvro value) {

    }
}
