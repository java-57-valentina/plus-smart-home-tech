package ru.yandex.practicum.analyzer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.listener.HubEventListener;
import ru.yandex.practicum.analyzer.listener.SnapshotListener;

@Component
@RequiredArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {
    private final HubEventListener hubEventListener;
    private final SnapshotListener snapshotListener;

    @Override
    public void run(String... args) throws Exception {
         Thread hubEventThread = new Thread(hubEventListener);
         hubEventThread.start();

         snapshotListener.run();
    }
}
