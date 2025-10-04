package ru.yandex.practicum.analyzer.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedEventProcessor extends HubEventProcessorBase {

    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadClass() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    @Transactional
    protected void handleImpl(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro) event.getPayload();

        if (sensorRepository.existsById(payload.getId())) {
            log.info("Device {} is already registered in hub {}", payload.getId(), event.getHubId());
            return;
        }

        Sensor sensor = Sensor.builder()
                .id(payload.getId())
                .hubId(event.getHubId())
                .build();
        sensorRepository.save(sensor);
        log.debug("Device {} ({}) was successfully added", payload.getId(), payload.getDeviceType());
    }
}
