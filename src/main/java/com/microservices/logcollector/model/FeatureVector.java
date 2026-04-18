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

    private int errorCount;
    private int warnCount;
    private int infoCount;

    private double avgLatency;
    private long maxLatency;

    private int totalEvents;
    private int uniqueEvents;
}