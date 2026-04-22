package com.microservices.logcollector.service;

import com.microservices.logcollector.model.FeatureVector;
import com.microservices.logcollector.model.LogEvent;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class DatasetService {

    public void export(List<FeatureVector> dataset) throws IOException {

        FileWriter writer = new FileWriter("dataset.csv");

        writer.write("errorCount,warnCount,infoCount,avgLatency,maxLatency,totalEvents,uniqueEvents,label\n");

        for (FeatureVector f : dataset) {

            writer.write(
                    f.getErrorCount() + "," +
                            f.getWarnCount() + "," +
                            f.getInfoCount() + "," +
                            f.getAvgLatency() + "," +
                            f.getMaxLatency() + "," +
                            f.getTotalEvents() + "," +
                            f.getUniqueEvents() + "," +
                            f.getLabel() + "\n"
            );
        }

        writer.close();
    }
}