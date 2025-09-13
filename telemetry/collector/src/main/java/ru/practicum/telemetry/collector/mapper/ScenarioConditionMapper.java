package ru.practicum.telemetry.collector.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@UtilityClass
public class ScenarioConditionMapper {

    public ScenarioConditionAvro toAvro(ScenarioCondition sc) {
        ConditionTypeAvro typeAvro = ConditionTypeAvro.valueOf(sc.getType().name());
        ConditionOperationAvro operationAvro = ConditionOperationAvro.valueOf(sc.getOperation().name());
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(sc.getSensorId())
                .setType(typeAvro)
                .setOperation(operationAvro)
                .setValue(sc.getValue())
                .build();
    }
}
