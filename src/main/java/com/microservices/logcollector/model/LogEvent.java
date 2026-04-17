package com.microservices.logcollector.model;

import lombok.Data;

@Data
public class LogEvent {

    private String timestamp;
    private String service;
    private String level;
    private String event;
    private String requestId;
    private Long latency;

}