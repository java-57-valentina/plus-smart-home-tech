package ru.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.service.CollectorService;
import ru.practicum.telemetry.collector.model.sensor.SensorEvent;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/events")
@RequiredArgsConstructor
public class EventController {

    private final CollectorService service;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.debug("\nrequest for collect sensor event: {}", event);
        service.collectSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.debug("\nrequest for collect hub event: {}", event);
        service.collectHubEvent(event);
    }
}
