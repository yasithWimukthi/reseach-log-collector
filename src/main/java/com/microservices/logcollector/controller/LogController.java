package com.microservices.logcollector.controller;

import com.microservices.logcollector.model.FeatureVector;
import com.microservices.logcollector.model.LogEvent;
import com.microservices.logcollector.service.DatasetBuilderService;
import com.microservices.logcollector.service.DatasetService;
import com.microservices.logcollector.service.LogService;
import com.microservices.logcollector.service.SlidingWindowService;
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
    private final SlidingWindowService slidingWindowService;
    private final DatasetBuilderService datasetBuilderService;

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

    @GetMapping("/windows")
    public List<List<LogEvent>> getWindows() {
        return slidingWindowService.generateWindows();
    }

    @GetMapping("/dataset")
    public List<FeatureVector> getDataset() {
        return datasetBuilderService.buildDataset();
    }
}