package ru.practicum.telemetry.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.mapper.ScenarioActionMapper;
import ru.practicum.telemetry.collector.mapper.ScenarioConditionMapper;
import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.practicum.telemetry.collector.model.hub.ScenarioAddedHubEvent;
import ru.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro toAvro(HubEvent event) {
        validateEventType(event, ScenarioAddedHubEvent.class);
        ScenarioAddedHubEvent hubEvent = (ScenarioAddedHubEvent)event;

        List<ScenarioConditionAvro> conditions = hubEvent.getConditions().stream()
                .map(ScenarioConditionMapper::toAvro)
                .toList();

        List<DeviceActionAvro> actions = hubEvent.getActions().stream()
                .map(ScenarioActionMapper::toAvro)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(hubEvent.getName())
                .setConditions(conditions)
                .setActions(actions)
                .build();
    }
}
