package com.microservices.logcollector.service;

import com.microservices.logcollector.model.FeatureVector;
import com.microservices.logcollector.model.LogEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FeatureExtractionService {

    public FeatureVector extract(List<LogEvent> logs) {

        if (logs.isEmpty()) return null;

        int errorCount = 0;
        int warnCount = 0;
        int infoCount = 0;

        int retryCount = 0;
        int slowCount = 0;

        int gatewayFailures = 0;
        int paymentFailures = 0;
        int userFailures = 0;

        List<Long> latencies = new ArrayList<>();
        long totalLatency = 0;
        long maxLatency = 0;

        Set<String> uniqueEvents = new HashSet<>();

        for (LogEvent log : logs) {

            String level = log.getLevel();
            String event = log.getEvent();
            String service = log.getService();

            // 🔹 Severity
            if ("ERROR".equalsIgnoreCase(level)) errorCount++;
            else if ("WARN".equalsIgnoreCase(level)) warnCount++;
            else infoCount++;

            // 🔹 Behavior
            if ("retry_attempt".equals(event) || "payment_retry_attempt".equals(event)) {
                retryCount++;
            }

            if ("slow_response".equals(event)) {
                slowCount++;
            }

            // 🔹 Failure Types (VERY IMPORTANT)
            if ("request_completed_failure".equals(event)) {

                if ("api-gateway".equals(service)) gatewayFailures++;
                else if ("payment-service".equals(service)) paymentFailures++;
                else if ("user-service".equals(service)) userFailures++;
            }

            // 🔹 Latency
            if (log.getLatency() != null) {
                long lat = log.getLatency();
                latencies.add(lat);

                totalLatency += lat;
                maxLatency = Math.max(maxLatency, lat);
            }

            uniqueEvents.add(event);
        }

        int totalEvents = logs.size();

        double avgLatency = latencies.isEmpty() ? 0 :
                (double) totalLatency / latencies.size();

        // 🔥 Latency Variance (VERY IMPORTANT)
        double variance = 0;
        if (!latencies.isEmpty()) {
            for (Long lat : latencies) {
                variance += Math.pow(lat - avgLatency, 2);
            }
            variance /= latencies.size();
        }

        // 🔹 Rates
        double errorRate = totalEvents == 0 ? 0 :
                (double) errorCount / totalEvents;

        double warnRate = totalEvents == 0 ? 0 :
                (double) warnCount / totalEvents;

        // 🔹 Ratio (avoid divide by zero)
        double errorToWarnRatio = warnCount == 0 ? errorCount :
                (double) errorCount / warnCount;

        return new FeatureVector(
                logs.get(0).getTimestamp(),
                logs.get(logs.size() - 1).getTimestamp(),

                // Severity
                errorCount,
                warnCount,
                infoCount,
                errorRate,
                warnRate,

                // Behavior
                retryCount,
                slowCount,

                // Failure types
                gatewayFailures,
                paymentFailures,
                userFailures,

                // Latency
                avgLatency,
                maxLatency,
                variance,

                // Distribution
                errorToWarnRatio,

                // General
                totalEvents,
                uniqueEvents.size(),

                0 // label assigned later
        );
    }
}