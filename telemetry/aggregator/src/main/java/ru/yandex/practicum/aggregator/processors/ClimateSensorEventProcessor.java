package ru.yandex.practicum.aggregator.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Slf4j
@Component
public class ClimateSensorEventProcessor extends SensorEventProcessorBase {

    @Override
    public Class<?> getPayloadClass() {
        return ClimateSensorAvro.class;
    }

    @Override
    protected boolean updatePayloadIfChanged(Object state, Object event) {

        ClimateSensorAvro curPayload = castPayload(state, ClimateSensorAvro.class);
        ClimateSensorAvro newPayload = castPayload(event, ClimateSensorAvro.class);

        boolean changed = false;
        changed |= updateFieldIfChanged(curPayload::getHumidity, curPayload::setHumidity, newPayload.getHumidity());
        changed |= updateFieldIfChanged(curPayload::getCo2Level, curPayload::setCo2Level, newPayload.getCo2Level());
        changed |= updateFieldIfChanged(curPayload::getTemperatureC, curPayload::setTemperatureC, newPayload.getTemperatureC());

        return changed;
    }
}
