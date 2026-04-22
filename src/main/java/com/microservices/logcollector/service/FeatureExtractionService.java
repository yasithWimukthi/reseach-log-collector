package com.microservices.logcollector.service;

import com.microservices.logcollector.model.FeatureVector;
import com.microservices.logcollector.model.LogEvent;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FeatureExtractionService {

    public FeatureVector extract(List<LogEvent> logs) {

        int errorCount = 0;
        int warnCount = 0;
        int infoCount = 0;

        long totalLatency = 0;
        long maxLatency = 0;
        int latencyCount = 0;

        Set<String> uniqueEvents = new HashSet<>();

        for (LogEvent log : logs) {

            // Count levels
            if ("ERROR".equalsIgnoreCase(log.getLevel())) errorCount++;
            else if ("WARN".equalsIgnoreCase(log.getLevel())) warnCount++;
            else infoCount++;

            // Latency
            if (log.getLatency() != null) {
                totalLatency += log.getLatency();
                latencyCount++;
                maxLatency = Math.max(maxLatency, log.getLatency());
            }

            // Unique events
            uniqueEvents.add(log.getEvent());
        }

        double avgLatency = latencyCount == 0 ? 0 :
                (double) totalLatency / latencyCount;

        return new FeatureVector(
                logs.get(0).getTimestamp(),
                logs.get(logs.size() - 1).getTimestamp(),
                errorCount,
                warnCount,
                infoCount,
                avgLatency,
                maxLatency,
                logs.size(),
                uniqueEvents.size(),
                0
        );
    }
}