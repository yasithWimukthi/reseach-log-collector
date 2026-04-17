package com.microservices.logcollector.service;

import com.microservices.logcollector.model.LogEvent;
import com.microservices.logcollector.storage.LogStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogStorage storage;

    public void save(LogEvent log) {
        storage.add(log);
    }

    public List<LogEvent> getLogs() {
        return storage.getAll();
    }
}