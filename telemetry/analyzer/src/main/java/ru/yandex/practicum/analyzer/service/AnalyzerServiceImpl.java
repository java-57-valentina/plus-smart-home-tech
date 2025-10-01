package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.processors.HubEventProcessor;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    private final Map<Class<?>, HubEventProcessor> hubEventProcessors;

    @GrpcClient("hubrouter")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterController;

    @Autowired
    public AnalyzerServiceImpl(List<HubEventProcessor> hubEventProcessors) {
        this.hubEventProcessors = hubEventProcessors.stream()
                .collect(Collectors.toMap(HubEventProcessor::getPayloadClass, Function.identity()));
    }

    @Override
    public void handle(SensorsSnapshotAvro record) {
        log.debug("handle snapshot event: {}", record);
    }

    @Override
    public void handle(HubEventAvro event) {
        log.debug("handle hub event {}", event);
        HubEventProcessor processor = getProcessorOrThrow(event);
        processor.handle(event);
    }

    private HubEventProcessor getProcessorOrThrow(HubEventAvro event) {
        Class<?> aClass = event.getPayload().getClass();
        HubEventProcessor processor = hubEventProcessors.get(aClass);
        if (processor == null) {
            throw new IllegalArgumentException("No processor found for payload type: " + aClass.getSimpleName());
        }
        return processor;
    }
}
