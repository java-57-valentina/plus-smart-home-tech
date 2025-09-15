package ru.practicum.telemetry.collector.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.telemetry.collector.model.hub.ScenarioAction;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

@UtilityClass
public class ScenarioActionMapper {
    public DeviceActionAvro toAvro(ScenarioAction action) {
        ActionTypeAvro actionType = ActionTypeAvro.valueOf(action.getType().name());
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(actionType)
                .setValue(action.getValue())
                .build();
    }
}
