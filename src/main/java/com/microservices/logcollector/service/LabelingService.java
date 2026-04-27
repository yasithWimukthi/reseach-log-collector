package com.microservices.logcollector.service;

import com.microservices.logcollector.model.FeatureVector;
import com.microservices.logcollector.model.LogEvent;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class LabelingService {

    public void applyLabels(List<FeatureVector> dataset,
                            List<LogEvent> allLogs) {

        // ✅ Step 1: use TRUE failures only
        List<Instant> failureTimes = allLogs.stream()
                .filter(log -> "request_completed_failure".equals(log.getEvent()))
                .map(log -> Instant.parse(log.getTimestamp()))
                .toList();

        for (FeatureVector feature : dataset) {

            Instant windowStart = Instant.parse(feature.getWindowStart());
            Instant windowEnd = Instant.parse(feature.getWindowEnd());

            boolean isPreFailure = false;

            for (Instant failureTime : failureTimes) {

                // ✅ Step 2: smaller prediction window
                Instant preFailureStart = failureTime.minus(Duration.ofMinutes(1));

                if (!windowEnd.isBefore(preFailureStart) &&
                        !windowStart.isAfter(failureTime)) {

                    isPreFailure = true;
                    break;
                }
            }

            feature.setLabel(isPreFailure ? 1 : 0);
        }
    }
}