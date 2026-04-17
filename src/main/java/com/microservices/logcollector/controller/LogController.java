package com.microservices.logcollector.controller;

import com.microservices.logcollector.model.LogEvent;
import com.microservices.logcollector.service.DatasetService;
import com.microservices.logcollector.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final DatasetService datasetService;

    @PostMapping
    public ResponseEntity<Void> receiveLog(@RequestBody LogEvent log) {

        logService.save(log);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<LogEvent> getLogs() {
        return logService.getLogs();
    }

    @GetMapping("/export")
    public String exportLogs() throws IOException {

        datasetService.export(logService.getLogs());

        return "Dataset exported!";
    }
}