package ru.yandex.practicum.analyzer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.hubevent.HubEventProcessor;
import ru.yandex.practicum.analyzer.snapshot.SnapshotProcessor;

@Component
@RequiredArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {
    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;

    @Override
    public void run(String... args) throws Exception {
         Thread hubEventThread = new Thread(hubEventProcessor);
         hubEventThread.start();

         snapshotProcessor.run();
    }
}
