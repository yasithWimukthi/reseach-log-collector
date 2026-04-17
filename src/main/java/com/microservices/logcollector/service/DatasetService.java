package com.microservices.logcollector.service;

import com.microservices.logcollector.model.LogEvent;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class DatasetService {

    public void export(List<LogEvent> logs) throws IOException {

        FileWriter writer = new FileWriter("dataset.csv");

        writer.write("timestamp,service,event,level,latency,requestId,error \n");

        for (LogEvent log : logs) {

            writer.write(
                    log.getTimestamp() + "," +
                            log.getService() + "," +
                            log.getEvent() + "," +
                            log.getLevel() + "," +
                            log.getLatency() + "," +
                            log.getRequestId() + "," +
                            log.getErrorMessage() + "\n"
            );
        }

        writer.close();
    }
}