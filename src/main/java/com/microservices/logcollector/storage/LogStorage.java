package com.microservices.logcollector.storage;

import com.microservices.logcollector.model.LogEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class LogStorage {

    private final List<LogEvent> logs = new CopyOnWriteArrayList<>();

    public void add(LogEvent log) {
        logs.add(log);
    }

    public List<LogEvent> getAll() {
        return logs;
    }

    public List<LogEvent> getLastN(int n) {
        return logs.stream()
                .skip(Math.max(0, logs.size() - n))
                .toList();
    }
}