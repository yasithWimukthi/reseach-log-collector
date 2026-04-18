package com.microservices.logcollector.service;

import com.microservices.logcollector.model.FeatureVector;
import com.microservices.logcollector.model.LogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetBuilderService {

    private final SlidingWindowService slidingWindowService;
    private final FeatureExtractionService featureExtractionService;

    public List<FeatureVector> buildDataset() {

        List<List<LogEvent>> windows = slidingWindowService.generateWindows();

        List<FeatureVector> dataset = new ArrayList<>();

        for (List<LogEvent> window : windows) {

            if (window.isEmpty()) continue;

            FeatureVector features =
                    featureExtractionService.extract(window);

            dataset.add(features);
        }

        return dataset;
    }
}