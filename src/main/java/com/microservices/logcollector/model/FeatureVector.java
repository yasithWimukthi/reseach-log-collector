package com.microservices.logcollector.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureVector {

    private String windowStart;
    private String windowEnd;

    // Severity
    private int errorCount;
    private int warnCount;
    private int infoCount;

    private double errorRate;
    private double warnRate;

    // Behavior
    private int retryCount;
    private int slowResponseCount;

    // Failure Types
    private int gatewayFailureCount;
    private int paymentFailureCount;
    private int userFailureCount;

    // Latency
    private double avgLatency;
    private long maxLatency;
    private double latencyVariance;

    // Distribution
    private double errorToWarnRatio;

    // General
    private int totalEvents;
    private int uniqueEvents;

    private int label;
}