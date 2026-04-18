package com.microservices.logcollector.service;

import com.microservices.logcollector.model.LogEvent;
import com.microservices.logcollector.storage.LogStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlidingWindowService {

    private final LogStorage logStorage;

    public List<List<LogEvent>> generateWindows() {

        List<LogEvent> logs = logStorage.getAll();

        if (logs.isEmpty()) {
            return Collections.emptyList();
        }
        // sort logs by timestamp
        logs.sort(Comparator.comparing(
                log -> Instant.parse(log.getTimestamp())
        ));

        List<List<LogEvent>> windows = new ArrayList<>();

        Duration windowSize = Duration.ofMinutes(2);
        Duration stepSize = Duration.ofSeconds(30);

        Instant startTime = Instant.parse(logs.get(0).getTimestamp());
        Instant endTime = Instant.parse(logs.get(logs.size() - 1).getTimestamp());

        for (Instant windowStart = startTime;
             windowStart.isBefore(endTime);
             windowStart = windowStart.plus(stepSize)) {

            Instant windowEnd = windowStart.plus(windowSize);

            Instant finalWindowStart = windowStart;
            List<LogEvent> windowLogs = logs.stream()
                    .filter(log -> {
                        Instant logTime = Instant.parse(log.getTimestamp());
                        return !logTime.isBefore(finalWindowStart) &&
                                logTime.isBefore(windowEnd);
                    })
                    .toList();

            if (!windowLogs.isEmpty()) {
                windows.add(windowLogs);
            }
        }

        return windows;
    }
}
