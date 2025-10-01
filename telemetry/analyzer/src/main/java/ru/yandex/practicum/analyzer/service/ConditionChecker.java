package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
@Component
public class ConditionChecker {

    private final Map<ConditionOperationAvro, BiFunction<Integer, Integer, Boolean>> operations = Map.of(
            ConditionOperationAvro.LOWER_THAN, (sensorValue, threshold) -> sensorValue < threshold,
            ConditionOperationAvro.GREATER_THAN, (sensorValue, threshold) -> sensorValue > threshold,
            ConditionOperationAvro.EQUALS, Integer::equals
    );

    public boolean evaluate(ConditionOperationAvro type, int sensorValue, int threshold) {
        return operations.get(type).apply(sensorValue, threshold);
    }

    public boolean check(ScenarioCondition scenarioCondition, Map<String, SensorStateAvro> stateMap) {
        log.debug("Check scenario condition");
        String sensorId = scenarioCondition.getSensor().getId();
        SensorStateAvro sensorStateAvro = stateMap.get(sensorId);
        if (sensorStateAvro == null)
            return false;

        Object payload = sensorStateAvro.getPayload();

        Condition condition = scenarioCondition.getCondition();
        ConditionOperationAvro operator = condition.getOperation();
        ConditionTypeAvro conditionType = condition.getType();
        int sensorValue = 0;

        switch (conditionType) {
            case MOTION: {
                MotionSensorAvro payload1 = (MotionSensorAvro) payload;
                sensorValue = payload1.getMotion() ? 1 : 0;
                break;
            }
            case LUMINOSITY: {
                LightSensorAvro payload1 = (LightSensorAvro) payload;
                sensorValue = payload1.getLuminosity();
                break;
            }
            case SWITCH: {
                SwitchSensorAvro payload1 = (SwitchSensorAvro) payload;
                sensorValue = payload1.getState() ? 1 : 0;
                break;
            }
            case TEMPERATURE: {
                // TemperatureSensorAvro
                ClimateSensorAvro payload1 = (ClimateSensorAvro) payload;
                sensorValue = payload1.getTemperatureC();
                break;
            }
            case CO2LEVEL: {
                ClimateSensorAvro payload1 = (ClimateSensorAvro) payload;
                sensorValue = payload1.getCo2Level();
                break;
            }
            case HUMIDITY: {
                ClimateSensorAvro payload1 = (ClimateSensorAvro) payload;
                sensorValue = payload1.getHumidity();
                break;
            }
        }

        boolean result = evaluate(operator, sensorValue, condition.getValue());
        log.debug("{}: value: {}  operation: {} threshold: {} result: {}",
                condition.getType(),
                sensorValue,
                condition.getOperation(),
                condition.getValue(),
                result);

        return result;
    }
}
