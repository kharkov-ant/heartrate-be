package com.heartrate.facade;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.heartrate.dto.PointDto;
import com.heartrate.service.BatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeartRateFacade {

    @Autowired
    private BatchService batchService;

    /*
     * This method can be potentially used if we want to sava data as a batch process
     * instead of real time saving
     */
    public void batchProcessing(Exchange exchange) {
        batchService.processMessage(exchange.getMessage().getBody(String.class));
    }

    public List<PointDto> loadMock() throws IOException {
        File mockData = new File("src/main/resources/ECG_2023-11-07.csv");
        MappingIterator<PointDto> pointIter = new CsvMapper().readerWithTypedSchemaFor(PointDto.class).readValues(mockData);
        return pointIter.readAll();
    }
    
}
